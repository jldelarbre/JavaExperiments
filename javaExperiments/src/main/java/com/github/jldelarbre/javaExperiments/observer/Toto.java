package com.github.jldelarbre.javaExperiments.observer;

import com.github.jldelarbre.javaExperiments.observer.observer.IEventRaiser;
import com.github.jldelarbre.javaExperiments.observer.observer.IObservable;
import com.github.jldelarbre.javaExperiments.observer.observer.IObservableRaiserMechanic;
import com.github.jldelarbre.javaExperiments.observer.observer.ObservableRaiserMechanic;

public class Toto
        implements IToto, IObservableRaiserMechanic<ITotoEvents1Events2Observer, IObservableTotosEvents1Events2> {

    private final IObservableRaiserMechanic<ITotoEvents1Events2Observer, IObservableTotosEvents1Events2> observableRaiserMechanic;
    private final IObservableRaiserMechanic<ITotoEvents1Observer, IObservableTotosEvents1> observableRaiserMechanicTotosEvents1;
    private final IObservableRaiserMechanic<ITotoEvents3Observer, IObservableTotosEvents3> observableRaiserMechanicTotosEvents3;

    private final IEventRaiser<? extends IObservableTotosEvents3> raiser3;

    public static IToto build() {
        final ObservableRaiserMechanic<ITotoEvents1Events2Observer, IObservableTotosEvents1Events2> observableRaiserMechanic =
            ObservableRaiserMechanic.build(IObservableTotosEvents1Events2.class, ITotoEvents1Events2Observer.class);

        final ObservableRaiserMechanic<ITotoEvents1Observer, IObservableTotosEvents1> observableRaiserMechanicTotosEvents1 =
            ObservableRaiserMechanic.buildWithRaiser(IObservableTotosEvents1.class,
                                                     ITotoEvents1Observer.class,
                                                     observableRaiserMechanic);

        final ObservableRaiserMechanic<ITotoEvents3Observer, IObservableTotosEvents3> observableRaiserMechanicTotosEvents3 =
            ObservableRaiserMechanic
                    .build(IObservableTotosEvents3.class, ITotoEvents3Observer.class, observableRaiserMechanic);

        return new Toto(observableRaiserMechanic,
                        observableRaiserMechanicTotosEvents1,
                        observableRaiserMechanicTotosEvents3);
    }

    private Toto(IObservableRaiserMechanic<ITotoEvents1Events2Observer, IObservableTotosEvents1Events2> observableRaiserMechanic,
                 IObservableRaiserMechanic<ITotoEvents1Observer, IObservableTotosEvents1> observableRaiserMechanicTotosEvents1,
                 IObservableRaiserMechanic<ITotoEvents3Observer, IObservableTotosEvents3> observableRaiserMechanicTotosEvents3) {

        this.observableRaiserMechanic = observableRaiserMechanic;
        this.observableRaiserMechanicTotosEvents1 = observableRaiserMechanicTotosEvents1;
        this.observableRaiserMechanicTotosEvents3 = observableRaiserMechanicTotosEvents3;

        this.raiser3 = observableRaiserMechanicTotosEvents3.getRaiser();
    }

    @Override
    public void f() {
        final String someParam = "yolo";
        this.raise().eventYouplaBoom1(someParam);
        this.raise().eventYoupla2();
        this.raise().eventCommon();

        this.raiser3.raise().eventAa3();
        this.raiser3.raise().eventCommon();
    }

    @Override
    public IObservableRaiserMechanic<ITotoEvents1Events2Observer, IObservableTotosEvents1Events2>
        getObservableRaiserMechanic() {
        return this.observableRaiserMechanic;
    }

    @Override
    public IObservable<ITotoEvents1Observer, IObservableTotosEvents1> manageObserverTotoEvents1() {
        return this.observableRaiserMechanicTotosEvents1.getObservable();
    }

    @Override
    public IObservable<ITotoEvents3Observer, IObservableTotosEvents3> manageObserverTotoEvents3() {
        return this.observableRaiserMechanicTotosEvents3.getObservable();
    }
}
