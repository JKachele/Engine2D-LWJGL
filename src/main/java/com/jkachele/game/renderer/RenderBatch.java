/******************************************
 *Project-------Engine2D-LWJGL
 *File----------RenderBatch.java
 *Author--------Justin Kachele
 *Date----------10/4/2022
 *License-------GNU GENERAL PUBLIC LICENSE
 ******************************************/
package com.jkachele.game.renderer;

import com.jkachele.game.components.SpriteRenderer;
import com.jkachele.game.engine.Window;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class RenderBatch implements Comparable<RenderBatch> {
    // Pos              Color                           UV Coords       texture ID
    // float, float,    float, float, float, float,     float, float,   float
    private final int POS_SIZE = 2;
    private final int COLOR_SIZE = 4;
    private final int UV_SIZE = 2;
    private final int TEX_ID_SIZE = 1;
    private final int ENTITY_ID_SIZE = 1;

    private final int POS_OFFSET = 0;
    private final int COLOR_OFFSET = POS_OFFSET + POS_SIZE * Float.BYTES;
    private final int UV_OFFSET = COLOR_OFFSET + COLOR_SIZE * Float.BYTES;
    private final int TEX_ID_OFFSET = UV_OFFSET + UV_SIZE * Float.BYTES;
    private final int ENTITY_ID_OFFSET = TEX_ID_OFFSET + TEX_ID_SIZE * Float.BYTES;

    private final int VERTEX_SIZE = POS_SIZE + COLOR_SIZE + UV_SIZE + TEX_ID_SIZE + ENTITY_ID_SIZE;
    private final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

    private SpriteRenderer[] sprites;
    private int numSprites;
    private Boolean hasRoom;
    private float[] vertices;
    private int[] elements;
    private int vaoID;
    private int vboID;
    private int eboID;
    private int maxBatchSize;
    private List<Texture> textures;
    private int[] texSlots = new int[16];   // Max 16 textures
    private int zIndex;

    public RenderBatch(int maxBatchSize, int zIndex) {
        init(maxBatchSize, zIndex);
    }

    public void init(int maxBatchSize, int zIndex) {
        this.maxBatchSize = maxBatchSize;
        this.sprites = new SpriteRenderer[maxBatchSize];
        this.zIndex = zIndex;

        // maxBatchSize quads, 4 vertices per quad, VERTEX_SIZE floats per vertex
        vertices = new float[maxBatchSize * 4 * VERTEX_SIZE];

        this.numSprites = 0;
        this.hasRoom = true;

        this.textures = new ArrayList<>();

        for (int i = 0; i < texSlots.length; i++) {
            texSlots[i] = i;
        }
    }

    public void start() {
        // Generate and bind a vertex array object
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // Allocate space for vertices
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, (long)vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);

        // Create and upload the indices buffer
        eboID = glGenBuffers();
        elements = generateIndices();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elements, GL_STATIC_DRAW);

        // Enable buffer attribute pointers
        // Position
        glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET);
        glEnableVertexAttribArray(0);
        // Color
        glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET);
        glEnableVertexAttribArray(1);
        // UV coordinates
        glVertexAttribPointer(2, UV_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, UV_OFFSET);
        glEnableVertexAttribArray(2);
        // Texture ID
        glVertexAttribPointer(3, TEX_ID_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_ID_OFFSET);
        glEnableVertexAttribArray(3);
        // Entity ID
        glVertexAttribPointer(4, ENTITY_ID_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, ENTITY_ID_OFFSET);
        glEnableVertexAttribArray(4);
    }

    public void addSprite(SpriteRenderer sprite) {
        // Get index and add renderObject
        int index = this.numSprites;    // index of next sprite to be added
        this.sprites[index] = sprite;
        this.numSprites++;

        if (sprite.getTexture() != null) {
            if (!textures.contains(sprite.getTexture())) {
                textures.add(sprite.getTexture());
            }
        }
        // Add properties to local vertices array
        loadVertexProperties(index);

        if (numSprites >= this.maxBatchSize) {
            this.hasRoom = false;
        }
    }

    public void render() {
        boolean rebufferData = false;
        for (int i = 0; i < numSprites; i++) {
            SpriteRenderer sprite = this.sprites[i];
            if (sprite.isDirty()) {
                loadVertexProperties(i);
                sprite.setClean();
                rebufferData = true;
            }
        }

        if (rebufferData) {
            // re-buffer data if dirty flag is set
            glBindBuffer(GL_ARRAY_BUFFER, vboID);
            glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);
        }

        // Use shader
        Shader shader = Renderer.getCurrentShader();
        shader.use();
        shader.uploadMat4f("uProjection", Window.getCurrentScene().getCamera().getProjectionMatrix());
        shader.uploadMat4f("uView", Window.getCurrentScene().getCamera().getViewMatrix());

        for (int i = 0; i < textures.size(); i++) {
            glActiveTexture(GL_TEXTURE0 + i + 1);
            textures.get(i).bind();
        }

        shader.uploadIntArray("uTextures", texSlots);


        // Bind the VAO
        glBindVertexArray(vaoID);

        // Enable the vertex attribute pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        // Draw what is in the vertex buffer
        glDrawElements(GL_TRIANGLES, this.numSprites * 6, GL_UNSIGNED_INT, 0);

        // Unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
        for (Texture texture : textures) {
            texture.unbind();
        }
        shader.detach();
    }

    private void loadVertexProperties(int index) {
        SpriteRenderer sprite = this.sprites[index];

        // Find the offset within array (4 vertices per sprite, VERTEX_SIZE attributes per vertex)
        int offset = index * 4 * VERTEX_SIZE;

        Vector4f color = sprite.getColor();
        Vector2f[] uvCoords = sprite.getUvCoords();

        int texID = 0;
        if (sprite.getTexture()!= null) {
            for (int i = 0; i < textures.size(); i++) {
                if (textures.get(i).equals(sprite.getTexture())) {
                    texID = i + 1;  // Reserve id 0 for no texture
                    break;
                }
            }
        }

        // Add the vertices with the appropriate attributes
        float xAdd = 1.0f;
        float yAdd = 1.0f;
        for (int i = 0; i < 4; i++) {
            if (i == 1) {
                yAdd = 0.0f;
            } else if (i == 2) {
                xAdd = 0.0f;
            } else if (i == 3) {
                yAdd = 1.0f;
            }

            // Load position
            vertices[offset] = sprite.gameObject.transform.position.x +
                    (xAdd * sprite.gameObject.transform.scale.x);
            vertices[offset + 1] = sprite.gameObject.transform.position.y +
                    (yAdd * sprite.gameObject.transform.scale.y);

            // Load color
            vertices[offset + 2] = color.x; //r
            vertices[offset + 3] = color.y; //g
            vertices[offset + 4] = color.z; //b
            vertices[offset + 5] = color.w; //a

            // Load UV coordinates
            vertices[offset + 6] = uvCoords[i].x;
            vertices[offset + 7] = uvCoords[i].y;

            // Load Texture ID
            vertices[offset + 8] = texID;

            // load Entity ID. +1 to allow background to be -1 and object 0 to be 0
            vertices[offset + 9] = sprite.gameObject.getUid() + 1;

            offset += VERTEX_SIZE;
        }

    }

    private int[] generateIndices() {
        // 6 indices per quad (3 indices per triangle)
        int[] elements = new int[maxBatchSize * 6];
        for (int i = 0; i < maxBatchSize; i ++) {
            loadElementIndices(elements, i);
        }
        return elements;
    }

    private void loadElementIndices(int[] elements, int index) {
        // Offset for the beginning of each quad (3 vertices per triangle, 2 triangles per quad)
        int offsetArrayIndex = 6 * index;

        // Offset for the first vertex for the quad (4 vertices per quad)
        int offsetVertexNum = 4 * index;

        /*
        3--0
        | /|
        |/ |
        2--1
                        Tri 1       Tri 2    */
        int[] indices = {3, 2, 0,    0, 2, 1};
        for (int i = 0; i < indices.length; i++) {
            elements[offsetArrayIndex + i] = offsetVertexNum + indices[i];
        }
    }

    public boolean hasRoom() {
        return this.hasRoom;
    }

    public boolean hasTextureRoom() {
        return this.textures.size() < 16;
    }

    public boolean hasTexture(Texture texture) {
        return this.textures.contains(texture);
    }

    public int zIndex() {
        return this.zIndex;
    }

    @Override
    public int compareTo(RenderBatch o) {
        return Integer.compare(this.zIndex, o.zIndex);
    }
}
