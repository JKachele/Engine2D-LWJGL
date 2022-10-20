/******************************************
 *Project-------Engine2D-LWJGL
 *File----------AssetPool.java
 *Author--------Justin Kachele
 *Date----------10/5/2022
 *License-------GNU GENERAL PUBLIC LICENSE
 ******************************************/
package com.jkachele.game.util;

import com.jkachele.game.components.Spritesheet;
import com.jkachele.game.renderer.Shader;
import com.jkachele.game.renderer.Texture;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AssetPool {
    private static Map<String, Shader> shaders = new HashMap<>();
    private static Map<String, Texture> textures = new HashMap<>();
    private static Map<String, Spritesheet> spritesheets = new HashMap<>();

    public static Shader getShader(String resourceName) {
        File file = new File(resourceName);
        if (AssetPool.shaders.containsKey(file.getAbsolutePath())) {
            return AssetPool.shaders.get(file.getAbsolutePath());
        } else {
            Shader shader = new Shader(resourceName);
            shader.compile();
            AssetPool.shaders.put(file.getAbsolutePath(), shader);
            return shader;
        }
    }

    public static Texture getTexture(String resourceName) {
        File file = new File(resourceName);
        if (AssetPool.textures.containsKey(file.getAbsolutePath())) {
            return AssetPool.textures.get(file.getAbsolutePath());
        } else {
            Texture texture = new Texture();
            texture.init(resourceName);
            AssetPool.textures.put(file.getAbsolutePath(), texture);
            return texture;
        }
    }

    public static void addSpritesheet(String resourceName, Spritesheet spriteSheet) {
        File file = new File(resourceName);
        if (!AssetPool.spritesheets.containsKey(file.getAbsolutePath())) {
            AssetPool.spritesheets.put(file.getAbsolutePath(), spriteSheet);
        }
    }

    public static Spritesheet getSpritesheet(String resourceName) {
        Spritesheet defaultSpritesheet = new Spritesheet(AssetPool.getTexture(
                "assets/images/NoTextureSpritesheet.png"),
                32, 32, 64, 0);

        File file = new File(resourceName);
        if (!AssetPool.spritesheets.containsKey(file.getAbsolutePath())) {
            System.err.println("Error: Tried to access spritesheet '" + resourceName +
                    "' and it has not been added to asset pool.");
        }
        return AssetPool.spritesheets.getOrDefault(file.getAbsolutePath(), defaultSpritesheet);
    }
}
