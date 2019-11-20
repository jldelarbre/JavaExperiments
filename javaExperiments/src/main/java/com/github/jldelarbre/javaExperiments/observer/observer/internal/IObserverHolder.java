package com.github.jldelarbre.javaExperiments.observer.observer.internal;

import java.util.Collection;

import com.github.jldelarbre.javaExperiments.observer.observer.IObservablesEvents;
import com.github.jldelarbre.javaExperiments.observer.observer.IObserver;

public interface IObserverHolder {

    <ObserverType extends IObserver<ObservablesEventsType>, ObservablesEventsType extends IObservablesEvents> boolean
        addObserver(Class<? extends ObserverType> observerType, ObserverType observer);

    <ObserverType extends IObserver<ObservablesEventsType>, ObservablesEventsType extends IObservablesEvents> boolean
        removeObserver(Class<? extends ObserverType> observerType, ObserverType observer);

    <ObserverType extends IObserver<ObservablesEventsType>, ObservablesEventsType extends IObservablesEvents> void
        removeAllObservers(Class<? extends ObserverType> observerType);

    Collection<? extends IObserver<?>> getAllObservers();

    <ObserverType extends IObserver<ObservablesEventsType>, ObservablesEventsType extends IObservablesEvents> boolean
        disableObservers(Class<? extends ObserverType> observerType);

    <ObserverType extends IObserver<ObservablesEventsType>, ObservablesEventsType extends IObservablesEvents> boolean
        enableObservers(Class<? extends ObserverType> observerType);
}
