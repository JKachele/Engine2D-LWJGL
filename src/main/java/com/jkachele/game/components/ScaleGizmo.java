/******************************************
 *Project-------Engine2D-LWJGL
 *File----------ScaleGizmo.java
 *Author--------Justin Kachele
 *Date----------10/31/2022
 *License-------MIT License
 ******************************************/
package com.jkachele.game.components;

import com.jkachele.game.editor.PropertiesWindow;
import com.jkachele.game.engine.MouseListener;

public class ScaleGizmo extends Gizmo{
    public ScaleGizmo(Sprite scaleSprite, PropertiesWindow propertiesWindow) {
        super(scaleSprite, propertiesWindow);
    }

    @Override
    public void editorUpdate(float dt) {
        if (activeGameObject != null) {
            if(xAxisActive && !yAxisActive && !zAxisActive) {
                activeGameObject.transform.scale.x -= MouseListener.getWorldDX();
            }
            if(yAxisActive && !xAxisActive && !zAxisActive) {
                activeGameObject.transform.scale.y -= MouseListener.getWorldDY();
            }
            if (zAxisActive && !xAxisActive && !yAxisActive) {
                float distance = (float)(Math.sqrt((MouseListener.getWorldDX() * MouseListener.getWorldDX()) +
                        (MouseListener.getWorldDY() * MouseListener.getWorldDY())));
                if (MouseListener.getWorldDX() < 0 || MouseListener.getWorldDY() < 0) {
                    distance *= -1;
                }
                activeGameObject.transform.scale.x -= distance;
                activeGameObject.transform.scale.y -= distance;
            }
        }
        super.editorUpdate(dt);
    }
}
