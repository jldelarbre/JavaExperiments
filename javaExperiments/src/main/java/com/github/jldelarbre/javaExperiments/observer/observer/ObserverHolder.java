package com.github.jldelarbre.javaExperiments.observer.observer;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class ObserverHolder implements IObserverHolder {

	private final Map<Class<? extends IObserver<?>>, Set<? extends IObserver<?>>> observerType2ObserverSet;

	private ObserverHolder() {
		super();
		this.observerType2ObserverSet = new HashMap<>();
	}

	public static ObserverHolder build() {
		return new ObserverHolder();
	}

	@Override
	public <ObserverType extends IObserver<ObservablesEventsType>, ObservablesEventsType extends IObservablesEvents>
	void addObserver(Class<ObserverType> observerType, ObserverType observer) {
		Collection<ObserverType> observers = getObservers(observerType);
		observers.add(observer);
	}

	@Override
	public <ObserverType extends IObserver<ObservablesEventsType>, ObservablesEventsType extends IObservablesEvents>
	void removeObserver(Class<ObserverType> observerType, ObserverType observer) {
		Collection<ObserverType> observers = getObservers(observerType);
		observers.remove(observer);
	}

	@Override
	public <ObserverType extends IObserver<ObservablesEventsType>, ObservablesEventsType extends IObservablesEvents>
	void removeAllObservers(Class<ObserverType> observerType) {
		Collection<ObserverType> observers = getObservers(observerType);
		observers.clear();
	}

	@Override
	public Collection<? extends IObserver<?>> getAllObservers() {
		Set<? extends IObserver<?>> observers = observerType2ObserverSet.values().stream()
			.reduce(new HashSet<>(),
					(globalSet, typedObservers) -> {
						@SuppressWarnings({ "rawtypes", "unchecked" })
						GenericGlue<?> genericGlue = new GenericGlue(globalSet, typedObservers);
						genericGlue.add();
						return globalSet;
					});

		return observers;
	}

	private static class GenericGlue<ObserverType extends IObserver<?>> {

		private final Set<ObserverType> observers;
		private final Set<ObserverType> typedObservers;

		public GenericGlue(Set<ObserverType> observers, Set<ObserverType> typedObservers) {
			super();
			this.observers = observers;
			this.typedObservers = typedObservers;
		}

		public void add() {
			observers.addAll(typedObservers);
		}
	}

	private <ObserverType extends IObserver<ObservablesEventsType>, ObservablesEventsType extends IObservablesEvents>
	Collection<ObserverType> getObservers(Class<ObserverType> observerType) {
		if (!observerType2ObserverSet.containsKey(observerType)) {
			observerType2ObserverSet.put(observerType, new HashSet<>());
		}
		@SuppressWarnings("unchecked")
		Collection<ObserverType> observers = (Collection<ObserverType>) observerType2ObserverSet.get(observerType);
		return observers;
	}
}
