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

import org.jgui.scene.transform.Quaternion;

/**
 * Created by ben on 20/11/14.
 */
public class Vector3f
{
    private float m_x;
    private float m_y;
    private float m_z;

    public Vector3f(float x, float y, float z)
    {
        this.m_x = x;
        this.m_y = y;
        this.m_z = z;
    }

    public float Length()
    {
        return (float)Math.sqrt(m_x * m_x + m_y * m_y + m_z * m_z);
    }

    public float Max()
    {
        return Math.max(m_x, Math.max(m_y, m_z));
    }

    public float Dot(Vector3f r)
    {
        return m_x * r.GetX() + m_y * r.GetY() + m_z * r.GetZ();
    }

    public Vector3f Cross(Vector3f r)
    {
        float x_ = m_y * r.GetZ() - m_z * r.GetY();
        float y_ = m_z * r.GetX() - m_x * r.GetZ();
        float z_ = m_x * r.GetY() - m_y * r.GetX();

        return new Vector3f(x_, y_, z_);
    }

    public Vector3f Normalized()
    {
        float length = Length();

        return new Vector3f(m_x / length, m_y / length, m_z / length);
    }

    public Vector3f Rotate(Vector3f axis, float angle)
    {
        float sinAngle = (float)Math.sin(-angle);
        float cosAngle = (float)Math.cos(-angle);

        return this.Cross(axis.Mul(sinAngle)).Add(           //Rotation on local X
                (this.Mul(cosAngle)).Add(                     //Rotation on local Z
                        axis.Mul(this.Dot(axis.Mul(1 - cosAngle))))); //Rotation on local Y
    }

    public Vector3f Rotate(Quaternion rotation)
    {
        Quaternion conjugate = rotation.conjugate();

        Quaternion w = rotation.multiply(new org.lwjgl.util.vector.Vector3f(GetX(), GetY(), GetZ())).multiply(conjugate);

        return new Vector3f(w.getX(), w.getY(), w.getZ());
    }

    public Vector3f Lerp(Vector3f dest, float lerpFactor)
    {
        return dest.Sub(this).Mul(lerpFactor).Add(this);
    }

    public Vector3f Add(Vector3f r)
    {
        return new Vector3f(m_x + r.GetX(), m_y + r.GetY(), m_z + r.GetZ());
    }

    public Vector3f Add(float r)
    {
        return new Vector3f(m_x + r, m_y + r, m_z + r);
    }

    public Vector3f Sub(Vector3f r)
    {
        return new Vector3f(m_x - r.GetX(), m_y - r.GetY(), m_z - r.GetZ());
    }

    public Vector3f Sub(float r)
    {
        return new Vector3f(m_x - r, m_y - r, m_z - r);
    }

    public Vector3f Mul(Vector3f r)
    {
        return new Vector3f(m_x * r.GetX(), m_y * r.GetY(), m_z * r.GetZ());
    }

    public Vector3f Mul(float r)
    {
        return new Vector3f(m_x * r, m_y * r, m_z * r);
    }

    public Vector3f Div(Vector3f r)
    {
        return new Vector3f(m_x / r.GetX(), m_y / r.GetY(), m_z / r.GetZ());
    }

    public Vector3f Div(float r)
    {
        return new Vector3f(m_x / r, m_y / r, m_z / r);
    }

    public Vector3f Abs()
    {
        return new Vector3f(Math.abs(m_x), Math.abs(m_y), Math.abs(m_z));
    }

    public String toString()
    {
        return "(" + m_x + " " + m_y + " " + m_z + ")";
    }

    public Vector2f GetXY() { return new Vector2f(m_x, m_y); }
    public Vector2f GetYZ() { return new Vector2f(m_y, m_z); }
    public Vector2f GetZX() { return new Vector2f(m_z, m_x); }

    public Vector2f GetYX() { return new Vector2f(m_y, m_x); }
    public Vector2f GetZY() { return new Vector2f(m_z, m_y); }
    public Vector2f GetXZ() { return new Vector2f(m_x, m_z); }

    public Vector3f Set(float x, float y, float z) { this.m_x = x; this.m_y = y; this.m_z = z; return this; }
    public Vector3f Set(Vector3f r) { Set(r.GetX(), r.GetY(), r.GetZ()); return this; }

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

    public float GetZ()
    {
        return m_z;
    }

    public void SetZ(float z)
    {
        this.m_z = z;
    }

    public boolean equals(Vector3f r)
    {
        return m_x == r.GetX() && m_y == r.GetY() && m_z == r.GetZ();
    }
}