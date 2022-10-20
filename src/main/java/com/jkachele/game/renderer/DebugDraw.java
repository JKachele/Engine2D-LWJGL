/******************************************
 *Project-------Engine2D-LWJGL
 *File----------DebugDraw.java
 *Author--------Justin Kachele
 *Date----------10/9/2022
 *License-------GNU GENERAL PUBLIC LICENSE
 ******************************************/
package com.jkachele.game.renderer;

import com.jkachele.game.engine.Window;
import com.jkachele.game.physics2d.primitives.Line2D;
import com.jkachele.game.util.AssetPool;
import com.jkachele.game.util.Color;
import com.jkachele.game.util.GameMath;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;


public class DebugDraw {
    private static final int MAX_LINES = 500;
    private static List<Line2D> lines = new ArrayList<>();

    // 7 floats per vertex (x, y, z, r, g, b, a), 2 vertices per line
    private static float[] vertexArray = new float[MAX_LINES * 7 * 2];
    private static Shader shader = AssetPool.getShader("assets/shaders/debugLine2D.glsl");

    private static int vaoID;
    private static int vboID;

    private static boolean started = false;

    public static void init() {
        // Generate the VAO
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // Generate the VBO amd buffer some memory
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, (long) vertexArray.length * Float.BYTES, GL_DYNAMIC_DRAW);

        // Enable the vertex array attribute pointers
        // Position
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 7 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);
        // Color
        glVertexAttribPointer(1, 4, GL_FLOAT, false, 7 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);

        glLineWidth(2.0f);
    }

    public static void beginFrame() {
        if (!started) {
            init();
            started = true;
        }

        // Remove deadlines
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).beginFrame() < 0) {
                lines.remove(i);
                i--;
            }
        }
    }

    public static void draw() {
        if (lines.size() == 0) {
            return;
        }

        int index = 0;
        for (Line2D line : lines) {
            for (int i = 0; i < 2; i++) {
                Vector2f position = (i == 0 ? line.getStart() : line.getEnd());
                Vector4f color = line.getColor();

                // Load position into array
                vertexArray[index] = position.x;
                vertexArray[index + 1] = position.y;
                vertexArray[index + 2] = -10.0f;

                // Load color into array
                vertexArray[index + 3] = color.x;
                vertexArray[index + 4] = color.y;
                vertexArray[index + 5] = color.z;
                vertexArray[index + 6] = color.w;
                index += 7;
            }
        }

        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, Arrays.copyOfRange(vertexArray, 0, lines.size() * 7 * 2));

        // Use our shader
        shader.use();
        shader.uploadMat4f("uProjection", Window.getCurrentScene().getCamera().getProjectionMatrix());
        shader.uploadMat4f("uView", Window.getCurrentScene().getCamera().getViewMatrix());

        // Bind the vao
        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        // Draw the batch
        glDrawArrays(GL_LINES, 0, lines.size() * 7 * 2);

        // Disable Location
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        // Unbind shader
        shader.detach();
    }

    // =========================================================
    // Add Line2D methods
    // =========================================================
    public static void addLine2D(Vector2f start, Vector2f end, Vector4f color, int lifetime) {
        if (lines.size() >= MAX_LINES) {
            return;
        }
        DebugDraw.lines.add(new Line2D(start, end, color, lifetime));
    }

    public static void addLine2D(Vector2f start, Vector2f end, Vector4f color) {
        addLine2D(start, end, color, 1);
    }

    public static void addLine2D(Vector2f start, Vector2f end) {
        addLine2D(start, end, Color.GREEN.toVector(), 1);
    }

    // =========================================================
    // Add Box2D methods
    // =========================================================
    public static void addBox2D(Vector2f center, Vector2f dimension, float rotation, Vector4f color, int lifetime) {
        // obtain the bottom left and top right by substring or adding half of the dimension
        Vector2f min = new Vector2f(center).sub(new Vector2f(dimension).div(2.0f));
        Vector2f max = new Vector2f(center).add(new Vector2f(dimension).div(2.0f));
        Vector2f[] vertices = {
                new Vector2f(min.x, min.y),     // Bottom left
                new Vector2f(min.x, max.y),     // Top left
                new Vector2f(max.x, max.y),     // Top right
                new Vector2f(max.x, min.y),     // Bottom right
        };

        if (rotation != 0.0f) {
            for (Vector2f vertex : vertices) {
                GameMath.rotate(vertex, center, rotation);
            }
        }

        addLine2D(vertices[0], vertices[1], color, lifetime);
        addLine2D(vertices[1], vertices[2], color, lifetime);
        addLine2D(vertices[2], vertices[3], color, lifetime);
        addLine2D(vertices[3], vertices[0], color, lifetime);
    }

    public static void addBox2D(Vector2f center, Vector2f dimension, float rotation, Vector4f color) {
        addBox2D(center, dimension, rotation, color, 1);
    }

    public static void addBox2D(Vector2f center, Vector2f dimension, float rotation) {
        addBox2D(center, dimension, rotation, Color.GREEN.toVector(), 1);
    }

    public static void addBox2D(Vector2f center, Vector2f dimension) {
        addBox2D(center, dimension, 0, Color.GREEN.toVector(), 1);
    }

    // =========================================================
    // Add Circle2D methods
    // =========================================================
    public static void addCircle(Vector2f center, float radius, Vector4f color, int segmentNum, int lifetime) {
        Vector2f[] points = new Vector2f[segmentNum];
        float increment = (float)360 / segmentNum;
        float currentAngle = 0.0f;

        for (int i = 0; i < segmentNum; i++) {
            Vector2f temp = new Vector2f(radius, 0);
            GameMath.rotate(temp, new Vector2f(0, 0), currentAngle);
            points[i] = new Vector2f(center).add(temp);

            if (i > 0) {
                addLine2D(points[i - 1], points[i], color, lifetime);
            }

            currentAngle += increment;
        }
        addLine2D(points[segmentNum - 1], points[0], color, lifetime);
    }

    public static void addCircle(Vector2f center, float radius, Vector4f color, int segmentNum) {
        addCircle(center, radius, color, segmentNum, 1);
    }

    public static void addCircle(Vector2f center, float radius, Vector4f color) {
        addCircle(center, radius, color, 32, 1);
    }

    public static void addCircle(Vector2f center, float radius) {
        addCircle(center, radius, Color.GREEN.toVector(), 32, 1);
    }

}
