package com.github.jldelarbre.javaExperiments.observer.observer.internal;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.stream.Stream;

import com.github.jldelarbre.javaExperiments.observer.observer.IEventRaiser;
import com.github.jldelarbre.javaExperiments.observer.observer.IEvents;
import com.github.jldelarbre.javaExperiments.observer.observer.IObserver;

public final class EventRaiser<EventsType extends IEvents> implements IEventRaiser<EventsType> {

    private final EventsType events;

    private EventRaiser(EventsType events) {
        this.events = events;
    }

    public static <EventsType extends IEvents>
        EventRaiser<EventsType> build(Class<EventsType> eventsType, IObserverMerger observerMerger) {

        final EventsType events = buildRaiser(eventsType, observerMerger);
        return new EventRaiser<>(events);
    }

    @Override
    public EventsType raise() {
        return this.events;
    }

    private static <EventsType extends IEvents>
        EventsType buildRaiser(Class<EventsType> eventsType, IObserverMerger observerMerger) {

        final InvocationHandler invocationHandler = (proxy, method, methodArgs) -> {
            final Class<?>[] methodArgsType = method.getParameterTypes();
            final String methodName = method.getName();
            final Class<?> declaringMethodClass = method.getDeclaringClass();

            for (final IObserver<?> currentObserver : observerMerger.getAllObservers()) {
                final Class<? extends IEvents> currentObservedEventsType =
                    currentObserver.getObservedEventsType();

                if (IEvents.class.isAssignableFrom(declaringMethodClass)
                        && declaringMethodClass.isAssignableFrom(currentObservedEventsType)
                        || getAllInterfaces(eventsType)
                            .anyMatch(interfaceType -> IEvents.class.isAssignableFrom(interfaceType)
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
        return eventsType.cast(Proxy.newProxyInstance(eventsType.getClassLoader(),
                                                      new Class[] { eventsType },
                                                      invocationHandler));
    }
    
    private static Stream<Class<?>> getAllInterfaces(Class<?> type) {
        return Arrays.stream(type.getInterfaces()).flatMap(EventRaiser::getAllInterfaces);
    }
}
