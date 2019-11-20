package com.github.jldelarbre.javaExperiments.observer;

public class ObserverTotoEvents3 implements IObserverTotoEvents3, IObservableTotosEvents3 {

	@Override
	public void eventCommon() {
		System.out.println("ObserverTotoEvents3.eventCommon()");
	}

	@Override
	public void eventAa3() {
		System.out.println("ObserverTotoEvents3.eventAa3()");
	}

	@Override
	public IObservableTotosEvents3 process() {
		return this;
	}

	@Override
	public Class<IObservableTotosEvents3> getObservablesEventsType() {
		return IObservableTotosEvents3.class;
	}
}
