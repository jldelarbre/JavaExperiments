package com.github.jldelarbre.javaExperiments.request.impl;

import com.github.jldelarbre.javaExperiments.request.Request;

import java.util.UUID;

public final class ResultImpl  implements Request.Result {

    private final Request.ExecutionStatus status;
    private final UUID requestId;
    private final String executionReport;

    private ResultImpl(Request.ExecutionStatus status, UUID requestId, String executionReport) {
        this.status = status;
        this.requestId = requestId;
        this.executionReport = executionReport;
    }

    public static Request.Result build(Request.ExecutionStatus status, UUID requestId, String executionReport) {
        return new ResultImpl(status, requestId, executionReport);
    }

    @Override
    public Request.ExecutionStatus getStatus() {
        return status;
    }

    @Override
    public UUID getRequestId() {
        return requestId;
    }

    @Override
    public String getExecutionReport() {
        return executionReport;
    }
}
