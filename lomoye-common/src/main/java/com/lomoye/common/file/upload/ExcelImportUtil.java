package com.lomoye.common.file.upload;

import com.lomoye.common.model.ResultModel;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Excel文件导入
 */
public class ExcelImportUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelImportUtil.class);

    //第一行必须保证是标题行,skipFirstLine=true代表的是开头行是说明
    public static ResultModel<List<String>> importListFromExcel(Workbook workbook, Sheet sheet, boolean skipFirstLine) {
        // 解析公式结果
        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

        ResultModel<List<String>> result = new ResultModel<>();
        List<List<String>> data = new ArrayList<>();

        int minRowNum = sheet.getFirstRowNum();
        int maxRowNum = sheet.getLastRowNum();
        if (skipFirstLine) {
            if (maxRowNum - minRowNum <= 0) {
                result.setIsSuccess(false);
                result.setResultMessage("excel格式错误,没有上传的数据");
                return result;
            } else {
                minRowNum++;
            }
        }

        boolean isHeader = true;
        boolean isNull = false;
        int nullLineNum = 0;
        List<String> header = null;
        for (int rowNum = minRowNum; rowNum <= maxRowNum; rowNum++) {
            Row row = sheet.getRow(rowNum);
            if (row == null) {
                if (isHeader) {
                    result.setIsSuccess(false);
                    result.setResultMessage("excel格式错误,标题行不能为空");
                    return result;
                } else {
                    isNull = true;  //处理最后连续的空行,数据中间不能有空行
                    nullLineNum = nullLineNum == 0 ? rowNum : nullLineNum;
                    continue; //空行不处理
                }
            }
            try {
                List<String> line = readRow(row, evaluator);
                if (!result.isSuccess()) {
                    return result;
                }
                if (isHeader) {
                    line = ImportUtil.getAdjustHeader(line);
                    header = line;
                    if (CollectionUtils.isEmpty(header)) {
                        result.setIsSuccess(false);
                        result.setResultMessage("excel格式错误,标题行不能为空");
                        return result;
                    }
                } else {
                    line = ImportUtil.getAdjustContent(line, header.size());
                }
                if (CollectionUtils.isEmpty(line)) {
                    isNull = true;  //处理最后连续的空行,数据中间不能有空行
                    nullLineNum = nullLineNum == 0 ? rowNum : nullLineNum;
                    continue;
                } else if (isNull) {
                    result.setIsSuccess(false);
                    result.setResultMessage("excel格式错误,第" + (nullLineNum + 1) + "行数据为空");
                    return result;
                }
                data.add(line);
                isHeader = false;
            } catch (Exception e) {
                LOGGER.error("get line encounter Exception ", e);
                result.setIsSuccess(false);
                result.setResultMessage("excel格式错误,第" + (rowNum + 1) + "行处理异常");
                return result;
            }
        }
        result.setData(data);
        if (data.size() <= 1) {
            result.setIsSuccess(false);
            result.setResultMessage("excel格式错误,上传数据内容不能为空");
            return result;
        }

        return result;
    }

    //注意:第一行必须是标题行,不会被处理的
    public static <T> ResultModel<T> importFromExcel(Workbook workbook, Sheet sheet, List<String> attr, Class<T> clazz,
                                                     LineObjectChecker<T> checker, boolean skipFirstLine, String dateFormat) {
        ResultModel<T> result = new ResultModel<>();

        ResultModel<List<String>> dataRes = importListFromExcel(workbook, sheet, skipFirstLine);
        if (!dataRes.isSuccess()) {
            result.setIsSuccess(false);
            result.setResultCode(dataRes.getResultCode());
            result.setResultMessage(dataRes.getResultMessage());
            return result;
        }

        List<List<String>> lists = dataRes.getData();
        List<String> header = lists.get(0);
        if (header.size() != attr.size()) {
            result.setIsSuccess(false);
            result.setResultMessage("excel格式错误,标题行列数不正确,请保持和模板的标题行一致");
            return result;
        }

        int offset = skipFirstLine ? 2 : 1;

        List<T> objList = ImportUtil.fillBean(lists.subList(1, lists.size()), attr, clazz, result, true, dateFormat, offset);
        if (!result.isSuccess()) {
            return result;
        }

        int line = offset;
        for (T obj : objList) {
            line++;
            if (checker != null && !checker.checkLine(obj, result)) {
                result.setIsSuccess(false);
                result.setResultMessage("excel格式错误,第" + line + "行数据验证异常|" + result.getResultMessage());
                return result;
            }
        }
        result.setData(objList);
        return result;
    }

    public static List<String> readRow(Row row, FormulaEvaluator evaluator) {
        short minCol = row.getFirstCellNum();
        short maxCol = row.getLastCellNum();

        List<String> list = new ArrayList<>();

        for (int col = minCol; col < maxCol; col++) {
            String value;
            Cell cell = row.getCell(col);
            CellValue cellValue = evaluator.evaluate(cell);
            if (cellValue == null) {
                list.add(null);
                continue;
            }
            DataFormatter formatter = new DataFormatter();
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_BOOLEAN:
                    value = formatter.formatCellValue(cell);
                    break;
                case HSSFCell.CELL_TYPE_NUMERIC:
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        value = "" + cell.getDateCellValue().getTime();
                    } else {
                        value = formatter.formatCellValue(cell);
                    }
                    break;
                case HSSFCell.CELL_TYPE_STRING:
                    value = cell.getRichStringCellValue().toString();
                    break;
                case Cell.CELL_TYPE_FORMULA:
                case Cell.CELL_TYPE_BLANK:
                case Cell.CELL_TYPE_ERROR:
                default:
                    value = null;
            }
            list.add(value);
        }
        return list;
    }

    public static Workbook getWorkbook(String fileName, InputStream inputStream) {
        Workbook workbook = null;
        try {
            if (fileName.endsWith("xlsx")) {
                workbook = new XSSFWorkbook(inputStream);
            } else {
                workbook = new HSSFWorkbook(inputStream);
            }
        } catch (Exception e) {
            LOGGER.error("getWorkbook encounter Exception ", e);
        }
        return workbook;
    }
}


