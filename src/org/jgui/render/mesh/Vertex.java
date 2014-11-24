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

import org.jgui.util.Vector.Vector2f;
import org.jgui.util.Vector.Vector3f;

/**
 * Created by ben on 20/11/14.
 */
public class Vertex
{
    public static final int SIZE = 11;

    private Vector3f m_pos;
    private Vector2f m_texCoord;
    private Vector3f m_normal;
    private Vector3f m_tangent;

    public Vertex(Vector3f pos)
    {
        this(pos, new Vector2f(0,0));
    }

    public Vertex(Vector3f pos, Vector2f texCoord)
    {
        this(pos, texCoord, new Vector3f(0,0,0));
    }

    public Vertex(Vector3f pos, Vector2f texCoord, Vector3f normal)
    {
        this(pos, texCoord, normal, new Vector3f(0,0,0));
    }

    public Vertex(Vector3f pos, Vector2f texCoord, Vector3f normal, Vector3f tangent)
    {
        this.m_pos = pos;
        this.m_texCoord = texCoord;
        this.m_normal = normal;
        this.m_tangent = tangent;
    }

    public Vector3f GetTangent() {
        return m_tangent;
    }

    public void SetTangent(Vector3f tangent) {
        this.m_tangent = tangent;
    }

    public Vector3f GetPos()
    {
        return m_pos;
    }

    public void SetPos(Vector3f pos)
    {
        this.m_pos = pos;
    }

    public Vector2f GetTexCoord()
    {
        return m_texCoord;
    }

    public void SetTexCoord(Vector2f texCoord)
    {
        this.m_texCoord = texCoord;
    }

    public Vector3f GetNormal()
    {
        return m_normal;
    }

    public void SetNormal(Vector3f normal)
    {
        this.m_normal = normal;
    }
}
