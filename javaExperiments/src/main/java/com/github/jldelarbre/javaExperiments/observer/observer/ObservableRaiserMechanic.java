package com.github.jldelarbre.javaExperiments.observer.observer;

import com.github.jldelarbre.javaExperiments.observer.observer.internal.IEventRaiserDefaultImpl;
import com.github.jldelarbre.javaExperiments.observer.observer.internal.IObserverHolder;
import com.github.jldelarbre.javaExperiments.observer.observer.internal.Observable;
import com.github.jldelarbre.javaExperiments.observer.observer.internal.ObserverHolder;

public final class ObservableRaiserMechanic<ObserverType extends IObserver<ObservablesEventsType>, ObservablesEventsType extends IObservablesEvents>
        implements IObservableRaiserMechanic<ObserverType, ObservablesEventsType> {

    private final IEventRaiser<? extends ObservablesEventsType> raiser;

    private final Observable<ObserverType, ObservablesEventsType> observable;

    private ObservableRaiserMechanic(IEventRaiser<? extends ObservablesEventsType> raiser,
                                     Observable<ObserverType, ObservablesEventsType> observable) {
        this.raiser = raiser;
        this.observable = observable;
    }

    public static <ObserverType extends IObserver<ObservablesEventsType>, ObservablesEventsType extends IObservablesEvents>
        ObservableRaiserMechanic<ObserverType, ObservablesEventsType>
        build(Class<? extends ObservablesEventsType> observablesEventsType, Class<ObserverType> observerType) {

        final IObserverHolder observerHolder = ObserverHolder.build();

        final Observable<ObserverType, ObservablesEventsType> observable =
            Observable.build(observerType, observerHolder);

        final IEventRaiser<ObservablesEventsType> raiser =
            IEventRaiserDefaultImpl.RaiserData.build(observablesEventsType, observable.getObserverHolder());

        return new ObservableRaiserMechanic<>(raiser, observable);
    }

    public static <ObserverType extends IObserver<ObservablesEventsType>, ObservablesEventsType extends IObservablesEvents>
        ObservableRaiserMechanic<ObserverType, ObservablesEventsType>
        build(Class<? extends ObservablesEventsType> observablesEventsType,
              Class<ObserverType> observerType,
              IObservableRaiserMechanic<?, ?> observableRaiserMechanic) {

        final IObserverHolder observerHolder = observableRaiserMechanic.getObserverHolder();

        final Observable<ObserverType, ObservablesEventsType> observable =
            Observable.build(observerType, observerHolder);

        final IEventRaiser<ObservablesEventsType> raiser =
            IEventRaiserDefaultImpl.RaiserData.build(observablesEventsType, observable.getObserverHolder());

        return new ObservableRaiserMechanic<>(raiser, observable);
    }

    public static <ObserverType extends IObserver<ObservablesEventsType>, ObservablesEventsType extends IObservablesEvents>
        ObservableRaiserMechanic<ObserverType, ObservablesEventsType>
        buildWithRaiser(Class<? extends ObservablesEventsType> observablesEventsType,
                        Class<ObserverType> observerType,
                        IObservableRaiserMechanic<?, ? extends ObservablesEventsType> observableRaiserMechanic) {

        final IObserverHolder observerHolder = observableRaiserMechanic.getObserverHolder();

        final Observable<ObserverType, ObservablesEventsType> observable =
            Observable.build(observerType, observerHolder);

        final IEventRaiser<? extends ObservablesEventsType> raiser = observableRaiserMechanic.getRaiser();

        return new ObservableRaiserMechanic<>(raiser, observable);
    }

    @Override
    public IEventRaiser<? extends ObservablesEventsType> getRaiser() {
        return this.raiser;
    }

    @Override
    public IObservable<ObserverType, ObservablesEventsType> getObservable() {
        return this.observable;
    }

    @Override
    public IObserverHolder getObserverHolder() {
        return this.observable.getObserverHolder();
    }

    @Override
    public Class<? extends ObserverType> getObserverType() {
        return this.observable.getObserverType();
    }

    @Override
    public IObservableRaiserMechanic<ObserverType, ObservablesEventsType> getObservableRaiserMechanic() {
        throw new UnsupportedOperationException("getObservableRaiserMechanic is expected to be used for delegation only");
    }
}
