package org.worldsproject.games.taikachou;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;

public class Maze {
    
    public static void main(String args[]) {
        Maze m = new Maze();
        ArrayList<Edge> g = m.generate(20,20);
        for(Edge e: g) {
            System.out.println(e);
        }
    }

    public ArrayList<Edge> generate(int width, int height) {
        ArrayList<Cell> unvisited = new ArrayList<Cell>();
        ArrayList<Cell> ends = new ArrayList<Cell>();
        LinkedHashSet<Edge> path = new LinkedHashSet<Edge>();
        LinkedList<Edge> edgesAvailable = new LinkedList<Edge>();
        
        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                    unvisited.add(new Cell(x, y));
            }
        }
        
        unvisited.remove(new Cell(0,0));
        
        Cell vertex = new Cell(0,0);
        
        while(!unvisited.isEmpty()) {
            addEdges(vertex, unvisited, ends, edgesAvailable);
            
            Edge e = edgesAvailable.remove();
            if(unvisited.remove(e.getEndCell())) {
                path.add(e);
                vertex = e.getEndCell();
            }
            Collections.shuffle(edgesAvailable);
        }

        return new ArrayList(Arrays.asList(path.toArray(new Edge[]{})));
    }
    
    private void addEdges(Cell visited, ArrayList<Cell> unvisited, ArrayList<Cell> ends, LinkedList<Edge> edgesAvailable) {
        Edge one = new Edge(visited, new Cell(visited.x+1, visited.y));
        Edge two = new Edge(visited, new Cell(visited.x-1, visited.y));
        Edge three = new Edge(visited, new Cell(visited.x, visited.y+1));
        Edge four = new Edge(visited, new Cell(visited.x, visited.y-1));
        
        if(unvisited.contains(one.getEndCell()) && !ends.contains(one.getEndCell())) {
            edgesAvailable.add(one);
            ends.add(one.getEndCell());
        }
        
        if(unvisited.contains(two.getEndCell()) && !ends.contains(two.getEndCell())) {
            edgesAvailable.add(two);
            ends.add(two.getEndCell());
        }
        
        if(unvisited.contains(three.getEndCell()) && !ends.contains(three.getEndCell())) {
            edgesAvailable.add(three);
            ends.add(three.getEndCell());
        }
        
        if(unvisited.contains(four.getEndCell()) && !ends.contains(four.getEndCell())) {
            edgesAvailable.add(four);
            ends.add(four.getEndCell());
        }
    }
}