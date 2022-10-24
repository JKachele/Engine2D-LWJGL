/******************************************
 *Project-------Engine2D-LWJGL
 *File----------Window.java
 *Author--------Justin Kachele
 *Date----------9/22/2022
 *License-------MIT License
 ******************************************/

package com.jkachele.game.engine;

import com.jkachele.game.renderer.Framebuffer;
import com.jkachele.game.renderer.PickingTexture;
import com.jkachele.game.scene.LevelEditorScene;
import com.jkachele.game.scene.LevelScene;
import com.jkachele.game.scene.Scene;
import com.jkachele.game.util.Color;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import java.util.Objects;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public enum Window {;

    private static int width;
    private static int height;
    private static String title;
    private static long glfwWindow;
    private static Color backgroundColor;
    private static boolean reset;
    private static ImGuiLayer imGuiLayer;
    private static Framebuffer framebuffer;
    private static PickingTexture pickingTexture;

    private static final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private static final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();
    private static final int framebufferWidth = 3840;
    private static final int framebufferHeight = 2160;

    private static Scene currentScene = null;

    public static void init(int width, int height, String title, Color backgroundColor, boolean reset) {
        Window.width = width;
        Window.height = height;
        Window.title = title;
        Window.backgroundColor = backgroundColor;
        Window.reset = reset;
    }

    public static void start() {
        initWindow();
        initImGui();
    }

    private static void initWindow() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        // Setup error callback to System.err
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW (Graphics Library Framework)
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        //configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);               // Window will remain hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);              // Window will be resizeable
        //glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);              // Window will be maximized after creation

        // Create the window
        glfwWindow = glfwCreateWindow(Window.width, Window.height, Window.title, NULL, NULL);
        if (glfwWindow == NULL) {
            throw new RuntimeException("Failed to create GLFW window");
        }

        // Setup key and mouse callbacks
        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);

        // Setup Callback when window size changes
        glfwSetWindowSizeCallback(glfwWindow, (window, width, height) -> {
            Window.width = width;
            Window.height = height;
        });
        glfwSetFramebufferSizeCallback(glfwWindow, (window, width, height) -> {
            //glViewport(0, 0, width, height);
            glViewport(0, 0, framebufferWidth, framebufferHeight);
        });

        // Center the window on the primary monitor
        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        if(vidMode != null) {
            glfwSetWindowPos(glfwWindow, (vidMode.width() - width) / 2, (vidMode.height() - height) / 2);
        }

        // Make the openGL context current
        glfwMakeContextCurrent(glfwWindow);
        //enable v-sync (waits 1 screen refresh to render new frame)
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(glfwWindow);

        /* This line is critical for LWJGL's interoperation with GLFW's
        OpenGL context, or any context that is managed externally.
        LWJGL detects the context that is current in the current thread,
        creates the GLCapabilities instance and makes the OpenGL
        bindings available for use. */
        GL.createCapabilities();

        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);

        Window.framebuffer = new Framebuffer(framebufferWidth, framebufferHeight);
        Window.pickingTexture = new PickingTexture(framebufferWidth, framebufferHeight);
        glViewport(0, 0, framebufferWidth, framebufferHeight);

        Window.imGuiLayer = new ImGuiLayer(Window.glfwWindow, pickingTexture);
        Window.imGuiLayer.initImGui();

        // Initialize first scene
        Window.changeScene(0);
    }

    private static void initImGui() {
    }

    public static Scene getCurrentScene() {
        return currentScene;
    }

    public static void changeScene(int newScene) {
        switch (newScene) {
            case 0:
                currentScene = new LevelEditorScene();
                break;
            case 1:
                currentScene = new LevelScene();
                break;
            default:
                assert false: "Unknown Scene '" + newScene + "'";
                break;
        }

        if (!Window.reset) {
            // Load the current scene from the level.txt
            currentScene.load();
        }
        currentScene.init(Window.reset);
        currentScene.start();
    }

    public static void clear() {
        // Free the memory
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        // Terminate glfw and free the error callback
        glfwTerminate();
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();
    }

    public static int getWidth() {
        return Window.width;
    }

    public static int getHeight() {
        return Window.height;
    }

    public static int getFramebufferWidth() {
        return framebufferWidth;
    }

    public static int getFramebufferHeight() {
        return framebufferHeight;
    }

    public static long getGlfwWindow() {
        return glfwWindow;
    }

    public static Color getBackgroundColor() {
        return backgroundColor;
    }

    public static ImGuiLayer getImGuiLayer() {
        return imGuiLayer;
    }

    public static Framebuffer getFramebuffer() {
        return framebuffer;
    }

    public static PickingTexture getPickingTexture() {
        return pickingTexture;
    }
}
