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

package org.jgui.events;

/**
 * Created by ben on 28/10/14.
 */
public class IRSeekerEvent {

    public int irSeekerID = 0;
    public int irSeekerDirection = 0;
    public int getIrSeekerS1 = 0;
    public int getIrSeekerS2 = 0;
    public int getIrSeekerS3 = 0;
    public int getIrSeekerS4 = 0;
    public int getIrSeekerS5 = 0;

    public IRSeekerEvent() {
    }

    public IRSeekerEvent(int irSeekerID, int irSeekerDirection, int getIrSeekerS1, int getIrSeekerS2, int getIrSeekerS3, int getIrSeekerS4, int getIrSeekerS5) {
        this.irSeekerID = irSeekerID;
        this.irSeekerDirection = irSeekerDirection;
        this.getIrSeekerS1 = getIrSeekerS1;
        this.getIrSeekerS2 = getIrSeekerS2;
        this.getIrSeekerS3 = getIrSeekerS3;
        this.getIrSeekerS4 = getIrSeekerS4;
        this.getIrSeekerS5 = getIrSeekerS5;
    }
}
