
package com.github.jldelarbre.javaExperiments.observer;

import com.github.jldelarbre.javaExperiments.observer.observer.IEventRaiser;
import com.github.jldelarbre.javaExperiments.observer.observer.IObservable;
import com.github.jldelarbre.javaExperiments.observer.observer.IObservableRaiserMechanic;
import com.github.jldelarbre.javaExperiments.observer.observer.IEvents;
import com.github.jldelarbre.javaExperiments.observer.observer.IObserver;
import com.github.jldelarbre.javaExperiments.observer.observer.ObservableRaiserMechanic;
import com.github.jldelarbre.javaExperiments.observer.observer.ObservableRaiserMechanic.ObservableRaiserMechanicConstruction;

public class Toto implements IToto, IObservableRaiserMechanic.IProxy<ITotosEvents1Events2, ITotoEvents1Events2Observer> {

    private final IObservableRaiserMechanic<ITotosEvents1Events2, ITotoEvents1Events2Observer> observableRaiserMechanic;
    private final IObservableRaiserMechanic<ITotosEvents1, IObserver<? extends ITotosEvents1>> observableRaiserMechanicTotosEvents1;
    private final IObservableRaiserMechanic<ITotosEvents3, IObserver<? extends ITotosEvents3>> observableRaiserMechanicTotosEvents3;

    private final IEventRaiser<? extends ITotosEvents3> raiser3;

    public static IToto build() {
        final Class<?>[] eventsTypes =
            { ITotosEvents1Events2.class, ITotosEvents1.class };
        @SuppressWarnings("unchecked")
        final Class<? extends IEvents>[] eventsTypesCast = (Class<? extends IEvents>[]) eventsTypes;
        final ObservableRaiserMechanicConstruction ormc = ObservableRaiserMechanic.build(eventsTypesCast);

        final ObservableRaiserMechanic<ITotosEvents1Events2, ITotoEvents1Events2Observer> observableRaiserMechanic =
            ormc.getObservableRaiserMechanic(ITotosEvents1Events2.class);

        final ObservableRaiserMechanic<ITotosEvents1, IObserver<? extends ITotosEvents1>> observableRaiserMechanicTotosEvents1 =
            ormc.getObservableRaiserMechanic(ITotosEvents1.class);

        final ObservableRaiserMechanic<ITotosEvents3, IObserver<? extends ITotosEvents3>> observableRaiserMechanicTotosEvents3 =
            ObservableRaiserMechanic.build(ITotosEvents3.class);

        return new Toto(observableRaiserMechanic,
                        observableRaiserMechanicTotosEvents1,
                        observableRaiserMechanicTotosEvents3);
    }

    private Toto(IObservableRaiserMechanic<ITotosEvents1Events2, ITotoEvents1Events2Observer> observableRaiserMechanic,
                 IObservableRaiserMechanic<ITotosEvents1, IObserver<? extends ITotosEvents1>> observableRaiserMechanicTotosEvents1,
                 IObservableRaiserMechanic<ITotosEvents3, IObserver<? extends ITotosEvents3>> observableRaiserMechanicTotosEvents3) {

        this.observableRaiserMechanic = observableRaiserMechanic;
        this.observableRaiserMechanicTotosEvents1 = observableRaiserMechanicTotosEvents1;
        this.observableRaiserMechanicTotosEvents3 = observableRaiserMechanicTotosEvents3;

        this.raiser3 = observableRaiserMechanicTotosEvents3;
    }

    @Override
    public void f() {
        final String someParam = "yolo";
        this.raise().eventYouplaBoom1(someParam);
        this.raise().eventYouplaBoom1(42);
        this.raise().eventYoupla1(77, 37);
        this.raise().eventYoupla2();
        this.raise().eventCommon();

        this.raiser3.raise().eventAa3();
        this.raiser3.raise().eventCommon();
    }

    @Override
    public IObservableRaiserMechanic<ITotosEvents1Events2, ITotoEvents1Events2Observer> getObservableRaiserMechanic() {
        return this.observableRaiserMechanic;
    }

    @Override
    public IObservable<ITotosEvents1, IObserver<? extends ITotosEvents1>> manageTotoEvents1Observable() {
        return this.observableRaiserMechanicTotosEvents1;
    }

    @Override
    public IObservable<ITotosEvents3, IObserver<? extends ITotosEvents3>> manageTotoEvents3Observable() {
        return this.observableRaiserMechanicTotosEvents3;
    }
}
