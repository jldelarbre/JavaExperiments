package com.github.jldelarbre.javaExperiments.request;

import java.util.List;

public interface UndoRedoService {

    void undo();

    void redo();

    List<String> getUndoActionShortDescriptions();

    List<String> getRedoActionShortDescriptions();
}
