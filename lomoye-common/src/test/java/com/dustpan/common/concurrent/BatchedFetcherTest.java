package com.lomoye.common.concurrent;

import com.lomoye.common.model.ResultModel;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * 测试类
 * Created by tommy on 2017/2/22.
 */
public class BatchedFetcherTest {

    @Test
    public void testFetchData() throws Exception {
        List<Long> list = Lists.newArrayList(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L, 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L);
        BatchedFetcher<Long> fetcher = new BatchedFetcher<>(2, 3, list);
        fetcher.batchedMethod(new BatchedMethod<Long>() {
            @Override
            public ResultModel<Long> getResultByIds(List<Long> ids) {
                List<Long> arr = new ArrayList<>();
                arr.addAll(ids);
                return new ResultModel<>(arr);
            }
        });
        ResultModel<Long> res = fetcher.fetchData();
        assertTrue(res.isSuccess() && res.getData().size() == 20);

        fetcher = new BatchedFetcher<>(3, 19, list);
        fetcher.batchedMethod(new BatchedMethod<Long>() {
            @Override
            public ResultModel<Long> getResultByIds(List<Long> ids) {
                List<Long> arr = new ArrayList<>();
                arr.addAll(ids);
                return new ResultModel<>(arr);
            }
        });
        res = fetcher.fetchData();
        assertTrue(res.isSuccess() && res.getData().size() == 20);
    }

    @Test
    public void testFetchDataException() throws Exception {
        List<Long> list = Lists.newArrayList(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L, 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L);
        BatchedFetcher<Long> fetcher = new BatchedFetcher<>(2, 2, list);
        fetcher.batchedMethod(new BatchedMethod<Long>() {
            @Override
            public ResultModel<Long> getResultByIds(List<Long> ids) {
                if (ids.contains(8L)) {
                    throw new RuntimeException("test exception");
                }
                List<Long> arr = new ArrayList<>();
                arr.addAll(ids);
                return new ResultModel<>(arr);
            }
        });
        Exception e = null;
        try {
            ResultModel<Long> res = fetcher.fetchData();
        } catch (Exception e1) {
            e = e1;
        }
        assertTrue(e != null);
    }

    @Test
    public void testFetchDataSingleThread() throws Exception {
        List<Long> list = Lists.newArrayList(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L, 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L);

        BatchedFetcher<Long> fetcher = new BatchedFetcher<>(2, 20, list);
        fetcher.batchedMethod(new BatchedMethod<Long>() {
            @Override
            public ResultModel<Long> getResultByIds(List<Long> ids) {
                List<Long> arr = new ArrayList<>();
                arr.addAll(ids);
                return new ResultModel<>(arr);
            }
        });
        ResultModel<Long> res = fetcher.fetchData();
        assertTrue(res.isSuccess() && res.getData().size() == 20);
    }

    @Test
    public void testFetchDataSingleThreadException() throws Exception {
        List<Long> list = Lists.newArrayList(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L, 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L);

        BatchedFetcher<Long> fetcher = new BatchedFetcher<>(2, 20, list);
        fetcher.batchedMethod(new BatchedMethod<Long>() {
            @Override
            public ResultModel<Long> getResultByIds(List<Long> ids) {
                if (ids.contains(8L)) {
                    throw new RuntimeException("test exception");
                }
                List<Long> arr = new ArrayList<>();
                arr.addAll(ids);
                return new ResultModel<>(arr);
            }
        });

        Exception e = null;
        try {
            ResultModel<Long> res = fetcher.fetchData();
        } catch (Exception e1) {
            e = e1;
        }
        assertTrue(e != null);
    }
}