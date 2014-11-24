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
public class OBJIndex
{
    private int m_vertexIndex;
    private int m_texCoordIndex;
    private int m_normalIndex;

    public int GetVertexIndex()   { return m_vertexIndex; }
    public int GetTexCoordIndex() { return m_texCoordIndex; }
    public int GetNormalIndex()   { return m_normalIndex; }

    public void SetVertexIndex(int val)   { m_vertexIndex = val; }
    public void SetTexCoordIndex(int val) { m_texCoordIndex = val; }
    public void SetNormalIndex(int val)   { m_normalIndex = val; }

    @Override
    public boolean equals(Object obj)
    {
        OBJIndex index = (OBJIndex)obj;

        return m_vertexIndex == index.m_vertexIndex
                && m_texCoordIndex == index.m_texCoordIndex
                && m_normalIndex == index.m_normalIndex;
    }

    @Override
    public int hashCode()
    {
        final int BASE = 17;
        final int MULTIPLIER = 31;

        int result = BASE;

        result = MULTIPLIER * result + m_vertexIndex;
        result = MULTIPLIER * result + m_texCoordIndex;
        result = MULTIPLIER * result + m_normalIndex;

        return result;
    }
}
