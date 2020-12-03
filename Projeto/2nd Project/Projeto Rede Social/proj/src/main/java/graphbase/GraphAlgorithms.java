/*
* A collection of graph algorithms.
*/
package graphbase;

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
        LinkedList<V> qaux = new LinkedList<>();

        qbfs.add(vert);
        qaux.add(vert);

        while (!qaux.isEmpty()) {
            vert = qaux.remove(0);
            for (V vAdj : g.adjVertices(vert)) {
                if (!qbfs.contains(vAdj)) {
                    qbfs.add(vAdj);
                    qaux.add(vAdj);
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
    private static<V,E> void DepthFirstSearch(Graph<V,E> g, V vOrig, boolean[] visited, LinkedList<V> qdfs) {
        qdfs.add(vOrig);
        int vKey = g.getKey(vOrig);
        visited[vKey] = true;

        for (V vAdj : g.adjVertices(vOrig)) {
            vKey = g.getKey(vAdj);
            if (!visited[vKey]) {
                DepthFirstSearch(g, vAdj, visited, qdfs);
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

        DepthFirstSearch(g, vert, visited, qdfs);

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

        path.push(vOrig);
        int vKey = g.getKey(vOrig);
        visited[vKey] = true;

        for (V vAdj : g.adjVertices(vOrig)) {
            if (vAdj.equals(vDest)) {
                path.push(vAdj);
                LinkedList<V> revpath = revPath(path);
                paths.add(new LinkedList(revpath)); //save clone of reverse path
                path.pop();
            } else {
                vKey = g.getKey(vAdj);
                if (!visited[vKey]) {
                    allPaths(g, vAdj, vDest, visited, path, paths);
                }
            }
        }
        V vElem = path.pop();
        vKey = g.getKey(vElem);
        visited[vKey] = false;
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
    protected static<V,E> void shortestPathLength(Graph<V,E> g, V vOrig, V[] vertices, boolean[] visited, int[] pathKeys, double[] dist) {
        int vKey = g.getKey(vOrig);
        dist[vKey] = 0;
        while (vKey != -1) {
            vOrig = vertices[vKey];
            visited[vKey] = true;

            for (V vAdj : g.adjVertices(vOrig)) {
                int vKeyAdj = g.getKey(vAdj);
                Edge<V, E> edge = g.getEdge(vOrig, vAdj);

                if (!visited[vKeyAdj] && dist[vKeyAdj] > dist[vKey] + edge.getWeight()) {
                    dist[vKeyAdj] = dist[vKey] + edge.getWeight();
                    pathKeys[vKeyAdj] = vKey;
                }
            }
            Double minDist = Double.MAX_VALUE;
            vKey = -1;

            for (int i = 0; i < g.numVertices(); i++) {
                if (visited[i] == false && dist[i] < minDist) {
                    minDist = dist[i];
                    vKey = i;
                }
            }
        }
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
    protected static<V,E> void getPath(Graph<V,E> g, V vOrig, V vDest, V[] verts, int[] pathKeys, LinkedList<V> path) { //Tive que acrescentar parâmetros
        if (!vOrig.equals(vDest)) {
            path.push(vDest);
            int vKey = g.getKey(vDest);
            int prevVKey = pathKeys[vKey];
            vDest = verts[prevVKey];

            getPath(g, vOrig, vDest, verts, pathKeys, path);
        } else {
            path.push(vOrig);
        }
    }

    //shortest-path between vOrig and vDest
    public static<V,E> double shortestPath(Graph<V,E> g, V vOrig, V vDest, LinkedList<V> shortPath){
        if (!g.validVertex(vOrig) || !g.validVertex(vDest)) {
            return 0;
        }

        int nverts = g.numVertices();
        boolean[] visited = new boolean[nverts];
        int[] pathKeys = new int[nverts];
        double[] dist = new double[nverts];
        V[] vertices = g.getAllKeys();

        for (int i = 0; i < nverts; i++) {
            dist[i] = Double.MAX_VALUE;
            pathKeys[i] = -1;
        }

        shortestPathLength(g, vOrig, vertices, visited, pathKeys, dist);

        double lengthPath = dist[g.getKey(vDest)];

        if (lengthPath != Double.MAX_VALUE) {
            getPath(g, vOrig, vDest, vertices, pathKeys, shortPath);
            return lengthPath;
        }

        return 0;
    }
   
    //shortest-path between voInf and all other
    public static<V,E> boolean shortestPaths(Graph<V,E> g, V vOrig, ArrayList<LinkedList<V>> paths, ArrayList<Double> dists) {
        if (!g.validVertex(vOrig)) {
            return false;
        }

        int nverts = g.numVertices();
        boolean[] visited = new boolean[nverts];
        int[] pathKeys = new int[nverts];
        double[] dist = new double[nverts];
        V[] vertices = g.getAllKeys();

        for (int i = 0; i < nverts; i++) {
            dist[i] = Double.MAX_VALUE;
            pathKeys[i] = -1;
        }

        shortestPathLength(g, vOrig, vertices, visited, pathKeys, dist);

        dists.clear();
        paths.clear();

        for (int i = 0; i < nverts; i++) {
            paths.add(null);
            dists.add(null);
        }

        for (int i = 0; i < nverts; i++) {
            LinkedList<V> shortPath = new LinkedList<>();
            if (dist[i] != Double.MAX_VALUE) {
                getPath(g, vOrig, vertices[i], vertices, pathKeys, shortPath);
            }
            paths.set(i, shortPath);
            dists.set(i, dist[i]);
        }
        return true;
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