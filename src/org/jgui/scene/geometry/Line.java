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
import org.jgui.scene.node.appearance.Material;
import org.jgui.scene.transform.Transform;
import org.jgui.util.Camera;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by ben on 12/11/14.
 */
public class Line {

    private Mesh mesh;

    private Transform transform;

    private Vector3f point1;

    private Vector3f point2;

    private Shader shader;

    private Material material;

    public Line(Vector3f point1, Vector3f point2, Material material) {
        this.point1 = point1;
        this.point2 = point2;
        this.material = material;

        transform = new Transform();
    }

    public Transform getTransform() {
        return transform;
    }

    public void setTransform(Transform transform) {
        this.transform = transform;
    }

    public Vector3f getPoint1() {
        return point1;
    }

    public void setPoint1(Vector3f point1) {
        this.point1 = point1;
    }

    public Vector3f getPoint2() {
        return point2;
    }

    public void setPoint2(Vector3f point2) {
        this.point2 = point2;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public void build() {
        mesh = new Mesh();
        transform.setTranslation(new Vector3f(0, 0, 0));
        transform.setScale(1);
        transform.setRotation(new Vector3f(0, 0, 0));
        mesh.getMesh().addVerticies(point1, point2);
//        mesh.getMesh().addVerticies(new Vector3f(0, 0, 0), new Vector3f(100, 0, 0));
        mesh.setMaterial(material);
        byte[] ind = {0, 1};
        mesh.getMesh().addIndecies(ind);
        mesh.getMesh().compile();

        shader = new Shader("2DBox/vs.glsl", "2DBox/fs.glsl");
        shader.loadShaders();
        shader.compile();

        shader.addUniform("modelMatrix");
        shader.addUniform("projectionMatrix");
        shader.addUniform("viewMatrix");
    }

    public void safeRender(Camera camera, OpenGLRenderer renderer) {
        shader.bind();
        transform.updateTransformation();
        shader.updateUniformMatrix4("modelMatrix", transform.getModelMatrix());
        shader.updateUniformMatrix4("projectionMatrix", camera.getOrthoGraphicMatrix());
        shader.updateUniformMatrix4("viewMatrix", camera.getOrthoTransformation());
        shader.unBind();

        renderer.renderMesh(mesh, shader, GL11.GL_LINES);
    }
}
