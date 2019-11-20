package com.github.jldelarbre.javaExperiments.observer.observer;

import java.util.Collection;

public interface IObserverHolder {

	<ObserverType extends IObserver<ObservablesEventsType>, ObservablesEventsType extends IObservablesEvents>
	void addObserver(Class<ObserverType> observerType, ObserverType observer);

	<ObserverType extends IObserver<ObservablesEventsType>, ObservablesEventsType extends IObservablesEvents>
	void removeObserver(Class<ObserverType> observerType, ObserverType observer);

	<ObserverType extends IObserver<ObservablesEventsType>, ObservablesEventsType extends IObservablesEvents>
	void removeAllObservers(Class<ObserverType> observerType);

	Collection<? extends IObserver<?>> getAllObservers();
}
