/******************************************
 *Project-------Learn-LWJGL
 *File----------DebugDraw.java
 *Author--------Justin Kachele
 *Date----------10/9/2022
 *License-------GNU GENERAL PUBLIC LICENSE
 ******************************************/
package com.jkachele.game.renderer;

import com.jkachele.game.engine.Window;
import com.jkachele.game.util.AssetPool;
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
    private static int maxLines = 500;
    private static List<Line2D> lines = new ArrayList<>();

    // 7 floats per vertex (x, y, z, r, g, b, a), 2 vertices per line
    private static float[] vertexArray = new float[maxLines * 7 * 2];
    private static Shader shader = AssetPool.getShader("assets/shader/debugLine2D.glsl");

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
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 6 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);
        // Color
        glVertexAttribPointer(1, 4, GL_FLOAT, false, 6 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);

        // TODO: SET LINE WIDTH
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

        // Use the shader
        shader.use();
        shader.uploadMat4f("uProjection", Window.getCurrentScene().getCamera().getProjectionMatrix());
        shader.uploadMat4f("uView", Window.getCurrentScene().getCamera().getViewMatrix());

        // Bind the vao
        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        // Draw the batch of lines
        glDrawArrays(GL_LINES, 0, lines.size() * 7 * 2);

        // Disable location
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        // Unbind shader
        shader.detach();
    }
}
