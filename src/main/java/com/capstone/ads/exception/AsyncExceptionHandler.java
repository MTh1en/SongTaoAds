package com.capstone.ads.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

@Slf4j
public class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {
    @Override
    public void handleUncaughtException(Throwable ex, Method method, Object... params) {
        // Ghi log lỗi của tất cả các phương thức @Async
        log.error("Async method has an exception: {}", method.getName(), ex);
    }
}