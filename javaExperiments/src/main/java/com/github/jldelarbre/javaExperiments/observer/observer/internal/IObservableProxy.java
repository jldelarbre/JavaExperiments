package com.github.jldelarbre.javaExperiments.observer.observer.internal;

import com.github.jldelarbre.javaExperiments.observer.observer.IObservable;
import com.github.jldelarbre.javaExperiments.observer.observer.IObservablesEvents;
import com.github.jldelarbre.javaExperiments.observer.observer.IObserver;

public interface IObservableProxy<ObserverType extends IObserver<ObservablesEventsType>, ObservablesEventsType extends IObservablesEvents>
    extends IObservable<ObserverType, ObservablesEventsType> {

    IObservable<ObserverType, ObservablesEventsType> getObservable();

    @Override
    default Class<? extends ObservablesEventsType> getObservablesEventsType() {
        return getObservable().getObservablesEventsType();
    }

    @Override
    default boolean addObserver(ObserverType observer) {
        return getObservable().addObserver(observer);
    }

    @Override
    default boolean removeObserver(ObserverType observer) {
        return getObservable().removeObserver(observer);
    }

    @Override
    default void removeAllObservers() {
        getObservable().removeAllObservers();
    }

    @Override
    default boolean disableEvents() {
        return getObservable().disableEvents();
    }

    @Override
    default boolean enableEvents() {
        return getObservable().enableEvents();
    }
}
