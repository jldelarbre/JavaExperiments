package com.github.jldelarbre.javaExperiments.observer.observer;

public interface IObservable<ObserverType extends IObserver<ObservablesEventsType>, ObservablesEventsType extends IObservablesEvents> {

	// enable/disable observer
	// addObserver with priority

	void addObserver(ObserverType observer);

	void removeObserver(ObserverType observer);

	void removeAllObservers();
}
