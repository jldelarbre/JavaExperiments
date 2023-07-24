package com.github.jldelarbre.javaExperiments.request.impl;

import com.github.jldelarbre.javaExperiments.request.Request;
import com.github.jldelarbre.javaExperiments.request.RequestExecutor;

public interface ExecutableRequest extends Request {
    Request.Result execute();

    void undo();

    void redo();

    boolean isAlreadyExecuted();

    /**
     * shallBeAddedToUndoRedoQueue
     * @return Requests like "save" action shall not be added to the undo/redo queue
     */
    boolean shallBeAddedToUndoRedoStack();

    /**
     * implyUndoRedoQueueReset
     * @return Requests like "load" action imply a reset of undo/redo queue
     * Any action before this are forgotten
     */
    boolean implyUndoRedoStackReset();

    /**
     * default {@code false}
     * @return {@code true} if the request should be executed in a dedicated thread by {@link RequestExecutor}
     */
    boolean isNeededSeparateThread();
}
