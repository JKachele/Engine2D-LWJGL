/******************************************
 *Project-------Engine2D-LWJGL
 *File----------Renderer.java
 *Author--------Justin Kachele
 *Date----------10/4/2022
 *License-------Mozilla Public License Version 2.0
 ******************************************/
package com.jkachele.game.renderer;

import com.jkachele.game.components.SpriteRenderer;
import com.jkachele.game.engine.GameObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Renderer {
    private final int MAX_BATCH_SIZE = 1000;
    private List<RenderBatch> batches;
    private static Shader currentShader;

    public Renderer() {
        this.batches = new ArrayList<>();
    }

    public void add(GameObject gameObject) {
        SpriteRenderer sprite = gameObject.getComponent(SpriteRenderer.class);
        if (sprite != null) {
            this.add(sprite);
        }
    }

    public void add(SpriteRenderer sprite) {
        boolean added = false;
        for (RenderBatch batch: batches) {
            if (batch.hasRoom() && batch.zIndex() == sprite.gameObject.transform.zIndex) {
                Texture texture = sprite.getTexture();
                if (texture == null || (batch.hasTexture(texture) || batch.hasTextureRoom())) {
                    batch.addSprite(sprite);
                    added = true;
                    break;
                }
            }
        }

        if (!added) {
            RenderBatch newBatch = new RenderBatch(MAX_BATCH_SIZE, sprite.gameObject.transform.zIndex, this);
            newBatch.start();
            batches.add(newBatch);
            newBatch.addSprite(sprite);
            Collections.sort(batches);
        }
    }

    public void destroyGameObject(GameObject gameObject) {
        if (gameObject.getComponent(SpriteRenderer.class) == null) {
            return;
        }

        for (RenderBatch batch : batches) {
            if (batch.destroyIfExists(gameObject)) {
                return;
            }
        }
    }

    public static void bindShader(Shader shader) {
        currentShader = shader;
    }

    public static Shader getCurrentShader() {
        return currentShader;
    }

    public void render() {
        currentShader.use();
        //noinspection ForLoopReplaceableByForEach
        for (int i = 0; i < batches.size(); i++) {
            batches.get(i).render();
        }
    }
}
