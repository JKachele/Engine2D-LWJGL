/******************************************
 *Project-------Engine2D-LWJGL
 *File----------MenuBar.java
 *Author--------Justin Kachele
 *Date----------11/4/2022
 *License-------Mozilla Public License Version 2.0
 ******************************************/
package com.jkachele.game.editor;

import com.jkachele.game.observers.EventSystem;
import com.jkachele.game.observers.events.Event;
import com.jkachele.game.observers.events.EventType;
import imgui.ImGui;

public class MenuBar {

    public void imGui() {
        ImGui.beginMainMenuBar();

        if (ImGui.beginMenu("File")){
            if(ImGui.menuItem("Save", "Ctrl+s")) {
                EventSystem.notify(null, new Event(EventType.SaveLevel));
            }
            if(ImGui.menuItem("Load", "Ctrl+O")) {
                EventSystem.notify(null, new Event(EventType.LoadLevel));
            }

            ImGui.endMenu();
        }

        ImGui.endMainMenuBar();
    }

}
