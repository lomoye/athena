package com.lomoye.common.file.upload;

import com.lomoye.common.model.ResultModel;
import com.lomoye.common.util.SerializationUtil;
import com.google.common.collect.Lists;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.assertTrue;

/**
 * Created by tommy on 2016/2/3.
 */
public class ExcelImportUtilTest {

    @Test
    public void testImportFromExcel() throws Exception {
        URL resource = getClass().getClassLoader().getResource("testdata");

        String path = resource.getPath();
        File file = new File(path + "/test.xls");

        Workbook workbook = ExcelImportUtil.getWorkbook("test.xls", new FileInputStream(file));
        Sheet sheet = workbook.getSheet("test");
        List<String> attrList = Lists.newArrayList("id", "age", "name", "birthday");

        ResultModel<TestDomain> result = ExcelImportUtil.importFromExcel(workbook, sheet, attrList, TestDomain.class, null, true, null);

        assertTrue(!result.isSuccess() && Objects.equals("excel格式错误,第6行数据为空", result.getResultMessage()));

        System.out.println(SerializationUtil.gson2String(result));
    }

    @Test
    public void testImportListFromExcel() throws Exception {
        URL resource = getClass().getClassLoader().getResource("testdata");

        String path = resource.getPath();
        File file = new File(path + "/test-string.xlsx");

        Workbook workbook = ExcelImportUtil.getWorkbook("test-string.xlsx", new FileInputStream(file));
        Sheet sheet = workbook.getSheet("test");

        ResultModel<List<String>> result = ExcelImportUtil.importListFromExcel(workbook, sheet, true);
        assertTrue(result.isSuccess() && result.getData().get(0).size() == 1);
        System.out.println("res=" + SerializationUtil.gson2String(result));
    }


    static class TestDomain {
        private Long id;
        private Long age;
        private String name;
        private Date birthday;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getAge() {
            return age;
        }

        public void setAge(Long age) {
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Date getBirthday() {
            return birthday;
        }

        public void setBirthday(Date birthday) {
            this.birthday = birthday;
        }
    }
}