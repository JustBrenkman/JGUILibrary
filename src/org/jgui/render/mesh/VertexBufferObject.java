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

import org.jgui.util.StringColors;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * Created by ben on 26/08/14.
 */
public class VertexBufferObject {

    private float[] vertices;

    private float[] colors;

    private int[] indecies;

    private float[] normals;

    private float[] textureCoords;

    private int vboID;

    private int cboID;

    private int iboID;

    private int nboID;

    private int tboID;

    private int vaoID;

    private FloatBuffer vertexBuffer;

    private FloatBuffer colorBuffer;

    private IntBuffer indexBuffer;

    private FloatBuffer normalBuffer;

    private FloatBuffer textureBuffer;

    private boolean hasNormals = false;

    private boolean hasTextureCoords = false;

    // Random Coolness buffer ID
    private int rcoID;

    public VertexBufferObject(float[] vertices, float[] colors, int[] indecies) {
        setVertices(vertices);
        setIndecies(indecies);
        setColors(colors);
    }

    public VertexBufferObject(float[] vertices, float[] colors, float[] normals, int[] indecies) {
        this(vertices, colors, indecies);
        setNormals(normals);
        setHasNormals(true);
    }

    public VertexBufferObject() {
    }

    public VertexBufferObject(float[] vertices) {
        setVertices(vertices);
    }

    public float[] getVertices() {
        return vertices;
    }

    public void setVertices(float[] vertices) {
        this.vertices = vertices;
    }

    public float[] getColors() {
        return colors;
    }

    public void setColors(float[] colors) {
        this.colors = colors;
    }

    public int[] getIndecies() {
        return indecies;
    }

    public void setIndecies(int[] indecies) {
        this.indecies = indecies;
    }

    public float[] getNormals() {
        return normals;
    }

    public void setNormals(float[] normals) {
        this.normals = normals;
    }

    public int getVboID() {
        return vboID;
    }

    public int getVaoID() {
        return vaoID;
    }

    public int getIndexID() {
        return iboID;
    }

    public boolean hasNormals() {
        return hasNormals;
    }

    private void setHasNormals(boolean hasNormals) {
        this.hasNormals = hasNormals;
    }

    public float[] getTextureCoords() {
        return textureCoords;
    }

    public void setTextureCoords(float[] textureCoords) {
        this.textureCoords = textureCoords;
        hasTextureCoords = true;
    }

    public boolean hasTextureCoords() {
        return hasTextureCoords;
    }

    public void setHasTextureCoords(boolean hasTextureCoords) {
        this.hasTextureCoords = hasTextureCoords;
    }

    public void createBuffers() {
        vertexBuffer = BufferUtils.createFloatBuffer(vertices.length);
        vertexBuffer.put(getVertices());
        vertexBuffer.flip();

        colorBuffer = BufferUtils.createFloatBuffer(colors.length);
        colorBuffer.put(getColors());
        colorBuffer.flip();

        indexBuffer = BufferUtils.createIntBuffer(indecies.length);
        indexBuffer.put(indecies);
        indexBuffer.flip();

        if (hasNormals) {
            normalBuffer = BufferUtils.createFloatBuffer(normals.length);
            normalBuffer.put(getNormals());
            normalBuffer.flip();
        }

        if (hasTextureCoords) {
            StringColors.printl(StringColors.ANSI_CYAN, "Has texture coords");
            textureBuffer = BufferUtils.createFloatBuffer(textureCoords.length);
            textureBuffer.put(textureCoords);
            textureBuffer.flip();
        }
    }


    /**
     * Sends Vertex Buffer Data to the GPU
     */
    public void uploadBuffer() {

        vaoID = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoID);


        /**
         * Vertex Binding
         */
        vboID = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexBuffer, GL15.GL_STATIC_DRAW);

        GL20.glVertexAttribPointer(0, 4, GL11.GL_FLOAT, false, 0, 0);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        /**
         * Color Binding
         */
        cboID = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, cboID);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, colorBuffer, GL15.GL_STATIC_DRAW);

        GL20.glVertexAttribPointer(1, 4, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        if (hasTextureCoords) {
            tboID = GL15.glGenBuffers();
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, tboID);
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, textureBuffer, GL15.GL_STATIC_DRAW);

            GL20.glVertexAttribPointer(2, 2, GL11.GL_FLOAT, false, 0, 0);
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        }

        if (hasNormals) {
            nboID = GL15.glGenBuffers();
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, nboID);
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, normalBuffer, GL15.GL_STATIC_DRAW);

            GL20.glVertexAttribPointer(3, 4, GL11.GL_FLOAT, false, 0, 0);
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

//            GL30.glBindVertexArray(0);
        }

        // Bind to buffer 0
        GL30.glBindVertexArray(0);

        /**
         * Index binding
         */
        iboID = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, iboID);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    public void updateBuffer() {}

    public void destroy() {
        GL30.glBindVertexArray(vaoID);
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL20.glDisableVertexAttribArray(3);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL15.glDeleteBuffers(vboID);
        GL15.glDeleteBuffers(cboID);
        GL15.glDeleteBuffers(tboID);
        GL15.glDeleteBuffers(nboID);

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL15.glDeleteBuffers(iboID);

        GL30.glBindVertexArray(0);
        GL30.glDeleteVertexArrays(vaoID);
    }
}
