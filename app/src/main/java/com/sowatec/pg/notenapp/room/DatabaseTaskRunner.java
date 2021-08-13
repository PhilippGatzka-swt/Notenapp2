package com.sowatec.pg.notenapp.room;

import android.os.Handler;
import android.os.Looper;

import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseTaskRunner {
    private final Logger LOGGER = Logger.getLogger(getClass().getName());
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler(Looper.getMainLooper());

    public <R> void executeAsync(Callable<R> callable, Callback<R> callback) {
        executor.execute(() -> {
            final R result;
            try {
                result = callable.call();
                handler.post(() -> callback.onComplete(result));
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, Arrays.toString(e.getStackTrace()));
            }
        });
    }

    public interface Callback<R> {
        void onComplete(R result);
    }
}
