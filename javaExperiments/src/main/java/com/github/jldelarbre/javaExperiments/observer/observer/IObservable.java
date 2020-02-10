package com.github.jldelarbre.javaExperiments.observer.observer;

import java.util.Set;

public interface IObservable<EventsType extends IEvents, ObserverType extends IObserver<? extends EventsType>> {

    // addObserver with priority

    Class<EventsType> getObservableEventsType();

    boolean addObserver(ObserverType observer);

    boolean removeObserver(ObserverType observer);

    void removeAllObservers();

    Set<ObserverType> getObservers();

    int getNumObservers();

    boolean disable();

    boolean enable();

    interface IProxy<EventsType extends IEvents, ObserverType extends IObserver<? extends EventsType>>
            extends IObservable<EventsType, ObserverType> {

        IObservable<EventsType, ObserverType> getObservable();

        @Override
        default Class<EventsType> getObservableEventsType() {
            return getObservable().getObservableEventsType();
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
