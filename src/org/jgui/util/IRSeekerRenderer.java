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
import org.jgui.render.OpenGLRenderer;
import org.jgui.scene.geometry.Box;
import org.jgui.scene.geometry.Circle;
import org.jgui.scene.geometry.Line;
import org.jgui.scene.node.appearance.Material;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import java.awt.*;

/**
 * Created by ben on 28/10/14.
 */
public class IRSeekerRenderer {

    private OpenGLRenderer renderer;

    private Camera camera;

    private Box sensor1;
    private Box sensor2;
    private Box sensor3;
    private Box sensor4;
    private Box sensor5;

    private Box sensor6;
    private Box sensor7;
    private Box sensor8;
    private Box sensor9;
    private Box sensor10;

//    private Box sensor11;
//    private Box sensor12;
//    private Box sensor13;

    private Circle circle;

    private Box sensor1Dir;

    private Line line;

    private SmoothMoves smoothMovesS1 = new SmoothMoves();
    private SmoothMoves smoothMovesS2 = new SmoothMoves();
    private SmoothMoves smoothMovesS3 = new SmoothMoves();
    private SmoothMoves smoothMovesS4 = new SmoothMoves();
    private SmoothMoves smoothMovesS5 = new SmoothMoves();

    private SmoothMoves smoothMovesS6 = new SmoothMoves();
    private SmoothMoves smoothMovesS7 = new SmoothMoves();
    private SmoothMoves smoothMovesS8 = new SmoothMoves();
    private SmoothMoves smoothMovesS9 = new SmoothMoves();
    private SmoothMoves smoothMovesS10 = new SmoothMoves();

    int i = 0;

    private boolean isDirty = false;

    public IRSeekerRenderer(Camera camera, OpenGLRenderer renderer) {
        this.renderer = renderer;
        this.camera = camera;
    }

    public void initialize() {
        EventBusService.subscribe(this);

        sensor1 = new Box(0, 0, 49, 50);
        sensor1.setColor(new Color(0xe51c23));
        sensor1.build();

        sensor2 = new Box(50, 0, 49, 50);
        sensor2.setColor(new Color(0xe91e63));
        sensor2.build();

        sensor3 = new Box(100, 0, 49, 100);
        sensor3.setColor(new Color(0x9c27b0));
        sensor3.build();

        sensor4 = new Box(150, 0, 49, 100);
        sensor4.setColor(new Color(0x673ab7));
        sensor4.build();

        sensor5 = new Box(200, 0, 49, 100);
        sensor5.setColor(new Color(0x3f51b5));
        sensor5.build();

        sensor1Dir = new Box(0, 250, 49, 20);
        sensor1Dir.setColor(new Color(0x2AE52A));
        sensor1Dir.build();

        sensor6 = new Box(Display.getWidth() - 250, 0, 49, 50);
        sensor6.setColor(new Color(0xe51c23));
        sensor6.build();

        sensor7 = new Box(Display.getWidth() - 200, 0, 49, 50);
        sensor7.setColor(new Color(0xe91e63));
        sensor7.build();

        sensor8 = new Box(Display.getWidth() - 150, 0, 49, 100);
        sensor8.setColor(new Color(0x9c27b0));
        sensor8.build();

        sensor9 = new Box(Display.getWidth() - 100, 0, 49, 100);
        sensor9.setColor(new Color(0x673ab7));
        sensor9.build();

        sensor10 = new Box(Display.getWidth() - 50, 0, 49, 100);
        sensor10.setColor(new Color(0x3F51B5));
        sensor10.build();

        /// RGB /////
//        sensor11 = new Box(Display.getWidth() - 150, 0, 50, 100);
//        sensor11.setColor(new Color(0xE51C23));
//        sensor11.build();
//
//        sensor12 = new Box(Display.getWidth() - 100, 0, 50, 100);
//        sensor12.setColor(new Color(0x2AE52A));
//        sensor12.build();
//
//        sensor13 = new Box(Display.getWidth() - 50, 0, 50, 100);
//        sensor13.setColor(new Color(0x2330E5));
//        sensor13.build();
        ////////////

        circle = new Circle(125, 475, 125);
        circle.setColor(new Color(0x00bcd4));
        circle.build();

        line = new Line(new Vector3f(0, 0, 0), new Vector3f(125, 0, 0), new Material(Color.RED));
        line.build();
    }

    @EventHandler
    public void handleIRSeeker(IRSeekerEvent event) {

        if (event.irSeekerID == 0) {

            smoothMovesS1.setTarget(event.getIrSeekerS1);
            smoothMovesS2.setTarget(event.getIrSeekerS2);
            smoothMovesS3.setTarget(event.getIrSeekerS3);
            smoothMovesS4.setTarget(event.getIrSeekerS4);
            smoothMovesS5.setTarget(event.getIrSeekerS5);

            sensor1.setHeight(smoothMovesS1.getCurrent());
            sensor2.setHeight(smoothMovesS2.getCurrent());
            sensor3.setHeight(smoothMovesS3.getCurrent());
            sensor4.setHeight(smoothMovesS4.getCurrent());
            sensor5.setHeight(smoothMovesS5.getCurrent());

            sensor1Dir.setX(50 * (event.irSeekerDirection) - 50);

        } else if (event.irSeekerID == 1) {

            smoothMovesS6.setTarget(event.getIrSeekerS1);
            smoothMovesS7.setTarget(event.getIrSeekerS2);
            smoothMovesS8.setTarget(event.getIrSeekerS3);
            smoothMovesS9.setTarget(event.getIrSeekerS4);
            smoothMovesS10.setTarget(event.getIrSeekerS5);

            sensor6.setHeight(smoothMovesS6.getCurrent());
            sensor7.setHeight(smoothMovesS7.getCurrent());
            sensor8.setHeight(smoothMovesS8.getCurrent());
            sensor9.setHeight(smoothMovesS9.getCurrent());
            sensor10.setHeight(smoothMovesS10.getCurrent());
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

            sensor1Dir.refresh();

            sensor6.refresh();
            sensor7.refresh();
            sensor8.refresh();
            sensor9.refresh();
            sensor10.refresh();

            isDirty = false;

            circle.refresh();
        }

        line.getTransform().setTranslation(new Vector3f(Mouse.getX(), Mouse.getY(), 0));
        circle.setX(Mouse.getX());
        circle.setY(Mouse.getY());

        circle.safeRender(camera, renderer);

        sensor1.safeRender(camera, renderer);
        sensor2.safeRender(camera, renderer);
        sensor3.safeRender(camera, renderer);
        sensor4.safeRender(camera, renderer);
        sensor5.safeRender(camera, renderer);

        sensor6.safeRender(camera, renderer);
        sensor7.safeRender(camera, renderer);
        sensor8.safeRender(camera, renderer);
        sensor9.safeRender(camera, renderer);
        sensor10.safeRender(camera, renderer);

        line.safeRender(camera, renderer);

//        sensor1Dir.safeRender(camera, renderer);

        smoothMovesS1.update();
        smoothMovesS2.update();
        smoothMovesS3.update();
        smoothMovesS4.update();
        smoothMovesS5.update();

        smoothMovesS6.update();
        smoothMovesS7.update();
        smoothMovesS8.update();
        smoothMovesS9.update();
        smoothMovesS10.update();
    }
}
