/******************************************
 *Project-------Engine2D-LWJGL
 *File----------SpriteSheet.java
 *Author--------Justin Kachele
 *Date----------10/5/2022
 *License-------GNU GENERAL PUBLIC LICENSE
 ******************************************/
package com.jkachele.game.components;

import com.jkachele.game.renderer.Texture;
import org.joml.Vector2f;

import java.util.List;

public class Spritesheet {
    Texture texture;
    private List<Sprite> sprites;

    public Spritesheet(Texture texture, int spriteWidth, int spriteHeight, int numSprites, int spacing) {
        this.texture = texture;
        sprites = new java.util.ArrayList<>();

        int currentX = 0;
        int currentY = texture.getHeight() - spriteHeight;  // Bottom left corner of top left sprite
        for (int i = 0; i < numSprites; i++) {
            float topY = (currentY + spriteHeight) / (float)texture.getHeight();
            float rightX = (currentX + spriteWidth) / (float)texture.getWidth();
            float leftX = currentX / (float)texture.getWidth();
            float bottomY = currentY / (float)texture.getHeight();

            Vector2f[] uvCoords = {
                    new Vector2f(rightX, topY),
                    new Vector2f(rightX, bottomY),
                    new Vector2f(leftX, bottomY),
                    new Vector2f(leftX, topY)
            };

            Sprite sprite = new Sprite();
            sprite.setTexture(this.texture);
            sprite.setUvCoords(uvCoords);
            sprite.setWidth(spriteWidth);
            sprite.setHeight(spriteHeight);
            this.sprites.add(sprite);

            currentX += spriteWidth + spacing;
            if (currentX >= texture.getWidth()) {
                currentX = 0;
                currentY -= spriteHeight + spacing;
            }
        }
    }

    public Sprite getSprite(int index) {
        if (index < 0 || index >= sprites.size()) {
            System.err.println("Error: Sprite index is out of range: " + index);
            return this.sprites.get(0);
        }
        return this.sprites.get(index);
    }

    public int size() {
        return this.sprites.size();
    }
}
