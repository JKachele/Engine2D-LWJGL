/******************************************
 *Project-------Engine2D-LWJGL
 *File----------Collider.java
 *Author--------Justin Kachele
 *Date----------11/4/2022
 *License-------MIT License
 ******************************************/
package com.jkachele.game.physics2d.components;

import com.jkachele.game.components.Component;
import org.joml.Vector2f;

public abstract class Collider extends Component {
    protected Vector2f offset = new Vector2f();

    public Vector2f getOffset() {
        return offset;
    }
}
