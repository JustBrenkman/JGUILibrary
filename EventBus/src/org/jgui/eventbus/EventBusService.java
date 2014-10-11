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

package org.jgui.eventbus;

import java.util.ServiceLoader;

/**
 * An {@link EventBus} factory that will return a singleton implementation loaded
 * via the Java 6 {@link ServiceLoader}. By default, without changes to the jar,
 * a {@link BasicEventBus} implementation will be returned via the factory methods.
 * <p>
 * This static factory also includes the same methods as EventBus which will delegate
 * to the ServiceLoader loaded instance.  Thus, the class creates a convenient single
 * location for which client code can be hooked to the configured EventBus.
 *
 * @author Adam Taft
 */
public final class EventBusService {

    private static final EventBus eventBus;
    static {
//        ServiceLoader<EventBus> ldr = ServiceLoader.load(EventBus.class);
//        eventBus = ldr.iterator().next();
//        eventBus = new BasicEventBus();
        eventBus = EventBusFactory.newEventBus();
    }

    public static EventBus getInstance() {
        return eventBus;
    }

    public static void subscribe(Object subscriber) {
        eventBus.subscribe(subscriber);
    }

    public static void unsubscribe(Object subscriber) {
        eventBus.unsubscribe(subscriber);
    }

    public static void publish(Object event) {
        eventBus.publish(event);
    }

    public static boolean hasPendingEvents() {
        return eventBus.hasPendingEvents();
    }

}
