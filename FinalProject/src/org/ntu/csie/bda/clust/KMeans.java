package org.ntu.csie.bda.clust;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.ntu.csie.bda.chart.TimeSeriesChart;

import com.dtw.TimeWarpInfo;
import com.timeseries.TimeSeries;
import com.util.DistanceFunction;
import com.util.DistanceFunctionFactory;

public class KMeans {
	// Data members
	private TimeSeries[] points; // Array of all points in dataset
	private Cluster[] clusters; // Array of all clusters; null until
								// performClustering is called.
	private HashMap<String, Double> disCache = new HashMap<String, Double>();
	final int RANGE = 100;
	final int ITERATION = 10;
	final static DistanceFunction distFn;
	static {
		distFn = DistanceFunctionFactory.getDistFnByName("EuclideanDistance"); // ManhattanDistance
																				// ,
																				// BinaryDistance
	}

	public KMeans() {
		clusters = null;
		// points = random(100); // for 2.1
		// points = randomND(); // for 2.2
		// points = randomMinND(); // for 2.3
	}

	public void setPoints(TimeSeries[] points) {
		this.points = points;
	}

	// gets the array of all points in the dataset.
	public TimeSeries[] getPoints() {
		return points;
	}

	// Gets the array of all clusters after performing k-means clustering. Note
	// that this method will return null if performClustering has not yet been
	// called.
	public Cluster[] getClusters() {
		return clusters;
	}

	// Perform k-means clustering with the specified number of clusters and
	// distance metric.
	public void performClustering(int numClusters) {
		// testDoNothing(numClusters); // a test
		byEuclidean(numClusters);

	}

	public void byEuclidean(int numClusters) {
//		System.out.println("byEuclidean start");
		// initial cluster
		TimeSeries[] centroids = rendomPickup(numClusters, this.points);
		Cluster[] tmpclusters = new Cluster[numClusters];
		Cluster c;
		int i = 0;
		for (TimeSeries p : centroids) {
			System.out.println("centroid :"+ p.inputfile);
			c = new Cluster();
			c.setCentroid(p);
			tmpclusters[i] = c;
			i++;
		}
		for (int j = 1; j < ITERATION; j++) {
			System.out.println("ITERATION :"+ j);
			
			tmpclusters = cleanCluster(tmpclusters);
			clusters = assignCluster(tmpclusters, points);
			clusters = updateCentroids(clusters);
			tmpclusters = clusters;
		}
	}
	
	public Cluster[] updateCentroids(Cluster[] tmpclusters) {
		for (int i = 0; i < tmpclusters.length; i++) {
			TimeSeries centroid = findCentroid(tmpclusters[i]);
			System.out.println("centroid :"+ centroid.inputfile);
			tmpclusters[i].setCentroid(centroid);
		}
		return tmpclusters;
	}
	
	public TimeSeries findCentroid(Cluster tmpcluster) {
		double distsum;
		double distavg;
		double distbest = -1;
		TimeSeries newcentroid = null;
		int size =tmpcluster.getPoints().size();
		for (TimeSeries tssrc: tmpcluster.getPoints()) {
			distsum = 0;
			for (TimeSeries tsdt: tmpcluster.getPoints()) {
				distsum +=calDTWnDistance(tssrc, tsdt);
			}
			
			distavg = distsum/size;
			//System.out.println(distavg);
			
			if(distbest == -1 || distbest > distavg){
				distbest = distavg;
				newcentroid = tssrc;
			}
		}
		return newcentroid;
	}

	public Cluster[] cleanCluster(Cluster[] tmpclusters) {
		for (int i = 0; i < tmpclusters.length; i++) {
			tmpclusters[i].clean();
		}
		return tmpclusters;
	}

	public Cluster[] assignCluster(Cluster[] tmpclusters, TimeSeries[] points) {
		int iNearest;
		for (TimeSeries p : points) {
			iNearest = findNearest(p, tmpclusters);
			tmpclusters[iNearest].addPoint(p);
		}
		return tmpclusters;
	}

	public double calEuclideanDistance(Point p1, Point p2) {
		return Math.sqrt(Math.pow((p1.getX() - p2.getX()), 2.0)
				+ Math.pow((p1.getY() - p2.getY()), 2.0));
	}

	public double calDTWnDistance(TimeSeries tsI, TimeSeries tsJ) {
		if(disCache.containsKey(tsI.inputfile+tsJ.inputfile)){
			return disCache.get(tsI.inputfile+tsJ.inputfile);
		}
		
		TimeWarpInfo info = com.dtw.DTW.getWarpInfoBetween(tsI, tsJ, distFn);
		
		disCache.put(tsI.inputfile+tsJ.inputfile, info.getDistance());
		return info.getDistance();
	}

	public int findNearest(TimeSeries p, Cluster[] tmpclusters) {
		int iNearest = 0;
		double dNearest = -1;
		double distance;
		TimeSeries centroid;
		for (int i = 0; i < tmpclusters.length; i++) {
			centroid = tmpclusters[i].getCentroid();
			distance = calDTWnDistance(p, centroid);
			if (dNearest == -1 || dNearest > distance) {
				iNearest = i;
				dNearest = distance;
			}
		}
		return iNearest;
	}

	public TimeSeries[] rendomPickup(int numClusters, TimeSeries[] points) {
		Random rand = new Random();
		List<Integer> pickNum = new ArrayList<Integer>();
		int randNum;
		TimeSeries[] centroids = new TimeSeries[numClusters];
		for (int i = 0; i < numClusters; i++) {
			randNum = rand.nextInt(points.length);
			while (pickNum.contains(randNum)) {
				randNum = rand.nextInt(points.length);
			}
			pickNum.add(randNum);
			centroids[i] = points[randNum];
		}
		return centroids;
	}

	public void testDoNothing(int numClusters) {
		clusters = new Cluster[numClusters];
		Cluster c;
		TimeSeries p;
		for (int j = 0; j < numClusters; j++) {
			c = new Cluster();
			for (int i = 0; i < points.length; i++) {
				p = points[i];
				// System.out.println("(" + p.getX() + "," + p.getY() + ")");
				c.addPoint(p);
			}
			clusters[j] = c;
		}
	}

	public Point[] random(int numPoints) {
		Point[] points = new Point[numPoints];
		Random rand = new Random();
		Point p;

		for (int i = 0; i < points.length; ++i) {
			points[i] = new Point(rand.nextInt(RANGE) + 1,
					rand.nextInt(RANGE) + 1);
		}

		return points;
	}

	public Point[] randomND() {
		Point[] points = random(2);
		double distance = calEuclideanDistance(points[0], points[1]);
		double std = distance / 4;
		List<Point> pointsa = randomND(points[0], std, 50);
		List<Point> pointsb = randomND(points[1], std, 50);
		pointsa.addAll(pointsb);
		return pointsa.toArray(new Point[100]);
	}

	public Point[] randomMinND() {
		Point[] points = random(3);
		double distance;
		double distance1 = calEuclideanDistance(points[0], points[1]);
		double distance2 = calEuclideanDistance(points[0], points[2]);
		double distance3 = calEuclideanDistance(points[1], points[2]);
		if (distance1 < distance2) {
			if (distance1 < distance3) {
				distance = distance1;
			} else {
				distance = distance3;
			}
		} else {
			if (distance2 < distance3) {
				distance = distance2;
			} else {
				distance = distance3;
			}
		}

		double std = distance / 4;
		List<Point> pointsa = randomND(points[0], std, 50);
		List<Point> pointsb = randomND(points[1], std, 50);
		List<Point> pointsc = randomND(points[2], std, 50);
		pointsa.addAll(pointsb);
		pointsa.addAll(pointsc);
		return pointsa.toArray(new Point[150]);
	}

	public List<Point> randomND(Point mean, double std, int numPoints) {
		List<Point> points = new ArrayList<Point>();
		Random rand = new Random();
		Point p;

		for (int i = 0; i < numPoints; ++i) {
			points.add(new Point(mean.getX() + std * rand.nextGaussian(), mean
					.getY() + std * rand.nextGaussian()));
		}

		return points;
	}

	public void printPoints() {
		for (TimeSeries p : getPoints()) {
			// System.out.println("(" + p.getX() + "," + p.getY() + ")");
		}
	}

	public void display() {

		// ScatterPlot demo = new ScatterPlot("K-means ", this.clusters);
		// demo.pack();
		// demo.setVisible(true);
	}

	public static void main(String[] astrArgs) {
		// TODO
		// Add code here to make a new KMeans object (which will load the data).
		// Then perform the clustering and output the results to the screen
		// and create the image files

		/*
		 * The code commented out here is just an example of how to use the
		 * provided functions and constructors.
		 * 
		 * KMeans KM = new KMeans( "yeast_stress.pcl" ); Cluster C = new
		 * Cluster( ); for( int i = 0; i < KM.getGenes( ).length; ++i )
		 * C.addGene( KM.getGenes( )[i] ); C.createJPG( "test", 1 );
		 */

		// Point p1 = new Point(2.0, 2.0);
		// Point p2 = new Point(5.0, 6.0);
		// System.out.println(calEuclideanDistance(p1, p2));

		List<TimeSeries> sampleLst = new ArrayList<TimeSeries>();

		String folderpath = "/Users/super_handsome_jason/jasoncode/FinalProject/data/tmp_esc_voltage/sample.dp0.03/";
		List<TimeSeries> tsLst = loadByFolder(folderpath);
		sampleLst.addAll(tsLst);

		KMeans KM = new KMeans();
		KM.setPoints(sampleLst.toArray(new TimeSeries[sampleLst.size()]));

		int numClusters = 3;

		// for (int i = 0; i < 6; i++) {
		KM.performClustering(numClusters);
		
		int index = 1;
		for(Cluster cl :KM.getClusters()){
			List<String> fileLst = new ArrayList<String>(); 
			for(TimeSeries ts :cl.getPoints()){
				fileLst.add(ts.inputfile);
			}
			TimeSeriesChart.displayByFileList(fileLst, "Cluster " + index + " ("+cl.getPoints().size()+")");
			index++;
		}
		//KM.display();
		// }
	}

	public static List<TimeSeries> loadByFolder(String folder) {
		List<TimeSeries> tsLst = new ArrayList<TimeSeries>();
		File srcfolder = new File(folder);
		for (File f : srcfolder.listFiles()) {
			TimeSeries ts = new TimeSeries(folder + f.getName(), false, false,
					',');
			tsLst.add(ts);
		}
		return tsLst;
	}
}