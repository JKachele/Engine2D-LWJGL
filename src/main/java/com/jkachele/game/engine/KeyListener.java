/******************************************
 *Project-------Learn-LWJGL
 *File----------KeyListener.java
 *Author--------Justin Kachele
 *Date----------9/24/2022
 *License-------GNU GENERAL PUBLIC LICENSE
 ******************************************/
package com.jkachele.game.engine;

import static org.lwjgl.glfw.GLFW.*;

public class KeyListener {
    private static KeyListener instance;
    private boolean[] keyPressed = new boolean[350];

    private KeyListener() {
    }

    public static KeyListener getInstance() {
        if (KeyListener.instance == null) {
            KeyListener.instance = new KeyListener();
        }
        return KeyListener.instance;
    }

    public static void keyCallback(long window, int key, int scancode, int action, int mods) {
        if (action == GLFW_PRESS) {
            if (key < getInstance().keyPressed.length && key >= 0) {
                getInstance().keyPressed[key] = true;
            }
        } else if (action == GLFW_RELEASE) {
            if (key < getInstance().keyPressed.length && key >= 0) {
                getInstance().keyPressed[key] = false;
            }
        }
    }

    public static boolean isKeyPressed(int keyCode) {
        if (keyCode < getInstance().keyPressed.length && keyCode >= 0) {
            return getInstance().keyPressed[keyCode];
        } else {
            System.err.println("Invalid key pressed");
            return false;
        }
    }
}
