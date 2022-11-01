/******************************************
 *Project-------Engine2D-LWJGL
 *File----------Gizmo.java
 *Author--------Justin Kachele
 *Date----------10/31/2022
 *License-------GNU GENERAL PUBLIC LICENSE
 ******************************************/
package com.jkachele.game.components;

import com.jkachele.game.editor.PropertiesWindow;
import com.jkachele.game.engine.GameObject;
import com.jkachele.game.engine.MouseListener;
import com.jkachele.game.engine.Prefabs;
import com.jkachele.game.engine.Window;
import com.jkachele.game.util.Color;
import com.jkachele.game.util.GameMath;
import org.joml.Vector2f;
import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class Gizmo extends Component{
    private Vector4f xColor = Color.RED.toVector();
    private Vector4f xColorHover = Color.RED.scale(0.25f).toVector();
    private Vector4f yColor = Color.BLUE.toVector();
    private Vector4f yColorHover = Color.BLUE.scale(0.25f).toVector();
    private Vector4f zColor = Color.GREEN.toVector();
    private Vector4f zColorHover = Color.GREEN.scale(0.25f).toVector();

    private final Vector2f xOffset = new Vector2f(82, 8);
    private final Vector2f yOffset = new Vector2f(24, 82);
    private final Vector2f zOffset = new Vector2f(75, 65);
    private int gizmoWidth = 16;
    private int gizmoHeight = 48;
    protected boolean xAxisActive = false;
    protected boolean yAxisActive = false;
    protected boolean zAxisActive = false;

    private boolean using = false;

    private GameObject xObject;
    private GameObject yObject;
    private GameObject zObject;
    private SpriteRenderer xSprite;
    private SpriteRenderer ySprite;
    private SpriteRenderer zSprite;

    protected GameObject activeGameObject = null;
    private PropertiesWindow propertiesWindow;

    public Gizmo(Sprite arrowSprite, PropertiesWindow propertiesWindow) {
        this.xObject = Prefabs.generateSpriteObject(arrowSprite, 16, 48, false);
        this.yObject = Prefabs.generateSpriteObject(arrowSprite, 16, 48, false);
        this.zObject = Prefabs.generateSpriteObject(arrowSprite, 16, 48, false);
        this.xSprite = xObject.getComponent(SpriteRenderer.class);
        this.ySprite = yObject.getComponent(SpriteRenderer.class);
        this.zSprite = zObject.getComponent(SpriteRenderer.class);

        Window.getCurrentScene().addGameObject(xObject);
        Window.getCurrentScene().addGameObject(yObject);
        Window.getCurrentScene().addGameObject(zObject);

        this.propertiesWindow = propertiesWindow;
    }

    @Override
    public void start() {
        this.xObject.transform.rotation = 90;
        this.yObject.transform.rotation = 180;
        this.zObject.transform.rotation = 135;

        this.xObject.setTransient(true);
        this.yObject.setTransient(true);
        this.zObject.setTransient(true);
    }

    @Override
    public void update(float dt) {
        if (!using) {
            return;
        }
        this.activeGameObject = this.propertiesWindow.getCurrentGameObject();
        if (this.activeGameObject != null) {
            this.setActive();
        } else {
            this.setInactive();
            return;
        }

        boolean xAxisHot = checkXHoverState();
        boolean yAxisHot = checkYHoverState();
        boolean zAxisHot = checkZHoverState();

        if ((xAxisHot || xAxisActive) && MouseListener.isDragging() &&
                MouseListener.isButtonPressed(GLFW_MOUSE_BUTTON_LEFT) && !yAxisActive && !zAxisActive) {
            xAxisActive = true;
        } else if ((yAxisHot || yAxisActive) && MouseListener.isDragging() &&
                MouseListener.isButtonPressed(GLFW_MOUSE_BUTTON_LEFT) && !xAxisActive && !zAxisActive) {
            yAxisActive = true;
        } else if ((zAxisHot || zAxisActive) && MouseListener.isDragging() &&
                MouseListener.isButtonPressed(GLFW_MOUSE_BUTTON_LEFT) && !xAxisActive && !yAxisActive) {
            zAxisActive = true;
        } else {
            xAxisActive = false;
            yAxisActive = false;
            zAxisActive = false;
        }

        if (this.activeGameObject != null) {
            this.xObject.transform.position.set(this.activeGameObject.transform.position);
            this.yObject.transform.position.set(this.activeGameObject.transform.position);
            this.zObject.transform.position.set(this.activeGameObject.transform.position);
            this.xObject.transform.position.add(this.xOffset);
            this.yObject.transform.position.add(this.yOffset);
            this.zObject.transform.position.add(this.zOffset);
        }
    }

    private boolean checkXHoverState() {
        Vector2f mousePos = new Vector2f(MouseListener.getOrthoX(), MouseListener.getOrthoY());

        if (mousePos.x <= this.xObject.transform.position.x &&
                mousePos.x >= this.xObject.transform.position.x - gizmoHeight &&
                mousePos.y >= this.xObject.transform.position.y &&
                mousePos.y <= this.xObject.transform.position.y + gizmoWidth) {

            xSprite.setColor(xColorHover);

            return true;
        }

        xSprite.setColor(xColor);
        return false;
    }

    private boolean checkYHoverState() {
        Vector2f mousePos = new Vector2f(MouseListener.getOrthoPos());

        if (mousePos.x <= this.yObject.transform.position.x &&
                mousePos.x >= this.yObject.transform.position.x - gizmoWidth &&
                mousePos.y <= this.yObject.transform.position.y &&
                mousePos.y >= this.yObject.transform.position.y - gizmoHeight) {

            ySprite.setColor(yColorHover);

            return true;
        }

        ySprite.setColor(yColor);
        return false;
    }

    private boolean checkZHoverState() {
        Vector2f mousePos = new Vector2f(MouseListener.getOrthoPos());
        GameMath.rotate(mousePos, zObject.transform.position, -135);

        if (mousePos.x >= this.zObject.transform.position.x &&
                mousePos.x <= this.zObject.transform.position.x + gizmoWidth &&
                mousePos.y >= this.zObject.transform.position.y &&
                mousePos.y <= this.zObject.transform.position.y + gizmoHeight) {

            zSprite.setColor(zColorHover);

            return true;
        }

        zSprite.setColor(zColor);
        return false;
    }

    private void setActive() {
        this.xSprite.setColor(xColor);
        this.ySprite.setColor(yColor);
        this.zSprite.setColor(zColor);
    }

    private void setInactive() {
        this.activeGameObject = null;
        this.xSprite.setColor(Color.NULL);
        this.ySprite.setColor(Color.NULL);
        this.zSprite.setColor(Color.NULL);
    }

    public void setUsing(boolean using) {
        this.using = using;
        if (!using) {
            this.setInactive();
        }
    }
}
