package me.indian.ostag.util;

import java.util.concurrent.ThreadFactory;

public class ThreadUtil implements ThreadFactory {
    private final String threadName;

    public ThreadUtil(String threadName) {
        this.threadName = threadName;
    }

    @Override
    public Thread newThread(Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.setName(threadName.replace(" ", "-"));
        return thread;
    }
}
