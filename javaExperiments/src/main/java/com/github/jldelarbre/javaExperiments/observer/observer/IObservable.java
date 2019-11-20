package com.github.jldelarbre.javaExperiments.observer.observer;

import java.util.Set;

public interface IObservable<ObservablesEventsType extends IObservablesEvents> {

    // addObserver with priority

    Class<ObservablesEventsType> getObservablesEventsType();

    boolean addObserver(IObserver<ObservablesEventsType> observer);

    boolean removeObserver(IObserver<ObservablesEventsType> observer);

    void removeAllObservers();

    Set<IObserver<ObservablesEventsType>> getObservers();

    int getNumObservers();

    boolean disableObservables();

    boolean enableObservables();
}
