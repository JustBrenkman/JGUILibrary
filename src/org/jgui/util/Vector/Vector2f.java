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

package org.jgui.util.Vector;

/**
 * Created by ben on 20/11/14.
 */
public class Vector2f
{
    private float m_x;
    private float m_y;

    public Vector2f(float x, float y)
    {
        this.m_x = x;
        this.m_y = y;
    }

    public float Length()
    {
        return (float)Math.sqrt(m_x * m_x + m_y * m_y);
    }

    public float Max()
    {
        return Math.max(m_x, m_y);
    }

    public float Dot(Vector2f r)
    {
        return m_x * r.GetX() + m_y * r.GetY();
    }

    public Vector2f Normalized()
    {
        float length = Length();

        return new Vector2f(m_x / length, m_y / length);
    }

    public float Cross(Vector2f r)
    {
        return m_x * r.GetY() - m_y * r.GetX();
    }

    public Vector2f Lerp(Vector2f dest, float lerpFactor)
    {
        return dest.Sub(this).Mul(lerpFactor).Add(this);
    }

    public Vector2f Rotate(float angle)
    {
        double rad = Math.toRadians(angle);
        double cos = Math.cos(rad);
        double sin = Math.sin(rad);

        return new Vector2f((float)(m_x * cos - m_y * sin),(float)(m_x * sin + m_y * cos));
    }

    public Vector2f Add(Vector2f r)
    {
        return new Vector2f(m_x + r.GetX(), m_y + r.GetY());
    }

    public Vector2f Add(float r)
    {
        return new Vector2f(m_x + r, m_y + r);
    }

    public Vector2f Sub(Vector2f r)
    {
        return new Vector2f(m_x - r.GetX(), m_y - r.GetY());
    }

    public Vector2f Sub(float r)
    {
        return new Vector2f(m_x - r, m_y - r);
    }

    public Vector2f Mul(Vector2f r)
    {
        return new Vector2f(m_x * r.GetX(), m_y * r.GetY());
    }

    public Vector2f Mul(float r)
    {
        return new Vector2f(m_x * r, m_y * r);
    }

    public Vector2f Div(Vector2f r)
    {
        return new Vector2f(m_x / r.GetX(), m_y / r.GetY());
    }

    public Vector2f Div(float r)
    {
        return new Vector2f(m_x / r, m_y / r);
    }

    public Vector2f Abs()
    {
        return new Vector2f(Math.abs(m_x), Math.abs(m_y));
    }

    public String toString()
    {
        return "(" + m_x + " " + m_y + ")";
    }

    public Vector2f Set(float x, float y) { this.m_x = x; this.m_y = y; return this; }
    public Vector2f Set(Vector2f r) { Set(r.GetX(), r.GetY()); return this; }

    public float GetX()
    {
        return m_x;
    }

    public void SetX(float x)
    {
        this.m_x = x;
    }

    public float GetY()
    {
        return m_y;
    }

    public void SetY(float y)
    {
        this.m_y = y;
    }

    public boolean equals(Vector2f r)
    {
        return m_x == r.GetX() && m_y == r.GetY();
    }
}
