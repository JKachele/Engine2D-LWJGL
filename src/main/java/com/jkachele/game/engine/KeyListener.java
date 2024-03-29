/******************************************
 *Project-------Engine2D-LWJGL
 *File----------KeyListener.java
 *Author--------Justin Kachele
 *Date----------9/24/2022
 *License-------Mozilla Public License Version 2.0
 ******************************************/
package com.jkachele.game.engine;

import static org.lwjgl.glfw.GLFW.*;

@SuppressWarnings("unused")
public class KeyListener {
    private static KeyListener instance;
    private static boolean[] keyPressed = new boolean[350];
    private static boolean[] keyBeginPress = new boolean[350];
    private KeyListener() {
    }

    public static void keyCallback(long window, int key, int scancode, int action, int mods) {
        if (action == GLFW_PRESS) {
            if (key < KeyListener.keyPressed.length && key >= 0) {
                KeyListener.keyPressed[key] = true;
                keyBeginPress[key] = true;
            }
        } else if (action == GLFW_RELEASE) {
            if (key < KeyListener.keyPressed.length && key >= 0) {
                KeyListener.keyPressed[key] = false;
                keyBeginPress[key] = false;
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

    public static boolean keyBeginPress(int keyCode) {
        boolean result = keyBeginPress[keyCode];
        if (result) {
            keyBeginPress[keyCode] = false;
        }
        return result;
    }
}
