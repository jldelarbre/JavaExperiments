package com.github.jldelarbre.javaExperiments.observer.observer;

import java.util.Set;

public interface IObservable<ObservablesEventsType extends IObservablesEvents, ObserverType extends IObserver<? extends ObservablesEventsType>> {

    // addObserver with priority

    Class<ObservablesEventsType> getObservablesEventsType();

    boolean addObserver(ObserverType observer);

    boolean removeObserver(ObserverType observer);

    void removeAllObservers();

    Set<ObserverType> getObservers();

    int getNumObservers();

    boolean disable();

    boolean enable();

    interface IProxy<ObservablesEventsType extends IObservablesEvents, ObserverType extends IObserver<? extends ObservablesEventsType>>
            extends IObservable<ObservablesEventsType, ObserverType> {

        IObservable<ObservablesEventsType, ObserverType> getObservable();

        @Override
        default Class<ObservablesEventsType> getObservablesEventsType() {
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
        default Set<ObserverType> getObservers() {
            return getObservable().getObservers();
        }

        @Override
        default int getNumObservers() {
            return getObservable().getNumObservers();
        }

        @Override
        default boolean disable() {
            return getObservable().disable();
        }

        @Override
        default boolean enable() {
            return getObservable().enable();
        }
    }
}
