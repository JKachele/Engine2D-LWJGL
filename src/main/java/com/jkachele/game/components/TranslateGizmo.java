/******************************************
 *Project-------Engine2D-LWJGL
 *File----------TranslateGizmo.java
 *Author--------Justin Kachele
 *Date----------10/24/2022
 *License-------Mozilla Public License Version 2.0
 ******************************************/
package com.jkachele.game.components;

import com.jkachele.game.editor.PropertiesWindow;
import com.jkachele.game.engine.MouseListener;

public class TranslateGizmo extends Gizmo {

    public TranslateGizmo(Sprite arrowSprite, PropertiesWindow propertiesWindow) {
        super(arrowSprite, propertiesWindow);
    }

    @Override
    public void editorUpdate(float dt) {
        if (activeGameObject != null) {
            if (xAxisActive && !yAxisActive && !zAxisActive) {
                activeGameObject.transform.position.x -= MouseListener.getWorldDX();
            }
            if (yAxisActive && !xAxisActive && !zAxisActive) {
                activeGameObject.transform.position.y -= MouseListener.getWorldDY();
            }
            if (zAxisActive && !xAxisActive && !yAxisActive) {
                activeGameObject.transform.position.x -= MouseListener.getWorldDX();
                activeGameObject.transform.position.y -= MouseListener.getWorldDY();
            }
        }
        super.editorUpdate(dt);
    }
}
