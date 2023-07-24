package com.github.jldelarbre.javaExperiments.request.impl;

import com.github.jldelarbre.javaExperiments.request.Request;
import com.github.jldelarbre.javaExperiments.request.RequestCallback;
import com.github.jldelarbre.javaExperiments.request.RequestExecutor;

import static com.github.jldelarbre.javaExperiments.request.Request.ExecutionStatus.FAILURE;

public final class RequestExecutorImpl implements RequestExecutor {

    private RequestExecutorImpl() {}

    @Override
    public Request.Result execute(Request request) {
        return innerExecution(request);
    }

    @Override
    public void execute(Request request, RequestCallback requestCallback) {
        Request.Result result = innerExecution(request);
        requestCallback.call(result);
    }

    private Request.Result innerExecution(Request request) {
        Request.Result result;
        try {
            ExecutableRequest executableRequest = (ExecutableRequest) request;
            result = executableRequest.execute();
        } catch (Exception e) {
            result = ResultImpl.build(FAILURE, request.getId(), "ERROR: " + e.getMessage());
        }
        return result;
    }

    public static RequestExecutor aRequestExecutor() {
        return new RequestExecutorImpl();
    }
}
