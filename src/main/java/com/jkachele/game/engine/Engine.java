/******************************************
 *Project-------Engine2D-LWJGL
 *File----------Engine.java
 *Author--------Justin Kachele
 *Date----------9/28/2022
 *License-------GNU GENERAL PUBLIC LICENSE
 ******************************************/
package com.jkachele.game.engine;

import com.jkachele.game.renderer.DebugDraw;
import com.jkachele.game.util.Color;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Engine implements Runnable{
    private int width;
    private int height;
    private String title;
    private Color backgroundColor;
    private boolean reset;

    private final Thread GAME_LOOP_THREAD;

    public Engine(int width, int height, String title, Color backgroundColor, boolean reset) {
        this.width = width;
        this.height = height;
        this.title = title;
        this.backgroundColor = backgroundColor;
        this.reset = reset;
        GAME_LOOP_THREAD = new Thread(this, "GAME_LOOP_THREAD");

    }

    private void init() {
        MouseListener.init();
        Window.init(width, height, title, backgroundColor, reset);
    }

    public void start() {
        GAME_LOOP_THREAD.start();
    }

    @Override
    public void run() {
        try {
            Window.start();
            gameLoop();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void gameLoop() {
        float beginTime = (float)glfwGetTime();
        float endTime;
        float dt = -1.0f;

        // Run the rendering loop until the user has attempted to close the window
        while (!glfwWindowShouldClose(Window.getGlfwWindow())) {
            Color backgroundColor = Window.getBackgroundColor();

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();

            // Render the scene into the framebuffer
            Window.getFramebuffer().bind();

            DebugDraw.beginFrame();

            // Set the clear color
            glClearColor(backgroundColor.getRed(), backgroundColor.getGreen(),
                    backgroundColor.getBlue(), backgroundColor.getAlpha());
            glClear(GL_COLOR_BUFFER_BIT);

            if(dt >= 0) {
                DebugDraw.draw();
                Window.getCurrentScene().update(dt);
            }
            // Render ImGUI into the window
            Window.getFramebuffer().unbind();

            Window.getImGuiLayer().update(dt, Window.getCurrentScene());

            glfwSwapBuffers(Window.getGlfwWindow());

            // Print the current FPS to the console
//            System.out.print("\r" + fps(dt));

            endTime = (float)glfwGetTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }

        Window.getCurrentScene().saveExit();
        glfwTerminate();
    }

    private static String fps(float dt) {
        return (1.0f / dt) + " FPS";
    }
}
