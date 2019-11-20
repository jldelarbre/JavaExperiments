package com.github.jldelarbre.javaExperiments.observer.observer;

public final class ObservableRaiserMechanic<ObserverType extends IObserver<ObservablesEventsType>, ObservablesEventsType extends IObservablesEvents> {

	private final IEventRaiser<? extends ObservablesEventsType> raiser;

	private final ObserverData<ObserverType, ObservablesEventsType> observerData;

	private ObservableRaiserMechanic(IEventRaiser<? extends ObservablesEventsType> raiser, ObserverData<ObserverType, ObservablesEventsType> observerData) {
		this.raiser = raiser;
		this.observerData = observerData;
	}

	public static <ObserverType extends IObserver<ObservablesEventsType>, ObservablesEventsType extends IObservablesEvents>
	ObservableRaiserMechanic<ObserverType, ObservablesEventsType> build(Class<ObservablesEventsType> observablesEventsType,
																		ObserverData<ObserverType, ObservablesEventsType> observerData) {

		final IEventRaiser<ObservablesEventsType> raiser = IEventRaiserDefaultImpl.RaiserData.build(observablesEventsType, observerData.getObserverHolder());

		return new ObservableRaiserMechanic<>(raiser, observerData);
	}

	public static <ObserverType extends IObserver<ObservablesEventsType>, ObservablesEventsType extends IObservablesEvents>
	ObservableRaiserMechanic<ObserverType, ObservablesEventsType> build(IEventRaiser<? extends ObservablesEventsType> raiser,
																		ObserverData<ObserverType, ObservablesEventsType> observerData) {

		return new ObservableRaiserMechanic<>(raiser, observerData);
	}

	public IEventRaiser<? extends ObservablesEventsType> getRaiser() {
		return raiser;
	}

	public ObserverData<ObserverType, ObservablesEventsType> getObserverData() {
		return observerData;
	}

	public IObserverHolder getObserverHolder() {
		return observerData.getObserverHolder();
	}

	public Class<ObserverType> getObserverType() {
		return observerData.getObserverType();
	}
}
