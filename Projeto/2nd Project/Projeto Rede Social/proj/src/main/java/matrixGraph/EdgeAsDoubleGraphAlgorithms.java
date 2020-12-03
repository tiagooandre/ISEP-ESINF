
package matrixGraph;

import model.User;

import java.util.LinkedList;

/**
 *
 * @author DEI-ESINF
 */
public class EdgeAsDoubleGraphAlgorithms {

    /**
     * Determine the shortest path to all vertices from a vertex using Dijkstra's algorithm
     * To be called by public short method
     * @param graph Graph object
     * @param sourceIdx Source vertex 
     * @param knownVertices previously discovered vertices
     * @param verticesIndex index of vertices in the minimum path
     * @param minDist minimum distances in the path
     *
     */
    private static <V,E> void shortestPath(AdjacencyMatrixGraph<V,E> graph, int sourceIdx, boolean[] knownVertices, int[] verticesIndex, double [] minDist){
        minDist[sourceIdx] = 0;
        while (sourceIdx != -1) {
            knownVertices[sourceIdx] = true;

            for (int i = 0; i < graph.numVertices; i++) {
                if (graph.privateGet(sourceIdx, i) != null) {
                    double aux = 0;
                    if (graph.privateGet(sourceIdx, i) != null) {
                        aux = 1;
                    }
                    if (!knownVertices[i] && minDist[i] > minDist[sourceIdx] + aux) {
                        minDist[i] = minDist[sourceIdx] + aux;

                        verticesIndex[i] = sourceIdx;
                    }


                }
            }

            double min = Double.MAX_VALUE;
            sourceIdx = -1;
            for (int i = 0; i < graph.numVertices; i++) {
                if (!knownVertices[i] && minDist[i] < min) {
                    min = minDist[i];
                    sourceIdx = i;
                }
            }
        }
    }

    /**
     * Determine the shortest path between two vertices using Dijkstra's algorithm
     * @param graph Graph object
     * @param source Source vertex 
     * @param dest Destination vertices
     * @param path Returns the vertices in the path (empty if no path)
     * @return minimum distance, -1 if vertices not in graph or no path
     *
     */
    public static <V, E> double shortestPath(AdjacencyMatrixGraph<V, E> graph, V source, V dest, LinkedList<V> path){

        int sourceIdx = graph.toIndex(source);
        if (sourceIdx == -1)
            return -1;

        int destIdx = graph.toIndex(dest);
        if (destIdx == -1)
            return -1;

        path.clear();

        boolean[] knownVertices = new boolean[graph.numVertices];
        int[] verticesIndex = new int[graph.numVertices]; 
        double[] minDist = new double[graph.numVertices];

        for (int i = 0; i < graph.numVertices; i++) {
            minDist[i] = Double.MAX_VALUE;
            verticesIndex[i] = -1;
        }

        shortestPath(graph, sourceIdx, knownVertices, verticesIndex, minDist); 
        if (knownVertices[destIdx] == false) 
            return -1;

        recreatePath(graph,sourceIdx,destIdx,verticesIndex,path); 

        // recreatePath builds path in reverse order, so reverse
        LinkedList<V> stack = new LinkedList<V>();  //create a stack
        while (!path.isEmpty())
            stack.push(path.remove());

        while(!stack.isEmpty())
            path.add(stack.pop());

        return minDist[destIdx];
    }

    
    /**
     * Recreates the minimum path between two vertex, from the result of Dikstra's algorithm
     * @param graph Graph object
     * @param sourceIdx Source vertex 
     * @param destIdx Destination vertices
     * @param verticesIndex index of vertices in the minimum path
     * @param Queue Vertices in the path (empty if no path)
     */
    private static <V, E> void recreatePath(AdjacencyMatrixGraph<V, E> graph, int sourceIdx,
                                           int destIdx, int[] verticesIndex, LinkedList<V> path){

        path.add(graph.vertices.get(destIdx));
        if (sourceIdx != destIdx){
            destIdx = verticesIndex[destIdx];        
            recreatePath(graph, sourceIdx, destIdx, verticesIndex, path);
        }
    }


}