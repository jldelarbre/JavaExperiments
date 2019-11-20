package com.github.jldelarbre.javaExperiments.observer.observer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

public interface IEventRaiserDefaultImpl<ObservablesEventsType extends IObservablesEvents> extends IEventRaiser<ObservablesEventsType> {

	IEventRaiser<? extends ObservablesEventsType> getRaiser();

	static <ObservablesEventsType extends IObservablesEvents>
	ObservablesEventsType build(Class<ObservablesEventsType> raisableObservablesEventsType, IObserverHolder observerHolder) {

		final InvocationHandler invocationHandler = (proxy, method, methodArgs) -> {
			Class<?>[] methodArgsType = extractMethodArgsType(methodArgs);

			for (IObserver<?> currentObserver : observerHolder.getAllObservers()) {
				Class<? extends IObservablesEvents> currentObserverObservablesEventsType = currentObserver.getObservablesEventsType();

				if (currentObserverObservablesEventsType.isAssignableFrom(raisableObservablesEventsType)) {
					try {
						Method methodToInvoke = currentObserverObservablesEventsType.getMethod(method.getName(), methodArgsType);
						methodToInvoke.invoke(currentObserver.process(), methodArgs);
					} catch (final NoSuchMethodException | NullPointerException | SecurityException | IllegalAccessException
						| IllegalArgumentException | InvocationTargetException | ExceptionInInitializerError e) {
						// Ignore if method to call does not exist on observer; normal case
					}
				}
			}
			return null;
		};
		return raisableObservablesEventsType.cast(Proxy.newProxyInstance(IEventRaiserDefaultImpl.class.getClassLoader(),
													new Class[] {raisableObservablesEventsType},
													invocationHandler));
	}

	static Class<?>[] extractMethodArgsType(Object[] methodArgs) {
		List<Class<?>> methodArgsTypeList = new ArrayList<>();
		if (methodArgs != null) {
			for (Object methodArg : methodArgs) {
				methodArgsTypeList.add(methodArg.getClass());
			}
		}
		Class<?>[] methodArgsType = methodArgsTypeList.toArray(new Class<?>[0]);
		return methodArgsType;
	}

	@Override
	default	ObservablesEventsType raise() {
		return getRaiser().raise();
	}

	static final class RaiserData<ObservablesEventsType extends IObservablesEvents> implements IEventRaiserDefaultImpl<ObservablesEventsType> {
		private final IEventRaiser<? extends ObservablesEventsType> raiser;

		private RaiserData(ObservablesEventsType observablesEventsType) {
			this.raiser = new IEventRaiserDefaultImpl<ObservablesEventsType>() {

				@Override
				public IEventRaiser<ObservablesEventsType> getRaiser() {
					return this;
				}

				@Override
				public ObservablesEventsType raise() {
					return observablesEventsType;
				}
			};
		}

		public static <ObservablesEventsType extends IObservablesEvents>
		RaiserData<ObservablesEventsType> build(Class<ObservablesEventsType> raisableObservablesEventsType,
												IObserverHolder observerHolder) {
			ObservablesEventsType observablesEventsType =
				IEventRaiserDefaultImpl.build(raisableObservablesEventsType, observerHolder);
			return new RaiserData<ObservablesEventsType>(observablesEventsType);
		}

		@Override
		public IEventRaiser<? extends ObservablesEventsType> getRaiser() {
			return raiser;
		}
	}
}
