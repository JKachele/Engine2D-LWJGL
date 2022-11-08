/******************************************
 *Project-------Engine2D-LWJGL
 *File----------Observer.java
 *Author--------Justin Kachele
 *Date----------11/3/2022
 *License-------Mozilla Public License Version 2.0
 ******************************************/
package com.jkachele.game.observers;

import com.jkachele.game.engine.GameObject;
import com.jkachele.game.observers.events.Event;

public interface Observer {
    void onNotify(GameObject gameObject, Event event);
}
