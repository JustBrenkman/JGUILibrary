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

package org.jgui.render.mesh.Loaders;

import org.jgui.render.mesh.Mesh;
import org.jgui.scene.node.appearance.Material;
import org.jgui.util.PathManager;
import org.lwjgl.util.vector.Vector3f;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ben on 17/11/14.
 */
public class OBJLoader {

    public static Mesh loadObJModel(String filename) {
        // TODO tidy up;
        Mesh mesh = new Mesh();
        FileReader fr = null;

        try {
            fr = new FileReader(new File(PathManager.getLocationPath() + "/resources/Models/" + filename + ".obj"));
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(fr);
        String line;

        List<Integer> indecies = new ArrayList<Integer>();
        List<Vector3f> verticies = new ArrayList<Vector3f>();
        List<Vector3f> vectors = new ArrayList<Vector3f>();
        List<Vector3f> normals = new ArrayList<Vector3f>();
        List<Vector3f> finalNormals = new ArrayList<>();

        float[] verticiesArray = null;
        float[] normalArray = null;
        float[] textureArray = null;
        int[] indecesArray = null;

        try {
            while (true) {
                line = reader.readLine();
                String[] currentLine = line.split(" ");
                if (line.startsWith("v ")) {
                    Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
                    verticies.add(vertex);
                } else if (line.startsWith("vt ")) {

                } else if (line.startsWith("vn ")) {
                    Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
                    normals.add(normal);
                } else if (line.startsWith("f ")) {
                    normalArray = new float[verticies.size() * 3];
                    break;
                }
            }

            while (line!=null) {
                if (!line.startsWith("f ")) {
                    line = reader.readLine();
                    continue;
                }


                String[] currentLine = line.split(" ");
                    String[] vertex1 = currentLine[1].split("/");
                    String[] vertex2 = currentLine[2].split("/");
                    String[] vertex3 = currentLine[3].split("/");

                    processVertex(vertex1, indecies, normals, vectors, verticies, normalArray, finalNormals);
                    processVertex(vertex2, indecies, normals, vectors, verticies, normalArray, finalNormals);
                    processVertex(vertex3, indecies, normals, vectors, verticies, normalArray, finalNormals);
//                    String vertex1 = currentLine[1];
//                    String vertex2 = currentLine[2];
//                    String vertex3 = currentLine[3];
//
//                    proccessVertexIndecies(vertex1, indecies);
//                    proccessVertexIndecies(vertex2, indecies);
//                    proccessVertexIndecies(vertex3, indecies);

                line = reader.readLine();
            }

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        mesh.getMesh().setVertices(verticies);
        indecesArray = new int[indecies.size()];

        for (Vector3f finalNormal : finalNormals) {
            mesh.getMesh().addNormal(finalNormal);
//            System.out.println("Added Normal");
        }

        for (int i = 0; i < indecies.size(); i++) {
            indecesArray[i] = indecies.get(i);
        }
        mesh.getMesh().addIndecies(indecesArray);

        mesh.setMaterial(new Material(Color.GREEN));
        mesh.getMesh().setCalulateNormals(true);
        mesh.getMesh().compile();

        return mesh;
    }

    private static void processVertex(String[] vertexData, List<Integer> indecies, List<Vector3f> normals, List<Vector3f> vectors, List<Vector3f> verticies, float[] normalsArray, List<Vector3f> finalNormals) {
        int currentVertexPointer = Integer.parseInt(vertexData[0]) - 1;
        indecies.add(currentVertexPointer);
        vectors.add(verticies.get(currentVertexPointer));
        Vector3f currentNorm = normals.get(Integer.parseInt(vertexData[2]) - 1);
//        normalsArray[currentVertexPointer * 3] = currentNorm.getX();
//        normalsArray[currentVertexPointer * 3 + 1] = currentNorm.getY();
//        normalsArray[currentVertexPointer * 3 + 2] = currentNorm.getZ();

        finalNormals.add(new Vector3f(currentNorm));
//        System.out.println("Normals:" + currentNorm.toString());
    }

    private static void proccessVertexIndecies(String vertexData, List<Integer> indecies) {
        int currentVertexPointer = Integer.parseInt(vertexData) - 1;
        indecies.add(currentVertexPointer);
    }

    public static Mesh loadTestOBJModel(String filename) {
        Mesh mesh = new Mesh();

        List<Vector3f> verticies = new ArrayList<Vector3f>();
        List<Integer> indecies = new ArrayList<Integer>();

        FileReader fr = null;

        try {
            fr = new FileReader(new File(PathManager.getLocationPath() + "/resources/Models/" + filename + ".obj"));
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(fr);
        String line;

        try {
            while ((line = reader.readLine()) != null) {
                String[] currentLine = line.split(" ");
                if (line.startsWith("v ")) {
                    verticies.add(new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3])));
                } else if (line.startsWith("vt ")) {

                } else if (line.startsWith("vn ")) {

                } else if (line.startsWith("f ")) {
                    indecies.add(Integer.parseInt(currentLine[1]) - 1);
                    indecies.add(Integer.parseInt(currentLine[2]) - 1);
                    indecies.add(Integer.parseInt(currentLine[3]) - 1);
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Vector3f[] ver = new Vector3f[verticies.size()];
        verticies.toArray(ver);

        Integer[] indexData = new Integer[indecies.size()];
        indecies.toArray(indexData);

        mesh.getMesh().setVertices(verticies);
        mesh.getMesh().addIndecies(toIntArray(indexData));

        mesh.setMaterial(new Material(Color.GREEN));
        mesh.getMesh().setCalulateNormals(true);
        mesh.getMesh().compile();

        for (int i = 0; i < ver.length; i++) {
            System.out.println("Vertex: " + ver[i].toString());
        }

        for (int i = 0; i < indexData.length; i++) {
            System.out.println("Indecies: " + indexData[i].toString());
        }

        return mesh;
    }

    public static Mesh loadTest2OBJModel(String filename) {
        Mesh mesh = new Mesh();

        List<Vector3f> verticies = new ArrayList<Vector3f>();
        List<Integer> indecies = new ArrayList<Integer>();

        FileReader fr = null;

        try {
            fr = new FileReader(new File(PathManager.getLocationPath() + "/resources/Models/" + filename + ".obj"));
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(fr);
        String line;

        try {
            while ((line = reader.readLine()) != null) {
                String[] currentLine = line.split(" ");
                if (line.startsWith("v ")) {
                    verticies.add(new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3])));
                } else if (line.startsWith("vt ")) {

                } else if (line.startsWith("vn ")) {

                } else if (line.startsWith("f ")) {
                    indecies.add(Integer.parseInt(currentLine[1]) - 1);
                    indecies.add(Integer.parseInt(currentLine[2]) - 1);
                    indecies.add(Integer.parseInt(currentLine[3]) - 1);
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<Vector3f> vertexies = new ArrayList<Vector3f>();
        List<Integer> indexies = new ArrayList<Integer>();

        for (int i = 0; i < indecies.size(); i += 3) {
            int i0 = indecies.get(i);
            int i1 = indecies.get(i + 1);
            int i2 = indecies.get(i + 2);

            vertexies.add(verticies.get(i0));
            vertexies.add(verticies.get(i1));
            vertexies.add(verticies.get(i2));

            indexies.add(vertexies.size() - 2);
            indexies.add(vertexies.size() - 1);
            indexies.add(vertexies.size());
        }

        Vector3f[] ver = new Vector3f[vertexies.size()];
        vertexies.toArray(ver);

        Integer[] indexData = new Integer[indexies.size()];
        indexies.toArray(indexData);

        mesh.getMesh().setVertices(vertexies);
        mesh.getMesh().addIndecies(toIntArray(indexData));

        mesh.setMaterial(new Material(Color.GREEN));
//        mesh.getMesh().setCalulateNormals(true);
        mesh.getMesh().compile();

        for (int i = 0; i < ver.length; i++) {
            System.out.println("Vertex: " + ver[i].toString());
        }

        for (int i = 0; i < indexData.length; i++) {
            System.out.println("Indecies: " + indexData[i].toString());
        }

        return mesh;
    }

    private static int[] toIntArray(Integer[] integers) {
        int[] result = new int[integers.length];

        for (int i = 0; i < integers.length; i++) {
            // result[i] = integers[i].intValue();
            result[i] = integers[i];
        }

        return result;
    }
}
