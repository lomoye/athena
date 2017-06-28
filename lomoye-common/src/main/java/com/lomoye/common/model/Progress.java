package com.lomoye.common.model;

import com.lomoye.common.enums.ProgressStatus;

/**
 * 返回给前端的进度对象.
 */
public class Progress {
    private long total;

    private long processed;

    private long success;

    private long failed;

    private String status; //Ref: ProgressStatus

    private long percent;  //完成度的进度百分比

    private long leftTime;  //预计还需要的时间,不需要就不填

    private String message; //可能需要的描述,不需要就不填

    public Progress() {
        status = ProgressStatus.IN_PROGRESS;
    }

    public Progress(long total, long success, long failed, long leftTime) {
        this.total = total;
        this.processed = success + failed;
        this.success = success;
        this.failed = failed;
        this.leftTime = leftTime;

        if (total == 0) {
            percent = 100;
        } else {
            percent = processed * 100 / total;
        }
        if (percent > 100) {
            percent = 100;
        }
        if (percent == 100) {
            status = ProgressStatus.SUCCESS;
        } else {
            status = ProgressStatus.IN_PROGRESS;
        }
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getProcessed() {
        return processed;
    }

    public void setProcessed(long processed) {
        this.processed = processed;
    }

    public long getSuccess() {
        return success;
    }

    public void setSuccess(long success) {
        this.success = success;
    }

    public long getFailed() {
        return failed;
    }

    public void setFailed(long failed) {
        this.failed = failed;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getPercent() {
        return percent;
    }

    public void setPercent(long percent) {
        this.percent = percent;
    }

    public long getLeftTime() {
        return leftTime;
    }

    public void setLeftTime(long leftTime) {
        this.leftTime = leftTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
