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

package org.jgui.mesh;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ben on 26/08/14.
 */
public class MeshData {

    private List<Vertex2f> vertices = new ArrayList<>();
    private List<Color> colors = new ArrayList<>();
    private List<Byte> indecies = new ArrayList<>();

    private VertexBufferObject vbo;

    public void addVertex(Vertex2f vertex2f) {
        vertices.add(vertex2f);
    }

    public void addVerticies(Vertex2f... v) {
        for (int i = 0; i < v.length; i++) {
            vertices.add(v[i]);
        }
    }

    public void addVerticies(float... v) {
        for (int i = 0; i < v.length; i += 2) {
            vertices.add(new Vertex2f(v[i / 2], v[(i / 2) + 1]));
        }
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

    public float[] getColors() {
        float[] ver = new float[colors.size() * 4];

        for (int i = 0; i < ver.length; i += 4) {
            ver[i] = colors.get(i / 4).getRed() / 255f;
            ver[i + 1] = colors.get(i / 4).getGreen() / 255f;
            ver[i + 2] = colors.get(i / 4).getBlue() / 255f;
            ver[i + 3] = colors.get(i / 4).getAlpha() / 255f;

            System.out.println("" + ver[i] + ", " + ver[i + 1] + ", " + ver[i + 2] + ", " + ver[i + 3]);
        }

        return ver;
    }

    public void addIndex() {

    }

    public void addIndecies(byte[] indexs) {
        for (int i = 0; i < indexs.length; i++) {
            indecies.add(indexs[i]);
        }
    }

    public byte[] getIndecies() {
        byte[] in = new byte[indecies.size()];

        for (int i = 0; i < indecies.size(); i++) {
            in[i] = indecies.get(i);
            System.out.println(in[i]);
        }

        return in;
    }

    public float[] getVerticies() {
        float[] ver = new float[vertices.size() * 2];

        for (int i = 0; i < ver.length; i += 2) {
            ver[i] = vertices.get(i / 2).getX();
            ver[i + 1] = vertices.get(i / 2).getY();

            System.out.println("" + ver[i] + ", " + ver[i + 1]);
        }

        return ver;
    }

    public List<Vertex2f> getVertices() {
        return vertices;
    }

    public int indexCount() {
        return indecies.size();
    }

    public void compile() {
        vbo = new VertexBufferObject(getVerticies(), getColors(), getIndecies());
        vbo.createBuffers();
        vbo.uploadBuffer();
    }

    public VertexBufferObject getVbo() {
        return vbo;
    }

    public int vertexCount() {
        return vertices.size() * 2;
    }
}
