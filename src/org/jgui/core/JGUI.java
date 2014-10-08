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

import org.jgui.render.mesh.Mesh;
import org.jgui.render.Display;
import org.jgui.render.IRenderer;
import org.jgui.render.OpenGLRenderer;
import org.jgui.render.Shader;
import org.jgui.scene.transform.Transform;
import org.jgui.util.Camera;
import org.jgui.util.FPS;
import org.jgui.util.PathManager;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import java.awt.*;

public class JGUI {

    Display display;

    private IRenderer renderer;

    private FPS fps;

    public JGUI() {
        display = new Display();

        renderer = new OpenGLRenderer();

        PathManager.getInstance();
        PathManager.initialize();
    }

    public void intitialize() {
        display.create();
        renderer.initialize();
        fps = new FPS();
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

        Camera camera = new Camera();
        camera.init();
        camera.getTransform().updateTransformation();

        Mesh mesh = new Mesh();

        Transform t = new Transform();
        t.setScale(new Vector3f(1, 1, 1));
        t.setRotation(new Vector3f(0, 0, -2f));
        t.setTranslation(new Vector3f(0, 0, 0));

        Transform tr = new Transform();
        tr.setTranslation(new Vector3f(0, 0, 0f));
        tr.setRotation(new Vector3f(0, 0, 0));
        tr.setScale(1);

        mesh.getMesh().addVertex(new Vector3f(-1, -1, 0));
        mesh.getMesh().addVertex(new Vector3f(0, 1, 0));
        mesh.getMesh().addVertex(new Vector3f(1, -1, 0));
        mesh.getMesh().addColor(new Color(0x2d89ef));
        mesh.getMesh().addColor(new Color(0x2D89EF));
        mesh.getMesh().addColor(new Color(0x2D89EF));
        byte[] indecies = {0, 1, 2};
        mesh.getMesh().addIndecies(indecies);

        mesh.getMesh().compile();

        Shader shader = new Shader("vs.glsl", "fs.glsl");
        shader.loadShaders();
        shader.compile();

        shader.addUniform("uniformFloat");
        shader.addUniform("modelMatrix");
        shader.addUniform("projectionMatrix");
        shader.addUniform("viewMatrix");

        /**
         * Normal Shader
         */
        Shader normalShader = new Shader("vs.glsl", "fs.glsl");
        normalShader.loadShaders();
        normalShader.compile();

        normalShader.addUniform("modelMatrix");
        normalShader.addUniform("projectionMatrix");
        normalShader.addUniform("viewMatrix");

        /**
         * Box mesh setup
         */
        Mesh box = new Mesh();
        box.getMesh().addVerticies(new Vector3f(-40f, 30f, 0), new Vector3f(-40f, -30f, 0), new Vector3f(40f, -30f, 0), new Vector3f(40f, 30f, 0));
        box.getMesh().addColor(new Color(0xe51c23));
        box.getMesh().addColor(new Color(0xe51c23));
        box.getMesh().addColor(new Color(0xe51c23));
        box.getMesh().addColor(new Color(0xe51c23));
        byte[] ind = {0, 1, 2, 2, 3, 0};
        box.getMesh().addIndecies(ind);
        box.getMesh().compile();

        /**
         * DisplayMode listing
         */
        org.lwjgl.opengl.DisplayMode[] modes = new org.lwjgl.opengl.DisplayMode[0];
        try {
            modes = org.lwjgl.opengl.Display.getAvailableDisplayModes();
        } catch (LWJGLException e) {
            e.printStackTrace();
        }

        for (int i=0;i<modes.length;i++) {
            org.lwjgl.opengl.DisplayMode current = modes[i];
            System.out.println(current.getWidth() + "x" + current.getHeight() + "x" +
                    current.getBitsPerPixel() + " " + current.getFrequency() + "Hz");
        }

        float temp = 0;

        try {
            Mouse.create();
        } catch (LWJGLException e) {
            e.printStackTrace();
        }

        fps.initialize();

        int lastFPS = 0;

        /**
         * main loop
         */
        while (!display.isCloseRequested()) {

            // updates FPS
            fps.update();

            // clears screen
            display.clear();

            temp += 0.1f;

            t.setTranslation(new Vector3f(0, 0, -2 - (float) Math.abs(Math.sin(temp))));
//            t.setTranslation(new Vector3f(((float) Mouse.getX() / org.lwjgl.opengl.Display.getWidth()) * 2 - 1, ((float)Mouse.getY() / Display.defaultHeight) * 2 - 1, 0));
            t.setRotation(new Vector3f(temp / 10, 0, 0));
            t.updateTransformation();

            tr.updateTransformation();

            shader.bind();
            shader.updateUniformf("uniformFloat", (float) Math.abs(Math.sin(temp)));
            shader.updateUniformMatrix4("modelMatrix", t.getModelMatrix());
            shader.updateUniformMatrix4("projectionMatrix", camera.getProjectionMatrix());
            shader.updateUniformMatrix4("viewMatrix", camera.getTransform().getModelMatrix());
            shader.unBind();

            normalShader.bind();
            normalShader.updateUniformMatrix4("modelMatrix", tr.getModelMatrix());
            normalShader.updateUniformMatrix4("projectionMatrix", camera.getOrthoGraphicMatrix());
            normalShader.updateUniformMatrix4("viewMatrix", camera.getTransform().getModelMatrix());
            normalShader.unBind();

//            renderer.renderMesh(mesh, shader);
            renderer.renderMesh(box, normalShader);

            // Calculates wether or not to print the fps
            if (lastFPS != fps.getFPS()) {
                System.out.println(fps.getFPS());
                lastFPS = fps.getFPS();
            }

            display.update();
        }

        shader.destroy();
        normalShader.destroy();
        mesh.destroy();
        box.destroy();

        display.destroy();
    }
}
