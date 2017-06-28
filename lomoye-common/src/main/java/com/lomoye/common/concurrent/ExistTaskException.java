package com.lomoye.common.concurrent;

/**
 * TaskQueue出现多个同类型task,但是队列又不支持
 * Created by tommy on 2017/2/13.
 */
public class ExistTaskException extends RuntimeException {

    public ExistTaskException(String msg) {
        super(msg);
    }

    public ExistTaskException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
