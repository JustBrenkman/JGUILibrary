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

import org.jgui.render.IRenderer;
import org.jgui.render.OpenGLRenderer;
import org.jgui.render.Shader;
import org.jgui.render.mesh.Mesh;
import org.jgui.scene.node.appearance.Material;
import org.jgui.scene.transform.Transform;
import org.jgui.util.Camera;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import java.awt.*;

/**
 * Created by ben on 28/10/14.
 */
public class Box {

    //Make private

    private int x = 0, y = 0, width = 0, height = 0;

    public Mesh boxMesh;

    public Transform transform;

    public byte[] indecies = {0, 1, 2, 2, 3, 0};

    public Color color;

    public Shader boxShader;

    public boolean isDirty = false;

    public Box() {

    }

    public Box(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        boxMesh = new Mesh();
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
        isDirty = true;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
        isDirty = true;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
        isDirty = true;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
        isDirty = true;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
        isDirty = true;
    }

    /**
     * Refreshes the verticies and colors of the box
     */
    public void refresh() {
        if (isDirty) {
            boxMesh.getMesh().clearVerticies();
            boxMesh.getMesh().addVerticies(new Vector3f(0, 0, 0), new Vector3f(getWidth(), 0, 0), new Vector3f(getWidth(), getHeight(), 0), new Vector3f(0, getHeight(), 0));

            boxMesh.getMesh().clearColors();
            boxMesh.getMesh().addColor(getColor());
            boxMesh.getMesh().addColor(getColor());
            boxMesh.getMesh().addColor(getColor());
            boxMesh.getMesh().addColor(getColor());

            boxMesh.getMesh().compile();

            isDirty = false;
        }
    }

    //Make private
    public void refreshTransform() {
        transform.setTranslation(new Vector3f(getX(), getY(), 0));
        transform.updateTransformation();
    }

    public void build() {
        transform = new Transform();
        transform.setTranslation(new Vector3f(getX(), getY(), 0));
        transform.setScale(1);
        transform.setRotation(new Vector3f(0, 0, 0));
        boxMesh.getMesh().addVerticies(new Vector3f(0, 0, 0), new Vector3f(getWidth(), 0, 0), new Vector3f(getWidth(), getHeight(), 0), new Vector3f(0, getHeight(), 0));
        boxMesh.setMaterial(new Material(getColor()));

        boxMesh.getMesh().addIndecies(indecies);

        boxMesh.getMesh().compile();

        boxShader = new Shader("2DBox/vs.glsl", "2DBox/fs.glsl");
        boxShader.loadShaders();
        boxShader.compile();

        boxShader.addUniform("modelMatrix");
        boxShader.addUniform("projectionMatrix");
        boxShader.addUniform("viewMatrix");
    }

    public void safeRender(Camera camera, OpenGLRenderer renderer) {
        boxShader.bind();
        refreshTransform();
        boxShader.updateUniformMatrix4("modelMatrix", transform.getModelMatrix());
        boxShader.updateUniformMatrix4("projectionMatrix", camera.getOrthoGraphicMatrix());
        boxShader.updateUniformMatrix4("viewMatrix", camera.getOrthoTransformation());
        boxShader.unBind();

//        renderer.renderMesh(boxMesh, boxShader);
        renderer.renderMesh(boxMesh, boxShader);
    }
}
