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
import com.jkachele.game.util.Settings;

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
            holdingObject.transform.position.x = (int)(MouseListener.getOrthoX() /
                    Settings.GRID_WIDTH) * Settings.GRID_WIDTH;
            holdingObject.transform.position.y = (int)(MouseListener.getOrthoY() /
                    Settings.GRID_HEIGHT) * Settings.GRID_HEIGHT;



            if (MouseListener.isButtonPressed(GLFW_MOUSE_BUTTON_LEFT)) {
                placeObject();
            }
        }
    }
}
