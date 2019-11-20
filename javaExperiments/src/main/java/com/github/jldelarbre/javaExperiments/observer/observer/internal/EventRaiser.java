package com.github.jldelarbre.javaExperiments.observer.observer.internal;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.github.jldelarbre.javaExperiments.observer.observer.IEventRaiser;
import com.github.jldelarbre.javaExperiments.observer.observer.IObservablesEvents;
import com.github.jldelarbre.javaExperiments.observer.observer.IObserver;

public final class EventRaiser<ObservablesEventsType extends IObservablesEvents>
        implements IEventRaiser<ObservablesEventsType> {

    private final ObservablesEventsType observablesEventsType;

    private EventRaiser(ObservablesEventsType observablesEventsType) {
        this.observablesEventsType = observablesEventsType;
    }

    public static <ObservablesEventsType extends IObservablesEvents> EventRaiser<ObservablesEventsType>
        build(Class<ObservablesEventsType> raisableObservablesEventsType, IObserverMerger observerMerger) {

        final ObservablesEventsType observablesEventsType = buildRaiser(raisableObservablesEventsType, observerMerger);
        return new EventRaiser<ObservablesEventsType>(observablesEventsType);
    }

    @Override
    public ObservablesEventsType raise() {
        return this.observablesEventsType;
    }

    private static <ObservablesEventsType extends IObservablesEvents> ObservablesEventsType
        buildRaiser(Class<ObservablesEventsType> raisableObservablesEventsType, IObserverMerger observerMerger) {

        final InvocationHandler invocationHandler = (proxy, method, methodArgs) -> {
            final Class<?>[] methodArgsType = method.getParameterTypes();

            for (final IObserver<?> currentObserver : observerMerger.getAllObservers()) {
                final Class<? extends IObservablesEvents> currentObserverObservedEventsType =
                    currentObserver.getObservedEventsType();

                if (currentObserverObservedEventsType.isAssignableFrom(raisableObservablesEventsType)) {
                    try {
                        final Method methodToInvoke =
                            currentObserverObservedEventsType.getMethod(method.getName(), methodArgsType);
                        methodToInvoke.invoke(currentObserver.process(), methodArgs);
                    } catch (final NullPointerException | SecurityException | IllegalAccessException
                            | IllegalArgumentException | InvocationTargetException | ExceptionInInitializerError e) {
                        throw e;
                    } catch (final NoSuchMethodException e) {
                        // Ignore if method to call does not exist on observer; normal case
                    }
                }
            }
            return null;
        };
        return raisableObservablesEventsType.cast(Proxy.newProxyInstance(raisableObservablesEventsType.getClassLoader(),
                                                                         new Class[] { raisableObservablesEventsType },
                                                                         invocationHandler));
    }
}
