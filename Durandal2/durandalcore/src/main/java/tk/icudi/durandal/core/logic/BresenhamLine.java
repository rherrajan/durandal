package tk.icudi.durandal.core.logic;

import java.io.Serializable;

class BresenhamLine implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int x0, y0;
	private int x1, y1;
	private int dx, dy;
	private int e2, err;
	private final int sx, sy;
	
	public BresenhamLine(Point origin, Point destination) {
		this.x0 = origin.x;
		this.y0 = origin.y;
		this.x1 = destination.x;
		this.y1 = destination.y;
		
		dx = Math.abs(x1 - x0);
		sx = x0 < x1 ? 1 : -1;
		dy = -Math.abs(y1 - y0);
		sy = y0 < y1 ? 1 : -1;
		err = dx + dy;
	}
	
	boolean isDone(){

//		int toMove = Math.abs(x0-x1) + Math.abs(y0-y1);
		
		return (x0==x1 && y0==y1);
	}
	
	Point next() {

	    this.e2 = 2 * this.err;
	    if (e2 > dy) { err += dy; x0 += sx; }
	    if (e2 < dx) { err += dx; y0 += sy; } 
		return new Point(x0, y0);
	}
}
