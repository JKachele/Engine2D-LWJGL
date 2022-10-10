/******************************************
 *Project-------Learn-LWJGL
 *File----------Camera.java
 *Author--------Justin Kachele
 *Date----------9/29/2022
 *License-------GNU GENERAL PUBLIC LICENSE
 ******************************************/
package com.jkachele.game.engine;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera {
    private Matrix4f projectionMatrix;
    private Matrix4f viewMatrix;
    private Matrix4f inverseProjection;
    private Matrix4f inverseView;
    private Vector2f position;

    // Virtual screen size of 1920 x 1080 pixels (60 x 33.75 grid of 32 pixel cells)
    private final Vector2f PROJECTION_SIZE = new Vector2f(60.0f * 32.0f, 33.75f * 32.0f);

    public Camera(Vector2f cameraPosition) {
        this.position = cameraPosition;
        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();
        this.inverseProjection = new Matrix4f();
        this.inverseView = new Matrix4f();
        adjustProjection();
    }

    public void adjustProjection() {
        projectionMatrix.identity();
        // Defines the size of the virtual screen the camera will output to
        projectionMatrix.ortho(0.0f, PROJECTION_SIZE.x,        //Screen Width in "pixels"
                0.0f, PROJECTION_SIZE.y,                          // Screen Height in "pixels"
                0.0f, 100.0f);                               // Near and far clipping planes
        projectionMatrix.invert(inverseProjection);
    }

    public Matrix4f getViewMatrix() {
        // Camera looks at -1 in the Z direction
        Vector3f cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
        Vector3f cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);

        this.viewMatrix.identity();
        this.viewMatrix.lookAt(
                new Vector3f(position.x, position.y, 20.0f),    // Where the camera is in world space
                cameraFront.add(position.x, position.y, 0.0f),
                cameraUp); // Where is the camera pointing
        this.viewMatrix.invert(inverseView);
        return this.viewMatrix;
    }

    public Matrix4f getProjectionMatrix() {
        return this.projectionMatrix;
    }

    public Vector2f getPosition() {
        return this.position;
    }

    public void setPosition(Vector2f position) {
        this.position = position;
    }

    public void movePosition(float x, float y) {
        this.position.x += x;
        this.position.y += y;
    }

    public Matrix4f getInverseProjection() {
        return this.inverseProjection;
    }

    public Matrix4f getInverseView() {
        return this.inverseView;
    }

    public Vector2f getProjectionSize() {
        return this.PROJECTION_SIZE;
    }
}
