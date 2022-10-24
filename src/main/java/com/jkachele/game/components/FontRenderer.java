/******************************************
 *Project-------Engine2D-LWJGL
 *File----------FontRenderer.java
 *Author--------Justin Kachele
 *Date----------10/3/2022
 *License-------MIT License
 ******************************************/
package com.jkachele.game.components;

public class FontRenderer extends Component {

    @Override
    public void start() {
        if (gameObject.getComponent(SpriteRenderer.class) != null) {
            System.out.println("Found font renderer!");
        }
    }

    @Override
    public void update(float dt) {

    }
}
