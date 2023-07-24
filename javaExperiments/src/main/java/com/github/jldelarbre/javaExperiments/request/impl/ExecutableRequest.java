package com.github.jldelarbre.javaExperiments.request.impl;

import com.github.jldelarbre.javaExperiments.request.Request;

public interface ExecutableRequest extends Request {
    Request.Result execute();
}
