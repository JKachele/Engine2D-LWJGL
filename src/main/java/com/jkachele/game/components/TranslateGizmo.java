/******************************************
 *Project-------Engine2D-LWJGL
 *File----------TranslateGizmo.java
 *Author--------Justin Kachele
 *Date----------10/24/2022
 *License-------GNU GENERAL PUBLIC LICENSE
 ******************************************/
package com.jkachele.game.components;

import com.jkachele.game.editor.PropertiesWindow;
import com.jkachele.game.engine.GameObject;
import com.jkachele.game.engine.Prefabs;
import com.jkachele.game.engine.Window;
import com.jkachele.game.util.Color;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class TranslateGizmo extends Component {
    private Vector4f xColor = Color.RED.toVector();
    private Vector4f xColorHover = Color.RED.toVector();
    private Vector4f yColor = Color.BLUE.toVector();
    private Vector4f yColorHover = Color.BLUE.toVector();

    private final Vector2f xOffset = new Vector2f(82, 8);
    private final Vector2f yOffset = new Vector2f(24, 82);

    private GameObject xObject;
    private GameObject yObject;
    private SpriteRenderer xSprite;
    private SpriteRenderer ySprite;

    private GameObject activeGameObject = null;
    private PropertiesWindow propertiesWindow;

    public TranslateGizmo(Sprite arrowSprite, PropertiesWindow propertiesWindow) {
        this.xObject = Prefabs.generateSpriteObject(arrowSprite, 16, 48);
        this.yObject = Prefabs.generateSpriteObject(arrowSprite, 16, 48);
        this.xSprite = xObject.getComponent(SpriteRenderer.class);
        this.ySprite = yObject.getComponent(SpriteRenderer.class);

        Window.getCurrentScene().addGameObject(xObject);
        Window.getCurrentScene().addGameObject(yObject);

        this.propertiesWindow = propertiesWindow;
    }

    @Override
    public void start() {
        this.xObject.transform.rotation = 90;
        this.yObject.transform.rotation = 180;

        this.xObject.setTransient(true);
        this.yObject.setTransient(true);
    }

    @Override
    public void update(float dt) {
        if (this.activeGameObject != null) {
            this.xObject.transform.position.set(this.activeGameObject.transform.position);
            this.yObject.transform.position.set(this.activeGameObject.transform.position);
            this.xObject.transform.position.add(this.xOffset);
            this.yObject.transform.position.add(this.yOffset);
        }

        this.activeGameObject = this.propertiesWindow.getCurrentGameObject();
        if (this.activeGameObject != null) {
            this.setActive();
        } else {
            this.setInactive();
        }
    }

    private void setActive() {
        this.xSprite.setColor(xColor);
        this.ySprite.setColor(yColor);
    }

    private void setInactive() {
        this.activeGameObject = null;
        this.xSprite.setColor(Color.NULL);
        this.ySprite.setColor(Color.NULL);
    }
}
