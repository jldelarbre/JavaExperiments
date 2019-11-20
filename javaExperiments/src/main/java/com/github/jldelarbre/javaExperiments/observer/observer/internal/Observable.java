package com.github.jldelarbre.javaExperiments.observer.observer.internal;

import com.github.jldelarbre.javaExperiments.observer.observer.IObservablesEvents;
import com.github.jldelarbre.javaExperiments.observer.observer.IObserver;

public final class Observable<ObserverType extends IObserver<ObservablesEventsType>, ObservablesEventsType extends IObservablesEvents>
        implements IObservableDefaultImpl<ObserverType, ObservablesEventsType> {

    private final Class<? extends ObserverType> observerType;
    private final IObserverHolder observerHolder;

    private Observable(Class<? extends ObserverType> observerType, IObserverHolder observerHolder) {
        this.observerType = observerType;
        this.observerHolder = observerHolder;
    }

    public static <ObserverType extends IObserver<ObservablesEventsType>, ObservablesEventsType extends IObservablesEvents>
        Observable<ObserverType, ObservablesEventsType>
        build(Class<? extends ObserverType> observerType, IObserverHolder observerHolder) {
        return new Observable<>(observerType, observerHolder);
    }

    @Override
    public Class<? extends ObserverType> getObserverType() {
        return this.observerType;
    }

    @Override
    public IObserverHolder getObserverHolder() {
        return this.observerHolder;
    }
}
