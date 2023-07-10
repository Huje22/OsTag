package me.indian.ostag.util;

import java.util.concurrent.ThreadFactory;

public class ThreadUtil implements ThreadFactory {
    private final String threadName;
    private final Runnable runnable;
    private int threadCount;

    public ThreadUtil(final String threadName) {
        this.threadName = MessageUtil.colorize("OsTag " + threadName + "-%b");
        this.runnable = null;
        this.threadCount = 0;
    }

    public ThreadUtil(final String threadName, final Runnable runnable) {
        this.threadName = MessageUtil.colorize("OsTag " + threadName + "-%b");
        this.runnable = runnable;
        this.threadCount = 0;
    }

    @Override
    public Thread newThread(final Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.setName(generateThreadName());
        return thread;
    }

    public Thread newThread() {
        Thread thread = new Thread(runnable);
        thread.setName(generateThreadName());
        return thread;
    }

    private String generateThreadName() {
        this.threadCount++;
        return this.threadName.replace("%b", String.valueOf(threadCount));
    }
}
