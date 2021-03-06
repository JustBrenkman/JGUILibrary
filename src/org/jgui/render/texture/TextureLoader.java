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

import de.matthiasmann.twl.utils.PNGDecoder;
import org.jgui.util.PathManager;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.util.vector.Vector2f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;

/**
 * Created by ben on 04/12/14.
 *
 * JGUILibrary
 */
public class TextureLoader {

    private HashMap<String, Texture> textures = new HashMap<String, Texture>();

    private Logger logger;

    public TextureLoader() {
        logger = LoggerFactory.getLogger(TextureLoader.class);
    }

    public Texture getTexture(String name) {
        Texture tex = textures.get(name);
        if (tex != null)
            return tex;

        tex = getTexture(name, TextureProperties.getDefaultInstance());

        textures.put(name, tex);

        return tex;
    }

    public Texture getTexture(String filelocation, TextureProperties textureProperties) {
        return null;
    }

    public void addTexture(String name, Texture texture) {
        if (textures.get(name) != null)
            return;

        textures.put(name, texture);
    }

    public Texture loadTexture(String filename, TextureProperties textureProperties) {

        Texture texture;

        texture = new Texture(TextureProperties.getDefaultInstance());

        try {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
            InputStream in = new FileInputStream(PathManager.getLocationPath() + "/resources/textures/" + filename);
            PNGDecoder decoder = new PNGDecoder(in);

            logger.info("width=" + decoder.getWidth());
            logger.info("height=" + decoder.getHeight());

            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
            decoder.decode(byteBuffer, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
            byteBuffer.flip();

            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
            GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, byteBuffer);
            texture.setSize(new Vector2f(decoder.getWidth(), decoder.getHeight()));
            textures.put(filename, texture);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return texture;
    }
}
