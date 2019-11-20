package com.github.jldelarbre.javaExperiments.observer.observer.internal;

import com.github.jldelarbre.javaExperiments.observer.observer.IObservable;
import com.github.jldelarbre.javaExperiments.observer.observer.IObservablesEvents;
import com.github.jldelarbre.javaExperiments.observer.observer.IObserver;

public interface IObservableDefaultImpl<ObserverType extends IObserver<ObservablesEventsType>, ObservablesEventsType extends IObservablesEvents>
        extends IObservable<ObserverType, ObservablesEventsType> {

    IObserverHolder getObserverHolder();

    @Override
    default boolean addObserver(ObserverType observer) {
        return getObserverHolder().addObserver(getObserverType(), observer);
    }

    @Override
    default boolean removeObserver(ObserverType observer) {
        return getObserverHolder().removeObserver(getObserverType(), observer);
    }

    @Override
    default void removeAllObservers() {
        getObserverHolder().removeAllObservers(getObserverType());
    }

    @Override
    default boolean disableObservers() {
        return getObserverHolder().disableObservers(getObserverType());
    }

    @Override
    default boolean enableObservers() {
        return getObserverHolder().enableObservers(getObserverType());
    }
}
