package org.ntu.csie.bda.clust;

import java.util.Vector;
import com.timeseries.TimeSeries;

public class Cluster {

	// Data members
	private Vector<TimeSeries> pointVector; // List of points in cluster
	private TimeSeries centroid;

	// Constructor; creates an empty cluster.
	public Cluster() {
		// TODO
		// make a new Vector of objects of type Point
		pointVector = new Vector<TimeSeries>();
	}
	
	public void clean(){
		pointVector = new Vector<TimeSeries>();
	}
	
	public TimeSeries getCentroid() {
		return centroid;
	}


	public void setCentroid(TimeSeries centroid) {
		this.centroid = centroid;
	}


	public void addPoint(TimeSeries point) {
		pointVector.add(point);
	}
	
	public Vector<TimeSeries> getPoints(){
		return pointVector;
	}

}
