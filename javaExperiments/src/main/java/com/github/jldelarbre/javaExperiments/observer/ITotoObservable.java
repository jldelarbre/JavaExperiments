
package com.github.jldelarbre.javaExperiments.observer;

import com.github.jldelarbre.javaExperiments.observer.observer.IObservable;
import com.github.jldelarbre.javaExperiments.observer.observer.IObserver;

public interface ITotoObservable extends IObservable<IObservableTotosEvents1Events2, ITotoEvents1Events2Observer> {

    IObservable<IObservableTotosEvents1, IObserver<? extends IObservableTotosEvents1>> manageTotoEvents1Observable();

    IObservable<IObservableTotosEvents3, IObserver<? extends IObservableTotosEvents3>> manageTotoEvents3Observable();
}
