package com.github.jldelarbre.javaExperiments.request.impl;

import com.github.jldelarbre.javaExperiments.request.UndoRedoService;

public interface UndoRedoManager extends UndoRedoService {

    void registerExecutedRequest(ExecutableRequest request);
}
