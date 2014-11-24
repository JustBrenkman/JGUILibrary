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

/**
 * Created by ben on 20/11/14.
 */
import org.jgui.scene.transform.Vector3fMath;
import org.jgui.util.Vector.Vector2f;
import org.jgui.util.Vector.Vector3f;

import java.util.ArrayList;

public class IndexedModel
{
    private ArrayList<Vector3f> m_positions;
    private ArrayList<Vector2f> m_texCoords;
    private ArrayList<Vector3f> m_normals;
    private ArrayList<Vector3f> m_tangents;
    private ArrayList<Integer>  m_indices;

    public IndexedModel()
    {
        m_positions = new ArrayList<Vector3f>();
        m_texCoords = new ArrayList<Vector2f>();
        m_normals = new ArrayList<Vector3f>();
        m_tangents = new ArrayList<Vector3f>();
        m_indices = new ArrayList<Integer>();
    }

    public void CalcNormals()
    {
        for(int i = 0; i < m_indices.size(); i += 3)
        {
            int i0 = m_indices.get(i);
            int i1 = m_indices.get(i + 1);
            int i2 = m_indices.get(i + 2);

            Vector3f v1 = m_positions.get(i1).Sub(m_positions.get(i0));
            Vector3f v2 = m_positions.get(i2).Sub(m_positions.get(i0));

            Vector3f normal = v1.Cross(v2).Normalized();

            m_normals.get(i0).Set(m_normals.get(i0).Add(normal));
            m_normals.get(i1).Set(m_normals.get(i1).Add(normal));
            m_normals.get(i2).Set(m_normals.get(i2).Add(normal));
        }

        for(int i = 0; i < m_normals.size(); i++)
            m_normals.get(i).Set(m_normals.get(i).Normalized());
    }

    public void CalcTangents()
    {
        for(int i = 0; i < m_indices.size(); i += 3)
        {
            int i0 = m_indices.get(i);
            int i1 = m_indices.get(i + 1);
            int i2 = m_indices.get(i + 2);

            Vector3f edge1 = m_positions.get(i1).Sub(m_positions.get(i0));
            Vector3f edge2 = m_positions.get(i2).Sub(m_positions.get(i0));

            float deltaU1 = m_texCoords.get(i1).GetX() - m_texCoords.get(i0).GetX();
            float deltaV1 = m_texCoords.get(i1).GetY() - m_texCoords.get(i0).GetY();
            float deltaU2 = m_texCoords.get(i2).GetX() - m_texCoords.get(i0).GetX();
            float deltaV2 = m_texCoords.get(i2).GetY() - m_texCoords.get(i0).GetY();

            float dividend = (deltaU1*deltaV2 - deltaU2*deltaV1);
            //TODO: The first 0.0f may need to be changed to 1.0f here.
            float f = dividend == 0 ? 0.0f : 1.0f/dividend;

            Vector3f tangent = new Vector3f(0,0,0);
            tangent.SetX(f * (deltaV2 * edge1.GetX() - deltaV1 * edge2.GetX()));
            tangent.SetY(f * (deltaV2 * edge1.GetY() - deltaV1 * edge2.GetY()));
            tangent.SetZ(f * (deltaV2 * edge1.GetZ() - deltaV1 * edge2.GetZ()));

            m_tangents.get(i0).Set(m_tangents.get(i0).Add(tangent));
            m_tangents.get(i1).Set(m_tangents.get(i1).Add(tangent));
            m_tangents.get(i2).Set(m_tangents.get(i2).Add(tangent));
        }

        for(int i = 0; i < m_tangents.size(); i++)
            m_tangents.get(i).Set(m_tangents.get(i).Normalized());
    }

    public ArrayList<Vector3f> GetPositions() { return m_positions; }
    public ArrayList<Vector2f> GetTexCoords() { return m_texCoords; }
    public ArrayList<Vector3f> GetNormals() { return m_normals; }
    public ArrayList<Vector3f> GetTangents() { return m_tangents; }
    public ArrayList<Integer>  GetIndices() { return m_indices; }
}