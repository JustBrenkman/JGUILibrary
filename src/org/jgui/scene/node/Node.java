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

package org.jgui.scene.node;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;

/**
 * Created by ben on 28/08/14.
 */
public class Node extends Spatial {

    private LinkedList<Spatial> children = new LinkedList<>();

    private Logger logger = LoggerFactory.getLogger(Node.class);

    public Node() {
    }

    public Node(String name) {
        super(name);
    }


    /**
     * Gets the number of children
     * @return
     */
    public int getAmountOfChildren() {return children.size();}


    /**
     * Returns the amount of children after addition
     * @param child
     * @return
     * @throws IllegalArgumentException
     */
    public int attachChild(Spatial child) throws IllegalArgumentException {
        if (child == null)
            throw new IllegalArgumentException("Child cannot be null");

        child.setParentNode(this);
        children.add(child);

        logger.info("Attached child at {0}", new Object[] {child.getSceneName()});

        return children.size();
    }

    /**
     * Detaches child
     * @param child
     * @return
     */
    public int detachChild(Spatial child) {
        if (child == null)
            throw new NullPointerException("Cannot remove a child that doesn't exist");

        if (child.getParentNode() == this) {
            int index = children.indexOf(child);
            if (index != -1) {
                detachChildAt(index);
            }
            return index;
        }
        return -1;
    }

    /**
     * removes specific child from index
     * @param index
     * @return
     */
    public Spatial detachChildAt(int index) {
        Spatial child = children.remove(index);

        logger.info("Child({0}) removed", new Object[] {child.getSceneName()});

        if (child == null) {
            child.setParentNode(null);
        }
        return child;
    }


    /**
     * Removes all the children
     */
    public void detachAllChildren() {
        for (int i = 0; i < children.size(); i++) {
            detachChildAt(i);
        }

        logger.info("All children were removed");
    }

    /**
     * Returns the index of a Child
     * @param child
     * @return
     */
    public int getIndexOfChild(Spatial child) {
        return children.indexOf(child);
    }


    /**
     * Gets child at index
     * @param index
     * @return
     */
    public Spatial getChild(int index) {
        if (index > children.size() || index < children.size())
            return null;

        return children.get(index);
    }

    /**
     *
     * Determines if spatial has a specific child or not
     *
     * @param spat
     * @return
     */
    public boolean hasChild(Spatial spat) {
        if (children.contains(spat))
            return true;

        for (Spatial child : children) {
            if (child instanceof Node && ((Node)child).hasChild(spat))
                return true;
        }

        return false;
    }
}
