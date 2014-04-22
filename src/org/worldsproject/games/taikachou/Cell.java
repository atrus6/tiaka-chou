package org.worldsproject.games.taikachou;

public class Cell
{	
	int x;
	int y;
	
	public Cell(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + this.x;
        hash = 71 * hash + this.y;
        return hash;
    }
        
        @Override
        public boolean equals(Object o) {
            if(o instanceof Cell == false) {
                return false;
            }
            
            Cell test = (Cell)o;
            
            if(test.x == this.x && test.y == this.y) {
                return true;
            }
            
            return false;
        }
	
	@Override
	public String toString()
	{
		return "(" + x + ", " + y + ")";
	}
}