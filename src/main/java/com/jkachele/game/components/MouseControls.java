/******************************************
 *Project-------Engine2D-LWJGL
 *File----------MouseControls.java
 *Author--------Justin Kachele
 *Date----------10/8/2022
 *License-------Mozilla Public License Version 2.0
 ******************************************/
package com.jkachele.game.components;

import com.jkachele.game.engine.GameObject;
import com.jkachele.game.engine.MouseListener;
import com.jkachele.game.engine.Window;
import com.jkachele.game.util.Constants;

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

    public void editorUpdate(float dt) {
        if (holdingObject!= null) {
            holdingObject.transform.position.x = ((int)(MouseListener.getOrthoX() /
                    Constants.GRID_WIDTH) * Constants.GRID_WIDTH)  + (Constants.GRID_WIDTH / 2f);
            holdingObject.transform.position.y = ((int)(MouseListener.getOrthoY() /
                    Constants.GRID_HEIGHT) * Constants.GRID_HEIGHT) + (Constants.GRID_HEIGHT / 2f);



            if (MouseListener.isButtonPressed(GLFW_MOUSE_BUTTON_LEFT)) {
                placeObject();
            }
        }
    }
}
