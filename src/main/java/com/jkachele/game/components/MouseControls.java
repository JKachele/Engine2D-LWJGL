/******************************************
 *Project-------Learn-LWJGL
 *File----------MouseControls.java
 *Author--------Justin Kachele
 *Date----------10/8/2022
 *License-------GNU GENERAL PUBLIC LICENSE
 ******************************************/
package com.jkachele.game.components;

import com.jkachele.game.engine.GameObject;
import com.jkachele.game.engine.MouseListener;
import com.jkachele.game.engine.Window;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class MouseControls extends Component {
    GameObject holdingObject = null;

    public void pickupObject(GameObject object) {
        holdingObject = object;
        Window.getCurrentScene().addGameObject(object);
    }

    public void placeObject() {
        this.holdingObject = null;
    }

    public void update(float dt) {
        if (holdingObject!= null) {
            holdingObject.transform.position.x = MouseListener.getOrthoX() - 16;
            holdingObject.transform.position.y = MouseListener.getOrthoY() - 16;

            if (MouseListener.isButtonPressed(GLFW_MOUSE_BUTTON_LEFT)) {
                placeObject();
            }
        }
    }
}
