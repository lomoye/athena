package com.lomoye.common.concurrent;

import com.lomoye.common.exception.BusinessException;
import com.lomoye.common.model.ResultModel;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 多线程批量通过idList获取数据的接口
 * Created by tommy on 2015/9/8.
 */
public class BatchedFetcher<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BatchedFetcher.class);

    private static final int DEFAULT_MAX_THREAD = 5;

    private int batchSize;

    private boolean useThread;

    private int minThreadNum;

    private int minBatchPerThread;

    private int maxThreadNum;

    private List<Long> ids = new ArrayList<>();

    private BatchedMethod<T> method;

    public BatchedFetcher(int batchSize, int minBatchPerThread, List<Long> ids) {
        this(ids, minBatchPerThread, 2, DEFAULT_MAX_THREAD, true, batchSize);
    }

    public BatchedFetcher(List<Long> ids, int minBatchPerThread, int minThreadNum, int maxThreadNum, boolean useThread, int batchSize) {
        this.ids = ids;
        this.minBatchPerThread = minBatchPerThread;
        this.minThreadNum = minThreadNum;
        this.maxThreadNum = maxThreadNum;
        this.useThread = useThread;
        this.batchSize = batchSize;
    }

    public ResultModel<T> fetchData() {
        if (CollectionUtils.isEmpty(ids)) {
            return new ResultModel<>();
        }

        int count = ids.size();
        int threadCnt = (count + minBatchPerThread - 1) / minBatchPerThread;
        if (threadCnt <= minThreadNum || !useThread) {
            ResultModel<T> result = fetchData(ids);
            if (!result.isSuccess()) {
                LOGGER.warn("Batch fetch error happen|" + result.getResultMessage());
                throw new BusinessException(result.getResultCode(), result.getResultMessage());
            } else {
                return result;
            }
        }
        threadCnt = Math.min(maxThreadNum, threadCnt);

        List<FetcherThread> threads = new ArrayList<>();
        int perThreadIds = (count + threadCnt - 1) / threadCnt;

        LOGGER.debug("Batch fetch start time :" + System.currentTimeMillis());
        for (int i = 0; i < threadCnt; i++) {
            int end = Math.min((i + 1) * perThreadIds, count);
            List<Long> subList = ids.subList(i * perThreadIds, end);
            FetcherThread thread = new FetcherThread(subList, "fetch-thread-" + i);
            threads.add(thread);
            thread.start();
        }
        LOGGER.debug("Batch fetch started time :" + System.currentTimeMillis());
        List<T> dataList = new ArrayList<>(count);
        for (FetcherThread thread : threads) {
            ThreadUtil.safeJoin(thread, "Batch fetch fetch thread interrupted error.....");
            ResultModel<T> result = thread.getResultModel();
            if (!result.isSuccess()) {
                LOGGER.warn("Batch fetch error happen|" + result.getResultMessage());
                throw new BusinessException(result.getResultCode(), result.getResultMessage());
            }
            dataList.addAll(result.getData());
        }
        LOGGER.debug("Batch fetch end time :" + System.currentTimeMillis());
        return new ResultModel<>(dataList);
    }

    public BatchedFetcher<T> minBatchPerThread(int minBatchPerThread) {
        this.minBatchPerThread = minBatchPerThread;
        return this;
    }

    public BatchedFetcher<T> minThreadNum(int minThreadNum) {
        this.minThreadNum = minThreadNum;
        return this;
    }

    public BatchedFetcher<T> useThread(boolean useThread) {
        this.useThread = useThread;
        return this;
    }


    public BatchedFetcher<T> batchSize(int batchSize) {
        this.batchSize = batchSize;
        return this;
    }

    public BatchedFetcher<T> ids(List<Long> ids) {
        this.ids = ids;
        return this;
    }

    public BatchedFetcher<T> batchedMethod(BatchedMethod<T> method) {
        this.method = method;
        return this;
    }

    class FetcherThread extends Thread {
        private List<Long> ids;
        private ResultModel<T> result;

        public FetcherThread(List<Long> ids, String threadName) {
            super(threadName);
            this.ids = ids;
            result = new ResultModel<>();
            result.setIsSuccess(false);
        }

        @Override
        public void run() {
            try {
                result = fetchData(ids);
            } catch (Exception e) {
                LOGGER.warn("FetcherThread fetch data failed", e);
            }
        }

        public ResultModel<T> getResultModel() {
            return result;
        }
    }

    private ResultModel<T> fetchData(List<Long> ids) {
        ResultModel<T> result = new ResultModel<>();

        LOGGER.debug("Thread " + Thread.currentThread().getName() + " batch fetch start: " + System.currentTimeMillis());
        int count = ids.size();
        for (int i = 0; i <= count / batchSize; i++) {
            int end = Math.min(count, (i + 1) * batchSize);
            List<Long> subIids = ids.subList(i * batchSize, end);
            if (!subIids.isEmpty()) {
                ResultModel<T> resp = method.getResultByIds(subIids);
                if (!resp.isSuccess()) {
                    return resp;
                } else {
                    result.addData(resp.getData());
                }
            }
        }
        LOGGER.debug("Thread " + Thread.currentThread().getName() + " batch fetch end: " + System.currentTimeMillis());
        return result;
    }
}
