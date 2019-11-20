package com.github.jldelarbre.javaExperiments.observer.observer;

public final class Observable<ObserverType extends IObserver<ObservablesEventsType>, ObservablesEventsType extends IObservablesEvents>
	implements IObservableDefaultImpl<ObserverType, ObservablesEventsType> {

	private final Class<ObserverType> observerType;
	private final IObserverHolder observerHolder;

	private Observable(Class<ObserverType> observerType, IObserverHolder observerHolder) {
		this.observerType = observerType;
		this.observerHolder = observerHolder;
	}

	public static
	<ObserverType extends IObserver<ObservablesEventsType>, ObservablesEventsType extends IObservablesEvents>
	Observable<ObserverType, ObservablesEventsType> build(Class<ObserverType> observerType, IObserverHolder observerHolder) {
		return new Observable<>(observerType, observerHolder);
	}

	@Override
	public Class<ObserverType> getObserverType() {
		return observerType;
	}

	@Override
	public IObserverHolder getObserverHolder() {
		return observerHolder;
	}
}
