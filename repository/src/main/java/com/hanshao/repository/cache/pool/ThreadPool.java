package com.hanshao.repository.cache.pool;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * AUTHOR: hanshao
 * DATE: 17/6/12.
 * ACTION:线程池
 */

public class ThreadPool {


    //线程池
    private static ThreadPoolExecutor threadPool = null;

    public static ThreadPoolExecutor pool() {
        if (threadPool == null) {
            synchronized (ThreadPool.class) {
                ThreadPoolExecutor temp = threadPool;
                if (temp == null) {
                    temp = new ThreadPoolExecutor
                            (8, 8, 80000, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<Runnable>(16));
                    threadPool = temp;
                }
            }

        }
        return threadPool;
    }
}

