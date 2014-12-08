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

import org.lwjgl.opengl.GL11;

/**
 * Created by ben on 05/12/14.
 * <p/>
 * JGUILibrary
 */
public class TextureProperties {

    private int dstPixelFormat;

    private int target;

    private int minFilter;

    private int magFilter;

    private static TextureProperties textureProperties;

    public TextureProperties(int dstPixelFormat, int target, int minFilter, int magFilter) {
        this.dstPixelFormat = dstPixelFormat;
        this.target = target;
        this.minFilter = minFilter;
        this.magFilter = magFilter;
    }

    public int getDstPixelFormat() {
        return dstPixelFormat;
    }

    public void setDstPixelFormat(int dstPixelFormat) {
        this.dstPixelFormat = dstPixelFormat;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public int getMinFilter() {
        return minFilter;
    }

    public void setMinFilter(int minFilter) {
        this.minFilter = minFilter;
    }

    public int getMagFilter() {
        return magFilter;
    }

    public void setMagFilter(int magFilter) {
        this.magFilter = magFilter;
    }

    public static TextureProperties getDefaultInstance() {
        if (textureProperties != null)
            return textureProperties;

            textureProperties = new TextureProperties(GL11.GL_TEXTURE_2D, GL11.GL_RGBA, GL11.GL_LINEAR, GL11.GL_LINEAR);
            return textureProperties;
    }
}
