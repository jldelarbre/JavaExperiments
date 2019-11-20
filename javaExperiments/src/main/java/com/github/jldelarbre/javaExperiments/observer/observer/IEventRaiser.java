package com.github.jldelarbre.javaExperiments.observer.observer;

public interface IEventRaiser<ObservablesEventsType extends IObservablesEvents> {

	ObservablesEventsType raise();
	//raise_async
	//raise_sync
}
