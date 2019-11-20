package com.github.jldelarbre.javaExperiments.observer;

import com.github.jldelarbre.javaExperiments.observer.observer.IObservable;

public interface IObservableToto extends IObservable<IObserverTotoEvents1Events2, IObservableTotosEvents1Events2> {

	IObservable<IObserverTotoEvents1, IObservableTotosEvents1> manageObserverTotoEvents1();

	IObservable<IObserverTotoEvents3, IObservableTotosEvents3> manageObserverTotoEvents3();
}
