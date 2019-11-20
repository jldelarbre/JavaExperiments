
package com.github.jldelarbre.javaExperiments.observer;

import com.github.jldelarbre.javaExperiments.observer.observer.IObservable;

public interface ITotoObservable extends IObservable<ITotoEvents1Events2Observer, IObservableTotosEvents1Events2> {

    IObservable<ITotoEvents1Observer, IObservableTotosEvents1> manageTotoEvents1Observable();

    IObservable<ITotoEvents3Observer, IObservableTotosEvents3> manageTotoEvents3Observable();
}
