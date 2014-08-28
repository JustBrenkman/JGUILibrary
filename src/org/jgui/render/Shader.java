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

import org.jgui.util.PathManager;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by ben on 26/08/14.
 */
public class Shader {

    private int vertexShaderID;

    private int fragmentShaderID;

    private int programID;

    private String vertexShaderLocation;

    private String fragmentShaderLocation;

    public Shader() {

    }

    public Shader(String vertexShader, String fragmentShader) {
        vertexShaderLocation = vertexShader;
        fragmentShaderLocation = fragmentShader;
    }

    public void loadShaders() {
        loadShaders(vertexShaderLocation, fragmentShaderLocation);
    }

    public void loadShaders(String vertexShader, String fragmentShader) {
        vertexShaderID = loadShader(vertexShader, GL20.GL_VERTEX_SHADER);
        fragmentShaderID = loadShader(fragmentShader, GL20.GL_FRAGMENT_SHADER);
    }

    public void compile() {
        programID = GL20.glCreateProgram();
        GL20.glAttachShader(programID, vertexShaderID);
        GL20.glAttachShader(programID, fragmentShaderID);

        GL20.glBindAttribLocation(programID, 0, "in_Position");
        GL20.glBindAttribLocation(programID, 1, "in_Color");
        GL20.glBindAttribLocation(programID, 2, "random_color");

        GL20.glLinkProgram(programID);

        GL20.glValidateProgram(programID);
    }

    public int loadShader(String location, int type) {
        int errorCheckValue = GL11.glGetError();

        StringBuilder shaderSource = new StringBuilder();
        int shaderID = 0;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(PathManager.getLocationPath() + "/resources/Shaders/" + location));
            String line;

            while ((line = reader.readLine()) != null) {
                shaderSource.append(line).append("\n");
            }

            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        shaderID = GL20.glCreateShader(type);
        GL20.glShaderSource(shaderID, shaderSource);
        GL20.glCompileShader(shaderID);

        return shaderID;
    }

    public void bind() {
        GL20.glUseProgram(programID);
    }

    public void unBind() {
        GL20.glUseProgram(0);
    }

    public void destroy() {
        GL20.glUseProgram(0);

        GL20.glDetachShader(programID, vertexShaderID);
        GL20.glDetachShader(programID, fragmentShaderID);
    }
}
