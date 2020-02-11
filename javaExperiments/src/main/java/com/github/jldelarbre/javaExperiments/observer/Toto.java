
package com.github.jldelarbre.javaExperiments.observer;

import static com.github.jldelarbre.javaExperiments.observer.observer.Factories.buildObservableRaiserEngine;
import static com.github.jldelarbre.javaExperiments.observer.observer.Factories.buildObservableRaiserEngineGroup;

import com.github.jldelarbre.javaExperiments.observer.observer.Factories.ObservableRaiserEngineGroup;
import com.github.jldelarbre.javaExperiments.observer.observer.IEventRaiser;
import com.github.jldelarbre.javaExperiments.observer.observer.IEvents;
import com.github.jldelarbre.javaExperiments.observer.observer.IObservable;
import com.github.jldelarbre.javaExperiments.observer.observer.IObservableRaiserEngine;
import com.github.jldelarbre.javaExperiments.observer.observer.IObserver;

public class Toto implements IToto, IObservableRaiserEngine.IProxy<ITotosEvents1Events2, ITotoEvents1Events2Observer> {

    private final IObservableRaiserEngine<ITotosEvents1Events2, ITotoEvents1Events2Observer> observableRaiserEngine;
    private final IObservableRaiserEngine<ITotosEvents1, IObserver<? extends ITotosEvents1>> observableRaiserEngineTotosEvents1;
    private final IObservableRaiserEngine<ITotosEvents3, IObserver<? extends ITotosEvents3>> observableRaiserEngineTotosEvents3;

    private final IEventRaiser<? extends ITotosEvents3> raiser3;

    public static IToto build() {
        final Class<?>[] eventsTypes =
            { ITotosEvents1Events2.class, ITotosEvents1.class };
        @SuppressWarnings("unchecked")
        final Class<? extends IEvents>[] eventsTypesCast = (Class<? extends IEvents>[]) eventsTypes;
        final ObservableRaiserEngineGroup ormc = buildObservableRaiserEngineGroup(eventsTypesCast);

        final IObservableRaiserEngine<ITotosEvents1Events2, ITotoEvents1Events2Observer> observableRaiserEngine =
            ormc.getObservableRaiserEngine(ITotosEvents1Events2.class);

        final IObservableRaiserEngine<ITotosEvents1, IObserver<? extends ITotosEvents1>> observableRaiserEngineTotosEvents1 =
            ormc.getObservableRaiserEngine(ITotosEvents1.class);

        final IObservableRaiserEngine<ITotosEvents3, IObserver<? extends ITotosEvents3>> observableRaiserEngineTotosEvents3 =
            buildObservableRaiserEngine(ITotosEvents3.class);

        return new Toto(observableRaiserEngine,
                        observableRaiserEngineTotosEvents1,
                        observableRaiserEngineTotosEvents3);
    }

    private Toto(IObservableRaiserEngine<ITotosEvents1Events2, ITotoEvents1Events2Observer> observableRaiserEngine,
                 IObservableRaiserEngine<ITotosEvents1, IObserver<? extends ITotosEvents1>> observableRaiserEngineTotosEvents1,
                 IObservableRaiserEngine<ITotosEvents3, IObserver<? extends ITotosEvents3>> observableRaiserEngineTotosEvents3) {

        this.observableRaiserEngine = observableRaiserEngine;
        this.observableRaiserEngineTotosEvents1 = observableRaiserEngineTotosEvents1;
        this.observableRaiserEngineTotosEvents3 = observableRaiserEngineTotosEvents3;

        this.raiser3 = observableRaiserEngineTotosEvents3;
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
    public IObservableRaiserEngine<ITotosEvents1Events2, ITotoEvents1Events2Observer> getObservableRaiserEngine() {
        return this.observableRaiserEngine;
    }

    @Override
    public IObservable<ITotosEvents1, IObserver<? extends ITotosEvents1>> manageTotoEvents1Observable() {
        return this.observableRaiserEngineTotosEvents1;
    }

    @Override
    public IObservable<ITotosEvents3, IObserver<? extends ITotosEvents3>> manageTotoEvents3Observable() {
        return this.observableRaiserEngineTotosEvents3;
    }
}
