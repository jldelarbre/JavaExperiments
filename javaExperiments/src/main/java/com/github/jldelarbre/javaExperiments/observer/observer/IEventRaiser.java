package com.github.jldelarbre.javaExperiments.observer.observer;

public interface IEventRaiser<EventsType extends IEvents> {

    EventsType raise();
    // raise_async
    // raise_sync

    interface IProxy<EventsType extends IEvents> extends IEventRaiser<EventsType> {

        IEventRaiser<EventsType> getRaiser();

        @Override
        default EventsType raise() {
            return getRaiser().raise();
        }
    }
}
