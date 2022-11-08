/******************************************
 *Project-------Engine2D-LWJGL
 *File----------GizmoSystem.java
 *Author--------Justin Kachele
 *Date----------11/1/2022
 *License-------Mozilla Public License Version 2.0
 ******************************************/
package com.jkachele.game.components;

import com.jkachele.game.engine.KeyListener;
import com.jkachele.game.engine.Window;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_E;

public class GizmoSystem extends Component{
    private Spritesheet gizmos;
    private int usingGizmo = 0;
    private final int NUM_GIZMOS = 2;
    private float debounceTime = 0.2f;

    public GizmoSystem(Spritesheet gizmos) {
        this.gizmos = gizmos;
    }

    @Override
    public void start() {
        gameObject.addComponent(new TranslateGizmo(gizmos.getSprite(1),
                Window.getInstance().getImGuiLayer().getPropertiesWindow()));
        gameObject.addComponent(new ScaleGizmo(gizmos.getSprite(2),
                Window.getInstance().getImGuiLayer().getPropertiesWindow()));
    }

    @Override
    public void editorUpdate(float dt) {
        debounceTime -= dt;

        if (usingGizmo == 0) {
            gameObject.getComponent(TranslateGizmo.class).setUsing(true);
            gameObject.getComponent(ScaleGizmo.class).setUsing(false);
        } else if (usingGizmo == 1) {
            gameObject.getComponent(TranslateGizmo.class).setUsing(false);
            gameObject.getComponent(ScaleGizmo.class).setUsing(true);
        }

        if (KeyListener.isKeyPressed(GLFW_KEY_E) && debounceTime < 0){
            usingGizmo++;
            if (usingGizmo == NUM_GIZMOS) {
                usingGizmo = 0;
            }
            this.debounceTime = 0.2f;
        }
    }
}
