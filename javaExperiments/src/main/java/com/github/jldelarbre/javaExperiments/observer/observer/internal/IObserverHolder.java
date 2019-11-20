
package com.github.jldelarbre.javaExperiments.observer.observer.internal;

import java.util.Collection;

import com.github.jldelarbre.javaExperiments.observer.observer.IObservablesEvents;
import com.github.jldelarbre.javaExperiments.observer.observer.IObserver;

public interface IObserverHolder {

    <ObserverType extends IObserver<ObservablesEventsType>, ObservablesEventsType extends IObservablesEvents> boolean
        addObserver(ObserverType observer);

    <ObserverType extends IObserver<ObservablesEventsType>, ObservablesEventsType extends IObservablesEvents> boolean
        removeObserver(ObserverType observer);

    <ObserverType extends IObserver<ObservablesEventsType>, ObservablesEventsType extends IObservablesEvents> void
        removeAllObservers(Class<? extends ObservablesEventsType> observerType);

    Collection<? extends IObserver<?>> getAllObservers();

    <ObserverType extends IObserver<ObservablesEventsType>, ObservablesEventsType extends IObservablesEvents> boolean
        disableEvents(Class<? extends ObservablesEventsType> observerType);

    <ObserverType extends IObserver<ObservablesEventsType>, ObservablesEventsType extends IObservablesEvents> boolean
        enableEvents(Class<? extends ObservablesEventsType> observerType);
}
