package com.github.jldelarbre.javaExperiments.observer.observer;

public final class ObservableRaiserMechanic<ObserverType extends IObserver<ObservablesEventsType>, ObservablesEventsType extends IObservablesEvents> {

	private final IEventRaiser<? extends ObservablesEventsType> raiser;

	private final Observable<ObserverType, ObservablesEventsType> observable;

	private ObservableRaiserMechanic(IEventRaiser<? extends ObservablesEventsType> raiser,
									 Observable<ObserverType, ObservablesEventsType> observable) {
		this.raiser = raiser;
		this.observable = observable;
	}

	public static <ObserverType extends IObserver<ObservablesEventsType>, ObservablesEventsType extends IObservablesEvents>
		ObservableRaiserMechanic<ObserverType, ObservablesEventsType>
		build(Class<? extends ObservablesEventsType> observablesEventsType,
			  Class<ObserverType> observerType) {

		IObserverHolder observerHolder = ObserverHolder.build();

		Observable<ObserverType, ObservablesEventsType> observable = Observable.build(observerType, observerHolder);

		final IEventRaiser<ObservablesEventsType> raiser =
			IEventRaiserDefaultImpl.RaiserData.build(observablesEventsType, observable.getObserverHolder());

		return new ObservableRaiserMechanic<>(raiser, observable);
	}

	public static <ObserverType extends IObserver<ObservablesEventsType>, ObservablesEventsType extends IObservablesEvents>
		ObservableRaiserMechanic<ObserverType, ObservablesEventsType>
		build(Class<? extends ObservablesEventsType> observablesEventsType,
			  Class<ObserverType> observerType,
			  ObservableRaiserMechanic<?, ?> observableRaiserMechanic) {

		IObserverHolder observerHolder = observableRaiserMechanic.getObserverHolder();

		Observable<ObserverType, ObservablesEventsType> observable = Observable.build(observerType, observerHolder);

		final IEventRaiser<ObservablesEventsType> raiser =
			IEventRaiserDefaultImpl.RaiserData.build(observablesEventsType, observable.getObserverHolder());

		return new ObservableRaiserMechanic<>(raiser, observable);
	}

	public static <ObserverType extends IObserver<ObservablesEventsType>, ObservablesEventsType extends IObservablesEvents>
		ObservableRaiserMechanic<ObserverType, ObservablesEventsType>
		buildWithRaiser(Class<? extends ObservablesEventsType> observablesEventsType,
						Class<ObserverType> observerType,
						ObservableRaiserMechanic<?, ? extends ObservablesEventsType> observableRaiserMechanic) {

		IObserverHolder observerHolder = observableRaiserMechanic.getObserverHolder();

		Observable<ObserverType, ObservablesEventsType> observable = Observable.build(observerType, observerHolder);

		final IEventRaiser<? extends ObservablesEventsType> raiser = observableRaiserMechanic.getRaiser();

		return new ObservableRaiserMechanic<>(raiser, observable);
	}

	public IEventRaiser<? extends ObservablesEventsType> getRaiser() {
		return raiser;
	}

	public Observable<ObserverType, ObservablesEventsType> getObservable() {
		return observable;
	}

	public IObserverHolder getObserverHolder() {
		return observable.getObserverHolder();
	}

	public Class<ObserverType> getObserverType() {
		return observable.getObserverType();
	}
}
