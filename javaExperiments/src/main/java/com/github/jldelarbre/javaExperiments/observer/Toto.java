package com.github.jldelarbre.javaExperiments.observer;

import com.github.jldelarbre.javaExperiments.observer.observer.IEventRaiser;
import com.github.jldelarbre.javaExperiments.observer.observer.IEventRaiserDefaultImpl;
import com.github.jldelarbre.javaExperiments.observer.observer.IObservable;
import com.github.jldelarbre.javaExperiments.observer.observer.IObservableDefaultImpl;
import com.github.jldelarbre.javaExperiments.observer.observer.IObserverHolder;
import com.github.jldelarbre.javaExperiments.observer.observer.ObservableRaiserMechanic;

public class Toto implements IToto, IObservableDefaultImpl<ITotoEvents1Events2Observer, IObservableTotosEvents1Events2>,
									IEventRaiserDefaultImpl<IObservableTotosEvents1Events2> {

	private final ObservableRaiserMechanic<ITotoEvents1Events2Observer, IObservableTotosEvents1Events2> observableRaiserMechanic;
	private final ObservableRaiserMechanic<ITotoEvents1Observer, IObservableTotosEvents1> observableRaiserMechanicTotosEvents1;
	private final ObservableRaiserMechanic<ITotoEvents3Observer, IObservableTotosEvents3> observableRaiserMechanicTotosEvents3;

	private final IEventRaiser<? extends IObservableTotosEvents3> raiser3;

	public static IToto build() {
		ObservableRaiserMechanic<ITotoEvents1Events2Observer, IObservableTotosEvents1Events2> observableRaiserMechanic =
			ObservableRaiserMechanic.build(IObservableTotosEvents1Events2.class, ITotoEvents1Events2Observer.class);

		ObservableRaiserMechanic<ITotoEvents1Observer, IObservableTotosEvents1> observableRaiserMechanicTotosEvents1 =
			ObservableRaiserMechanic.buildWithRaiser(IObservableTotosEvents1.class, ITotoEvents1Observer.class, observableRaiserMechanic);

		ObservableRaiserMechanic<ITotoEvents3Observer, IObservableTotosEvents3> observableRaiserMechanicTotosEvents3 =
			ObservableRaiserMechanic.build(IObservableTotosEvents3.class, ITotoEvents3Observer.class, observableRaiserMechanic);

		return new Toto(observableRaiserMechanic, observableRaiserMechanicTotosEvents1, observableRaiserMechanicTotosEvents3);
	}

	private Toto(ObservableRaiserMechanic<ITotoEvents1Events2Observer, IObservableTotosEvents1Events2> observableRaiserMechanic,
				 ObservableRaiserMechanic<ITotoEvents1Observer, IObservableTotosEvents1> observableRaiserMechanicTotosEvents1,
				 ObservableRaiserMechanic<ITotoEvents3Observer, IObservableTotosEvents3> observableRaiserMechanicTotosEvents3) {

		this.observableRaiserMechanic = observableRaiserMechanic;
		this.observableRaiserMechanicTotosEvents1 = observableRaiserMechanicTotosEvents1;
		this.observableRaiserMechanicTotosEvents3 = observableRaiserMechanicTotosEvents3;

		raiser3 = observableRaiserMechanicTotosEvents3.getRaiser();
	}

	@Override
	public void f() {
		String someParam = "yolo";
		raise().eventYouplaBoom1(someParam);
		raise().eventYoupla2();
		raise().eventCommon();

		raiser3.raise().eventAa3();
		raiser3.raise().eventCommon();
	}

	@Override
	public Class<ITotoEvents1Events2Observer> getObserverType() {
		return observableRaiserMechanic.getObserverType();
	}

	@Override
	public IObserverHolder getObserverHolder() {
		return observableRaiserMechanic.getObserverHolder();
	}

	@Override
	public IEventRaiser<? extends IObservableTotosEvents1Events2> getRaiser() {
		return observableRaiserMechanic.getRaiser();
	}

	@Override
	public IObservable<ITotoEvents1Observer, IObservableTotosEvents1> manageObserverTotoEvents1() {
		return observableRaiserMechanicTotosEvents1.getObservable();
	}

	@Override
	public IObservable<ITotoEvents3Observer, IObservableTotosEvents3> manageObserverTotoEvents3() {
		return observableRaiserMechanicTotosEvents3.getObservable();
	}
}
