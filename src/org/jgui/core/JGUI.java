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

package org.jgui.core;

import org.jgui.mesh.Mesh;
import org.jgui.render.Display;
import org.jgui.render.IRenderer;
import org.jgui.render.OpenGLRenderer;
import org.jgui.render.Shader;
import org.jgui.util.PathManager;

import java.awt.*;

public class JGUI {

    Display display;

    private IRenderer renderer;

    public JGUI() {
        display = new Display();

        renderer = new OpenGLRenderer();

        PathManager.getInstance();
        PathManager.initialize();
    }

    public void intitialize() {
        display.create();
        renderer.initialize();
//        renderer.clearBuffers();
    }

    public void registerWindow() {

    }

    public void registerApplication() {

    }

    public void startApplication(String name) {

    }

    public IRenderer getRenderer() {
        return renderer;
    }

    /**
     * Careful this is the last method that you can call
     *
     * Calls render methods
     */
    public void start() {
        mainLoop();
    }

    private void mainLoop() {

        Mesh mesh = new Mesh();

        mesh.getMesh().addVerticies(-0.5f, 0.5f, -0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f);
        mesh.getMesh().addColor(new Color(0x2D89EF));
        mesh.getMesh().addColor(new Color(0x2D89EF));
        mesh.getMesh().addColor(new Color(0x2D89EF));
        mesh.getMesh().addColor(new Color(0x2D89EF));
        byte[] indecies = {0, 1, 2, 2, 3, 0};
        mesh.getMesh().addIndecies(indecies);

        mesh.getMesh().compile();

        Shader shader = new Shader("vs.glsl", "fs.glsl");
        shader.loadShaders();
        shader.compile();

        Mesh box = new Mesh();
        box.getMesh().addVerticies(-0.2f, 0.2f, -0.2f, -0.2f, 0.2f, -0.2f, 0.2f, 0.2f);
        box.getMesh().addColor(new Color(0x00A300));
        box.getMesh().addColor(new Color(0x00A300));
        box.getMesh().addColor(new Color(0x00A300));
        box.getMesh().addColor(new Color(0x00A300));
        byte[] boxi = {0, 1, 2, 2, 3, 0};
        box.getMesh().addIndecies(boxi);
        box.getMesh().compile();

        while (!display.isCloseRequested()) {

            display.clear();

            renderer.renderMesh(mesh, shader);

            renderer.renderMesh(box, shader);

            display.update();
        }

        display.destroy();
    }
}
