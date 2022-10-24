/******************************************
 *Project-------Engine2D-LWJGL
 *File----------Engine.java
 *Author--------Justin Kachele
 *Date----------9/28/2022
 *License-------MIT License
 ******************************************/
package com.jkachele.game.engine;

import com.jkachele.game.renderer.DebugDraw;
import com.jkachele.game.renderer.Renderer;
import com.jkachele.game.renderer.Shader;
import com.jkachele.game.util.AssetPool;
import com.jkachele.game.util.Color;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Engine implements Runnable{

    private final Thread GAME_LOOP_THREAD;

    public Engine(int width, int height, String title, Color backgroundColor, boolean reset) {
        GAME_LOOP_THREAD = new Thread(this, "GAME_LOOP_THREAD");
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

        Shader defaultShader = AssetPool.getShader("assets/shaders/default.glsl");
        Shader pickingShader = AssetPool.getShader("assets/shaders/pickingShader.glsl");

        // Run the rendering loop until the user has attempted to close the window
        while (!glfwWindowShouldClose(Window.getGlfwWindow())) {
            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();

            // Render pass 1: render to picking texture
            glDisable(GL_BLEND);
            Window.getPickingTexture().enableWriting();

            glViewport(0, 0, Window.getFramebufferWidth(), Window.getFramebufferHeight());
            glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            Renderer.bindShader(pickingShader);
            Window.getCurrentScene().render();

            Window.getPickingTexture().disableWriting();
            glEnable(GL_BLEND);

            // Render pass 2: render actual scene to Screen
            DebugDraw.beginFrame();

            // Render the scene into the framebuffer
            Window.getFramebuffer().bind();

            // Set the clear color
            Color backgroundColor = Window.getBackgroundColor();
            glClearColor(backgroundColor.getRed(), backgroundColor.getGreen(),
                    backgroundColor.getBlue(), backgroundColor.getAlpha());
            glClear(GL_COLOR_BUFFER_BIT);

            if(dt >= 0) {
                DebugDraw.draw();
                Renderer.bindShader(defaultShader);
                Window.getCurrentScene().update(dt);
                Window.getCurrentScene().render();
            }
            // Render ImGUI into the window
            Window.getFramebuffer().unbind();

            Window.getImGuiLayer().update(dt, Window.getCurrentScene());

            glfwSwapBuffers(Window.getGlfwWindow());

            MouseListener.endFrame();

            // Print the current FPS to the console
            System.out.print("\r" + fps(dt));

            endTime = (float)glfwGetTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }

        Window.getCurrentScene().saveExit();
        glfwTerminate();
    }

    private static String fps(float dt) {
        return String.format("%.2f FPS ", 1.0f / dt);
    }
}
