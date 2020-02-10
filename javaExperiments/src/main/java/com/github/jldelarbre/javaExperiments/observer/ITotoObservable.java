
package com.github.jldelarbre.javaExperiments.observer;

import com.github.jldelarbre.javaExperiments.observer.observer.IObservable;
import com.github.jldelarbre.javaExperiments.observer.observer.IObserver;

public interface ITotoObservable extends IObservable<ITotosEvents1Events2, ITotoEvents1Events2Observer> {

    IObservable<ITotosEvents1, IObserver<? extends ITotosEvents1>> manageTotoEvents1Observable();

    IObservable<ITotosEvents3, IObserver<? extends ITotosEvents3>> manageTotoEvents3Observable();
}
