package com.lomoye.common.file.upload;

import com.lomoye.common.file.ReflectionHelper;
import com.lomoye.common.model.ResultModel;
import com.lomoye.common.util.DateUtil;
import com.lomoye.common.util.MoneyUtil;
import com.google.common.base.Predicates;
import com.google.common.base.Strings;
import org.apache.commons.collections.CollectionUtils;
import org.reflections.ReflectionUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * 导入的基础工具类,供ExcelImport和CsvImport使用
 */
class ImportUtil {
    public static <T> List<String> getAttrList(List<String> headerChName, Map<String, String> attrMap, ResultModel<T> result) {
        List<String> attrList = new ArrayList<>();
        for (String chName : headerChName) {
            String attr = attrMap.get(chName.trim());
            attrList.add(attr);
        }
        return attrList;
    }

    public static <T> List<T> fillBean(List<List<String>> lists, List<String> attr, Class<T> clazz, ResultModel<T> result, boolean exactlyMatch, String dateFormat, int offset) {
        List<T> objList = new ArrayList<>();

        Set<Method> methods = ReflectionUtils.getAllMethods(clazz, Predicates.and(ReflectionUtils.withModifier(Modifier.PUBLIC),
                ReflectionUtils.withPrefix("set")));
        Map<String, Method> methodMap = new HashMap<>();


        for (Method method : methods) {
            String attrName = ReflectionHelper.getAttrNameFromMethod(method.getName());
            if (attr.contains(attrName)) {
                methodMap.put(attrName, method);
            }
        }

        if (methodMap.keySet().size() != attr.size() && exactlyMatch) {
            result.setIsSuccess(false);
            result.setResultMessage("上传的文件格式错误,列表和数据不一致,处理失败");
            return objList;
        }

        int line = offset;
        try {
            for (List<String> list : lists) {
                line++;
                T obj = doFillBean(list, attr, clazz, methodMap, result, line, dateFormat);
                if (!result.isSuccess()) {
                    return objList;
                }
                objList.add(obj);
            }
        } catch (Exception e) {
            result.setIsSuccess(false);
            result.setResultMessage("上传的文件数据错误,第" + line + "行数据填充异常");
        }

        return objList;
    }

    public static <T> T doFillBean(List<String> list, List<String> attr, Class<T> clazz, Map<String, Method> methodMap, ResultModel<T> result, int line, String dateFormat) throws IllegalAccessException, InstantiationException {
        T obj = clazz.newInstance();
        for (int i = 0; i < attr.size(); i++) {
            if (attr.get(i) == null) {
                continue;
            }
            Method method = methodMap.get(attr.get(i));
            try {
                Object val = getValueFromString(attr.get(i), list.get(i), method, line, result, dateFormat);
                if (!result.isSuccess()) {
                    return obj;
                }
                method.invoke(obj, val);
            } catch (Exception e) {
                result.setIsSuccess(false);
                result.setResultMessage("上传的文件格式错误,不能获取第" + line + "行对应列的数据|列名是 " + attr.get(i));
                return obj;
            }
        }
        return obj;
    }

    public static <T> Object getValueFromString(String head, String str, Method method, int line, ResultModel<T> result, String dateFormat) {
        Class<?>[] types = method.getParameterTypes();
        if (types == null || types.length == 0) {
            throw new RuntimeException("找不到对应的set类型|" + method.getName());
        }
        String type = types[0].getSimpleName();
        if (Strings.isNullOrEmpty(str)) {
            return null;
        }

        if (type.equalsIgnoreCase("String")) {
            return str;
        } else if (type.equalsIgnoreCase("Character")) {
            return str.charAt(0);
        } else if (type.equalsIgnoreCase("Long")) {
            if (str.contains(".")) {
                return MoneyUtil.conventStringToLong(str); //小数点,钱相关的需要*100
            } else {
                return Long.valueOf(str);
            }
        } else if (type.equalsIgnoreCase("Integer")) {
            return Integer.valueOf(str);
        } else if (type.equalsIgnoreCase("Short")) {
            return Short.valueOf(str);
        } else if (type.equalsIgnoreCase("Byte")) {
            return Byte.valueOf(str);
        } else if (type.equalsIgnoreCase("Float")) {
            return Float.valueOf(str);
        } else if (type.equalsIgnoreCase("Double")) {
            return Double.valueOf(str);
        } else if (type.equalsIgnoreCase("Boolean")) {
            return Boolean.valueOf(str);
        } else if (type.equalsIgnoreCase("Date")) {
            if (!Strings.isNullOrEmpty(dateFormat)) {
                return DateUtil.parseDateTime(dateFormat, str);
            }
            return new Date(Long.valueOf(str));
        }
        result.setIsSuccess(false);
        result.setResultMessage("上传的文件格式错误,不能获取第" + line + "行对应列的数据|列名是 " + head);
        return null;

    }

    public static List<String> getAdjustHeader(List<String> header) {
        if (CollectionUtils.isEmpty(header)) {
            return header;
        }
        int columnNo = header.size() - 1;
        for (int i = columnNo; i >= 0; i--) {
            columnNo = i;
            if (!Strings.isNullOrEmpty(header.get(i))) {
                break;
            }
        }
        columnNo = columnNo + 1;
        header = header.subList(0, columnNo);
        return header;
    }

    public static List<String> getAdjustContent(List<String> line, int headerLength) {
        if (CollectionUtils.isEmpty(line)) {
            return line;
        }
        for (int i = line.size(); i < headerLength; i++) {
            line.add(null);
        }

        if (line.size() > headerLength) {
            line = line.subList(0, headerLength);
        }

        boolean isNull = true;
        for (String col : line) {
            if (!Strings.isNullOrEmpty(col)) {
                isNull = false;
                break;
            }
        }
        if (isNull) {
            return null;
        }
        return line;
    }
}
