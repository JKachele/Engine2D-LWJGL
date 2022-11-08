/******************************************
 *Project-------Engine2D-LWJGL
 *File----------Prefabs.java
 *Author--------Justin Kachele
 *Date----------10/8/2022
 *License-------Mozilla Public License Version 2.0
 ******************************************/
package com.jkachele.game.engine;

import com.jkachele.game.components.Sprite;
import com.jkachele.game.components.SpriteRenderer;

public class Prefabs {
    public static GameObject generateSpriteObject(Sprite sprites, float sizeX, float sizeY, boolean pickable) {
        GameObject block = Window.getCurrentScene().createGameObject("Sprite_Object_Gen");
        block.transform.scale.x = sizeX;
        block.transform.scale.y = sizeY;
        SpriteRenderer renderer = new SpriteRenderer();
        renderer.setSprite(sprites);
        block.addComponent(renderer);
        block.setPickable(pickable);
        return block;
    }

    public static GameObject generateSpriteObject(Sprite sprites, float sizeX, float sizeY) {
        return generateSpriteObject(sprites, sizeX, sizeY, true);
    }
}
