package com.github.jldelarbre.javaExperiments.observer.observer.internal;

import java.util.HashSet;
import java.util.Set;

import com.github.jldelarbre.javaExperiments.observer.observer.IObservable;
import com.github.jldelarbre.javaExperiments.observer.observer.IObservablesEvents;
import com.github.jldelarbre.javaExperiments.observer.observer.IObserver;

public final class Observable<ObservablesEventsType extends IObservablesEvents>
        implements IObservable<ObservablesEventsType> {

    private final Class<ObservablesEventsType> observablesEventsType;
    private final Set<IObserver<? extends ObservablesEventsType>> observers;

    private boolean enable = true;

    private Observable(Class<ObservablesEventsType> observablesEventsType) {
        this.observablesEventsType = observablesEventsType;
        this.observers = new HashSet<>();
    }

    public static <ObservablesEventsType extends IObservablesEvents> Observable<ObservablesEventsType>
        build(Class<ObservablesEventsType> observablesEventsType) {
        return new Observable<>(observablesEventsType);
    }

    @Override
    public Class<ObservablesEventsType> getObservablesEventsType() {
        return this.observablesEventsType;
    }

    @Override
    public boolean addObserver(IObserver<? extends ObservablesEventsType> observer) {
        return this.observers.add(observer);
    }

    @Override
    public boolean removeObserver(IObserver<? extends ObservablesEventsType> observer) {
        return this.observers.remove(observer);
    }

    @Override
    public void removeAllObservers() {
        this.observers.clear();
    }

    @Override
    public Set<IObserver<? extends ObservablesEventsType>> getObservers() {
        if (this.enable) {
            return new HashSet<>(this.observers);
        } else {
            return new HashSet<>();
        }
    }

    @Override
    public int getNumObservers() {
        return this.observers.size();
    }

    @Override
    public boolean disable() {
        final boolean change = this.enable == true;
        this.enable = false;
        return change;
    }

    @Override
    public boolean enable() {
        final boolean change = this.enable == false;
        this.enable = true;
        return change;
    }
}
