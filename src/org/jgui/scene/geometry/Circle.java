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

package org.jgui.scene.geometry;

import org.jgui.render.OpenGLRenderer;
import org.jgui.render.Shader;
import org.jgui.render.mesh.Mesh;
import org.jgui.scene.transform.Transform;
import org.jgui.util.Camera;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import java.awt.*;

/**
 * Created by ben on 01/11/14.
 */
public class Circle {

    private int x = 0, y = 0, radius = 1;

    private Mesh mesh;

    private Transform transform;

    private Color color;

    private Shader circleShader;

    private int numOfSlices = 9;

//    private byte[] indecies = {0, 1, 2, 0, 2, 3, 0, 3, 4, 0, 4, 5, 0, 5, 6, 0, 6, 7, 0, 7, 8, 0, 8, 9, 0, 9, 10, 0, 10, 11};
    private int[] indecies = {0};
    private int[] indeciesForPionts = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
//    private byte[] indecies = {0, 1, 2, 0, 2, 3, 0, 3, 4, 0, 4, 5, 0, 5, 6, 0, 7, 8, 0, 9, 10, 0, 10, 1};

    public Circle(int radius) {
        this.radius = radius;
    }

    public Circle(int x, int y, int radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void refresh() {

    }

    public void build() {

        transform = new Transform();
        transform.setTranslation(new Vector3f(getX(), getY(), 0));
        transform.setScale(1);
        transform.setRotation(new Vector3f(0, 0, 0));

        mesh = new Mesh();

        mesh.getMesh().addVertex(new Vector3f(0, 0, 0));

        // Add verticies
//        mesh.getMesh().addVertex(new Vector3f(getX(), getY(), 0));
//        mesh.getMesh().addColor(getColor());
//        for (int i = 1; i < numOfSlices + 1; i++) {
//            float theta = (float) Math.PI * 2.0f * (float) i / numOfSlices;
//            mesh.getMesh().addVertex(new Vector3f((float) (radius * Math.cos(theta)) + getX(), (float) (radius * Math.sin(theta)) + getY(), 0));
//            mesh.getMesh().addColor(getColor());
//        }
//        mesh.getMesh().addVertex(new Vector3f((float) (radius * Math.cos((float) Math.PI * 2.0f / numOfSlices)) + getX(), (float) (radius * Math.sin((float) Math.PI * 2.0f / numOfSlices)) + getY(), 0));
        mesh.getMesh().addColor(getColor());
        //////////////////////////////////////////////

        //indecies
//        byte[] ind = new byte[numOfSlices * 3];
//        Byte[] indb = new Byte[numOfSlices * 3];
//
//        int j = 0;
//
//        for (byte i = 0; i < numOfSlices; i += 3) {
//
//            j++;
//
//            ind[i] = 0;
//            ind[j] = (byte) (i + 0b00000001);
//            ind[j + 1] = (byte) (i + 0b00000010);
//        }
//
//        for (int i = 0; i < ind.length; i++) {
//            System.out.println(ind[i]);
//        }
//
//        mesh.getMesh().addIndecies(indecies);
        mesh.getMesh().addIndecies(indeciesForPionts);
        //////////////////////////////////////////////

        mesh.getMesh().compile();

        circleShader = new Shader("GeometryCircle/vs.glsl", "GeometryCircle/fs.glsl", "GeometryCircle/gs.glsl");
//        circleShader = new Shader("2DBox/vs.glsl", "2DBox/fs.glsl");
        circleShader.loadShaders();
        circleShader.compile();

        circleShader.addUniform("modelMatrix");
        circleShader.addUniform("projectionMatrix");
        circleShader.addUniform("viewMatrix");
        circleShader.addUniform("radius");
    }

    private void refreshTransform() {
        transform.setTranslation(new Vector3f(getX(), getY(), 0));
        transform.updateTransformation();
    }

    public void safeRender(Camera camera, OpenGLRenderer renderer) {
        circleShader.bind();
        refreshTransform();
        circleShader.updateUniformMatrix4("modelMatrix", transform.getModelMatrix());
        circleShader.updateUniformMatrix4("projectionMatrix", camera.getOrthoGraphicMatrix());
        circleShader.updateUniformMatrix4("viewMatrix", camera.getOrthoTransformation());
        circleShader.updateUniformf("radius", getRadius());
        circleShader.unBind();

//        renderer.renderMesh(boxMesh, boxShader);
        renderer.renderGeometryMesh(mesh, circleShader, GL11.GL_POINTS);
    }
}
