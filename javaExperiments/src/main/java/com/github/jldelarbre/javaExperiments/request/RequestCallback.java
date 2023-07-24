package com.github.jldelarbre.javaExperiments.request;

@FunctionalInterface
public interface RequestCallback {

    /**
     * Callback used to get result of a request. It is called when the request terminates.
     * It is mostly used with request in a separate thread to get result at a later time.
     * It is an input of: {@link RequestExecutor#execute(Request, RequestCallback)}
     *
     * @param result Request result
     */
    void call(Request.Result result);
}
