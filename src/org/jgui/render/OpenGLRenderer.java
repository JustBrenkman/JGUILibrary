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

package org.jgui.render;

import org.jgui.render.mesh.Mesh;
import org.jgui.scene.node.Element;
import org.jgui.util.FPS;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.lwjgl.opengl.GL11.*;

public class OpenGLRenderer implements IRenderer {

    int lastFPS = 0;
    private Logger logger = LoggerFactory.getLogger(OpenGLRenderer.class);
    // FPS monitoring
    private FPS fps;

    public static void clear() {
        glClear(GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void renderImage() {

    }

    @Override
    public void initialize() {
        glClearColor(43f / 255f, 43f / 255f, 43f / 255f, 0f);
        glViewport(0, 0, DisplayManager.defaultWidth, DisplayManager.defaultHeight);

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_DEPTH_TEST);

        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);

        glEnable(GL_TEXTURE_2D);

//        glPointSize(5f);
//        glPolygonMode(GL_FRONT_AND_BACK, GL_POINT);

        logger.info("Initialized OpenGL");
    }

    @Override
    public void renderLine() {

    }

    @Override
    public void renderRectangle() {

    }

    @Override
    public void renderTriangle() {

    }

    @Override
    public void renderCircle() {

    }

    @Override
    public void renderVBO() {

    }

    @Override
    public void setFrameBuffer() {

    }

    @Override
    public void renderMesh(Mesh mesh, Shader shader) {

        shader.bind();

        GL30.glBindVertexArray(mesh.getMesh().getVbo().getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        if (mesh.getMesh().getVbo().hasTextureCoords())
            GL20.glEnableVertexAttribArray(2);

        if (mesh.getMesh().getVbo().hasNormals())
            GL20.glEnableVertexAttribArray(3);

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, mesh.getMesh().getVbo().getIndexID());

//        GL11.glDrawElements(GL11.GL_TRIANGLES, mesh.getMesh().indexCount(), GL11.GL_UNSIGNED_BYTE, 0);
        GL11.glDrawElements(GL11.GL_TRIANGLES, mesh.getMesh().indexCount(), GL11.GL_UNSIGNED_INT, 0);

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);

        if (mesh.getMesh().getVbo().hasTextureCoords())
            GL20.glDisableVertexAttribArray(2);

        if (mesh.getMesh().getVbo().hasNormals())
            GL20.glDisableVertexAttribArray(3);

        GL30.glBindVertexArray(0);

        shader.unBind();
    }

    public void renderMesh(Mesh mesh, Shader shader, int renderMode) {
        shader.bind();

        GL30.glBindVertexArray(mesh.getMesh().getVbo().getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, mesh.getMesh().getVbo().getIndexID());

//        GL11.glDrawElements(renderMode, mesh.getMesh().indexCount(), GL11.GL_UNSIGNED_BYTE, 0);
        GL11.glDrawElements(renderMode, mesh.getMesh().indexCount(), GL11.GL_UNSIGNED_INT, 0);

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);

        GL30.glBindVertexArray(0);

        shader.unBind();
    }

    public void renderGeometryMesh(Mesh mesh, Shader shader, int renderMode) {
        shader.bind();

        GL30.glBindVertexArray(mesh.getMesh().getVbo().getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, mesh.getMesh().getVbo().getIndexID());

//        GL11.glDrawArrays(renderMode, 0, mesh.getMesh().vertexCount());
        GL11.glDrawElements(renderMode, mesh.getMesh().indexCount(), GL11.GL_UNSIGNED_INT, 0);

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);

        GL30.glBindVertexArray(0);

        shader.unBind();
    }

    @Override
    public void renderElement(Element element) {

    }

    @Override
    public void clearBuffers() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    @Override
    public void uploadVBO() {

    }

    @Override
    public void shutDown() {
        // TODO destroy everything
    }

    @Override
    public void enableScissor(int x, int y, int width, int height) {

    }

    @Override
    public void disableScissor() {

    }

    @Override
    public void setUpMonitoring() {
        fps = new FPS();

        // FPS control don't delete
        fps.initialize();
    }

    @Override
    public void updateMonitoring() {
        // Calculates whether or not to print the fps
        if (lastFPS != fps.getFPS()) {
//                System.out.println(fps.getFPS());
            lastFPS = fps.getFPS();
        }
    }

    public void listPossibleDisplaySizes() {
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
            //System.out.println(current.getWidth() + "x" + current.getHeight() + "x" +
            //current.getBitsPerPixel() + " " + current.getFrequency() + "Hz");
        }

    }
}
