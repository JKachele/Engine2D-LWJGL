/******************************************
 *Project-------Learn-LWJGL
 *File----------MouseListener.java
 *Author--------Justin Kachele
 *Date----------9/24/20.022
 *License-------GNU GENERAL PUBLIC LICENSE
 ******************************************/
package com.jkachele.game.engine;

import static org.lwjgl.glfw.GLFW.*;

public class MouseListener {
    private static MouseListener instance;
    private double scrollX, scrollY;
    private double posX,posY;
    private double lastX, lastY;
    private boolean[] mouseButtonPressed = new boolean[5];
    private boolean isDragging;

    private MouseListener() {
        this.scrollX = 0.0;
        this.scrollY = 0.0;
        this.posX = 0.0;
        this.posY = 0.0;
        this.lastX = 0.0;
        this.lastY = 0.0;
    }

    public static MouseListener getInstance() {
        if(MouseListener.instance ==  null) {
            MouseListener.instance = new MouseListener();
        }
        return MouseListener.instance;
    }

    public static void mousePosCallback(long window, double posX, double posY) {
        getInstance().lastX = getInstance().posX;
        getInstance().lastX = getInstance().posX;
        getInstance().posX = posX;
        getInstance().posY = posY;
        getInstance().isDragging = isButtonPressed();
    }

    public static void mouseButtonCallback(long window, int button, int action, int mods) {
        //if mouse button is pressed, set element of array to true
        if(action == GLFW_PRESS) {
            //check if button pressed is valid
            if(button < getInstance().mouseButtonPressed.length) {
                getInstance().mouseButtonPressed[button] = true;
            }
        } else if(action == GLFW_RELEASE) {
            if(button < getInstance().mouseButtonPressed.length) {
                getInstance().mouseButtonPressed[button] = false;
                getInstance().isDragging = false;
            }
        }
    }

    public static void mouseScrollCallback(long window, double xOffset, double yOffset) {
        getInstance().scrollX = xOffset;
        getInstance().scrollY = yOffset;
    }

    public static void endFrame() {
        getInstance().scrollX = 0.0;
        getInstance().scrollY = 0.0;
        getInstance().lastX = getInstance().posX;
        getInstance().lastY = getInstance().posY;
    }

    public static float getX() {
        return (float) getInstance().posX;
    }

    public static float getY() {
        return (float) getInstance().posY;
    }

    public static float getDX() {
        return (float) (getInstance().lastX - getInstance().posX);
    }

    public static float getDY() {
        return (float) (getInstance().lastY - getInstance().posY);
    }

    public static float getScrollX() {
        return (float) getInstance().scrollX;
    }

    public static float getScrollY() {
        return (float) getInstance().scrollY;
    }

    public static boolean isDragging() {
        return getInstance().isDragging;
    }

    public static boolean isButtonPressed(int button) {
        if (button < getInstance().mouseButtonPressed.length) {
            return getInstance().mouseButtonPressed[button];
        } else {
            return false;
        }
    }

    private static boolean isButtonPressed() {
        for (int i = 0; i < getInstance().mouseButtonPressed.length; i++) {
            if (getInstance().mouseButtonPressed[i]) {
                return true;
            }
        }
        return false;
    }
}
