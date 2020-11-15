/*
* A collection of graph algorithms.
*/
package graphbase;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author DEI-ESINF
 */

public class GraphAlgorithms {
    
   /**
   * Performs breadth-first search of a Graph starting in a Vertex 
   * @param g Graph instance
   * @param vInf information of the Vertex that will be the source of the search
   * @return qbfs a queue with the vertices of breadth-first search 
   */
    public static<V,E> LinkedList<V> BreadthFirstSearch(Graph<V,E> g, V vert) {
        if (!g.validVertex(vert)) {
            return null;
        }

        LinkedList<V> qbfs = new LinkedList<>();
        ArrayList<V> qaux = new ArrayList<>();
        boolean[] visited = new boolean[g.numVertices()];
        qbfs.add(vert);
        qaux.add(vert);
        visited[g.getKey(vert)] = true;
        while (!qaux.isEmpty()) {
            V vInf = qaux.remove(0);
            for (V vAdj : g.adjVertices(vInf)) {
                if (!visited[g.getKey(vAdj)]) {
                    qbfs.add(vAdj);
                    qaux.add(vAdj);
                    visited[g.getKey(vAdj)] = true;
                }
            }

        }
        return qbfs;
    }
   
   /**
   * Performs depth-first search starting in a Vertex   
   * @param g Graph instance
   * @param vOrig Vertex of graph g that will be the source of the search
   * @param visited set of discovered vertices
   * @param qdfs queue with vertices of depth-first search
   */
    private static<V,E> void DepthFirstSearch(Graph<V,E> g, V vOrig, LinkedList<V> qdfs, boolean[] visited) { //Adicionei o visited como parâmetro porque não existia mas penso que seja necessário
        qdfs.add(vOrig);
        visited[g.getKey(vOrig)] = true;

        for (V vAdj : g.adjVertices(vOrig)) {
            if (!visited[g.getKey(vAdj)]) {
                DepthFirstSearch(g, vAdj, qdfs, visited);
            }
        }
    }
  
   /**
   * @param g Graph instance
   * @param vInf information of the Vertex that will be the source of the search
   * @return qdfs a queue with the vertices of depth-first search 
   */
    public static<V,E> LinkedList<V> DepthFirstSearch(Graph<V,E> g, V vert){
        if (!g.validVertex(vert)) {
            return null;
        }

        LinkedList<V> qdfs = new LinkedList<>();
        boolean[] visited = new boolean[g.numVertices()];
        qdfs.add(vert);
        visited[g.getKey(vert)] = true;
        for (V vAdj : g.adjVertices(vert)) {
            if (!g.validVertex(vAdj)) {
                return null;
            }

            if (!visited[g.getKey(vAdj)]) {
                DepthFirstSearch(g, vAdj, qdfs, visited);
            }
        }
        return qdfs;
    }
   
    /**
   * Returns all paths from vOrig to vDest
   * @param g Graph instance
   * @param vOrig Vertex that will be the source of the path
   * @param vDest Vertex that will be the end of the path
   * @param visited set of discovered vertices
   * @param path stack with vertices of the current path (the path is in reverse order)
   * @param paths ArrayList with all the paths (in correct order)
   */
    private static<V,E> void allPaths(Graph<V,E> g, V vOrig, V vDest, boolean[] visited, LinkedList<V> path, ArrayList<LinkedList<V>> paths) {

        visited[g.getKey(vOrig)] = true;
        path.push(vOrig);
        for (V vAdj : g.adjVertices(vOrig)) {
            if (vAdj.equals(vDest)) {
                path.push(vDest);
                paths.add(path);
                path.pop();
            } else {
                if (!visited[g.getKey(vAdj)]) {
                    allPaths(g, vAdj, vDest, visited, path, paths);
                }
            }
        }
        V vertex = path.pop();
        visited[g.getKey(vertex)] = false;
    }
    
   /**
   * @param g Graph instance
   * @param voInf information of the Vertex origin
   * @param vdInf information of the Vertex destination 
   * @return paths ArrayList with all paths from voInf to vdInf 
   */
    public static<V,E> ArrayList<LinkedList<V>> allPaths(Graph<V,E> g, V vOrig, V vDest){
        if (!g.validVertex(vOrig) || !g.validVertex(vDest)) {
            return null;
        }

        ArrayList<LinkedList<V>> paths = new ArrayList<>();
        LinkedList<V> path = new LinkedList<>();
        boolean[] visited = new boolean[g.numVertices()];
        if (g.validVertex(vOrig) && g.validVertex(vDest)) {
            allPaths(g, vOrig, vDest, visited, path, paths);
        }
        return paths;
    }
    
    /**
   * Computes shortest-path distance from a source vertex to all reachable 
   * vertices of a graph g with nonnegative edge weights
   * This implementation uses Dijkstra's algorithm
   * @param g Graph instance
   * @param vOrig Vertex that will be the source of the path
   * @param visited set of discovered vertices
   * @param pathkeys minimum path vertices keys  
   * @param dist minimum distances
   */
    protected static<V,E> void shortestPathLength(Graph<V,E> g, V vOrig,
                                    boolean[] visited, int[] pathKeys, double[] dist){
//        dist[g.getKey(vOrig)] = 0;
//        while (vOrig != null) {
//            visited[g.getKey(vOrig)] = true;
//            for (V vAdj : g.adjVertices(vOrig)) {
//                Edge e = g.getEdge(vOrig, vAdj);
//                if (!visited[g.getKey(vAdj)] && dist[g.getKey(vAdj)] > (dist[g.getKey(vOrig)] + e.getWeight())) {
//                    dist[g.getKey(vAdj)] = dist[g.getKey(vOrig)] + e.getWeight();
//                    pathKeys[g.getKey(vAdj)] = g.getKey(vOrig);
//                }
//            }
//            vOrig = null;
//            Double minDistance = Double.MAX_VALUE;
//            for (V vert : g.vertices()) {
//                if (!visited[g.getKey(vert)] && dist[g.getKey(vert)] < minDistance) {
//                    vOrig = vert;
//                    minDistance = dist[g.getKey(vert)];
//                }
//            }
//        }
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    /**
    * Extracts from pathKeys the minimum path between voInf and vdInf
    * The path is constructed from the end to the beginning
    * @param g Graph instance
    * @param voInf information of the Vertex origin
    * @param vdInf information of the Vertex destination 
    * @param pathkeys minimum path vertices keys  
    * @param path stack with the minimum path (correct order)
    */
    protected static<V,E> void getPath(Graph<V,E> g, V vOrig, V vDest, V[] pathKeys, LinkedList<V> path){
//        path.push(vDest);
//        V vKey = pathKeys[g.getKey(vDest)];
//        if (vKey != -1) {
//            vDest = verts[vKey];
//            getPath(g, vOrig, vDest, verts, pathKeys, path);
//        }
        throw new UnsupportedOperationException("Not supported yet.");
    }

    //shortest-path between vOrig and vDest
    public static<V,E> double shortestPath(Graph<V,E> g, V vOrig, V vDest, LinkedList<V> shortPath){

        throw new UnsupportedOperationException("Not supported yet.");
    }
   
    //shortest-path between voInf and all other
    public static<V,E> boolean shortestPaths(Graph<V,E> g, V vOrig, ArrayList<LinkedList<V>> paths, ArrayList<Double> dists){
      
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
    /**
     * Reverses the path
     * @param path stack with path
     */
    private static<V,E> LinkedList<V> revPath(LinkedList<V> path){ 
   
        LinkedList<V> pathcopy = new LinkedList<>(path);
        LinkedList<V> pathrev = new LinkedList<>();
        
        while (!pathcopy.isEmpty())
            pathrev.push(pathcopy.pop());
        
        return pathrev ;
    }    
}