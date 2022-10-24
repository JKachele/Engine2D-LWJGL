/******************************************
 *Project-------Engine2D-LWJGL
 *File----------Framebuffer.java
 *Author--------Justin Kachele
 *Date----------10/15/2022
 *License-------MIT License
 ******************************************/
package com.jkachele.game.renderer;

import static org.lwjgl.opengl.GL30.*;

public class Framebuffer {
    private int fboID;
    private Texture texture;

    public Framebuffer(int width, int height) {
        // Generate Framebuffer ID
        fboID = glGenFramebuffers();
        // Bind it to our FBO
        glBindFramebuffer(GL_FRAMEBUFFER, fboID);

        // Create the texture to render the data to and attach it to the buffer
        this.texture = new Texture(width, height);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D,
                this.texture.getID(), 0);

        // Create the renderbuffer to store the depth data
        int rboID = glGenRenderbuffers();
        glBindRenderbuffer(GL_RENDERBUFFER, rboID);
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT32, width, height);

        // Attach the renderbuffer to the FBO
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, rboID);

        assert (glCheckFramebufferStatus(GL_FRAMEBUFFER) == GL_FRAMEBUFFER_COMPLETE) : "Framebuffer is not complete";

        // Unbind the current Framebuffer
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    public void bind() {
        glBindFramebuffer(GL_FRAMEBUFFER, fboID);
    }

    public void unbind() {
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    public int getFboID() {
        return fboID;
    }

    public Texture getTexture() {
        return texture;
    }
}
