/******************************************
 *Project-------Engine2D-LWJGL
 *File----------Event.java
 *Author--------Justin Kachele
 *Date----------11/3/2022
 *License-------MIT License
 ******************************************/
package com.jkachele.game.observers.events;

public class Event {
    public EventType eventType;

    public Event(EventType eventType) {
        this.eventType = eventType;
    }

    public Event() {
        this.eventType = EventType.UserEvent;
    }
}
