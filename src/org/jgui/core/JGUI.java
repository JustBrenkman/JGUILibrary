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

import org.jgui.eventbus.EventBusService;
import org.jgui.events.ShutDownEvent;
import org.jgui.render.mesh.Mesh;
import org.jgui.render.Display;
import org.jgui.render.IRenderer;
import org.jgui.render.OpenGLRenderer;
import org.jgui.render.Shader;
import org.jgui.scene.node.appearance.Material;
import org.jgui.scene.transform.Transform;
import org.jgui.util.*;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import java.awt.*;

public class JGUI {

    Display display;

    private OpenGLRenderer renderer;

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
        EventBusService.subscribe(this);
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


        IRSeekerRenderer irSeekerRenderer = new IRSeekerRenderer(camera, renderer);
        irSeekerRenderer.initialize();

        Transform transform = new Transform(new Vector3f(0, 0, -10));
        transform.updateTransformation();

        Shader shader = new Shader("Experimental/Light/vs.glsl", "Experimental/Light/fs.glsl");
//        Shader shader = new Shader("vs.glsl", "fs.glsl");
        shader.loadShaders();
        shader.compile();

        shader.addUniform("modelMatrix");
        shader.addUniform("projectionMatrix");
        shader.addUniform("viewMatrix");
        shader.addUniform("light_Pos");
        shader.addUniform("light_Col");

        Mesh box = new Mesh();
        box.getMesh().addVerticies(new Vector3f(-0.5f, -0.5f, 0.5f), new Vector3f(0.5f, -0.5f, 0.5f), new Vector3f(0.5f, 0.5f, 0.5f), new Vector3f(-0.5f, 0.5f, 0.5f), // Front
                new Vector3f(-0.5f, -0.5f, -0.5f), new Vector3f(0.5f, -0.5f, -0.5f), new Vector3f(0.5f, 0.5f, -0.5f), new Vector3f(-0.5f, 0.5f, -0.5f), // Back
                new Vector3f(-0.5f, -0.5f, -0.5f), new Vector3f(-0.5f, -0.5f, 0.5f), new Vector3f(-0.5f, 0.5f, 0.5f), new Vector3f(-0.5f, 0.5f, -0.5f), // Right
                new Vector3f(-0.5f, -0.5f, 0.5f), new Vector3f(0.5f, -0.5f, 0.5f), new Vector3f(-0.5f, -0.5f, -0.5f), new Vector3f(0.5f, -0.5f, -0.5f), // Bottom
                new Vector3f(-0.5f, 0.5f, 0.5f), new Vector3f(0.5f, 0.5f, 0.5f), new Vector3f(-0.5f, 0.5f, -0.5f), new Vector3f(0.5f, 0.5f, -0.5f), // Top
                new Vector3f(0.5f, -0.5f, -0.5f), new Vector3f(0.5f, -0.5f, 0.5f), new Vector3f(0.5f, 0.5f, 0.5f), new Vector3f(0.5f, 0.5f, -0.5f)); // Left
        Material mat = new Material(Color.BLUE);
        box.setMaterial(mat);
        byte[] index = {0, 1, 2, 0, 2, 3,       // Front
                       4, 6, 5, 4, 7, 6,        // Back
                       8, 9, 10, 8, 10, 11,     // Right
                       12, 15, 13, 12, 14, 15,  // Bottom
                       16, 17, 19, 16, 19, 18,  // Top
                       20, 22, 21, 20, 23, 22}; // Left
        box.getMesh().addIndecies(index);
        box.getMesh().addNormals(new Vector3f(0, 0, 1), new Vector3f(0, 0, 1), new Vector3f(0, 0, 1), new Vector3f(0, 0, 1),
                new Vector3f(0, 0, -1), new Vector3f(0, 0, -1), new Vector3f(0, 0, -1), new Vector3f(0, 0, -1),
                new Vector3f(-1, 0, 0), new Vector3f(-1, 0, 0), new Vector3f(-1, 0, 0), new Vector3f(-1, 0, 0),
                new Vector3f(0, -1, 0), new Vector3f(0, -1, 0), new Vector3f(0, -1, 0), new Vector3f(0, -1, 0),
                new Vector3f(0, 1, 0), new Vector3f(0, 1, 0), new Vector3f(0, 1, 0), new Vector3f(0, 1, 0),
                new Vector3f(1, 0, 0), new Vector3f(1, 0, 0), new Vector3f(1, 0, 0), new Vector3f(1, 0, 0));
        box.getMesh().setCalulateNormals(true);
        box.getMesh().compile();

        float rot = 0;
        Vector3f camRot = new Vector3f(0, 0, 0);

        Vector3f lightPos = new Vector3f(0, 0, 1);
        Vector3f lightCol = new Vector3f(1, 1, 1);

        float lastMouseX = Mouse.getX();
        float lastMouseY = Mouse.getY();

        /**
         * main loop
         */
        while (!display.isCloseRequested()) {

            if(Keyboard.isKeyDown(Keyboard.KEY_W))
                camera.move(camera.getTransform().getQ_rotation().getForward(), 0.1f);
            if(Keyboard.isKeyDown(Keyboard.KEY_S))
                camera.move(camera.getTransform().getQ_rotation().getForward(), -0.1f);
            if(Keyboard.isKeyDown(Keyboard.KEY_A))
                camera.move(camera.getTransform().getQ_rotation().getRight(), 0.1f);
            if(Keyboard.isKeyDown(Keyboard.KEY_D))
                camera.move(camera.getTransform().getQ_rotation().getLeft(), 0.1f);

            if(Keyboard.isKeyDown(Keyboard.KEY_UP)) {
//                camRot.setZ(camRot.getZ() - 0.01f);
                camera.rotate(camera.degreesToRadians(1), Axis.X_AXIS);
            }
            if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
//                camRot.setZ(camRot.getZ() + 0.01f);
                camera.rotate(-camera.degreesToRadians(1), Axis.X_AXIS);
            }
            if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
//                camRot.setY(camRot.getY() - 0.01f);
                camera.rotate(camera.degreesToRadians(1), Axis.Y_AXIS);
            }
            if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
//                camRot.setY(camRot.getY() + 0.01f);
                camera.rotate(-camera.degreesToRadians(1), Axis.Y_AXIS);
            }

//            camera.getTransform().setRotation(camRot);

            renderer.clearBuffers();

//            camRot.setY(camRot.getY() + 0.01f);

//            camera.getTransform().setRotation(camRot);
//            camera.getTransform().updateTransformation();

            // Render 3D stuff here
            rot += 1f;
            transform.setRotation(new Vector3f(camera.degreesToRadians(rot), camera.degreesToRadians(rot), camera.degreesToRadians(rot)));
            camera.updateTransform();

            shader.bind();
            transform.updateTransformation();
            shader.updateUniformMatrix4("modelMatrix", transform.getModelMatrix());
            shader.updateUniformMatrix4("projectionMatrix", camera.getProjectionMatrix());
            shader.updateUniformMatrix4("viewMatrix", camera.getViewMatrix());
            shader.updateUniformVector3f("light_Pos", lightPos);
            shader.updateUniformVector3f("light_Col", lightCol);
            shader.unBind();

            renderer.renderMesh(box, shader);


            // Reset the Camera up for Orthographics
//            camera.getTransform().setRotation(new Vector3f(0, 0, 0));
            camera.getTransform().updateTransformation();

            // Render 2D stuff here
            irSeekerRenderer.render();

            display.update();
        }

        EventBusService.publish(new ShutDownEvent(true));
        box.destroy();
        display.destroy();
    }
}
