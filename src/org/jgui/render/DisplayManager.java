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

package org.jgui.render;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Vector2f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DisplayManager {

    public static final int defaultWidth = 800;
//    public static final int defaultWidth = 1600;
    public static final int defaultHeight = 600;
//    public static final int defaultHeight = 900;

    private static int height;
    private static int width;

    public boolean fullscreen = false;

    private static Vector2f centerPosition;

    private String title = "Window";

    Logger logger;

    public DisplayManager() {
        this(defaultWidth, defaultHeight);
    }

    public DisplayManager(int width, int height) {
        logger = LoggerFactory.getLogger(DisplayManager.class);
        centerPosition = new Vector2f();
        setWidth(width);
        setHeight(height);
    }

    public void Display(boolean full) {
        this.fullscreen = full;
    }

    public void create() {
        try {
            PixelFormat pixelFormat = new PixelFormat();
            ContextAttribs contextAtrributes = new ContextAttribs(3, 2).withForwardCompatible(true).withProfileCore(true);
            Display.setDisplayMode(new DisplayMode(getWidth(), getHeight()));
            Display.setTitle("JGUI");
            Display.create(pixelFormat, contextAtrributes);

            GL11.glViewport(0, 0, width, height);

            logger.info("Created Display: " + width + " X " + height);
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
    }

    public void clear() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
    }

    public void destroy() {
        Display.destroy();
    }

    public void update() {
        Display.update();
    }

    public void sync(int sync) {
        Display.sync(sync);
    }

    public static int getHeight() {
        return height;
    }

    public static void setHeight(int height) {
        DisplayManager.height = height;
        centerPosition.setY(height / 2);
    }

    public static int getWidth() {
        return width;
    }

    public static void setWidth(int width) {
        DisplayManager.width = width;
        centerPosition.setX(width / 2);
    }

    public boolean isFullscreen() {
        return fullscreen;
    }

    public void setFullscreen(boolean fullscreen) {
        this.fullscreen = fullscreen;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public static Vector2f getCenterPosition() {
        return centerPosition;
    }

    public boolean isCloseRequested() {
        return Display.isCloseRequested();
    }
}
