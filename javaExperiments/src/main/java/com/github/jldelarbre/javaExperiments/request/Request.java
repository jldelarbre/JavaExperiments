package com.github.jldelarbre.javaExperiments.request;

import java.util.UUID;

public interface Request {

    UUID getId();

    String getShortDescription();

    String getDescription();

    enum ExecutionStatus {
        SUCCESS,
        FAILURE
    }

    interface Result {
        ExecutionStatus getStatus();

        UUID getRequestId();

        String getExecutionReport();
    }

    interface Builder {
        Request build();
    }
}
