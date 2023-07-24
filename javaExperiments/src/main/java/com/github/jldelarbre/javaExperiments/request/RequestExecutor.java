package com.github.jldelarbre.javaExperiments.request;

public interface RequestExecutor {
    Request.Result execute(Request request);

    void execute(Request request, RequestCallback requestCallback);
}
