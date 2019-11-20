package com.github.jldelarbre.javaExperiments.observer.observer;

public interface IObservableDefaultImpl<ObserverType extends IObserver<ObservablesEventsType>, ObservablesEventsType extends IObservablesEvents>
	extends IObservable<ObserverType, ObservablesEventsType> {

	Class<ObserverType> getObserverType();

	IObserverHolder getObserverHolder();

	@Override
	default void addObserver(ObserverType observer) {
		getObserverHolder().addObserver(getObserverType(), observer);
	}

	@Override
	default void removeObserver(ObserverType observer) {
		getObserverHolder().removeObserver(getObserverType(), observer);
	}

	@Override
	default void removeAllObservers() {
		getObserverHolder().removeAllObservers(getObserverType());
	}
}
