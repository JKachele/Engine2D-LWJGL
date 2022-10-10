/******************************************
 *Project-------Learn-LWJGL
 *File----------GridLines.java
 *Author--------Justin Kachele
 *Date----------10/9/2022
 *License-------GNU GENERAL PUBLIC LICENSE
 ******************************************/
package com.jkachele.game.components;

import com.jkachele.game.engine.Window;
import com.jkachele.game.renderer.DebugDraw;
import com.jkachele.game.util.Color;
import com.jkachele.game.util.Settings;
import org.joml.Vector2f;

public class GridLines extends Component {

    @Override
    public void update(float dt) {
        Vector2f cameraPos = Window.getCurrentScene().getCamera().getPosition();
        Vector2f projectionSize = Window.getCurrentScene().getCamera().getProjectionSize();

        int firstX = ((int)(cameraPos.x / Settings.GRID_WIDTH) - 1) * Settings.GRID_WIDTH;
        int firstY = ((int)(cameraPos.y / Settings.GRID_HEIGHT) - 1) * Settings.GRID_HEIGHT;

        int width = (int)projectionSize.x + Settings.GRID_WIDTH * 2;
        int height = (int)projectionSize.y + Settings.GRID_HEIGHT * 2;

        int numVerticalLines = width / Settings.GRID_WIDTH + 2;
        int numHorizontalLines = height / Settings.GRID_HEIGHT + 2;

        int maxLines = Math.max(numVerticalLines, numHorizontalLines);
        for (int i = 0; i < maxLines; i++) {
            int x = firstX + (Settings.GRID_WIDTH * i);
            int y = firstY + (Settings.GRID_HEIGHT * i);

            if (i < numVerticalLines) {
                DebugDraw.addLine2D(new Vector2f(x, firstY), new Vector2f(x, firstY + height), Color.DARK_GRAY.toVector());
            }
            if (i < numHorizontalLines) {
                DebugDraw.addLine2D(new Vector2f(firstX, y), new Vector2f(firstX + width, y), Color.DARK_GRAY.toVector());
            }
        }
    }
}
