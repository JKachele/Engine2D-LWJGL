/******************************************
 *Project-------Engine2D-LWJGL
 *File----------GameViewWindow.java
 *Author--------Justin Kachele
 *Date----------10/18/2022
 *License-------GNU GENERAL PUBLIC LICENSE
 ******************************************/
package com.jkachele.game.editor;

import com.jkachele.game.engine.MouseListener;
import com.jkachele.game.engine.Window;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiWindowFlags;
import org.joml.Vector2f;

public class GameViewWindow {
    private float minX;
    private float minY;
    private float maxX;
    private float maxY;

    private final float aspectRatio = 16.0f / 9.0f;

    public void imgui() {
        ImGui.begin("Game Viewport",
                ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoScrollWithMouse);

        ImVec2 windowSize = getLargestSizeForViewport();
        ImVec2 windowPos = getPositionForViewport(windowSize);

        ImGui.setCursorPos(windowPos.x, windowPos.y);

        ImVec2 topLeft = new ImVec2();
        ImGui.getCursorScreenPos(topLeft);
        topLeft.x -= ImGui.getScrollX();
        topLeft.y -= ImGui.getScrollY();

        minX = topLeft.x;
        minY = topLeft.y;
        maxX = topLeft.x + windowSize.x;
        maxY = topLeft.y + windowSize.y;

        int textureID = Window.getFramebuffer().getTexture().getID();
        ImGui.image(textureID, windowSize.x, windowSize.y, 0, 1, 1, 0);

        MouseListener.setGameViewportPos(new Vector2f(topLeft.x, topLeft.y));
        MouseListener.setGameViewportSize(new Vector2f(windowSize.x, windowSize.y));


        ImGui.end();
    }

    private ImVec2 getLargestSizeForViewport() {
        ImVec2 windowSize = new ImVec2();
        ImGui.getContentRegionAvail(windowSize);
        windowSize.x -= ImGui.getScrollX();
        windowSize.y -= ImGui.getScrollY();

        float aspectWidth = windowSize.x;
        float aspectHeight = aspectWidth / aspectRatio;

        if (aspectHeight > windowSize.y) {
            // We must switch to pillarBox mode
            aspectHeight = windowSize.y;
            aspectWidth = aspectHeight * aspectRatio;
        }

        return new ImVec2(aspectWidth, aspectHeight);
    }

    private ImVec2 getPositionForViewport(ImVec2 aspectSize) {
        ImVec2 windowSize = new ImVec2();
        ImGui.getContentRegionAvail(windowSize);
        windowSize.x -= ImGui.getScrollX();
        windowSize.y -= ImGui.getScrollY();

        float viewportX = (windowSize.x / 2.0f) - (aspectSize.x / 2.0f);
        float viewportY = (windowSize.y / 2.0f) - (aspectSize.y / 2.0f);

        return new ImVec2(viewportX + ImGui.getCursorPosX(), viewportY + ImGui.getCursorPosY());
    }

    public boolean getWantCaptureMouse() {
        return  MouseListener.getX() >= minX &&
                MouseListener.getX() <= maxX &&
                MouseListener.getY() >= minY &&
                MouseListener.getY() <= maxY;
    }
}
