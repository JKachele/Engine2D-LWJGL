/******************************************
 *Project-------Learn-LWJGL
 *File----------Component.java
 *Author--------Justin Kachele
 *Date----------10/3/2022
 *License-------GNU GENERAL PUBLIC LICENSE
 ******************************************/
package com.jkachele.game.engine;

public abstract class Component {

    public GameObject gameObject = null;

    public abstract void update(float dt);

    public void start() {

    }

}
