package com.github.jldelarbre.javaExperiments.observer.observer.internal;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.stream.Stream;

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
        return new EventRaiser<>(observablesEventsType);
    }

    @Override
    public ObservablesEventsType raise() {
        return this.observablesEventsType;
    }

    private static <ObservablesEventsType extends IObservablesEvents> ObservablesEventsType
        buildRaiser(Class<ObservablesEventsType> raisableObservablesEventsType, IObserverMerger observerMerger) {

        final InvocationHandler invocationHandler = (proxy, method, methodArgs) -> {
            final Class<?>[] methodArgsType = method.getParameterTypes();
            final String methodName = method.getName();
            final Class<?> declaringMethodClass = method.getDeclaringClass();

            for (final IObserver<?> currentObserver : observerMerger.getAllObservers()) {
                final Class<? extends IObservablesEvents> currentObservedEventsType =
                    currentObserver.getObservedEventsType();

                if (IObservablesEvents.class.isAssignableFrom(declaringMethodClass)
                        && declaringMethodClass.isAssignableFrom(currentObservedEventsType)
                        || getAllInterfaces(raisableObservablesEventsType)
                            .anyMatch(interfaceType -> IObservablesEvents.class.isAssignableFrom(interfaceType)
                                && interfaceType.isAssignableFrom(currentObservedEventsType)
                                && Arrays.stream(interfaceType.getMethods())
                                    .anyMatch(imethod -> {
                                        return methodName.equals(imethod.getName())
                                            && Arrays.equals(methodArgsType, imethod.getParameterTypes());
                                    }))) {
                    try {
                        final Method methodToInvoke =
                            currentObservedEventsType.getMethod(methodName, methodArgsType);
                        methodToInvoke.invoke(currentObserver.process(), methodArgs);
                    } catch (final NullPointerException | SecurityException | IllegalAccessException
                            | IllegalArgumentException | InvocationTargetException | ExceptionInInitializerError
                            | NoSuchMethodException e) {
                        throw e;
                    }
                }
            }
            return null;
        };
        return raisableObservablesEventsType.cast(Proxy.newProxyInstance(raisableObservablesEventsType.getClassLoader(),
                                                                         new Class[] { raisableObservablesEventsType },
                                                                         invocationHandler));
    }
    
    private static Stream<Class<?>> getAllInterfaces(Class<?> type) {
        return Arrays.stream(type.getInterfaces()).flatMap(EventRaiser::getAllInterfaces);
    }
}
