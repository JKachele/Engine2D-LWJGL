/******************************************
 *Project-------Engine2D-LWJGL
 *File----------MouseListener.java
 *Author--------Justin Kachele
 *Date----------9/24/20.022
 *License-------Mozilla Public License Version 2.0
 ******************************************/
package com.jkachele.game.engine;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.*;

@SuppressWarnings("unused")
public class MouseListener {
    private static MouseListener instance;
    private static double scrollX = 0.0;
    private static double scrollY = 0.0;
    private static double posX = 0.0;
    private static double posY = 0.0;
    private static double lastX = 0.0;
    private static double lastY = 0.0;
    private static double worldX = 0.0;
    private static double worldY = 0.0;
    private static double lastWorldX = 0.0;
    private static double lastWorldY = 0.0;
    private static boolean[] mouseButtonPressed = new boolean[9];
    private static int mouseButtonsDown = 0;
    private static boolean isDragging;

    private static  Vector2f gameViewportPos = new Vector2f();
    private static Vector2f gameViewportSize = new Vector2f();

    public static void mousePosCallback(long window, double posX, double posY) {
        if(MouseListener.mouseButtonsDown > 0) {
            MouseListener.isDragging = true;
        }

        MouseListener.lastX = MouseListener.posX;
        MouseListener.lastY = MouseListener.posY;
        MouseListener.posX = posX;
        MouseListener.posY = posY;
        MouseListener.lastWorldX = MouseListener.worldX;
        MouseListener.lastWorldY = MouseListener.worldY;
        calcWorldX();
        calcWorldY();
    }

    public static void mouseButtonCallback(long window, int button, int action, int mods) {
        //if mouse button is pressed, set element of array to true
        if(action == GLFW_PRESS) {
            MouseListener.mouseButtonsDown++;
            //check if button pressed is valid
            if(button < MouseListener.mouseButtonPressed.length) {
                MouseListener.mouseButtonPressed[button] = true;
            }
        } else if(action == GLFW_RELEASE) {
            MouseListener.mouseButtonsDown--;
            if(button < MouseListener.mouseButtonPressed.length) {
                MouseListener.mouseButtonPressed[button] = false;
                MouseListener.isDragging = false;
            }
        }
    }

    public static void mouseScrollCallback(long window, double xOffset, double yOffset) {
        MouseListener.scrollX = xOffset;
        MouseListener.scrollY = yOffset;
    }

    public static void endFrame() {
        MouseListener.scrollX = 0.0;
        MouseListener.scrollY = 0.0;
        MouseListener.lastX = MouseListener.posX;
        MouseListener.lastY = MouseListener.posY;
        MouseListener.lastWorldX = MouseListener.worldX;
        MouseListener.lastWorldY = MouseListener.worldY;
    }

    public static float getX() {
        return (float) MouseListener.posX;
    }

    public static float getY() {
        return (float) MouseListener.posY;
    }

    public static float getDX() {
        return (float) (MouseListener.lastX - MouseListener.posX);
    }

    public static float getDY() {
        return (float) (MouseListener.lastY - MouseListener.posY);
    }

    public static float getWorldDX() {
        return (float) (MouseListener.lastWorldX - MouseListener.worldX);
    }

    public static float getWorldDY() {
        return (float) (MouseListener.lastWorldY - MouseListener.worldY);
    }

    public static float getScrollX() {
        return (float) MouseListener.scrollX;
    }

    public static float getScrollY() {
        return (float) MouseListener.scrollY;
    }

    public static boolean isDragging() {
        return MouseListener.isDragging;
    }

    public static void setGameViewportPos(Vector2f gameViewportPos) {
        MouseListener.gameViewportPos.set(gameViewportPos);
    }

    public static void setGameViewportSize(Vector2f gameViewportSize) {
        MouseListener.gameViewportSize.set(gameViewportSize);
    }

    public static boolean isButtonPressed(int button) {
        if (button < MouseListener.mouseButtonPressed.length) {
            return MouseListener.mouseButtonPressed[button];
        } else {
            return false;
        }
    }

    private static boolean isButtonPressed() {
        for (int i = 0; i < MouseListener.mouseButtonPressed.length; i++) {
            if (MouseListener.mouseButtonPressed[i]) {
                return true;
            }
        }
        return false;
    }

    // Convert mouse coordinates to game viewport coordinates
    private static void calcWorldX() {
        float currentX = getX() - MouseListener.gameViewportPos.x;
        currentX = (currentX / MouseListener.gameViewportSize.x) * 2.0f - 1.0f;
        Vector4f tmp = new Vector4f(currentX, 0, 0, 1);

        Camera camera = Window.getCurrentScene().getCamera();
        Matrix4f viewProjection = new Matrix4f();
        camera.getInverseView().mul(camera.getInverseProjection(), viewProjection);
        tmp.mul(viewProjection);
        worldX = tmp.x;
    }

    public static float getOrthoX() {
        return (float)worldX;
    }

    private static void calcWorldY() {
        float currentY = getY() - MouseListener.gameViewportPos.y;
        currentY = -((currentY / MouseListener.gameViewportSize.y) * 2.0f - 1.0f);
        Vector4f tmp = new Vector4f(0, currentY, 0, 1);

        Camera camera = Window.getCurrentScene().getCamera();
        Matrix4f viewProjection = new Matrix4f();
        camera.getInverseView().mul(camera.getInverseProjection(), viewProjection);
        tmp.mul(viewProjection);
        worldY = tmp.y;
    }

    public static float getOrthoY() {
        return (float)worldY;
    }

    public static Vector2f getOrthoPos() {
        return new Vector2f(getOrthoX(), getOrthoY());
    }

    public static float getScreenX() {
        float currentX = getX() - MouseListener.gameViewportPos.x;
        currentX = (currentX / MouseListener.gameViewportSize.x) * Window.getInstance().getFramebufferWidth();

        return currentX;
    }

    public static float getScreenY() {
        float currentY = getY() - MouseListener.gameViewportPos.y;
        currentY = Window.getInstance().getFramebufferHeight() - ((currentY / MouseListener.gameViewportSize.y) *
                Window.getInstance().getFramebufferHeight());

        return currentY;
    }

    public static Vector2f getScreenPos() {
        return new Vector2f(getScreenX(), getScreenY());
    }
}
