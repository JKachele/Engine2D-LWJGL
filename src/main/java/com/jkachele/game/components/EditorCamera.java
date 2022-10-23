/******************************************
 *Project-------Engine2D-LWJGL
 *File----------EditorCamera.java
 *Author--------Justin Kachele
 *Date----------10/22/2022
 *License-------GNU GENERAL PUBLIC LICENSE
 ******************************************/
package com.jkachele.game.components;

import com.jkachele.game.engine.Camera;
import com.jkachele.game.engine.KeyListener;
import com.jkachele.game.engine.MouseListener;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

public class EditorCamera extends Component{
    private Camera levelEditorCamera;
    private Vector2f clickOrigin;
    private boolean reset = false;

    private float dragDebounce = 2.0f / 90.0f;      // 2 frames
    private float dragSensitivity = 30.0f;
    private float scrollSensitivity = 0.1f;
    private float lerpTime = 0.0f;
    private float maxZoomOut = 2.75f;

    public EditorCamera(Camera levelEditorCamera) {
        this.levelEditorCamera = levelEditorCamera;
        this.clickOrigin = new Vector2f();
    }

    @Override
    public void update(float dt) {
        if (MouseListener.isButtonPressed(GLFW_MOUSE_BUTTON_RIGHT) && dragDebounce > 0) {
            this.clickOrigin = new Vector2f(MouseListener.getOrthoX(), MouseListener.getOrthoY());
            dragDebounce -= dt;
            return;
        } else if (MouseListener.isButtonPressed(GLFW_MOUSE_BUTTON_RIGHT)) {
            Vector2f mousePos = new Vector2f(MouseListener.getOrthoX(), MouseListener.getOrthoY());
            Vector2f mouseDelta = new Vector2f(mousePos).sub(this.clickOrigin);

            Vector2f newPosition = levelEditorCamera.getPosition().sub(mouseDelta.mul(dt).mul(dragSensitivity));
            levelEditorCamera.setPosition(newPosition);

            this.clickOrigin.lerp(mousePos, dt);
            return;
        }

        if (dragDebounce <= 0.0f && !MouseListener.isButtonPressed(GLFW_MOUSE_BUTTON_RIGHT)) {
            dragDebounce = 0.1f;
        }

        if (MouseListener.getScrollY() != 0.0f) {
            float addValue = (float)Math.pow(Math.abs(MouseListener.getScrollY() * scrollSensitivity),
                    1 / levelEditorCamera.getZoom());
            addValue *= -Math.signum(MouseListener.getScrollY());

            // Limit zoom out
            if (levelEditorCamera.getZoom() + addValue <= maxZoomOut) {
                levelEditorCamera.addZoom(addValue);
            } else {
                levelEditorCamera.setZoom(maxZoomOut);
            }
        }

        if (KeyListener.isKeyPressed(GLFW_KEY_HOME)) {
            reset = true;
        }

        if (reset) {
            Vector2f homePosition = levelEditorCamera.getPosition().lerp(new Vector2f(), lerpTime);
            levelEditorCamera.setPosition(homePosition);
            this.lerpTime += 0.1f * dt;

            float currentZoom = this.levelEditorCamera.getZoom();
            levelEditorCamera.setZoom(currentZoom + ((1.0f - currentZoom) * lerpTime));

            if (Math.abs(levelEditorCamera.getPosition().x) <= 5.0f &&
                    Math.abs(levelEditorCamera.getPosition().y) <= 5.0f) {
                levelEditorCamera.setPosition(new Vector2f());
                levelEditorCamera.setZoom(1.0f);
                this.lerpTime = 0.0f;
                reset = false;
            }
        }
    }
}
