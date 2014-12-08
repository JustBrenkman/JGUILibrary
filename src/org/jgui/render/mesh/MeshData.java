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

package org.jgui.render.mesh;

import org.jgui.scene.node.appearance.Material;
import org.jgui.scene.transform.Vector3fMath;
import org.lwjgl.util.vector.Vector3f;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ben on 26/08/14.
 *
 * JGUILibrary
 */
public class MeshData {

    private List<Vector3f> vertices = new ArrayList<>();
    private List<Color> colors = new ArrayList<>();
    private List<Integer> indecies = new ArrayList<>();
    private List<Vector3f> normals = new ArrayList<>();
    private Material material;

    private boolean calulateNormals = false;

    private float[] verticies;

    private VertexBufferObject vbo;

    private boolean flagDirty = true;

    public void addVertex(Vector3f vector3f) {
        vertices.add(vector3f);
    }

    public void addVerticies(Vector3f... v) {
        for (int i = 0; i < v.length; i++) {
            vertices.add(v[i]);
        }
    }

    public void clearVerticies() {
        vertices.clear();
    }

    /**
     * Each color must be 4 floats long red, green, blue, alpha
     * @param c - r, g, b, a + next floats
     */
    public void addColors(float... c) {
        for (int i = 0; i < c.length; i += 4) {
            colors.add(new Color(c[i], c[(i) + 1], c[(i) + 2], c[(i) + 3]));
        }
    }

    public void clearColors() {
        colors.clear();
    }

    public void addColor(Color col) {
        colors.add(col);
    }

    public float[] getColors() {
        float[] ver;
        if (material != null) {
            ver = new float[vertices.size() * 4];
            for (int i = 0; i < ver.length; i += 4) {
                ver[i] = material.getColor().getRed() / 255f;
                ver[i + 1] = material.getColor().getGreen() / 255f;
                ver[i + 2] = material.getColor().getBlue() / 255f;
                ver[i + 3] = material.getColor().getAlpha() / 255f;
            }
        } else {
            ver = new float[colors.size() * 4];

            for (int i = 0; i < ver.length; i += 4) {
                ver[i] = colors.get(i / 4).getRed() / 255f;
                ver[i + 1] = colors.get(i / 4).getGreen() / 255f;
                ver[i + 2] = colors.get(i / 4).getBlue() / 255f;
                ver[i + 3] = colors.get(i / 4).getAlpha() / 255f;
            }
        }
        return ver;
    }

    public void addIndex(int index) {
        indecies.add(index);
    }

    public void addIndecies(int... indexs) {
        for (int i = 0; i < indexs.length; i++) {
            indecies.add(indexs[i]);
        }
    }

    public int[] getIndecies() {
        int[] in = new int[indecies.size()];

        for (int i = 0; i < indecies.size(); i++) {
            in[i] = indecies.get(i);
        }

        return in;
    }

    public float[] getVerticies() {
        float ver[] = new float[vertices.size() * 4];

        for (int i = 0; i < vertices.size() * 4; i += 4) {
            ver[i] = vertices.get(i / 4).getX();
            ver[i + 1] = vertices.get(i / 4).getY();
            ver[i + 2] = vertices.get(i / 4).getZ();
            ver[i + 3] = 1;

//            System.out.println(ver[i] + ", " + ver[i + 1] + ", " + ver[i + 2] + ", " + ver[i + 3]);
        }

        return ver;
    }

    public void setVertices(List<Vector3f> vertices) {
        this.vertices = vertices;
    }

    public float[] getNormals() {
        float nor[] = new float[normals.size() * 4];

        for (int i = 0; i < normals.size() * 4; i += 4) {
            nor[i] = normals.get(i / 4).getX();
            nor[i + 1] = normals.get(i / 4).getY();
            nor[i + 2] = normals.get(i / 4).getZ();
            nor[i + 3] = 0;
            System.out.println(nor[i] + ", " + nor[i + 1] + ", " + nor[i + 2]);
        }

        return nor;
    }

    public void addNormals(Vector3f... n) {
        for (int i = 0; i < n.length; i++) {
            normals.add(n[i]);
        }
    }

    public void addNormal(Vector3f n) {
        normals.add(n);
    }

    public void setNormals(List<Vector3f> normals) {
        this.normals = normals;
    }

    public boolean isCalulateNormals() {
        return calulateNormals;
    }

    public void setCalulateNormals(boolean calulateNormals) {
        this.calulateNormals = calulateNormals;
    }

    public List<Vector3f> getVertices() {
        return vertices;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public int indexCount() {
        return indecies.size();
    }

    public void compile() {
        if (calulateNormals) {
            calulateNormals();
            vbo = new VertexBufferObject(getVerticies(), getColors(), getNormals(), getIndecies());
        } else {
            vbo = new VertexBufferObject(getVerticies(), getColors(), getIndecies());
        }
        vbo.createBuffers();
        vbo.uploadBuffer();
    }

    public VertexBufferObject getVbo() {
        return vbo;
    }

    public int vertexCount() {
        return vertices.size() * 2;
    }

    public void calulateNormals() {

        if (normals.size() < indecies.size()) {
            for (int i = 0; i < indecies.size(); i++) {
                normals.add(new Vector3f());
            }
        }

        for (int i = 0; i < indecies.size(); i += 3) {
            int i0 = indecies.get(i);
            int i1 = indecies.get(i + 1);
            int i2 = indecies.get(i + 2);

            Vector3f v1 = Vector3fMath.subtract(vertices.get(i1), vertices.get(i0));
            Vector3f v2 = Vector3fMath.subtract(vertices.get(i2), vertices.get(i0));

            Vector3f normal = Vector3fMath.cross(v1, v2);
            normal.normalise(normal);

//            System.out.println("Index number: " + i0);
//            System.out.println("Index number: " + i1);
//            System.out.println("Index number: " + i2);
//            System.out.println("normals size: " + normals.size());

            normals.set(i0, normal);
            normals.set(i1, normal);
            normals.set(i2, normal);
        }
    }
}
