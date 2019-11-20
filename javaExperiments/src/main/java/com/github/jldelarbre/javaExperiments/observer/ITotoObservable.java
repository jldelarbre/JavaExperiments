
package com.github.jldelarbre.javaExperiments.observer;

import com.github.jldelarbre.javaExperiments.observer.observer.IObservable;

public interface ITotoObservable extends IObservable<IObservableTotosEvents1Events2> {

    IObservable<IObservableTotosEvents1> manageTotoEvents1Observable();

    IObservable<IObservableTotosEvents3> manageTotoEvents3Observable();
}
