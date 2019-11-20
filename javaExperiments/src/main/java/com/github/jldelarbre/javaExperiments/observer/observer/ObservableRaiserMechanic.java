
package com.github.jldelarbre.javaExperiments.observer.observer;

import com.github.jldelarbre.javaExperiments.observer.observer.internal.EventRaiser;
import com.github.jldelarbre.javaExperiments.observer.observer.internal.IObserverHolder;
import com.github.jldelarbre.javaExperiments.observer.observer.internal.Observable;
import com.github.jldelarbre.javaExperiments.observer.observer.internal.ObserverHolder;

public final class ObservableRaiserMechanic<ObserverType extends IObserver<ObservablesEventsType>, ObservablesEventsType extends IObservablesEvents>
    implements IObservableRaiserMechanic<ObserverType, ObservablesEventsType> {

    private final IEventRaiser<? extends ObservablesEventsType> raiser;

    private final IObservable<ObserverType, ObservablesEventsType> observable;

    private final IObserverHolder observerHolder;

    private ObservableRaiserMechanic(IEventRaiser<? extends ObservablesEventsType> raiser,
                                     IObservable<ObserverType, ObservablesEventsType> observable,
                                     IObserverHolder observerHolder) {
        this.raiser = raiser;
        this.observable = observable;
        this.observerHolder = observerHolder;
    }

    public static <ObserverType extends IObserver<ObservablesEventsType>, ObservablesEventsType extends IObservablesEvents>
        ObservableRaiserMechanic<ObserverType, ObservablesEventsType>
        build(Class<? extends ObservablesEventsType> observablesEventsType) {

        final IObserverHolder observerHolder = ObserverHolder.build();

        final Observable<ObserverType, ObservablesEventsType> observable =
            Observable.build(observablesEventsType, observerHolder);

        final IEventRaiser<? extends ObservablesEventsType> raiser =
            EventRaiser.build(observablesEventsType, observerHolder);

        return new ObservableRaiserMechanic<>(raiser, observable, observerHolder);
    }

    public static <ObserverType extends IObserver<ObservablesEventsType>, ObservablesEventsType extends IObservablesEvents>
        ObservableRaiserMechanic<ObserverType, ObservablesEventsType>
        build(Class<? extends ObservablesEventsType> observablesEventsType,
              ObservableRaiserMechanic<?, ?> observableRaiserMechanic) {

        final IObserverHolder observerHolder = observableRaiserMechanic.observerHolder;

        final Observable<ObserverType, ObservablesEventsType> observable =
            Observable.build(observablesEventsType, observerHolder);

        final IEventRaiser<? extends ObservablesEventsType> raiser = EventRaiser.build(observablesEventsType, observerHolder);

        return new ObservableRaiserMechanic<>(raiser, observable, observerHolder);
    }

    public static <ObserverType extends IObserver<ObservablesEventsType>, ObservablesEventsType extends IObservablesEvents>
        ObservableRaiserMechanic<ObserverType, ObservablesEventsType>
        buildWithRaiser(Class<? extends ObservablesEventsType> observablesEventsType,
                        ObservableRaiserMechanic<?, ? extends ObservablesEventsType> observableRaiserMechanic) {

        final IObserverHolder observerHolder = observableRaiserMechanic.observerHolder;

        final Observable<ObserverType, ObservablesEventsType> observable =
            Observable.build(observablesEventsType, observerHolder);

        final IEventRaiser<? extends ObservablesEventsType> raiser = observableRaiserMechanic.getRaiser();

        return new ObservableRaiserMechanic<>(raiser, observable, observerHolder);
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
    public IObservableRaiserMechanic<ObserverType, ObservablesEventsType> getObservableRaiserMechanic() {
        throw new UnsupportedOperationException("getObservableRaiserMechanic is expected to be used for delegation only");
    }
}
