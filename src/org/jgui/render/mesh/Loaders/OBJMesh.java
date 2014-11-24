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

package org.jgui.render.mesh.Loaders;

/**
 * Created by ben on 20/11/14.
 */

import org.jgui.render.mesh.IndexedModel;
import org.jgui.render.mesh.MeshResource;
import org.jgui.render.mesh.OBJModel;
import org.jgui.render.mesh.Vertex;
import org.jgui.util.PathManager;
import org.jgui.util.Util;
import org.jgui.util.Vector.Vector3f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

import java.util.ArrayList;
import java.util.HashMap;

public class OBJMesh
{
    private static HashMap<String, MeshResource> s_loadedModels = new HashMap<String, MeshResource>();
    private MeshResource m_resource;
    private String       m_fileName;

    public OBJMesh(String fileName)
    {
        this.m_fileName = fileName;
        MeshResource oldResource = s_loadedModels.get(fileName);

        if(oldResource != null)
        {
            m_resource = oldResource;
            m_resource.AddReference();
        }
        else
        {
            LoadMesh(fileName);
            s_loadedModels.put(fileName, m_resource);
        }
    }

    public OBJMesh(Vertex[] vertices, int[] indices)
    {
        this(vertices, indices, false);
    }

    public OBJMesh(Vertex[] vertices, int[] indices, boolean calcNormals)
    {
        m_fileName = "";
        AddVertices(vertices, indices, calcNormals);
    }

    @Override
    protected void finalize()
    {
        if(m_resource.RemoveReference() && !m_fileName.isEmpty())
        {
            s_loadedModels.remove(m_fileName);
        }
    }

    private void AddVertices(Vertex[] vertices, int[] indices, boolean calcNormals)
    {
        if(calcNormals)
        {
            CalcNormals(vertices, indices);
        }

        m_resource = new MeshResource(indices.length);

        glBindBuffer(GL_ARRAY_BUFFER, m_resource.GetVbo());
        glBufferData(GL_ARRAY_BUFFER, Util.CreateFlippedBuffer(vertices), GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, m_resource.GetIbo());
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, Util.CreateFlippedBuffer(indices), GL_STATIC_DRAW);
    }

    public void Draw()
    {
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        glEnableVertexAttribArray(3);

        glBindBuffer(GL_ARRAY_BUFFER, m_resource.GetVbo());
        glVertexAttribPointer(0, 3, GL_FLOAT, false, Vertex.SIZE * 4, 0);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, Vertex.SIZE * 4, 12);
        glVertexAttribPointer(2, 3, GL_FLOAT, false, Vertex.SIZE * 4, 20);
        glVertexAttribPointer(3, 3, GL_FLOAT, false, Vertex.SIZE * 4, 32);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, m_resource.GetIbo());
        glDrawElements(GL_TRIANGLES, m_resource.GetSize(), GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glDisableVertexAttribArray(3);
    }

    private void CalcNormals(Vertex[] vertices, int[] indices)
    {
        for(int i = 0; i < indices.length; i += 3)
        {
            int i0 = indices[i];
            int i1 = indices[i + 1];
            int i2 = indices[i + 2];

            Vector3f v1 = vertices[i1].GetPos().Sub(vertices[i0].GetPos());
            Vector3f v2 = vertices[i2].GetPos().Sub(vertices[i0].GetPos());

            Vector3f normal = v1.Cross(v2).Normalized();

            vertices[i0].SetNormal(vertices[i0].GetNormal().Add(normal));
            vertices[i1].SetNormal(vertices[i1].GetNormal().Add(normal));
            vertices[i2].SetNormal(vertices[i2].GetNormal().Add(normal));
        }

        for(int i = 0; i < vertices.length; i++)
            vertices[i].SetNormal(vertices[i].GetNormal().Normalized());
    }

    private OBJMesh LoadMesh(String fileName)
    {
//        String[] splitArray = fileName.split("\\.");
//        String ext = splitArray[splitArray.length - 1];
//
//        if(!ext.equals("obj"))
//        {
//            System.err.println("Error: '" + ext + "' file format not supported for mesh data.");
//            new Exception().printStackTrace();
//            System.exit(1);
//        }

        OBJModel test = new OBJModel(PathManager.getLocationPath() + "/resources/Models/" + fileName + ".obj");
        IndexedModel model = test.ToIndexedModel();

        ArrayList<Vertex> vertices = new ArrayList<Vertex>();

        for(int i = 0; i < model.GetPositions().size(); i++)
        {
            vertices.add(new Vertex(model.GetPositions().get(i),
                    model.GetTexCoords().get(i),
                    model.GetNormals().get(i),
                    model.GetTangents().get(i)));
        }

        Vertex[] vertexData = new Vertex[vertices.size()];
        vertices.toArray(vertexData);

        Integer[] indexData = new Integer[model.GetIndices().size()];
        model.GetIndices().toArray(indexData);

        AddVertices(vertexData, Util.ToIntArray(indexData), false);

        return this;
    }
}