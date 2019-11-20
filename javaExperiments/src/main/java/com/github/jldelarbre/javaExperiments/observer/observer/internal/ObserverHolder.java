package com.github.jldelarbre.javaExperiments.observer.observer.internal;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.github.jldelarbre.javaExperiments.observer.observer.IObservablesEvents;
import com.github.jldelarbre.javaExperiments.observer.observer.IObserver;

public final class ObserverHolder implements IObserverHolder {

    private final Map<Class<? extends IObserver<?>>, Set<? extends IObserver<?>>> observerType2ObserverSet;
    private final Map<Class<? extends IObserver<?>>, Set<? extends IObserver<?>>> disableObserverSet;

    private ObserverHolder() {
        this.observerType2ObserverSet = new HashMap<>();
        this.disableObserverSet = new HashMap<>();
    }

    public static ObserverHolder build() {
        return new ObserverHolder();
    }

    @Override
    public <ObserverType extends IObserver<ObservablesEventsType>, ObservablesEventsType extends IObservablesEvents>
        boolean addObserver(Class<? extends ObserverType> observerType, ObserverType observer) {
        final Collection<ObserverType> observers = this.getObservers(observerType);
        return observers.add(observer);
    }

    @Override
    public <ObserverType extends IObserver<ObservablesEventsType>, ObservablesEventsType extends IObservablesEvents>
        boolean removeObserver(Class<? extends ObserverType> observerType, ObserverType observer) {
        final Collection<ObserverType> observers = this.getObservers(observerType);
        return observers.remove(observer);
    }

    @Override
    public <ObserverType extends IObserver<ObservablesEventsType>, ObservablesEventsType extends IObservablesEvents>
        void removeAllObservers(Class<? extends ObserverType> observerType) {
        final Collection<ObserverType> observers = this.getObservers(observerType);
        observers.clear();
    }

    @Override
    public <ObserverType extends IObserver<ObservablesEventsType>, ObservablesEventsType extends IObservablesEvents>
        boolean disableObservers(Class<? extends ObserverType> observerType) {

        this.createIfNecessary(observerType);
        if (this.observerType2ObserverSet.containsKey(observerType)) {
            this.disableObserverSet.put(observerType, this.observerType2ObserverSet.get(observerType));
            this.observerType2ObserverSet.remove(observerType);
            return true;
        }
        return false;
    }

    @Override
    public <ObserverType extends IObserver<ObservablesEventsType>, ObservablesEventsType extends IObservablesEvents>
        boolean enableObservers(Class<? extends ObserverType> observerType) {

        if (this.disableObserverSet.containsKey(observerType)) {
            this.observerType2ObserverSet.put(observerType, this.disableObserverSet.get(observerType));
            this.disableObserverSet.remove(observerType);
            return true;
        }
        return false;
    }

    @Override
    public Collection<? extends IObserver<?>> getAllObservers() {
        final Set<? extends IObserver<?>> observers =
            this.observerType2ObserverSet.values().stream().reduce(new HashSet<>(), ObserverHolder::accumulate);

        return observers;
    }

    private static Set<? extends IObserver<?>> accumulate(Set<? extends IObserver<?>> globalSet,
                                                          Set<? extends IObserver<?>> typedObservers) {

        @SuppressWarnings({ "rawtypes", "unchecked" })
        final GenericGlue<?> genericGlue = new GenericGlue(globalSet, typedObservers);
        genericGlue.add();
        return globalSet;
    }

    private static class GenericGlue<ObserverType extends IObserver<?>> {

        private final Set<ObserverType> observers;
        private final Set<ObserverType> typedObservers;

        GenericGlue(Set<ObserverType> observers, Set<ObserverType> typedObservers) {
            super();
            this.observers = observers;
            this.typedObservers = typedObservers;
        }

        public void add() {
            this.observers.addAll(this.typedObservers);
        }
    }

    @SuppressWarnings("unchecked")
    private <ObserverType extends IObserver<ObservablesEventsType>, ObservablesEventsType extends IObservablesEvents>
        Collection<ObserverType> getObservers(Class<? extends ObserverType> observerType) {

        this.createIfNecessary(observerType);

        Collection<ObserverType> observers = null;
        if (this.observerType2ObserverSet.containsKey(observerType)) {
            observers = (Collection<ObserverType>) this.observerType2ObserverSet.get(observerType);
        } else if (this.disableObserverSet.containsKey(observerType)) {
            observers = (Collection<ObserverType>) this.disableObserverSet.get(observerType);
        }
        return observers;
    }

    private <ObserverType extends IObserver<ObservablesEventsType>, ObservablesEventsType extends IObservablesEvents>
        void createIfNecessary(Class<? extends ObserverType> observerType) {

        final boolean doNotExistInEnableSet = !this.observerType2ObserverSet.containsKey(observerType);
        if (doNotExistInEnableSet) {
            final boolean doNotExistInDisableSet = !this.disableObserverSet.containsKey(observerType);
            if (doNotExistInDisableSet) {
                this.observerType2ObserverSet.put(observerType, new HashSet<>());
            }
        }
    }
}
