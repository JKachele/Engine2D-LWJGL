/******************************************
 *Project-------Engine2D-LWJGL
 *File----------EventSystem.java
 *Author--------Justin Kachele
 *Date----------11/3/2022
 *License-------MIT License
 ******************************************/
package com.jkachele.game.observers;

import com.jkachele.game.engine.GameObject;
import com.jkachele.game.observers.events.Event;

import java.util.ArrayList;
import java.util.List;

public class EventSystem {
    private static List<Observer> observers = new ArrayList<>();

    public static void addObserver(Observer observer) {
        observers.add(observer);
    }

    public static void notify(GameObject gameObject, Event event) {
        for (Observer observer : observers) {
            observer.onNotify(gameObject, event);
        }
    }
}
