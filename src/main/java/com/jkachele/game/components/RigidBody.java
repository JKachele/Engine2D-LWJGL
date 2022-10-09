/******************************************
 *Project-------Learn-LWJGL
 *File----------RigidBody.java
 *Author--------Justin Kachele
 *Date----------10/8/2022
 *License-------GNU GENERAL PUBLIC LICENSE
 ******************************************/
package com.jkachele.game.components;

import org.joml.Vector3f;
import org.joml.Vector4f;

public class RigidBody extends Component {

    private int colliderType = 0;
    private float friction = 0.8f;
    public Vector3f velocity = new Vector3f(0, 0.5f, 0);
    public transient Vector4f tmp = new Vector4f(0, 0, 0, 0);
}
