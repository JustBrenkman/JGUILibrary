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

package org.experimental;

import org.jgui.render.OpenGLRenderer;
import org.jgui.render.Shader;
import org.jgui.scene.geometry.Box;
import org.jgui.scene.transform.Transform;
import org.jgui.util.Camera;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by ben on 07/11/14.
 */
public class LightTwoD extends Box {

    public LightTwoD(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void build() {
        transform = new Transform();
        transform.setTranslation(new Vector3f(getX(), getY(), 0));
        transform.setScale(1);
        transform.setRotation(new Vector3f(0, 0, 0));
        boxMesh.getMesh().addVerticies(new Vector3f(0, 0, 0), new Vector3f(getWidth(), 0, 0), new Vector3f(getWidth(), getHeight(), 0), new Vector3f(0, getHeight(), 0));
        boxMesh.getMesh().addColor(getColor());
        boxMesh.getMesh().addColor(getColor());
        boxMesh.getMesh().addColor(getColor());
        boxMesh.getMesh().addColor(getColor());

        boxMesh.getMesh().addIndecies(indecies);

        boxMesh.getMesh().compile();

        boxShader = new Shader("Experimental/2DLightBox/vs.glsl", "Experimental/2DLightBox/fs.glsl");
        boxShader.loadShaders();
        boxShader.compile();

        boxShader.addUniform("modelMatrix");
        boxShader.addUniform("projectionMatrix");
        boxShader.addUniform("viewMatrix");
        boxShader.addUniform("mouse_Pos");
    }

    public void safeRender(Camera camera, OpenGLRenderer renderer) {
        boxShader.bind();
        refreshTransform();
        boxShader.updateUniformMatrix4("modelMatrix", transform.getModelMatrix());
        boxShader.updateUniformMatrix4("projectionMatrix", camera.getOrthoGraphicMatrix());
        boxShader.updateUniformMatrix4("viewMatrix", camera.getTransform().getModelMatrix());
        boxShader.updateUniformVector3f("mouse_Pos", new Vector3f(Mouse.getX(), Mouse.getY(), 0));
        boxShader.unBind();

//        renderer.renderMesh(boxMesh, boxShader);
        renderer.renderMesh(boxMesh, boxShader);
    }
}
