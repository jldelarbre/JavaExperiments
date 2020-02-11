package com.github.jldelarbre.javaExperiments.observer.observer.internal;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.github.jldelarbre.javaExperiments.observer.observer.IEvents;
import com.github.jldelarbre.javaExperiments.observer.observer.IObservable;
import com.github.jldelarbre.javaExperiments.observer.observer.IObserver;

final class ObserverMerger implements IObserverMerger {

    // pair observable + event
    private final Map<Class<? extends IEvents>, IObservable<?, ?>> observablesMap;

    ObserverMerger(Map<Class<? extends IEvents>, IObservable<?, ?>> observablesMap) {
        this.observablesMap = observablesMap;
    }

    @Override
    public Collection<IObserver<?>> getAllObservers() {
        final Set<IObserver<?>> observersOut = new HashSet<>();

        for (final IObservable<?, ?> observable : this.observablesMap.values()) {
            final Set<? extends IObserver<?>> observers = observable.getObservers();
            observersOut.addAll(observers);
        }

        return observersOut;
    }
}
