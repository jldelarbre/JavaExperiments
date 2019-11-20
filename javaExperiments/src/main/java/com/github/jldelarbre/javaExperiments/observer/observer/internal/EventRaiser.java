package com.github.jldelarbre.javaExperiments.observer.observer.internal;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import com.github.jldelarbre.javaExperiments.observer.observer.IEventRaiser;
import com.github.jldelarbre.javaExperiments.observer.observer.IObservablesEvents;
import com.github.jldelarbre.javaExperiments.observer.observer.IObserver;

public final class EventRaiser<ObservablesEventsType extends IObservablesEvents> implements IEventRaiser<ObservablesEventsType> {

    private final ObservablesEventsType observablesEventsType;

    private EventRaiser(ObservablesEventsType observablesEventsType) {
        this.observablesEventsType = observablesEventsType;
    }

    public static <ObservablesEventsType extends IObservablesEvents> EventRaiser<ObservablesEventsType>
        build(Class<ObservablesEventsType> raisableObservablesEventsType,
              IObserverHolder observerHolder) {

        final ObservablesEventsType observablesEventsType = buildRaiser(raisableObservablesEventsType, observerHolder);
        return new EventRaiser<ObservablesEventsType>(observablesEventsType);
    }

    @Override
    public ObservablesEventsType raise() {
        return this.observablesEventsType;
    }

    private static <ObservablesEventsType extends IObservablesEvents> ObservablesEventsType
        buildRaiser(Class<ObservablesEventsType> raisableObservablesEventsType, IObserverHolder observerHolder) {

        final InvocationHandler invocationHandler = (proxy, method, methodArgs) -> {
            final Class<?>[] methodArgsType = extractMethodArgsType(methodArgs);

            for (final IObserver<?> currentObserver : observerHolder.getAllObservers()) {
                final Class<? extends IObservablesEvents> currentObserverObservablesEventsType =
                    currentObserver.getObservablesEventsType();

                if (currentObserverObservablesEventsType.isAssignableFrom(raisableObservablesEventsType)) {
                    try {
                        final Method methodToInvoke =
                            currentObserverObservablesEventsType.getMethod(method.getName(), methodArgsType);
                        methodToInvoke.invoke(currentObserver.process(), methodArgs);
                    } catch (final NoSuchMethodException | NullPointerException | SecurityException
                        | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                        | ExceptionInInitializerError e) {
                        // Ignore if method to call does not exist on observer; normal case
                    }
                }
            }
            return null;
        };
        return raisableObservablesEventsType.cast(Proxy.newProxyInstance(raisableObservablesEventsType.getClassLoader(),
                                                                         new Class[] {raisableObservablesEventsType},
                                                                         invocationHandler));
    }

    static Class<?>[] extractMethodArgsType(Object[] methodArgs) {
        final List<Class<?>> methodArgsTypeList = new ArrayList<>();
        if (methodArgs != null) {
            for (final Object methodArg : methodArgs) {
                methodArgsTypeList.add(methodArg.getClass());
            }
        }
        final Class<?>[] methodArgsType = methodArgsTypeList.toArray(new Class<?>[0]);
        return methodArgsType;
    }
}
