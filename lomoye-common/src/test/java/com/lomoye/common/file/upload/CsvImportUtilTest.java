package com.lomoye.common.file.upload;

import com.lomoye.common.model.ResultModel;
import com.lomoye.common.util.SerializationUtil;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tommy on 2017/2/21.
 */
public class CsvImportUtilTest {

    @Test
    public void testImportFromCsv() throws Exception {
        URL resource = getClass().getClassLoader().getResource("testdata");

        String path = resource.getPath();
        File file = new File(path + "/test.csv");
        Map<String, String> attrMap = new HashMap<>();
        attrMap.put("id", "id");
        attrMap.put("年龄", "age");
        attrMap.put("名字", "name");
        attrMap.put("生日", "birthday");

        InputStream in = new BufferedInputStream(new FileInputStream(file));
        ResultModel<CsvTestDomain> result = CsvImportUtil.importFromCsv(in, ",", "UTF-8", attrMap, CsvTestDomain.class, null, "yyyy-MM-dd HH:mm:ss");

        System.out.println("res=" + SerializationUtil.gson2String(result));
    }

    static class CsvTestDomain {
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