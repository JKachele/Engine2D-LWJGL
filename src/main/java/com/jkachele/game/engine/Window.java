/******************************************
 *Project-------Engine2D-LWJGL
 *File----------Window.java
 *Author--------Justin Kachele
 *Date----------9/22/2022
 *License-------MIT License
 ******************************************/

package com.jkachele.game.engine;

import com.jkachele.game.observers.EventSystem;
import com.jkachele.game.observers.Observer;
import com.jkachele.game.observers.events.Event;
import com.jkachele.game.renderer.Framebuffer;
import com.jkachele.game.renderer.PickingTexture;
import com.jkachele.game.scene.LevelEditorSceneInitializer;
import com.jkachele.game.scene.Scene;
import com.jkachele.game.scene.SceneInitializer;
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

public class Window implements Observer {
    private static Window instance = null;

    private int width;
    private int height;
    private String title;
    private long glfwWindow;
    private Color backgroundColor;
    private boolean reset;
    private ImGuiLayer imGuiLayer;
    private Framebuffer framebuffer;
    private PickingTexture pickingTexture;

    private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();
    private final int framebufferWidth = 3840;
    private final int framebufferHeight = 2160;
    private final String levelEditorSceneLocation = "assets/levels/levelEditor.txt";

    private static Scene currentScene = null;
    private boolean editorActive = false;

    // Singleton instance of Window: Only one instance of this class can be created
    public static Window getInstance() {
        if (instance == null) {
            instance = new Window();
        }
        return instance;
    }

    private Window() {}

    public void init(int width, int height, String title, Color backgroundColor, boolean reset) {
        this.width = width;
        this.height = height;
        this.title = title;
        this.backgroundColor = backgroundColor;
        this.reset = reset;
        EventSystem.addObserver(this);
    }

    public void start() {
        initWindow();
        initImGui();
    }

    private void initWindow() {
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
        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
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
            this.width = width;
            this.height = height;
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

        this.framebuffer = new Framebuffer(framebufferWidth, framebufferHeight);
        this.pickingTexture = new PickingTexture(framebufferWidth, framebufferHeight);
        glViewport(0, 0, framebufferWidth, framebufferHeight);

        this.imGuiLayer = new ImGuiLayer(this.glfwWindow, pickingTexture);
        this.imGuiLayer.initImGui();

        // Initialize first scene
        Window.changeScene(new LevelEditorSceneInitializer());
    }

    private void initImGui() {
    }

    public static Scene getCurrentScene() {
        return currentScene;
    }

    public static void changeScene(SceneInitializer sceneInitializer) {

        if (currentScene != null) {
            currentScene.destroy();
        }

        Window.getInstance().getImGuiLayer().getPropertiesWindow().setCurrentGameObject(null);
        currentScene = new Scene(sceneInitializer);
        if (!Window.getInstance().isReset()) {
            // Load the current scene from the level.txt
            currentScene.load(Window.getInstance().levelEditorSceneLocation);
        }
        currentScene.init();
        currentScene.start();
    }

    public void clear() {
        // Free the memory
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        // Terminate glfw and free the error callback
        glfwTerminate();
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();
    }

    @Override
    public void onNotify(GameObject gameObject, Event event) {
        switch (event.eventType) {
            case GameEngineStartPlay -> {
                this.editorActive = true;
                currentScene.save(levelEditorSceneLocation);
                Window.changeScene(new LevelEditorSceneInitializer());
            }
            case GameEngineStopPlay -> {
                this.editorActive = false;
                Window.changeScene(new LevelEditorSceneInitializer());
            }
            case LoadLevel -> Window.changeScene(new LevelEditorSceneInitializer());
            case SaveLevel -> Window.currentScene.save(levelEditorSceneLocation);
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getTitle() {
        return title;
    }

    public long getGlfwWindow() {
        return glfwWindow;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public boolean isReset() {
        return reset;
    }

    public ImGuiLayer getImGuiLayer() {
        return imGuiLayer;
    }

    public Framebuffer getFramebuffer() {
        return framebuffer;
    }

    public PickingTexture getPickingTexture() {
        return pickingTexture;
    }

    public ImGuiImplGlfw getImGuiGlfw() {
        return imGuiGlfw;
    }

    public ImGuiImplGl3 getImGuiGl3() {
        return imGuiGl3;
    }

    public int getFramebufferWidth() {
        return framebufferWidth;
    }

    public int getFramebufferHeight() {
        return framebufferHeight;
    }

    public boolean isEditorActive() {
        return editorActive;
    }
}
