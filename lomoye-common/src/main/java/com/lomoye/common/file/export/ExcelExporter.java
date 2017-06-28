package com.lomoye.common.file.export;

import com.lomoye.common.exception.BusinessException;
import com.lomoye.common.file.ReflectionHelper;
import com.google.common.base.Predicates;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * Created by tommy on 2016/2/3.
 */
public class ExcelExporter {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelExporter.class);

    public static <T> Workbook exportExcelFromList(String sheetName, List<T> objList, List<String> attr,
                                                   List<String> header, Class<T> clazz) {
        if (header.size() != attr.size()) {
            throw new RuntimeException("excel Header和传入的列数不匹配");
        }

        Workbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet(sheetName);


        int colNum = header.size();
        int rowNum = 0;
        Row row = sheet.createRow(rowNum++);

        for (int col = 0; col < colNum; col++) {
            row.createCell(col).setCellValue(header.get(col));
        }

        List<List<Object>> lists = getValueList(objList, clazz, attr);

        for (List<Object> list : lists) {
            row = sheet.createRow(rowNum++);
            for (int col = 0; col < colNum; col++) {
                Cell cell = row.createCell(col);
                writeCell(workbook, list.get(col), cell);
            }
        }
        return workbook;
    }

    private static void writeCell(Workbook workbook, Object obj, Cell cell) {
        if (obj == null) {
            cell.setCellValue("");
        } else if (obj instanceof String)
            cell.setCellValue((String) obj);
        else if (obj instanceof Character)
            cell.setCellValue((Character) obj);

        else if (obj instanceof Long)
            cell.setCellValue((Long) obj);
        else if (obj instanceof Integer)
            cell.setCellValue((Integer) obj);
        else if (obj instanceof Short)
            cell.setCellValue((Short) obj);
        else if (obj instanceof Byte)
            cell.setCellValue((Byte) obj);

        else if (obj instanceof Float)
            cell.setCellValue((Float) obj);
        else if (obj instanceof Double)
            cell.setCellValue((Double) obj);

        else if (obj instanceof Boolean)
            cell.setCellValue((Boolean) obj);

        else if (obj instanceof Date) {
            CellStyle cellStyle = workbook.createCellStyle();
            CreationHelper createHelper = workbook.getCreationHelper();
            cellStyle.setDataFormat(
                    createHelper.createDataFormat().getFormat("yyyy/m/d h:mm:ss"));
            cell.setCellValue((Date) obj);
            cell.setCellStyle(cellStyle);
        }
    }

    public static <T> List<List<Object>> getValueList(List<T> objList, Class<T> clazz, List<String> attr) {
        Set<Method> methods = ReflectionUtils.getAllMethods(clazz, Predicates.and(ReflectionUtils.withModifier(Modifier.PUBLIC),
                ReflectionUtils.withPrefix("get")));
        Map<String, Method> methodMap = new HashMap<>();


        for (Method method : methods) {
            String attrName = ReflectionHelper.getAttrNameFromMethod(method.getName());
            if (attr.contains(attrName)) {
                methodMap.put(attrName, method);
            }
        }

        if (methodMap.keySet().size() != attr.size()) {
            throw new BusinessException("", "excel格式错误,列表和数据不一致,处理失败");
        }

        List<List<Object>> res = new ArrayList<>();
        for (T obj : objList) {
            List<Object> list = new ArrayList<>();
            for (String name : attr) {
                Method method = methodMap.get(name);
                try {
                    list.add(method.invoke(obj));
                } catch (Exception e) {
                    LOGGER.error("get value error");
                    throw new BusinessException("", "excel格式错误,不能获取对应列的数据|" + name);
                }
            }
            res.add(list);
        }
        return res;
    }
}
