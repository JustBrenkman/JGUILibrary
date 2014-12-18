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
import org.jgui.render.DisplayManager;
import org.jgui.render.IRenderer;
import org.jgui.render.OpenGLRenderer;
import org.jgui.render.Shader;
import org.jgui.render.mesh.Mesh;
import org.jgui.render.texture.Texture;
import org.jgui.render.texture.TextureLoader;
import org.jgui.render.texture.TextureProperties;
import org.jgui.scene.geometry.Line;
import org.jgui.scene.node.appearance.Material;
import org.jgui.scene.transform.Transform;
import org.jgui.scene.transform.Vector3fMath;
import org.jgui.util.*;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.awt.*;

public class JGUI {

    DisplayManager display;

    private OpenGLRenderer renderer;

    private TextureLoader textureLoader;

    public JGUI() {
        display = new DisplayManager();

        renderer = new OpenGLRenderer();

        PathManager.getInstance();
        PathManager.initialize();

        textureLoader = new TextureLoader();
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
        shader.addUniform("color");

        Shader textureShader = new Shader("Experimental/Light/Texture/vs.glsl", "Experimental/Light/Texture/fs.glsl");
//        Shader textureShader = new Shader("Experimental/Light/vs.glsl", "Experimental/Light/fs.glsl");
        textureShader.loadShaders();
        textureShader.compile();

        textureShader.addUniform("modelMatrix");
        textureShader.addUniform("projectionMatrix");
        textureShader.addUniform("viewMatrix");
        textureShader.addUniform("light_Pos");
        textureShader.addUniform("light_Col");
        textureShader.addUniform("color");
        textureShader.addUniform("tex");

        Mesh box = new Mesh();
        box.getMesh().addVerticies(new Vector3f(-0.5f, -0.5f, 0.5f), new Vector3f(0.5f, -0.5f, 0.5f), new Vector3f(0.5f, 0.5f, 0.5f), new Vector3f(-0.5f, 0.5f, 0.5f), // Front
                new Vector3f(-0.5f, -0.5f, -0.5f), new Vector3f(0.5f, -0.5f, -0.5f), new Vector3f(0.5f, 0.5f, -0.5f), new Vector3f(-0.5f, 0.5f, -0.5f), // Back
                new Vector3f(-0.5f, -0.5f, -0.5f), new Vector3f(-0.5f, -0.5f, 0.5f), new Vector3f(-0.5f, 0.5f, 0.5f), new Vector3f(-0.5f, 0.5f, -0.5f), // Right
                new Vector3f(-0.5f, -0.5f, 0.5f), new Vector3f(0.5f, -0.5f, 0.5f), new Vector3f(-0.5f, -0.5f, -0.5f), new Vector3f(0.5f, -0.5f, -0.5f), // Bottom
                new Vector3f(-0.5f, 0.5f, 0.5f), new Vector3f(0.5f, 0.5f, 0.5f), new Vector3f(-0.5f, 0.5f, -0.5f), new Vector3f(0.5f, 0.5f, -0.5f), // Top
                new Vector3f(0.5f, -0.5f, -0.5f), new Vector3f(0.5f, -0.5f, 0.5f), new Vector3f(0.5f, 0.5f, 0.5f), new Vector3f(0.5f, 0.5f, -0.5f)); // Left
        Material mat = new Material(new Color(0xdb6a3c));
        box.setMaterial(mat);
        int[] index = {0, 1, 2, 0, 2, 3,       // Front
                       4, 6, 5, 4, 7, 6,        // Back
                       8, 9, 10, 8, 10, 11,     // Right
                       12, 15, 13, 12, 14, 15,  // Bottom
                       16, 17, 19, 16, 19, 18,  // Top
                       20, 22, 21, 20, 23, 22}; // Left
        box.getMesh().addIndecies(index);
        // no need to add the normals
//        box.getMesh().addNormals(new Vector3f(0, 1, 0), new Vector3f(0, 1, 0), new Vector3f(0, 1, 0), new Vector3f(0, 1, 0),
//                new Vector3f(0, 0, -1), new Vector3f(0, 0, -1), new Vector3f(0, 0, -1), new Vector3f(0, 0, -1),
//                new Vector3f(-1, 0, 0), new Vector3f(-1, 0, 0), new Vector3f(-1, 0, 0), new Vector3f(-1, 0, 0),
//                new Vector3f(0, -1, 0), new Vector3f(0, -1, 0), new Vector3f(0, -1, 0), new Vector3f(0, -1, 0),
//                new Vector3f(0, 1, 0), new Vector3f(0, 1, 0), new Vector3f(0, 1, 0), new Vector3f(0, 1, 0),
//                new Vector3f(1, 0, 0), new Vector3f(1, 0, 0), new Vector3f(1, 0, 0), new Vector3f(1, 0, 0));
        box.getMesh().setCalulateNormals(true);
        box.getMesh().compile();

        Transform planeTransform = new Transform(new Vector3f(0, -10, 0));
        planeTransform.updateTransformation();

        Mesh plane = new Mesh();
        plane.getMesh().addVerticies(new Vector3f(-10, 0, 10), new Vector3f(10, 0, 10), new Vector3f(10, 0, -10), new Vector3f(-10, 0, -10));
        plane.getMesh().addNormals(new Vector3f(0, 1, 0), new Vector3f(0, 1, 0), new Vector3f(0, 1, 0), new Vector3f(0, 1, 0));
//        plane.getMesh().addTextureCoords(0, 0, 1, 0, 1, 1, 0, 0, 1, 1, 0, 1);
        plane.getMesh().addTextureCoords(new Vector2f(0, 0), new Vector2f(1, 0), new Vector2f(1, 1), new Vector2f(0, 1));
        plane.getMesh().setCalulateNormals(true);
        Material plane_Material = new Material(new Color(0, 0, 0, 255));
        plane.setMaterial(plane_Material);
        int[] indecies = {0, 1, 2, 0, 2, 3};
        plane.getMesh().addIndecies(indecies);
        plane.getMesh().setCalulateNormals(true);
        plane.getMesh().compile();

        float rot = 0;


        Vector3f lightPos = new Vector3f(-20, 10, 0);
        Vector3f lightCol = new Vector3f(1, 1, 1);

        Line line = new Line(new Vector3f(0, -100, 0), new Vector3f(0, 100, 0), new Material(Color.GREEN));
        line.build();
        Line line1 = new Line(new Vector3f(-100, 0, 0), new Vector3f(1000, 0, 0), new Material(Color.BLUE));
        line1.build();
        Line line2 = new Line(new Vector3f(0, 0, -100), new Vector3f(0, 0, 100), new Material(Color.RED));
        line2.build();

        Texture text = textureLoader.loadTexture("redwhite.png", TextureProperties.getDefaultInstance());

        /**
         * main loop
         */
        while (!display.isCloseRequested()) {

            Vector2f centerPosition = DisplayManager.getCenterPosition();
            rot += 0.1f;

            ///////////////////////// Mouse locked code /////////////////////////
            if (Input.isKeyDown(Input.KEY_ESCAPE))
                Input.setCursorGrabbed(false);

            if (Input.getMouseDown(0)) {
                Input.setMousePosition(centerPosition);
                Input.setCursorGrabbed(true);
            }

            if(Input.isKeyDown(Input.KEY_W))
                camera.move(camera.getTransform().getQ_rotation().getForward(), 0.1f);
            if(Input.isKeyDown(Input.KEY_S))
                camera.move(camera.getTransform().getQ_rotation().getForward(), -0.1f);
            if(Input.isKeyDown(Input.KEY_A))
                camera.move(camera.getTransform().getQ_rotation().getRight(), 0.1f);
            if(Input.isKeyDown(Input.KEY_D))
                camera.move(camera.getTransform().getQ_rotation().getLeft(), 0.1f);
            if (Input.isKeyDown(Input.KEY_SPACE))
                camera.move(camera.getTransform().getQ_rotation().getDown(), 0.1f);
            if (Input.isKeyDown(Input.KEY_LSHIFT))
                camera.move(camera.getTransform().getQ_rotation().getUp(), 0.1f);

            if(Input.isKeyDown(Input.KEY_UP)) {
                camera.rotate(camera.degreesToRadians(1), camera.getTransform().getQ_rotation().getRight());
            }
            if(Input.isKeyDown(Input.KEY_DOWN)) {
                camera.rotate(camera.degreesToRadians(1), camera.getTransform().getQ_rotation().getLeft());
            }
            if(Input.isKeyDown(Input.KEY_LEFT)) {
                camera.rotate(camera.degreesToRadians(1), Axis.Y_AXIS);
            }
            if(Input.isKeyDown(Input.KEY_RIGHT)) {
                camera.rotate(-camera.degreesToRadians(1), Axis.Y_AXIS);
            }

            if (Input.isCursorGrabbed()) {
                Vector3f deltaPos = Vector3fMath.subtract(Input.getMousePosition(), new Vector3f(centerPosition.getX(), centerPosition.getY(), 0));

                boolean rotY = deltaPos.getX() != 0;
                boolean rotX = deltaPos.getY() != 0;

                if (rotY)
                    camera.rotate(camera.degreesToRadians(-deltaPos.getX() * 0.1f), Axis.Y_AXIS);
                if (rotX)
                      camera.rotate(camera.degreesToRadians(deltaPos.getY() * 0.1f), camera.getTransform().getQ_rotation().getRight());
//                    camera.rotate(camera.degreesToRadians(-deltaPos.getY() * 0.3f), Axis.X_AXIS);
                if (rotY || rotX)
                    Input.setMousePosition(centerPosition);
            }

            Input.update();

            renderer.clearBuffers();

            // Render 3D stuff here
            transform.setRotation(new Vector3f(0, camera.degreesToRadians(rot), 0));
            camera.updateTransform();

            // Render the box
            shader.bind();
            transform.updateTransformation();
            shader.updateUniformMatrix4("modelMatrix", transform.getModelMatrix());
            shader.updateUniformMatrix4("projectionMatrix", camera.getProjectionMatrix());
            shader.updateUniformMatrix4("viewMatrix", camera.getViewMatrix());
            shader.updateUniformVector3f("light_Pos", lightPos);
            shader.updateUniformVector3f("light_Col", lightCol);
            shader.unBind();

            renderer.renderMesh(box, shader);

            textureShader.bind();
            planeTransform.updateTransformation();
            textureShader.updateUniformMatrix4("modelMatrix", planeTransform.getModelMatrix());
            textureShader.updateUniformMatrix4("projectionMatrix", camera.getProjectionMatrix());
            textureShader.updateUniformMatrix4("viewMatrix", camera.getViewMatrix());
            textureShader.updateUniformVector3f("light_Pos", lightPos);
            textureShader.updateUniformVector3f("light_Col", lightCol);
            textureShader.updateSampler2D("tex", 0);
            textureShader.unBind();

            text.bind();
            renderer.renderMesh(plane, textureShader);
            text.unbind();


//            line.render(camera, renderer);
//            line1.render(camera, renderer);
//            line2.render(camera, renderer);

            // Reset the Camera up for Orthographics
            camera.getTransform().updateTransformation();

            // Render 2D stuff here
//            irSeekerRenderer.render();

            display.update();
        }

        EventBusService.publish(new ShutDownEvent(true));
        box.destroy();
        plane.destroy();
        display.destroy();
    }
}
