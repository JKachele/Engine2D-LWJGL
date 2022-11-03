/******************************************
 *Project-------Engine2D-LWJGL
 *File----------ForceGenerator.java
 *Author--------Justin Kachele
 *Date----------10/17/2022
 *License-------MIT License
 ******************************************/
package com.jkachele.game.physics2dtmp.forces;

import com.jkachele.game.physics2dtmp.rigidbody.Rigidbody2D;

public interface ForceGenerator {
    void updateForce(Rigidbody2D rigidbody, float dt);
}
