package com.github.jldelarbre.javaExperiments.request.impl;

import com.github.jldelarbre.javaExperiments.request.RequestExecutor;
import com.github.jldelarbre.javaExperiments.request.ServiceProvider;
import com.github.jldelarbre.javaExperiments.request.UndoRedoService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class ServiceProviderImpl implements ServiceProvider {

    private final RequestExecutor requestExecutor;
    private final UndoRedoManager undoRedoManager;

    private ServiceProviderImpl(RequestExecutor requestExecutor, UndoRedoManager undoRedoManager) {
        this.requestExecutor = requestExecutor;
        this.undoRedoManager = undoRedoManager;
    }

    public static ServiceProvider aServiceProvider() {
        // Inject the same service provider for requestExecutor and undoRedoManager
        // so that any execute, undo or redo operation on request are processed sequentially
        // as single thread executor guaranties
        ExecutorService executor = Executors.newSingleThreadExecutor();
        UndoRedoManager undoRedoManager = new UndoRedoManagerImpl(executor);
        RequestExecutor requestExecutor = new RequestExecutorImpl(undoRedoManager, executor);
        return new ServiceProviderImpl(requestExecutor, undoRedoManager);
    }

    @Override
    public RequestExecutor getRequestExecutor() {
        return requestExecutor;
    }

    @Override
    public UndoRedoService getUndoRedoService() {
        return undoRedoManager;
    }
}
