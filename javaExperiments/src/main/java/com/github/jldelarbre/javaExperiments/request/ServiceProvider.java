package com.github.jldelarbre.javaExperiments.request;

public interface ServiceProvider {

    RequestExecutor getRequestExecutor();

    UndoRedoService getUndoRedoService();
}
