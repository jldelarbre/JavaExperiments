package com.github.jldelarbre.javaExperiments.request;

import org.junit.Before;
import org.junit.Test;

import static com.github.jldelarbre.javaExperiments.request.Request.ExecutionStatus.FAILURE;
import static com.github.jldelarbre.javaExperiments.request.Request.ExecutionStatus.SUCCESS;
import static com.github.jldelarbre.javaExperiments.request.impl.ServiceProviderImpl.aServiceProvider;
import static com.github.jldelarbre.javaExperiments.request.utils.FakeRequest.Builder.*;
import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;

public class RequestExecutorShould {

    private ServiceProvider serviceProvider;
    private RequestExecutor requestExecutor;
    private Request.Result futureResult = null;

    @Before
    public void setUp() throws Exception {
        serviceProvider = aServiceProvider();
        requestExecutor = serviceProvider.getRequestExecutor();
    }

    @Test
    public void executeSuccedingRequest() {
        Request succedingRequest = aFakeSuccedingRequest().build();

        Request.Result result = requestExecutor.execute(succedingRequest);

        assertEquals("The request shall succeed", SUCCESS, result.getStatus());
    }

    @Test
    public void executeFailingRequest() {
        Request failingRequest = aFakeFailingRequest().build();

        Request.Result result = requestExecutor.execute(failingRequest);

        assertEquals("The request shall fail", FAILURE, result.getStatus());
    }

    @Test
    public void OutputResultWithSameIdThanItsRequest() {
        Request request = aFakeRequest().build();

        Request.Result result = requestExecutor.execute(request);

        assertEquals("Request and result shall have the same id", request.getId(), result.getRequestId());
    }

    @Test
    public void RespondToCallback() {
        Request request = aFakeRequest().build();
        RequestCallback requestCallback = new RequestCallback() {
            @Override
            public void call(Request.Result result) {
                futureResult = result;
            }
        };

        requestExecutor.execute(request, requestCallback);

        assertEquals("Expect request callback to be called with a result with same id than request",
                     request.getId(),
                     futureResult.getRequestId());
    }

    @Test
    public void sendFailureResultWhenRequestThrowException() {
        String errorMessage = "An error message";
        Request throwingExceptionRequest = aFakeThrowingExceptionRequest(errorMessage).build();

        Request.Result result = requestExecutor.execute(throwingExceptionRequest);

        assertEquals("The request shall fail", FAILURE, result.getStatus());
        assertEquals("The result should provide error information",
                     "ERROR: " + errorMessage + " (RuntimeException)",
                     result.getExecutionReport());
    }

    @Test
    public void notExecuteARequestTwice() {
        Request request = aFakeRequest().build();

        requestExecutor.execute(request);
        Request.Result result = requestExecutor.execute(request);

        assertEquals("Request should not execute twice but fail second time", FAILURE, result.getStatus());
        assertEquals("The result should provide error information",
                     "ERROR: Request already executed (RuntimeException)",
                     result.getExecutionReport());
    }
}
