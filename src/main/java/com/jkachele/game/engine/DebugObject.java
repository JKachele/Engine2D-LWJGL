/******************************************
 *Project-------Engine2D-LWJGL
 *File----------DebugObject.java
 *Author--------Justin Kachele
 *Date----------10/18/2022
 *License-------Mozilla Public License Version 2.0
 ******************************************/
package com.jkachele.game.engine;

import com.jkachele.game.components.Transform;

public class DebugObject {
    public Transform transform;
    private int zIndex;

    public DebugObject(String name) {
        this.transform = new Transform();
        this.zIndex = 0;
    }

    public DebugObject(String name, Transform transform, int zIndex) {
        this.transform = transform;
        this.zIndex = zIndex;
    }

    public int zIndex() {
        return zIndex;
    }
}
