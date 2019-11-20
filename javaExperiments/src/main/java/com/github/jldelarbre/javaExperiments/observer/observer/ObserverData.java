package com.github.jldelarbre.javaExperiments.observer.observer;

public final class ObserverData<ObserverType extends IObserver<ObservablesEventsType>, ObservablesEventsType extends IObservablesEvents>
	implements IObservableDefaultImpl<ObserverType, ObservablesEventsType> {

	private final Class<ObserverType> observerType;
	private final IObserverHolder observerHolder;

	private ObserverData(Class<ObserverType> observerType, IObserverHolder observerHolder) {
		this.observerType = observerType;
		this.observerHolder = observerHolder;
	}

	public static
	<ObserverType extends IObserver<ObservablesEventsType>, ObservablesEventsType extends IObservablesEvents>
	ObserverData<ObserverType, ObservablesEventsType> build(Class<ObserverType> observerType, IObserverHolder observerHolder) {
		return new ObserverData<>(observerType, observerHolder);
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
