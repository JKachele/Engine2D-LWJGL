/******************************************
 *Project-------Engine2D-LWJGL
 *File----------PickingTexture.java
 *Author--------Justin Kachele
 *Date----------10/20/2022
 *License-------Mozilla Public License Version 2.0
 ******************************************/
package com.jkachele.game.renderer;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL30.*;

public class PickingTexture {
    private int pickingTextureID;
    private int fboID;
    private int depthTexture;

    public PickingTexture(int width, int height) {
        //noinspection RedundantIfStatement
        if (!init(width, height)) {
            assert false : "PickingTexture not initialized!";
        }
    }

    public boolean init(int width, int height) {
        // Generate Framebuffer ID
        fboID = glGenFramebuffers();
        // Bind it to our FBO
        glBindFramebuffer(GL_FRAMEBUFFER, fboID);

        // Create the texture to render the data to and attach it to the buffer
        pickingTextureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, pickingTextureID);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB32F, width, height, 0, GL_RGB, GL_FLOAT, 0);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D,
                this.pickingTextureID, 0);

        // create the texture object for the depth buffer
        glEnable(GL_TEXTURE_2D);
        depthTexture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, depthTexture);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT,
                width, height, 0, GL_DEPTH_COMPONENT, GL_FLOAT, 0);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, depthTexture, 0);

        // Disable the reading
        glReadBuffer(GL_NONE);
        glDrawBuffer(GL_COLOR_ATTACHMENT0);

        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            assert false : "Framebuffer is not complete";
            return false;
        }

        // Unbind the current texture and framebuffer
        glBindTexture(GL_TEXTURE_2D, 0);
        glBindFramebuffer(GL_FRAMEBUFFER, 0);

        return true;
    }

    public void enableWriting() {
        glBindFramebuffer(GL_DRAW_FRAMEBUFFER, fboID);
    }

    public void disableWriting() {
        glBindFramebuffer(GL_DRAW_FRAMEBUFFER, 0);
    }

    public int readPixel(int x, int y) {
        glBindFramebuffer(GL_READ_FRAMEBUFFER, fboID);
        glReadBuffer(GL_COLOR_ATTACHMENT0);

        float[] pixels = new float[3];
        glReadPixels(x, y, 1, 1, GL_RGB, GL_FLOAT, pixels);

        return (int)(pixels[0]) - 1;
    }
}
