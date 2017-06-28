package com.lomoye.common.concurrent;

/**
 * 记录TaskQueue的一些统计信息
 * Created by tommy on 2016/12/26.
 */
public class TaskQueueStat {
    private long wait; //等待中的

    private long ready; //就绪的任务

    private long process; //正在处理的

    private long finished; //上次一段时间处理完成的任务数量(目前为10分钟)

    private long totalTime; //总共处理的时间

    private long avgTime; //平均处理时间

    private long maxTime; //最长处理时间

    private long minTime; //最短处理时间

    private long threadNum; //线程数量

    public TaskQueueStat() {
        wait = 0;
        ready = 0;
        process = 0;
        finished = 0;
        totalTime = 0;
        avgTime = 0;
        maxTime = 0;
        maxTime = 0;
        threadNum = 0;
    }

    public long getWait() {
        return wait;
    }

    public void setWait(long wait) {
        this.wait = wait;
    }

    public long getReady() {
        return ready;
    }

    public void setReady(long ready) {
        this.ready = ready;
    }

    public long getProcess() {
        return process;
    }

    public void setProcess(long process) {
        this.process = process;
    }

    public long getFinished() {
        return finished;
    }

    public void setFinished(long finished) {
        this.finished = finished;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }

    public long getAvgTime() {
        return avgTime;
    }

    public void setAvgTime(long avgTime) {
        this.avgTime = avgTime;
    }

    public long getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(long maxTime) {
        this.maxTime = maxTime;
    }

    public long getMinTime() {
        return minTime;
    }

    public void setMinTime(long minTime) {
        this.minTime = minTime;
    }

    public long getThreadNum() {
        return threadNum;
    }

    public void setThreadNum(long threadNum) {
        this.threadNum = threadNum;
    }
}
