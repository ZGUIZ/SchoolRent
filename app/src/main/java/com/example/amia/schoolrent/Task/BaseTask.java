package com.example.amia.schoolrent.Task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public interface BaseTask {
    ExecutorService service = Executors.newSingleThreadExecutor();
}
