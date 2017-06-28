package com.lomoye.common.util;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * 测试主要注意:
 * 1. 空串的情况
 * 2. 异常格式的处理
 * Created by tommy on 2017/2/23.
 */
public class SplitterUtilTest {

    @Test
    public void testSplitToIntegerList() throws Exception {
        String s = null;
        String splitter = ",";

        List<Integer> res;
        res = SplitterUtil.splitToIntegerList(s, splitter, null);
        assertTrue(res.isEmpty());

        s = "";
        res = SplitterUtil.splitToIntegerList(s, splitter, null);
        assertTrue(res.isEmpty());

        s = "1";
        res = SplitterUtil.splitToIntegerList(s, splitter, null);
        assertTrue(res.size() == 1);

        s = "1,2";
        res = SplitterUtil.splitToIntegerList(s, splitter, null);
        assertTrue(res.size() == 2);

        s = "1!2";
        res = SplitterUtil.splitToIntegerList(s, splitter, null);
        assertTrue(res.size() == 0);

        s = "1!2, 2";
        res = SplitterUtil.splitToIntegerList(s, splitter, null);
        assertTrue(res.size() == 1);

        s = "1, 655534";
        res = SplitterUtil.splitToIntegerList(s, splitter, null);
        assertTrue(res.size() == 2);

    }

    @Test
    public void testSplitToLongList() throws Exception {
        String s = null;
        String splitter = ",";

        List<Long> res;
        res = SplitterUtil.splitToLongList(s, splitter, null);
        assertTrue(res.isEmpty());

        s = "";
        res = SplitterUtil.splitToLongList(s, splitter, null);
        assertTrue(res.isEmpty());

        s = "1";
        res = SplitterUtil.splitToLongList(s, splitter, null);
        assertTrue(res.size() == 1);

        s = "1,2";
        res = SplitterUtil.splitToLongList(s, splitter, null);
        assertTrue(res.size() == 2);

        s = "1!2";
        res = SplitterUtil.splitToLongList(s, splitter, null);
        assertTrue(res.size() == 0);

        s = "1!2, 2";
        res = SplitterUtil.splitToLongList(s, splitter, null);
        assertTrue(res.size() == 1);

        s = "1, 123456789012345678";
        res = SplitterUtil.splitToLongList(s, splitter, null);
        assertTrue(res.size() == 2);
    }
}