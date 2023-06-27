package me.indian.ostag.util;

import java.util.concurrent.ThreadFactory;

public class ThreadUtil implements ThreadFactory {
    private final String threadName;
    private final Runnable runnable;


    public ThreadUtil(final String threadName) {
        this.threadName = threadName;
        this.runnable = null;
    }

    public ThreadUtil(final String threadName, final Runnable runnable) {
        this.threadName = threadName;
        this.runnable = runnable;
    }

    @Override
    public Thread newThread(final Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.setName(threadName);
        return thread;
    }

    public Thread newThread() {
        Thread thread = new Thread(runnable);
        thread.setName(threadName);
        return thread;
    }
}
