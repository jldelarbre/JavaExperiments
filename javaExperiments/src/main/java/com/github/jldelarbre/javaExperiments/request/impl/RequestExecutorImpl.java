package com.github.jldelarbre.javaExperiments.request.impl;

import com.github.jldelarbre.javaExperiments.request.Request;
import com.github.jldelarbre.javaExperiments.request.RequestCallback;
import com.github.jldelarbre.javaExperiments.request.RequestExecutor;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import static com.github.jldelarbre.javaExperiments.request.Request.ExecutionStatus.FAILURE;

public final class RequestExecutorImpl implements RequestExecutor {

    private final UndoRedoManager undoRedoManager;
    private final ExecutorService executor;

    /**
     * Ensure that every task weather or not there are executed in calling thread or in separate thread
     * is executed in sequence.
     * It could happen in the following case:
     *  <li>A long task with a callback in a separate thread is launched</li>
     *  <li>A second short task in calling thread is called before the end od the previous one</li>
     */
    static final Callable<Void> ORDERING_TASK = new Callable<Void>() {
        @Override
        public Void call() throws Exception {
            return null;
        }
    };

    RequestExecutorImpl(UndoRedoManager undoRedoManager, ExecutorService executor) {
        this.undoRedoManager = undoRedoManager;
        this.executor = executor;
    }

    @Override
    public Request.Result execute(Request request) {
        return innerExecution(request, Optional.empty());
    }

    @Override
    public void execute(Request request, RequestCallback requestCallback) {
        innerExecution(request, Optional.of(requestCallback));
    }

    private Request.Result innerExecution(Request request, Optional<RequestCallback> requestCallback) {
        Request.Result result;
        try {
            ExecutableRequest executableRequest = (ExecutableRequest) request;
            if (executableRequest.isAlreadyExecuted()) {
                throw new RuntimeException("Request already executed");
            }
            RequestTask requestTask = new RequestTask(executableRequest, undoRedoManager, requestCallback);
            if (executableRequest.isNeededSeparateThread()) {
                Future<Request.Result> resultFuture = executor.submit(requestTask);
                if (requestCallback.isPresent()) {
                    // A callback is used to return the request result when the computation will come to its end
                    // So we do not wait for the Future and we do not need its result
                    result = null;
                } else {
                    try {
                        // The request is executed in a separate thread but nevertheless we block the calling thread
                        // since we shall return the result
                        result = resultFuture.get();
                    } catch (ExecutionException e) {
                        throw e.getCause();
                    }
                }
            } else {
                executor.submit(ORDERING_TASK).get();
                result = requestTask.call();
            }
        } catch (Throwable e) {
            result = buildErrorResult(request, e);
        }
        return result;
    }

    private static final class RequestTask implements Callable<Request.Result> {

        private final ExecutableRequest request;

        private final UndoRedoManager undoRedoManager;
        private final Optional<RequestCallback> requestCallback;
        private RequestTask(ExecutableRequest request,
                            UndoRedoManager undoRedoManager,
                            Optional<RequestCallback> requestCallback1) {
            this.request = request;
            this.undoRedoManager = undoRedoManager;
            this.requestCallback = requestCallback1;
        }

        @Override
        public Request.Result call() throws Exception {
            Request.Result result;
            try {
                result = request.execute();
            } catch (Throwable e) {
                result = buildErrorResult(request, e);
            }
            undoRedoManager.registerExecutedRequest(request);
            if (requestCallback.isPresent()) {
                requestCallback.get().call(result);
            }
            return result;
        }
    }

    private static Request.Result buildErrorResult(Request request, Throwable e) {
        Request.Result result;
        result = ResultImpl.build(FAILURE,
                request.getId(),
                "ERROR: " + e.getMessage() + " (" + e.getClass().getSimpleName() + ")");
        return result;
    }
}
