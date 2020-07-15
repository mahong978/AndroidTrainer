package com.example.androidtrainer.util;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public final class ThreadManager {

    private static final int CORE_POOL_SIZE = 8;
    private static final int MAXIMUM_POOL_SIZE = 8;
    private static final int KEEP_ALIVE_TIME = 5;
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.MINUTES;

    private static ThreadManager INSTANCE;

    private final BlockingQueue<Runnable> workQueue;

    private final ThreadPoolExecutor threadPoolExecutor;

    static {
        INSTANCE = new ThreadManager();
    }

    private ThreadManager() {
        workQueue = new LinkedBlockingQueue<>();

        threadPoolExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE,
                KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT, workQueue);
    }

    public static ThreadManager getInstance() {
        return INSTANCE;
    }

    public static void enqueue(Runnable runnable) {
        getInstance().threadPoolExecutor.execute(runnable);
    }

}
