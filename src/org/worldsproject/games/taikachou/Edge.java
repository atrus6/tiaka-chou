package org.worldsproject.games.taikachou;

import java.util.Random;

public class Edge implements Comparable<Edge>
{
	private Cell startCell;
	private Cell endCell;
	
	public Edge(Cell start, Cell end)
	{
		startCell = start;
		endCell = end;
	}
	
	public Cell getStartCell()
	{
		return startCell;
	}

	public Cell getEndCell()
	{
		return endCell;
	}
        
    @Override
        public String toString() {
            return "(" + startCell.x + ", " + startCell.y + ") to (" + endCell.x + ", " + endCell.y + ")";
        }
        
    @Override
        public boolean equals(Object o) {
            if(o instanceof Edge) {
                Edge test = (Edge)o;
                
                if(test.startCell == this.startCell && test.endCell == this.endCell) {
                    return true;
                }
            }
            
            return false;
        }

    public int compareTo(Edge t) {
        if(this.equals(t)) {
            return 0;
        }
       return 1;
    }
}