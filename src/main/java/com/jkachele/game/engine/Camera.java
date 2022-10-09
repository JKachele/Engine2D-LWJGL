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
    private Vector2f camRangeX;
    private Vector2f camRangeY;

    // Virtual screen size of 1920 x 1080 pixels
    private final float GRID_TILE_SIZE = 32.0f;
    private final float GRID_WIDTH = 60.0f;
    private final float GRID_HEIGHT = 33.75f;
    private final float CAM_WIDTH_PIXELS = GRID_WIDTH * GRID_TILE_SIZE;
    private final float CAM_HEIGHT_PIXELS = GRID_HEIGHT * GRID_TILE_SIZE;

    public Camera(Vector2f cameraPosition) {
        this.position = cameraPosition;
        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();
        this.inverseProjection = new Matrix4f();
        this.inverseView = new Matrix4f();
        adjustProjection();
        setCameraRange();
    }

    public void adjustProjection() {
        projectionMatrix.identity();
        // Defines the size of the virtual screen the camera will output to
        projectionMatrix.ortho(0.0f, CAM_WIDTH_PIXELS,        //Screen Width in "pixels"
                0.0f, CAM_HEIGHT_PIXELS,                          // Screen Height in "pixels"
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

    public void setCameraRange() {
        camRangeX = new Vector2f(0 + this.position.x, CAM_WIDTH_PIXELS + this.position.x);
        camRangeY = new Vector2f(0 + this.position.y, CAM_HEIGHT_PIXELS + this.position.y);
    }

    public Matrix4f getProjectionMatrix() {
        return this.projectionMatrix;
    }

    public Vector2f getPosition() {
        return this.position;
    }

    public void setPosition(Vector2f position) {
        this.position = position;
        setCameraRange();
    }

    public void movePosition(float x, float y) {
        this.position.x += x;
        this.position.y += y;
        setCameraRange();
    }

    public Vector2f getCamRangeX() {
        return this.camRangeX;
    }

    public Vector2f getCamRangeY() {
        return this.camRangeY;
    }

    public Matrix4f getInverseProjection() {
        return this.inverseProjection;
    }

    public Matrix4f getInverseView() {
        return this.inverseView;
    }
}
