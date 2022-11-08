/******************************************
 *Project-------Engine2D-LWJGL
 *File----------ForceRegistry.java
 *Author--------Justin Kachele
 *Date----------10/17/2022
 *License-------Mozilla Public License Version 2.0
 ******************************************/
package com.jkachele.game.physics2dtmp.forces;

import com.jkachele.game.physics2dtmp.rigidbody.Rigidbody2D;

import java.util.ArrayList;
import java.util.List;

public class ForceRegistry {
    private List<ForceRegistration> registry;

    public ForceRegistry() {
        this.registry = new ArrayList<>();
    }

    public void add(Rigidbody2D rigidbody, ForceGenerator generator) {
        ForceRegistration registration = new ForceRegistration(generator, rigidbody);
        registry.add(registration);
    }

    public void remove(Rigidbody2D rigidbody, ForceGenerator generator) {
        ForceRegistration registration = new ForceRegistration(generator, rigidbody);
        registry.remove(registration);
    }

    public void clear() {
        registry.clear();
    }

    public void updateForces(float dt) {
        for (ForceRegistration registration : registry) {
            registration.forceGenerator.updateForce(registration.rigidbody, dt);
        }
    }

    // TODO: Implement me
//    public void zeroForces() {
//        for (ForceRegistration registration : registry) {
//            registration.rigidbody.zeroForces();
//        }
//    }
}
