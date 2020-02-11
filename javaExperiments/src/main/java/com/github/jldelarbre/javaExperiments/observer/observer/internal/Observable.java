package com.github.jldelarbre.javaExperiments.observer.observer.internal;

import java.util.HashSet;
import java.util.Set;

import com.github.jldelarbre.javaExperiments.observer.observer.IObservable;
import com.github.jldelarbre.javaExperiments.observer.observer.IEvents;
import com.github.jldelarbre.javaExperiments.observer.observer.IObserver;

final class Observable<EventsType extends IEvents, ObserverType extends IObserver<? extends EventsType>>
        implements IObservable<EventsType, ObserverType> {

    private final Class<EventsType> eventsType;
    private final Set<ObserverType> observers;

    private boolean enable = true;

    Observable(Class<EventsType> eventsType) {
        this.eventsType = eventsType;
        this.observers = new HashSet<>();
    }

    @Override
    public Class<EventsType> getObservableEventsType() {
        return this.eventsType;
    }

    @Override
    public boolean addObserver(ObserverType observer) {
        return this.observers.add(observer);
    }

    @Override
    public boolean removeObserver(ObserverType observer) {
        return this.observers.remove(observer);
    }

    @Override
    public void removeAllObservers() {
        this.observers.clear();
    }

    @Override
    public Set<ObserverType> getObservers() {
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
