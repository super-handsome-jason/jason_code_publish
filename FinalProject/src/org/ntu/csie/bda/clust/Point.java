package org.ntu.csie.bda.clust;
/**
 * 
 * 
 * 
 * @author allen
 *
 */
public class Point {
	private double x;
	private double y;
	
	public boolean equals(Object o) {
		Point c = (Point) o;
		return c.x == x && c.y == y;
	}

	public Point(double x, double y) {
		super();
		this.x = x;
		this.y = y;
	}

	public int hashCode() {
		return new Integer(x + "0" + y);
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
	
	
}
