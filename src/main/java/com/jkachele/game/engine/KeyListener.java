/******************************************
 *Project-------Engine2D-LWJGL
 *File----------KeyListener.java
 *Author--------Justin Kachele
 *Date----------9/24/2022
 *License-------MIT License
 ******************************************/
package com.jkachele.game.engine;

import static org.lwjgl.glfw.GLFW.*;

public class KeyListener {
    private static KeyListener instance;
    private static boolean[] keyPressed = new boolean[350];

    private KeyListener() {
    }

    public static void keyCallback(long window, int key, int scancode, int action, int mods) {
        if (action == GLFW_PRESS) {
            if (key < KeyListener.keyPressed.length && key >= 0) {
                KeyListener.keyPressed[key] = true;
            }
        } else if (action == GLFW_RELEASE) {
            if (key < KeyListener.keyPressed.length && key >= 0) {
                KeyListener.keyPressed[key] = false;
            }
        }
    }

    public static boolean isKeyPressed(int keyCode) {
        if (keyCode < KeyListener.keyPressed.length && keyCode >= 0) {
            return KeyListener.keyPressed[keyCode];
        } else {
            System.err.println("Invalid key pressed");
            return false;
        }
    }
}
