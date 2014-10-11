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

// TODO Make EventBus for GUI, ExceptionHandling ect.

/**
 * An EventBus is a simple pattern to promote loose coupling between various
 * components.  An event bus can be used in place of a traditional listener
 * or observer pattern, and is useful when ties between multiple components
 * become too complicated to track.
 */
public interface EventBus {

    /**
     *Subscribes the specified subscriber to the event bus.  A subscribed object
     * will be notified of any published events on the methods annotated with the
     * Each event handler method should take a single parameter indicating the
     * type of event it wishes to receive.  When events are published on the
     * bus, only subscribers who have an EventHandler method with a matching
     * parameter of the same type as the published event will receive the
     * event notification from the bus.
     *
     * @param subscribedObject The object to subscribe to the event bus.
     */
    public void subscribe(Object subscribedObject);

    /**
     * Removes the specified object from the event bus subscription list.  Once
     * removed, the specified object will no longer receive events posted to the
     * event bus.
     *
     * @param subscriber The object previous subscribed to the event bus.
     */
    void unsubscribe(Object subscriber);

    /**
     * Sends a message on the bus which will be propagated to the appropriate
     * subscribers of the event type.  Only subscribers which have elected to
     * subscribe to the same event type as the supplied event will be notified
     * of the event.
     * There is no specification given as to how the messages will be delivered,
     * in terms of synchronous or asynchronous.
     *
     * @param event The event to send out to the subscribers of the same type.
     */
    void publish(Object event);


    /**
     * Indicates whether the bus has pending events to publish.  Since message/event
     * delivery can be asynchronous (on other threads), the method can be used to
     * start or stop certain actions based on all the events having been published.
     * I.e. perhaps before an application closes, etc.
     *
     * @return True if events are still being delivered.
     */
    boolean hasPendingEvents();
}
