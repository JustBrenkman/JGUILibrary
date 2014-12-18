/*
 *
 *  * Copyright (c) 2014.
 *  * All rights reserved.
 *  *
 *  * Redistribution and use in source and binary forms, with or without
 *  * modification, are permitted provided that the following conditions are met:
 *  *
 *  *  1. Redistributions of source code must retain the above copyright notice, this
 *  *     list of conditions and the following disclaimer.
 *  *  2. Redistributions in binary form must reproduce the above copyright notice,
 *  *     this list of conditions and the following disclaimer in the documentation
 *  *     and/or other materials provided with the distribution.
 *  *
 *  *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *  *  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  *  DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 *  *  ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  *  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *  *
 *  *  The views and conclusions contained in the software and documentation are those
 *  *  of the authors and should not be interpreted as representing official policies,
 *  *  either expressed or implied, of the FreeBSD Project.
 *
 */

package org.jgui.render.texture;

import org.jgui.util.StringColors;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.util.vector.Vector2f;

/**
 * Created by ben on 04/12/14.
 * Class contains code for holding textures
 */
public class Texture {

    private Vector2f size;

    private int textureID;

    private TextureProperties textureProperties;

    public Texture(TextureProperties textureProperties) {
        size = new Vector2f();
        textureID = 0;
        this.textureProperties = textureProperties;
        generateTextureID();
    }

    public static void unbind(TextureProperties textureProperties) {
        if (textureProperties != null) {
            GL11.glBindTexture(textureProperties.getTarget(), 0);
        } else {
            StringColors.printl(StringColors.ANSI_RED, "Texture properties are null!");
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        }
    }

    public TextureProperties getTextureProperties() {
        return textureProperties;
    }

    public void setTextureProperties(TextureProperties textureProperties) {
        this.textureProperties = textureProperties;
    }

    public Vector2f getSize() {
        return size;
    }

    public void setSize(Vector2f size) {
        this.size = size;
    }

    public int getTextureID() {
        return textureID;
    }

    public void setTextureID(int textureID) {
        this.textureID = textureID;
    }

    /**
     * Generates the textureID if textureID doesn't exist
     */
    public void generateTextureID() {
            textureID = GL11.glGenTextures();
    }

    public void bind() {
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        if (textureProperties != null) {
            // TODO use the TextureProperties.getTarget() Method
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
        } else {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
        }
    }

    public void unbind() {
        if (textureProperties != null) {
            GL11.glBindTexture(textureProperties.getTarget(), 0);
        } else {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        }
    }
}
