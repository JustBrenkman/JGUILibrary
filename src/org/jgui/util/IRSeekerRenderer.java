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

import org.jgui.eventbus.EventBusService;
import org.jgui.eventbus.EventHandler;
import org.jgui.events.IRSeekerEvent;
import org.jgui.render.IRenderer;
import org.jgui.render.mesh.Box;

import java.awt.*;

/**
 * Created by ben on 28/10/14.
 */
public class IRSeekerRenderer {

    private IRenderer renderer;

    private Camera camera;

    private Box sensor1;
    private Box sensor2;
    private Box sensor3;
    private Box sensor4;
    private Box sensor5;

    int i = 0;

    private boolean isDirty = false;

    public IRSeekerRenderer(Camera camera, IRenderer renderer) {
        this.renderer = renderer;
        this.camera = camera;
    }

    public void initialize() {
        EventBusService.subscribe(this);

        sensor1 = new Box(0, 0, 100, 100);
        sensor1.setColor(new Color(0xE51C23));
        sensor1.build();

        sensor2 = new Box(100, 0, 100, 100);
        sensor2.setColor(new Color(0x2AE52A));
        sensor2.build();

        sensor3 = new Box(200, 0, 100, 100);
        sensor3.setColor(new Color(0x2330E5));
        sensor3.build();

        sensor4 = new Box(300, 0, 100, 100);
        sensor4.setColor(new Color(0xE2E52B));
        sensor4.build();

        sensor5 = new Box(400, 0, 100, 100);
        sensor5.setColor(new Color(0xE51CD6));
        sensor5.build();
    }

    @EventHandler
    public void handleIRSeeker(IRSeekerEvent event) {

        if (event.irSeekerID == 0) {

            sensor1.setHeight(event.getIrSeekerS1);
            sensor2.setHeight(event.getIrSeekerS2);
            sensor3.setHeight(event.getIrSeekerS3);
            sensor4.setHeight(event.getIrSeekerS4);
            sensor5.setHeight(event.getIrSeekerS5);

        } else if (event.irSeekerID == 1) {



        }

        isDirty = true;
    }

    public void render() {

        if (isDirty) {
            sensor1.refresh();
            sensor2.refresh();
            sensor3.refresh();
            sensor4.refresh();
            sensor5.refresh();
            isDirty = false;
        }

        sensor1.safeRender(camera, renderer);
        sensor2.safeRender(camera, renderer);
        sensor3.safeRender(camera, renderer);
        sensor4.safeRender(camera, renderer);
        sensor5.safeRender(camera, renderer);
    }
}
