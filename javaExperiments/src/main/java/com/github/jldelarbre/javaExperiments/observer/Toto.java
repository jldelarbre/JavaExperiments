package com.github.jldelarbre.javaExperiments.observer;

import com.github.jldelarbre.javaExperiments.observer.observer.IEventRaiser;
import com.github.jldelarbre.javaExperiments.observer.observer.IEventRaiserDefaultImpl;
import com.github.jldelarbre.javaExperiments.observer.observer.IObservable;
import com.github.jldelarbre.javaExperiments.observer.observer.IObservableDefaultImpl;
import com.github.jldelarbre.javaExperiments.observer.observer.IObserverHolder;
import com.github.jldelarbre.javaExperiments.observer.observer.ObservableRaiserMechanic;
import com.github.jldelarbre.javaExperiments.observer.observer.ObserverData;
import com.github.jldelarbre.javaExperiments.observer.observer.ObserverHolder;

public class Toto implements IToto, IObservableDefaultImpl<IObserverTotoEvents1Events2, IObservableTotosEvents1Events2>,
									IEventRaiserDefaultImpl<IObservableTotosEvents1Events2> {

	private final ObservableRaiserMechanic<IObserverTotoEvents1Events2, IObservableTotosEvents1Events2> observableRaiserMechanic;
	private final ObservableRaiserMechanic<IObserverTotoEvents1, IObservableTotosEvents1> observableRaiserMechanicTotosEvents1;
	private final ObservableRaiserMechanic<IObserverTotoEvents3, IObservableTotosEvents3> observableRaiserMechanicTotosEvents3;

	private final IEventRaiser<? extends IObservableTotosEvents3> raiser3;

	public static IToto build() {
		IObserverHolder observerHolder = ObserverHolder.build();

		ObserverData<IObserverTotoEvents1Events2, IObservableTotosEvents1Events2> observerData =
			ObserverData.build(IObserverTotoEvents1Events2.class, observerHolder);
		ObservableRaiserMechanic<IObserverTotoEvents1Events2, IObservableTotosEvents1Events2> observableRaiserMechanic =
			ObservableRaiserMechanic.build(IObservableTotosEvents1Events2.class, observerData);

		ObserverData<IObserverTotoEvents1, IObservableTotosEvents1> observerDataTotoEvent1 =
			ObserverData.build(IObserverTotoEvents1.class, observerHolder);
		ObservableRaiserMechanic<IObserverTotoEvents1, IObservableTotosEvents1> observableRaiserMechanicTotosEvents1 =
			ObservableRaiserMechanic.build(observableRaiserMechanic.getRaiser(), observerDataTotoEvent1);

		ObserverData<IObserverTotoEvents3, IObservableTotosEvents3> observerDataTotoEvent3 =
			ObserverData.build(IObserverTotoEvents3.class, observerHolder);
		ObservableRaiserMechanic<IObserverTotoEvents3, IObservableTotosEvents3> observableRaiserMechanicTotosEvents3 =
			ObservableRaiserMechanic.build(IObservableTotosEvents3.class, observerDataTotoEvent3);

		return new Toto(observableRaiserMechanic, observableRaiserMechanicTotosEvents1, observableRaiserMechanicTotosEvents3);
	}

	private Toto(ObservableRaiserMechanic<IObserverTotoEvents1Events2, IObservableTotosEvents1Events2> observableRaiserMechanic,
				 ObservableRaiserMechanic<IObserverTotoEvents1, IObservableTotosEvents1> observableRaiserMechanicTotosEvents1,
				 ObservableRaiserMechanic<IObserverTotoEvents3, IObservableTotosEvents3> observableRaiserMechanicTotosEvents3) {

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
	public Class<IObserverTotoEvents1Events2> getObserverType() {
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
	public IObservable<IObserverTotoEvents1, IObservableTotosEvents1> manageObserverTotoEvents1() {
		return observableRaiserMechanicTotosEvents1.getObserverData();
	}

	@Override
	public IObservable<IObserverTotoEvents3, IObservableTotosEvents3> manageObserverTotoEvents3() {
		return observableRaiserMechanicTotosEvents3.getObserverData();
	}
}
