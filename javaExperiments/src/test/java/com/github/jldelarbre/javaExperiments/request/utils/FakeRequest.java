package com.github.jldelarbre.javaExperiments.request.utils;

import com.github.jldelarbre.javaExperiments.request.impl.ExecutableRequest;
import com.github.jldelarbre.javaExperiments.request.Request;
import com.github.jldelarbre.javaExperiments.request.impl.ResultImpl;

import java.util.Optional;
import java.util.UUID;

import static com.github.jldelarbre.javaExperiments.request.Request.ExecutionStatus.FAILURE;
import static com.github.jldelarbre.javaExperiments.request.Request.ExecutionStatus.SUCCESS;

public final class FakeRequest implements ExecutableRequest {

    private final Request.Result result;
    private final UUID id;
    private final String shortDescription;
    private final String description;
    private final Optional<String> errorMessage;

    public FakeRequest(Request.Result result,
                       UUID id,
                       String shortDescription,
                       String description,
                       Optional<String> errorMessage) {
        this.result = result;
        this.id = id;
        this.shortDescription = shortDescription;
        this.description = description;
        this.errorMessage = errorMessage;
    }

    @Override
    public Request.Result execute() {
        if (errorMessage.isPresent()) {
            throw new RuntimeException(errorMessage.get());
        }
        return result;
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

        @Override
        public Request build() {
            UUID id = UUID.randomUUID();
            return new FakeRequest(ResultImpl.build(executionStatus, id, executionReport),
                                   id,
                     "shortDescription",
                         "description",
                                   errorMessage);
        }
    }
}
