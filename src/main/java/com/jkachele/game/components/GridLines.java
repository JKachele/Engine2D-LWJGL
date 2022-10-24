/******************************************
 *Project-------Engine2D-LWJGL
 *File----------GridLines.java
 *Author--------Justin Kachele
 *Date----------10/9/2022
 *License-------MIT License
 ******************************************/
package com.jkachele.game.components;

import com.jkachele.game.engine.Camera;
import com.jkachele.game.engine.Window;
import com.jkachele.game.renderer.DebugDraw;
import com.jkachele.game.util.Color;
import com.jkachele.game.util.Constants;
import org.joml.Vector2f;

public class GridLines extends Component {

    @Override
    public void update(float dt) {
        Camera camera = Window.getCurrentScene().getCamera();

        Vector2f cameraPos = camera.getPosition();
        Vector2f projectionSize = camera.getProjectionSize();

        int firstX = ((int)(cameraPos.x / Constants.GRID_WIDTH) - 1) * Constants.GRID_WIDTH;
        int firstY = ((int)(cameraPos.y / Constants.GRID_HEIGHT) - 1) * Constants.GRID_HEIGHT;

        int width = (int)(projectionSize.x * camera.getZoom()) + Constants.GRID_WIDTH * 2;
        int height = (int)(projectionSize.y * camera.getZoom()) + Constants.GRID_HEIGHT * 2;

        int numVerticalLines = (int)(projectionSize.x * camera.getZoom())/ Constants.GRID_WIDTH + 2;
        int numHorizontalLines = (int)(projectionSize.y * camera.getZoom()) / Constants.GRID_HEIGHT + 2;

        int maxLines = Math.max(numVerticalLines, numHorizontalLines);
        for (int i = 0; i < maxLines; i++) {
            int x = firstX + (Constants.GRID_WIDTH * i);
            int y = firstY + (Constants.GRID_HEIGHT * i);

            if (i < numVerticalLines) {
                DebugDraw.addLine2D(new Vector2f(x, firstY), new Vector2f(x, firstY + height), Color.DARK_GRAY.toVector());
            }
            if (i < numHorizontalLines) {
                DebugDraw.addLine2D(new Vector2f(firstX, y), new Vector2f(firstX + width, y), Color.DARK_GRAY.toVector());
            }
        }
    }
}
