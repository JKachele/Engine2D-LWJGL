/******************************************
 *Project-------Engine2D-LWJGL
 *File----------SceneInitializer.java
 *Author--------Justin Kachele
 *Date----------11/4/2022
 *License-------MIT License
 ******************************************/
package com.jkachele.game.scene;

public abstract class SceneInitializer {
    public abstract void init(Scene scene);
    public abstract void loadResources(Scene scene);
    public abstract void imGui();

}
