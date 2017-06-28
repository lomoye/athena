//package com.lomoye.common.file.export;
//
//import com.lomoye.common.file.TestDomain;
//import com.google.common.collect.Lists;
//import org.apache.poi.ss.usermodel.Workbook;
//import org.junit.Test;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import static org.junit.Assert.assertTrue;
//
///**
// * Created by tommy on 2016/2/3.
// */
//public class ExcelExporterTest {
//    @Test
//    public void testExportExcelFromList() throws Exception {
//        List<TestDomain> lists = new ArrayList<>();
//        lists.add(new TestDomain(12L, 13, "唐密", new Date()));
//        lists.add(new TestDomain(122L, 3333, "唐密", new Date()));
//
//        List<String> attrList = Lists.newArrayList("id", "age", "name", "birthday");
//
//        List<String> header = Lists.newArrayList("id", "年龄", "name", "出生日期");
//        Workbook workbook = ExcelExporter.exportExcelFromList("test", lists, attrList, header, TestDomain.class);
//
//        assertTrue(workbook != null);
//
//        URL resource = getClass().getClassLoader().getResource("testdata");
//
//        String path = resource.getPath();
//        File file = new File(path + "/test.xls");
//        workbook.write(new FileOutputStream(file));
//    }
//
//    @Test
//    public void testGetValueList() throws Exception {
//
//    }
//}
