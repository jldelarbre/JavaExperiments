
package com.github.jldelarbre.javaExperiments.observer.observer;

public interface IObservable<ObserverType extends IObserver<ObservablesEventsType>, ObservablesEventsType extends IObservablesEvents> {

    // addObserver with priority

    Class<? extends ObservablesEventsType> getObservablesEventsType();

    boolean addObserver(ObserverType observer);

    boolean removeObserver(ObserverType observer);

    void removeAllObservers();

    boolean disableEvents();

    boolean enableEvents();
}
