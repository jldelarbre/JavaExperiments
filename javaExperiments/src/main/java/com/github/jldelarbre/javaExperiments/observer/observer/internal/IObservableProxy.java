package com.github.jldelarbre.javaExperiments.observer.observer.internal;

import java.util.Set;

import com.github.jldelarbre.javaExperiments.observer.observer.IObservable;
import com.github.jldelarbre.javaExperiments.observer.observer.IObservablesEvents;
import com.github.jldelarbre.javaExperiments.observer.observer.IObserver;

public interface IObservableProxy<ObservablesEventsType extends IObservablesEvents>
        extends IObservable<ObservablesEventsType> {

    IObservable<ObservablesEventsType> getObservable();

    @Override
    default Class<ObservablesEventsType> getObservablesEventsType() {
        return getObservable().getObservablesEventsType();
    }

    @Override
    default boolean addObserver(IObserver<ObservablesEventsType> observer) {
        return getObservable().addObserver(observer);
    }

    @Override
    default boolean removeObserver(IObserver<ObservablesEventsType> observer) {
        return getObservable().removeObserver(observer);
    }

    @Override
    default void removeAllObservers() {
        getObservable().removeAllObservers();
    }

    @Override
    default Set<IObserver<ObservablesEventsType>> getObservers() {
        return getObservable().getObservers();
    }

    @Override
    default int getNumObservers() {
        return getObservable().getNumObservers();
    }

    @Override
    default boolean disableObservables() {
        return getObservable().disableObservables();
    }

    @Override
    default boolean enableObservables() {
        return getObservable().enableObservables();
    }
}
