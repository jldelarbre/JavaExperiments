package com.github.jldelarbre.javaExperiments.request.impl;

import com.google.common.collect.ImmutableList;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

import static com.github.jldelarbre.javaExperiments.request.impl.RequestExecutorImpl.ORDERING_TASK;

public final class UndoRedoManagerImpl implements UndoRedoManager {

    private final Deque<ExecutableRequest> undoStack = new LinkedList<>();
    private final Deque<ExecutableRequest> redoStack = new LinkedList<>();
    private final ExecutorService executor;

    public UndoRedoManagerImpl(ExecutorService executor) {
        this.executor = executor;
    }

    @Override
    public void undo() {
        if (undoStack.isEmpty()) {
            return;
        }
        ExecutableRequest requestToUndo = undoStack.pop();
        redoStack.push(requestToUndo);
        try {
            if (requestToUndo.isNeededSeparateThread()) {
                UndoTask undoTask = new UndoTask(requestToUndo);
                executor.submit(undoTask);
            } else {
                // ORDERING_TASK submit not checked by test for undo/redo
                executor.submit(ORDERING_TASK).get();
                requestToUndo.undo();
            }
        } catch (Throwable t) {
            // TODO: Report Error to user
            // Adapt to the appropriate way used by your application
            System.err.println(t.getMessage() + " (" + t.getClass().getSimpleName() + ")");
        }
    }

    @Override
    public void redo() {
        if (redoStack.isEmpty()) {
            return;
        }
        ExecutableRequest redoableRequest = redoStack.pop();
        undoStack.push(redoableRequest);
        try {
            if (redoableRequest.isNeededSeparateThread()) {
                RedoTask redoTask = new RedoTask(redoableRequest);
                executor.submit(redoTask);
            } else {
                // ORDERING_TASK submit not checked by test for undo/redo
                executor.submit(ORDERING_TASK).get();
                redoableRequest.redo();
            }
        } catch (Throwable t) {
            // TODO: Report Error to user
            // Adapt to the appropriate way used by your application
            System.err.println(t.getMessage() + " (" + t.getClass().getSimpleName() + ")");
        }
    }

    @Override
    public List<String> getUndoActionShortDescriptions() {
        return undoStack.stream()
                .map(ExecutableRequest::getShortDescription)
                .collect(ImmutableList.toImmutableList());
    }

    @Override
    public List<String> getRedoActionShortDescriptions() {
        return redoStack.stream()
                .map(ExecutableRequest::getShortDescription)
                .collect(ImmutableList.toImmutableList());
    }

    @Override
    public void registerExecutedRequest(ExecutableRequest request) {
        redoStack.clear();

        if (request.implyUndoRedoStackReset()) {
            undoStack.clear();
            return;
        }

        if (request.shallBeAddedToUndoRedoStack()) {
            undoStack.push(request);
        }
    }

    private static final class UndoTask implements Callable<Void> {

        private final ExecutableRequest requestToUndo;

        private UndoTask(ExecutableRequest requestToUndo) {
            this.requestToUndo = requestToUndo;
        }

        @Override
        public Void call() throws Exception {
            requestToUndo.undo();
            return null;
        }
    }

    private static final class RedoTask implements Callable<Void> {

        private final ExecutableRequest requestToRedo;

        private RedoTask(ExecutableRequest requestToRedo) {
            this.requestToRedo = requestToRedo;
        }

        @Override
        public Void call() throws Exception {
            requestToRedo.redo();
            return null;
        }
    }
}
