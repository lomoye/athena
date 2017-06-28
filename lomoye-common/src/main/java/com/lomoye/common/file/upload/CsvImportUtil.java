package com.lomoye.common.file.upload;

import com.lomoye.common.model.ResultModel;
import com.google.common.base.Splitter;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 上传CSV的工具类
 */
public class CsvImportUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(CsvImportUtil.class);

    //注意:第一行必须是标题行,不会被处理的
    public static <T> ResultModel<T> importFromCsv(InputStream inputStream, String splitter, String encoding, Map<String, String> attrMap, Class<T> clazz,
                                                   LineObjectChecker<T> checker, String dateFormat) throws IOException {
        BufferedReader reader = null;
        ResultModel<T> result = new ResultModel<>();
        try {
            reader = new BufferedReader(new InputStreamReader(inputStream, encoding));
            return doImportListFromCsv(reader, splitter, attrMap, clazz, checker, dateFormat);
        } catch (Exception e) {
            LOGGER.warn("importFromCsv exception", e);
            result.setIsSuccess(false);
            result.setResultMessage("Csv文件格式错误,读取csv文件内容失败");
            return result;
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (Exception e) {
                LOGGER.warn("Close csv stream exception", e);
            }
        }
    }

    private static <T> ResultModel<T> doImportListFromCsv(BufferedReader reader, String splitter, Map<String, String> attrMap, Class<T> clazz,
                                                          LineObjectChecker<T> checker, String dateFormat) throws IOException {
        List<T> objList;   //对象列表
        List<List<String>> lists = new ArrayList<>();  //没行的String列表
        ResultModel<T> result = new ResultModel<>();   //返回的结果

        String lineStr;
        boolean isHeader = true;
        List<String> header = null;
        int line = 0;
        boolean isNull = false;
        int nullLineNum = 0;
        while ((lineStr = reader.readLine()) != null) {
            line++;
            List<String> list = Splitter.on(splitter).splitToList(lineStr);
            List<String> values = new ArrayList<>();
            if (list.size() > 0) {
                for (String field : list) {
                    if (field != null && field.startsWith("\"")) {
                        field = field.substring(1, field.length());
                    }
                    if (field != null && field.endsWith("\"")) {
                        field = field.substring(0, field.length() - 1);
                    }
                    if (field != null && field.startsWith("\'")) {
                        field = field.substring(1, field.length()); //兼容处理:淘宝下载的历史订单文件数据会有个'开头。。。
                    }
                    values.add(field);
                }
                if (isHeader) {
                    values = ImportUtil.getAdjustHeader(values);
                    header = values;
                    if (CollectionUtils.isEmpty(values)) {
                        result.setIsSuccess(false);
                        result.setResultMessage("csv格式错误,标题行不能为空");
                        return result;
                    }
                } else {
                    values = ImportUtil.getAdjustContent(values, header.size());
                }

                if (CollectionUtils.isEmpty(values)) {
                    isNull = true;  //处理最后连续的空行,数据中间不能有空行
                    nullLineNum = nullLineNum == 0 ? line : nullLineNum;
                    continue;
                } else if (isNull) {
                    result.setIsSuccess(false);
                    result.setResultMessage("csv格式错误,第" + nullLineNum + "行数据为空");
                    return result;
                }
                lists.add(values);
                isHeader = false;
            } else {
                if (isHeader) {
                    result.setIsSuccess(false);
                    result.setResultMessage("csv格式错误,标题行不能为空");
                    return result;
                } else {
                    isNull = true;
                    nullLineNum = nullLineNum == 0 ? line : nullLineNum;
                }
            }
        }

        if (lists.size() <= 1) {
            result.setIsSuccess(false);
            result.setResultMessage("csv格式错误,没有数据");
            return result;
        }

        List<String> attr = ImportUtil.getAttrList(lists.get(0), attrMap, result); //第一行是头
        if (!result.isSuccess()) {
            return result;
        } else {
            lists = lists.subList(1, lists.size());  //去掉第一行,其他是数据行
        }

        int attrSize = attr.size();
        for (List<String> data : lists) {
            for (int i = data.size(); i < attrSize; i++) {
                data.add(null);
            }
        }

        objList = ImportUtil.fillBean(lists, attr, clazz, result, false, dateFormat, 1);
        if (!result.isSuccess()) {
            return result;
        }

        line = 1;
        for (T obj : objList) {
            line++;
            if (checker != null && !checker.checkLine(obj, result)) {
                result.setIsSuccess(false);
                result.setResultMessage("csv格式错误,第" + line + "行数据验证异常|" + result.getResultMessage());
                return result;
            }
        }
        result.setData(objList);
        return result;
    }
}
