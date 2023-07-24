package com.github.jldelarbre.javaExperiments.request.utils;

import com.github.jldelarbre.javaExperiments.request.impl.ExecutableRequest;
import com.github.jldelarbre.javaExperiments.request.Request;
import com.github.jldelarbre.javaExperiments.request.impl.ResultImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Semaphore;

import static com.github.jldelarbre.javaExperiments.request.Request.ExecutionStatus.FAILURE;
import static com.github.jldelarbre.javaExperiments.request.Request.ExecutionStatus.SUCCESS;

public final class FakeRequest implements ExecutableRequest {

    private final Request.Result result;
    private final UUID id;
    private final String shortDescription;
    private final String description;
    private final Optional<String> errorMessage;
    private final boolean shallBeAddedToUndoRedoStack;
    private final boolean implyUndoRedoStackReset;
    private boolean isAlreadyExecuted = false;
    private final List<String> actions;
    private final List<Long> threads;
    private final boolean needSeparateThread;
    private final Optional<Semaphore> semaphoreToRelease;

    public FakeRequest(Result result,
                       UUID id,
                       String shortDescription,
                       String description,
                       Optional<String> errorMessage,
                       boolean shallBeAddedToUndoRedoStack,
                       boolean implyUndoRedoStackReset,
                       List<String> actions,
                       List<Long> threads,
                       boolean needSeparateThread,
                       Optional<Semaphore> semaphore) {
        this.result = result;
        this.id = id;
        this.shortDescription = shortDescription;
        this.description = description;
        this.errorMessage = errorMessage;
        this.shallBeAddedToUndoRedoStack = shallBeAddedToUndoRedoStack;
        this.implyUndoRedoStackReset = implyUndoRedoStackReset;
        this.actions = actions;
        this.threads = threads;
        this.needSeparateThread = needSeparateThread;
        this.semaphoreToRelease = semaphore;
    }

    @Override
    public Request.Result execute() {
        isAlreadyExecuted = true;
        actions.add("execute");
        threads.add(Thread.currentThread().getId());
        if (semaphoreToRelease.isPresent()) {
            semaphoreToRelease.get().release();
        }
        if (errorMessage.isPresent()) {
            throw new RuntimeException(errorMessage.get());
        }
        return result;
    }

    @Override
    public void undo() {
        actions.add("undo");
        threads.add(Thread.currentThread().getId());
        if (semaphoreToRelease.isPresent()) {
            semaphoreToRelease.get().release();
        }
    }

    @Override
    public void redo() {
        actions.add("redo");
        threads.add(Thread.currentThread().getId());
        if (semaphoreToRelease.isPresent()) {
            semaphoreToRelease.get().release();
        }
    }

    @Override
    public boolean isAlreadyExecuted() {
        return isAlreadyExecuted;
    }

    @Override
    public boolean shallBeAddedToUndoRedoStack() {
        return shallBeAddedToUndoRedoStack;
    }

    @Override
    public boolean implyUndoRedoStackReset() {
        return implyUndoRedoStackReset;
    }

    @Override
    public boolean isNeededSeparateThread() {
        return needSeparateThread;
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public String getShortDescription() {
        return shortDescription;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public static final class Builder implements Request.Builder {
        private final Request.ExecutionStatus executionStatus;
        private final Optional<String> errorMessage;
        private final String executionReport;
        private boolean shallBeAddedToUndoRedoStack = true;
        private boolean implyUndoRedoStackReset = false;
        private String shortDescription = "shortDescription";
        private List<String> actions = new ArrayList<>();
        private List<Long> threads = new ArrayList<>();
        private boolean needSeparateThread = false;
        private Optional<Semaphore> semaphoreToRelease = Optional.empty();

        private Builder(ExecutionStatus executionStatus, Optional<String> errorMessage, String executionReport) {
            this.executionStatus = executionStatus;
            this.errorMessage = errorMessage;
            this.executionReport = executionReport;
        }

        public static Builder aFakeRequest() {
            return new Builder(SUCCESS, Optional.empty(), "execution report");
        }

        public static Builder aFakeSuccedingRequest() {
            return new Builder(SUCCESS, Optional.empty(), "execution report");
        }

        public static Builder aFakeFailingRequest() {
            return new Builder(FAILURE, Optional.empty(), "execution report");
        }

        public static Request.Builder aFakeThrowingExceptionRequest(String errorMessage) {
            return new Builder(SUCCESS, Optional.of(errorMessage), errorMessage);
        }

        public Builder thatShallBeAddedToUndoRedoStack(boolean shallBeAddedToUndoRedoStack) {
            this.shallBeAddedToUndoRedoStack = shallBeAddedToUndoRedoStack;
            return this;
        }

        public Builder thatImplyUndoRedoStackReset(boolean implyUndoRedoStackReset) {
            this.implyUndoRedoStackReset = implyUndoRedoStackReset;
            return this;
        }

        public Builder withShortDescription(String shortDescription) {
            this.shortDescription = shortDescription;
            return this;
        }

        public Builder thatNeedSeparateThread(boolean needSeparateThread) {
            this.needSeparateThread = needSeparateThread;
            return this;
        }

        public Builder withSemaphoreToRelease(Semaphore semaphore) {
            this.semaphoreToRelease = Optional.ofNullable(semaphore);
            return this;
        }

        public List<String> getActions() {
            return actions;
        }

        public List<Long> getThreads() {
            return threads;
        }

        @Override
        public Request build() {
            UUID id = UUID.randomUUID();
            return new FakeRequest(ResultImpl.build(executionStatus, id, executionReport),
                                   id,
                                   shortDescription,
                                   "description",
                                   errorMessage,
                                   shallBeAddedToUndoRedoStack,
                                   implyUndoRedoStackReset,
                                   actions,
                                   threads,
                                   needSeparateThread,
                                   semaphoreToRelease);
        }
    }
}
