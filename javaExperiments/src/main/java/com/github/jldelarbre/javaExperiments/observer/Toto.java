
package com.github.jldelarbre.javaExperiments.observer;

import com.github.jldelarbre.javaExperiments.observer.observer.IEventRaiser;
import com.github.jldelarbre.javaExperiments.observer.observer.IObservable;
import com.github.jldelarbre.javaExperiments.observer.observer.IObservableRaiserMechanic;
import com.github.jldelarbre.javaExperiments.observer.observer.IObservablesEvents;
import com.github.jldelarbre.javaExperiments.observer.observer.IObserver;
import com.github.jldelarbre.javaExperiments.observer.observer.ObservableRaiserMechanic;
import com.github.jldelarbre.javaExperiments.observer.observer.ObservableRaiserMechanic.ObservableRaiserMechanicConstruction;

public class Toto implements IToto, IObservableRaiserMechanic.IProxy<IObservableTotosEvents1Events2, ITotoEvents1Events2Observer> {

    private final IObservableRaiserMechanic<IObservableTotosEvents1Events2, ITotoEvents1Events2Observer> observableRaiserMechanic;
    private final IObservableRaiserMechanic<IObservableTotosEvents1, IObserver<? extends IObservableTotosEvents1>> observableRaiserMechanicTotosEvents1;
    private final IObservableRaiserMechanic<IObservableTotosEvents3, IObserver<? extends IObservableTotosEvents3>> observableRaiserMechanicTotosEvents3;

    private final IEventRaiser<? extends IObservableTotosEvents3> raiser3;

    public static IToto build() {
        final Class<?>[] observablesEventsTypes =
            { IObservableTotosEvents1Events2.class, IObservableTotosEvents1.class };
        @SuppressWarnings("unchecked")
        final Class<? extends IObservablesEvents>[] observablesEventsTypesCast =
            (Class<? extends IObservablesEvents>[]) observablesEventsTypes;
        final ObservableRaiserMechanicConstruction ormc = ObservableRaiserMechanic.build(observablesEventsTypesCast);

        final ObservableRaiserMechanic<IObservableTotosEvents1Events2, ITotoEvents1Events2Observer> observableRaiserMechanic =
            ormc.getObservableRaiserMechanic(IObservableTotosEvents1Events2.class);

        final ObservableRaiserMechanic<IObservableTotosEvents1, IObserver<? extends IObservableTotosEvents1>> observableRaiserMechanicTotosEvents1 =
            ormc.getObservableRaiserMechanic(IObservableTotosEvents1.class);

        final ObservableRaiserMechanic<IObservableTotosEvents3, IObserver<? extends IObservableTotosEvents3>> observableRaiserMechanicTotosEvents3 =
            ObservableRaiserMechanic.build(IObservableTotosEvents3.class);

        return new Toto(observableRaiserMechanic,
                        observableRaiserMechanicTotosEvents1,
                        observableRaiserMechanicTotosEvents3);
    }

    private Toto(IObservableRaiserMechanic<IObservableTotosEvents1Events2, ITotoEvents1Events2Observer> observableRaiserMechanic,
                 IObservableRaiserMechanic<IObservableTotosEvents1, IObserver<? extends IObservableTotosEvents1>> observableRaiserMechanicTotosEvents1,
                 IObservableRaiserMechanic<IObservableTotosEvents3, IObserver<? extends IObservableTotosEvents3>> observableRaiserMechanicTotosEvents3) {

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
    public IObservableRaiserMechanic<IObservableTotosEvents1Events2, ITotoEvents1Events2Observer> getObservableRaiserMechanic() {
        return this.observableRaiserMechanic;
    }

    @Override
    public IObservable<IObservableTotosEvents1, IObserver<? extends IObservableTotosEvents1>> manageTotoEvents1Observable() {
        return this.observableRaiserMechanicTotosEvents1;
    }

    @Override
    public IObservable<IObservableTotosEvents3, IObserver<? extends IObservableTotosEvents3>> manageTotoEvents3Observable() {
        return this.observableRaiserMechanicTotosEvents3;
    }
}
