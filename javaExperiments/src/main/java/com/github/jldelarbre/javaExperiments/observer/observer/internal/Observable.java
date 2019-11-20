
package com.github.jldelarbre.javaExperiments.observer.observer.internal;

import com.github.jldelarbre.javaExperiments.observer.observer.IObservable;
import com.github.jldelarbre.javaExperiments.observer.observer.IObservablesEvents;
import com.github.jldelarbre.javaExperiments.observer.observer.IObserver;

public final class Observable<ObserverType extends IObserver<ObservablesEventsType>, ObservablesEventsType extends IObservablesEvents>
    implements IObservable<ObserverType, ObservablesEventsType> {

    private final Class<? extends ObservablesEventsType> observablesEventsType;
    private final IObserverHolder observerHolder;

    private Observable(Class<? extends ObservablesEventsType> observablesEventsType, IObserverHolder observerHolder) {
        this.observablesEventsType = observablesEventsType;
        this.observerHolder = observerHolder;
    }

    public static <ObserverType extends IObserver<ObservablesEventsType>, ObservablesEventsType extends IObservablesEvents>
        Observable<ObserverType, ObservablesEventsType>
        build(Class<? extends ObservablesEventsType> observablesEventsType, IObserverHolder observerHolder) {
        return new Observable<>(observablesEventsType, observerHolder);
    }

    @Override
    public Class<? extends ObservablesEventsType> getObservablesEventsType() {
        return this.observablesEventsType;
    }

    @Override
    public boolean addObserver(ObserverType observer) {
        return this.observerHolder.addObserver(observer);
    }

    @Override
    public boolean removeObserver(ObserverType observer) {
        return this.observerHolder.removeObserver(observer);
    }

    @Override
    public void removeAllObservers() {
        this.observerHolder.removeAllObservers(this.getObservablesEventsType());
    }

    @Override
    public boolean disableEvents() {
        return this.observerHolder.disableEvents(this.getObservablesEventsType());
    }

    @Override
    public boolean enableEvents() {
        return this.observerHolder.enableEvents(this.getObservablesEventsType());
    }
}
