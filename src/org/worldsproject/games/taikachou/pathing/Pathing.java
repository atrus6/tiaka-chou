/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.worldsproject.games.taikachou.pathing;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.worldsproject.games.taikachou.Cell;
import org.worldsproject.games.taikachou.Edge;

/**
 *
 * @author atrus
 */
public class Pathing {

    private UndirectedGraph<Cell, DefaultEdge> paths = new SimpleGraph<Cell, DefaultEdge>(DefaultEdge.class);

    public Pathing(List<Edge> edgeList) {
        for (Edge e : edgeList) {
            paths.addVertex(e.getStartCell());
            paths.addVertex(e.getEndCell());
            paths.addEdge(e.getStartCell(), e.getEndCell());
        }
    }

    public Cell getRandomDirection(Cell currentLocation) {
        Set<DefaultEdge> s = paths.edgesOf(currentLocation);
        Random r = new Random();
        Object[] ea = s.toArray();
        for(Object o: ea) {
            System.out.print(o + " ");
        }
        System.out.println();
        DefaultEdge de = (DefaultEdge)(ea[r.nextInt(ea.length)]);
        
        Cell rv = paths.getEdgeTarget(de);
        
        if(!rv.equals(currentLocation)) {
            System.out.println(rv + " " + currentLocation);
            return rv;
        } else {
            System.out.println("Backtrack");
            return paths.getEdgeSource(de);
        }
    }
    
    public ArrayList<Cell> getPath(Cell currentLocation, Cell toLocation) {
        List<DefaultEdge> path = DijkstraShortestPath.findPathBetween(paths, currentLocation, toLocation);
        
        ArrayList<Cell> rv = new ArrayList<Cell>();
        
        for(DefaultEdge e: path) {
            rv.add(paths.getEdgeTarget(e));
        }
        
        return rv;
    }
}
