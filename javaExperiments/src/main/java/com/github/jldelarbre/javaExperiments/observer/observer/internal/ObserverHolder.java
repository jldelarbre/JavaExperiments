
package com.github.jldelarbre.javaExperiments.observer.observer.internal;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.github.jldelarbre.javaExperiments.observer.observer.IObservablesEvents;
import com.github.jldelarbre.javaExperiments.observer.observer.IObserver;

public final class ObserverHolder implements IObserverHolder {

    private final Map<Class<? extends IObservablesEvents>, Set<? extends IObserver<?>>> eventType2ObserverSet;
    private final Map<Class<? extends IObservablesEvents>, Set<? extends IObserver<?>>> disableObserverSet;

    private ObserverHolder() {
        this.eventType2ObserverSet = new HashMap<>();
        this.disableObserverSet = new HashMap<>();
    }

    public static ObserverHolder build() {
        return new ObserverHolder();
    }

    @Override
    public <ObserverType extends IObserver<ObservablesEventsType>, ObservablesEventsType extends IObservablesEvents>
        boolean addObserver(ObserverType observer) {
        final Collection<ObserverType> observers = this.getObservers(observer.getObservablesEventsType());
        return observers.add(observer);
    }

    @Override
    public <ObserverType extends IObserver<ObservablesEventsType>, ObservablesEventsType extends IObservablesEvents>
        boolean removeObserver(ObserverType observer) {
        final Collection<ObserverType> observers = this.getObservers(observer.getObservablesEventsType());
        return observers.remove(observer);
    }

    @Override
    public <ObserverType extends IObserver<ObservablesEventsType>, ObservablesEventsType extends IObservablesEvents>
        void removeAllObservers(Class<? extends ObservablesEventsType> eventType) {
        final Collection<ObserverType> observers = this.getObservers(eventType);
        observers.clear();
    }

    @Override
    public <ObserverType extends IObserver<ObservablesEventsType>, ObservablesEventsType extends IObservablesEvents>
        boolean disableEvents(Class<? extends ObservablesEventsType> eventType) {

        this.createIfNecessary(eventType);
        if (this.eventType2ObserverSet.containsKey(eventType)) {
            this.disableObserverSet.put(eventType, this.eventType2ObserverSet.get(eventType));
            this.eventType2ObserverSet.remove(eventType);
            return true;
        }
        return false;
    }

    @Override
    public <ObserverType extends IObserver<ObservablesEventsType>, ObservablesEventsType extends IObservablesEvents>
        boolean enableEvents(Class<? extends ObservablesEventsType> eventType) {

        if (this.disableObserverSet.containsKey(eventType)) {
            this.eventType2ObserverSet.put(eventType, this.disableObserverSet.get(eventType));
            this.disableObserverSet.remove(eventType);
            return true;
        }
        return false;
    }

    @Override
    public Collection<? extends IObserver<?>> getAllObservers() {
        final Set<? extends IObserver<?>> observers =
            this.eventType2ObserverSet.values().stream().reduce(new HashSet<>(), ObserverHolder::accumulate);

        return observers;
    }

    private static Set<? extends IObserver<?>> accumulate(Set<? extends IObserver<?>> globalSet,
                                                          Set<? extends IObserver<?>> typedObservers) {

        @SuppressWarnings({"rawtypes", "unchecked"})
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
        Collection<ObserverType> getObservers(Class<? extends ObservablesEventsType> eventType) {

        this.createIfNecessary(eventType);

        Collection<ObserverType> observers = null;
        if (this.eventType2ObserverSet.containsKey(eventType)) {
            observers = (Collection<ObserverType>) this.eventType2ObserverSet.get(eventType);
        } else if (this.disableObserverSet.containsKey(eventType)) {
            observers = (Collection<ObserverType>) this.disableObserverSet.get(eventType);
        }
        return observers;
    }

    private <ObserverType extends IObserver<ObservablesEventsType>, ObservablesEventsType extends IObservablesEvents>
        void createIfNecessary(Class<? extends ObservablesEventsType> eventType) {

        final boolean doNotExistInEnableSet = !this.eventType2ObserverSet.containsKey(eventType);
        if (doNotExistInEnableSet) {
            final boolean doNotExistInDisableSet = !this.disableObserverSet.containsKey(eventType);
            if (doNotExistInDisableSet) {
                this.eventType2ObserverSet.put(eventType, new HashSet<>());
            }
        }
    }
}
