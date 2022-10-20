/******************************************
 *Project-------Engine2D-LWJGL
 *File----------ForceRegistration.java
 *Author--------Justin Kachele
 *Date----------10/17/2022
 *License-------GNU GENERAL PUBLIC LICENSE
 ******************************************/
package com.jkachele.game.physics2d.forces;

import com.jkachele.game.physics2d.rigidbody.Rigidbody2D;

public class ForceRegistration {
    public ForceGenerator forceGenerator;
    public Rigidbody2D rigidbody;

    public ForceRegistration(ForceGenerator forceGenerator, Rigidbody2D rigidbody) {
        this.forceGenerator = forceGenerator;
        this.rigidbody = rigidbody;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass()!= o.getClass()) return false;

        ForceRegistration that = (ForceRegistration) o;
        return that.rigidbody == this.rigidbody && that.forceGenerator == this.forceGenerator;
    }
}
