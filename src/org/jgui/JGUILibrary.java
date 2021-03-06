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

package org.jgui;

import org.jgui.eventbus.EventBusService;
import org.jgui.eventbus.EventHandler;
import org.jgui.events.Event;
import org.jgui.events.ShutDownEvent;
import org.jgui.util.BlueTooth;
import org.jgui.util.NXTComm;
import org.jgui.util.StringColors;
import org.lwjgl.opengl.GL11;
import org.jgui.core.JGUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JGUILibrary {

    public static JGUI gui = new JGUI();

    static Logger logger = LoggerFactory.getLogger(JGUILibrary.class);

    public Thread nxtComm;

    @EventHandler
    public void handleShutdown(ShutDownEvent event) {
        if (event.shutingDown) {
            if (nxtComm != null) {
                nxtComm.stop();
            }
        }
    }

    public static void main(String[] args) {
        JGUILibrary jguiLibrary = new JGUILibrary();
        EventBusService.subscribe(jguiLibrary);
        jguiLibrary.start();
    }

    public void start() {

//        System.setProperty("org.lwjgl.opengl.Window.undecorated", String.valueOf(true));
//        System.setProperty("java.library.path", "natives/linux");

//        BlueTooth tooth = new BlueTooth();
//        EventBusService.subscribe(tooth);
//
//        nxtComm = new Thread(new NXTComm(tooth.openConnection()));
//        nxtComm.start();

        System.out.println(StringColors.ANSI_BLUE + "Hello to the JGUI Library" + StringColors.ANSI_RESET);

        gui.intitialize();

        logger.info("OpenGL version: " + GL11.glGetString(GL11.GL_VERSION));

        gui.start();
    }
}
