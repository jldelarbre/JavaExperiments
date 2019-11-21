package com.github.jldelarbre.javaExperiments.observer.observer;

import java.util.Set;

public interface IObservable<ObservablesEventsType extends IObservablesEvents> {

    // addObserver with priority

    Class<ObservablesEventsType> getObservablesEventsType();

    boolean addObserver(IObserver<? extends ObservablesEventsType> observer);

    boolean removeObserver(IObserver<? extends ObservablesEventsType> observer);

    void removeAllObservers();

    Set<IObserver<? extends ObservablesEventsType>> getObservers();

    int getNumObservers();

    boolean disable();

    boolean enable();

    interface IProxy<ObservablesEventsType extends IObservablesEvents>
            extends IObservable<ObservablesEventsType> {

        IObservable<ObservablesEventsType> getObservable();

        @Override
        default Class<ObservablesEventsType> getObservablesEventsType() {
            return getObservable().getObservablesEventsType();
        }

        @Override
        default boolean addObserver(IObserver<? extends ObservablesEventsType> observer) {
            return getObservable().addObserver(observer);
        }

        @Override
        default boolean removeObserver(IObserver<? extends ObservablesEventsType> observer) {
            return getObservable().removeObserver(observer);
        }

        @Override
        default void removeAllObservers() {
            getObservable().removeAllObservers();
        }

        @Override
        default Set<IObserver<? extends ObservablesEventsType>> getObservers() {
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
