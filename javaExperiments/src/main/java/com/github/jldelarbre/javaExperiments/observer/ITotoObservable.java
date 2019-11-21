
package com.github.jldelarbre.javaExperiments.observer;

import com.github.jldelarbre.javaExperiments.observer.observer.IObservable;

public interface ITotoObservable extends IObservable<IObservableTotosEvents1Events2, ITotoEvents1Events2Observer> {

    IObservable<IObservableTotosEvents1, ITotoEvents1Observer> manageTotoEvents1Observable();

    IObservable<IObservableTotosEvents3, ITotoEvents3Observer> manageTotoEvents3Observable();
}
