package com.github.jldelarbre.javaExperiments.observer.observer.internal;

import java.util.Collection;

import com.github.jldelarbre.javaExperiments.observer.observer.IObserver;

public interface IObserverMerger {

    Collection<IObserver<?>> getAllObservers();
}
