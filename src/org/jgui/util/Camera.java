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

package org.jgui.util;

import org.jgui.scene.transform.Transform;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import java.nio.FloatBuffer;

/**
 * Created by ben on 27/08/14.
 */
public class Camera {

    private Transform transform = new Transform();

    private Matrix4f projectionMatrix = null;
    private Matrix4f orthoGraphicMatrix = null;

    private float fov = 60f;

    private float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();

    float near_plane = 0.1f;

    float far_plane = 800f;

    float y_scale = this.coTangent(this.degreesToRadians(fov / 2f));

    float x_scale = y_scale / aspectRatio;

    float frustum_length = far_plane - near_plane;

    // Setup view matrix
    Matrix4f viewMatrix = new Matrix4f();

    // Create a FloatBuffer with the proper size to store our matrices later
    FloatBuffer matrix44Buffer = BufferUtils.createFloatBuffer(16);

    public Camera() {
        transform.setTranslation(new Vector3f(0, 0, 0));
        projectionMatrix = new Matrix4f();
        orthoGraphicMatrix = new Matrix4f();
    }

    private float coTangent(float angle) {
        return (float)(1f / Math.tan(angle));
    }

    private float degreesToRadians(float degrees) {
        return degrees * (float)(Math.PI / 180d);
    }

    public void init() {
        projectionMatrix.m00 = x_scale;
        projectionMatrix.m11 = y_scale;
        projectionMatrix.m22 = -((far_plane + near_plane) / frustum_length);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * near_plane * far_plane) / frustum_length);
        projectionMatrix.m33 = 0;

//        orthoGraphicMatrix = Ortho(-Display.getWidth() / 2, Display.getWidth() / 2, Display.getHeight() / 2, -Display.getHeight() / 2, 0, 100);
        orthoGraphicMatrix = Ortho(0, Display.getWidth(), Display.getHeight(), 0, 0, 100);
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public Matrix4f getOrthoGraphicMatrix() {
        return orthoGraphicMatrix;
    }

    public Transform getTransform() {
        return transform;
    }

    public Matrix4f orthoMatrix(float left, float right, float bottom, float top, float near, float far) {
        Matrix4f matrix = new Matrix4f();
        float x_orth = 2 / (right - left);
        float y_orth = 2 / (top - bottom);
        float z_orth = -2 / (far - near);

        float tx = -(right + left) / (right - left);
        float ty = -(top + bottom) / (top - bottom);
        float tz = -(far + near) / (far - near);

        matrix.m00 = x_orth;
        matrix.m10 = 0;
        matrix.m20 = 0;
        matrix.m30 = 0;
        matrix.m01 = 0;
        matrix.m11 = y_orth;
        matrix.m21 = 0;
        matrix.m31 = 0;
        matrix.m02 = 0;
        matrix.m12 = 0;
        matrix.m22 = z_orth;
        matrix.m32 = 0;
        matrix.m03 = tx;
        matrix.m13 = ty;
        matrix.m23 = tz;
        matrix.m33 = 1;

        return matrix;
    }

    public static Matrix4f identityMat() {
        Matrix4f mat = new Matrix4f();
        mat.m00 = 1;
        mat.m01 = 0;
        mat.m02 = 0;
        mat.m03 = 0;
        mat.m10 = 0;
        mat.m11 = 1;
        mat.m12 = 0;
        mat.m13 = 0;
        mat.m20 = 0;
        mat.m21 = 0;
        mat.m22 = 1;
        mat.m23 = 0;
        mat.m30 = 0;
        mat.m31 = 0;
        mat.m32 = 0;
        mat.m33 = 1;
        return mat;
    }

    /**
     *
     * @param x - left
     * @param y - bottom
     * @param width - right
     * @param height - top
     * @param near - near
     * @param far - far
     * @return
     */
    public Matrix4f orthograpic(float x, float y, float width, float height, float near, float far) {

        Matrix4f mat = identityMat();
        float x_orth = 2 / (width - x);
        float y_orth = 2 / (height - y);
        float z_orth = -2 / (far - near);

        float tx = -(width + x) / (width - x);
        float ty = -(height + y) / (height - y);
        float tz = -(far + near) / (far - near);

        mat.m00 = x_orth;
        mat.m01 = 0;
        mat.m02 = 0;
        mat.m03 = 0;
        mat.m10 = 0;
        mat.m11 = y_orth;
        mat.m12 = 0;
        mat.m13 = 0;
        mat.m20 = 0;
        mat.m21 = 0;
        mat.m22 = z_orth;
        mat.m23 = 0;
        mat.m30 = tx;
        mat.m31 = ty;
        mat.m32 = tz;
        mat.m33 = 1;

        return mat;
    }

    public Matrix4f Ortho(float left, float right, float top, float bottom, float znear, float zfar) {

        Matrix4f mat = identityMat();
        
        mat.m00 = 2.0f/(right-left);
        mat.m01 = 0.0f;
        mat.m02 = 0.0f;
        mat.m03 = 0.0f;

        mat.m10 = 0.0f;
        mat.m11 = 2.0f/(top-bottom);
        mat.m12 = 0.0f;
        mat.m13 = 0.0f;

        mat.m20 = 0.0f;
        mat.m21 = 0.0f;
        mat.m22 = -2.0f/(zfar-znear);
        mat.m23 = 0.0f;

        mat.m30 = -(right+left)/(right-left);
        mat.m31 = -(top+bottom)/(top-bottom);
        mat.m32 = -(zfar+znear)/(zfar-znear);
        mat.m33 = 1.0f;

        return mat;
    }
}
