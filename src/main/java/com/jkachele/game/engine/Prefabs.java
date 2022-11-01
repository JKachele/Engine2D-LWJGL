/******************************************
 *Project-------Engine2D-LWJGL
 *File----------Prefabs.java
 *Author--------Justin Kachele
 *Date----------10/8/2022
 *License-------MIT License
 ******************************************/
package com.jkachele.game.engine;

import com.jkachele.game.components.Sprite;
import com.jkachele.game.components.SpriteRenderer;
import org.joml.Vector2f;

public class Prefabs {
    public static GameObject generateSpriteObject(Sprite sprites, float sizeX, float sizeY, int zIndex, boolean pickable) {
        GameObject block = new GameObject("Sprite_Object_Gen",
                new Transform(new Vector2f(), new Vector2f(sizeX, sizeY)), 0);
        SpriteRenderer renderer = new SpriteRenderer();
        renderer.setSprite(sprites);
        block.addComponent(renderer);
        block.setPickable(pickable);
        return block;
    }

    public static GameObject generateSpriteObject(Sprite sprites, float sizeX, float sizeY, int zIndex) {
        return generateSpriteObject(sprites, sizeX, sizeY, zIndex, true);
    }

    public static GameObject generateSpriteObject(Sprite sprites, float sizeX, float sizeY, boolean pickable) {
        return generateSpriteObject(sprites, sizeX, sizeY, 0, pickable);
    }

    public static GameObject generateSpriteObject(Sprite sprites, float sizeX, float sizeY) {
        return generateSpriteObject(sprites, sizeX, sizeY, 0, true);
    }
}
