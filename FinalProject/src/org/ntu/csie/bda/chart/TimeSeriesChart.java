package org.ntu.csie.bda.chart;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.RefineryUtilities;
import org.ntu.csie.bda.loader.DataLoader;

public class TimeSeriesChart  extends ApplicationFrame{

    /**
     * A demonstration application showing how to create a simple time series 
     * chart.  This example uses monthly data.
     *
     * @param title  the frame title.
     */
    public TimeSeriesChart(String title) {
        super(title);
        
    }
    
    public void createNewChart(XYDataset dataset){
    	ChartPanel chartPanel = (ChartPanel) createDemoPanel(dataset);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 300));
        chartPanel.setMouseZoomable(true, false);
        setContentPane(chartPanel);
    }

    /**
     * Creates a chart.
     * 
     * @param dataset  a dataset.
     * 
     * @return A chart.
     */
    private static JFreeChart createChart(XYDataset dataset) {

        JFreeChart chart = ChartFactory.createTimeSeriesChart(
            "CH:E",  // title
            "Timestamp",             // x-axis label
            "Pressure",   // y-axis label
            dataset,            // data
            false,               // create legend?
            false,               // generate tooltips?
            false               // generate URLs?
        );

        chart.setBackgroundPaint(Color.white);

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        plot.setAxisOffset(new RectangleInsets(1.0,1.0, 1.0, 1.0));
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);
        
        XYItemRenderer r = plot.getRenderer();
        if (r instanceof XYLineAndShapeRenderer) {
            XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
            //renderer.setBaseShapesVisible(true);
            renderer.setBaseShapesFilled(false);
        }
        
        DateAxis axis = (DateAxis) plot.getDomainAxis();
        axis.setDateFormatOverride(new SimpleDateFormat("MMM-yyyy"));
        
        return chart;

    }
    
    /**
     * Creates a dataset, consisting of two series of monthly data.
     *
     * @return The dataset.
     */
    private static XYDataset createDataset() {

        TimeSeries s1 = new TimeSeries("L&G European Index Trust", Month.class);
        s1.add(new Month(2, 2001), 181.8);
        s1.add(new Month(3, 2001), 167.3);
        s1.add(new Month(4, 2001), 153.8);
        s1.add(new Month(5, 2001), 167.6);
        s1.add(new Month(6, 2001), 158.8);
        s1.add(new Month(7, 2001), 148.3);
        s1.add(new Month(8, 2001), 153.9);
        s1.add(new Month(9, 2001), 142.7);
        s1.add(new Month(10, 2001), 123.2);
        s1.add(new Month(11, 2001), 131.8);
        s1.add(new Month(12, 2001), 139.6);
        s1.add(new Month(1, 2002), 142.9);
        s1.add(new Month(2, 2002), 138.7);
        s1.add(new Month(3, 2002), 137.3);
        s1.add(new Month(4, 2002), 143.9);
        s1.add(new Month(5, 2002), 139.8);
        s1.add(new Month(6, 2002), 137.0);
        s1.add(new Month(7, 2002), 132.8);

        TimeSeries s2 = new TimeSeries("L&G UK Index Trust", Month.class);
        s2.add(new Month(2, 2001), 129.6);
        s2.add(new Month(3, 2001), 123.2);
        s2.add(new Month(4, 2001), 117.2);
        s2.add(new Month(5, 2001), 124.1);
        s2.add(new Month(6, 2001), 122.6);
        s2.add(new Month(7, 2001), 119.2);
        s2.add(new Month(8, 2001), 116.5);
        s2.add(new Month(9, 2001), 112.7);
        s2.add(new Month(10, 2001), 101.5);
        s2.add(new Month(11, 2001), 106.1);
        s2.add(new Month(12, 2001), 110.3);
        s2.add(new Month(1, 2002), 111.7);
        s2.add(new Month(2, 2002), 111.0);
        s2.add(new Month(3, 2002), 109.6);
        s2.add(new Month(4, 2002), 113.2);
        s2.add(new Month(5, 2002), 111.6);
        s2.add(new Month(6, 2002), 108.8);
        s2.add(new Month(7, 2002), 101.6);

        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(s1);
        dataset.addSeries(s2);

        dataset.setDomainIsPointsInTime(true);

        return dataset;

    }

    /**
     * Creates a panel for the demo (used by SuperDemo.java).
     * 
     * @return A panel.
     */
    public static JPanel createDemoPanel(XYDataset dataset) {
        JFreeChart chart = createChart(dataset);
        return new ChartPanel(chart);
    }
    
    /**
     * Starting point for the demonstration application.
     *
     * @param args  ignored.
     */
    public static void main(String[] args) {
    	showTemplate();
//    	String folder = "C:/Users/allen/Desktop/tool/workspace/FinalProject/data/tmp_esc_voltage/sample/";
//    	List<String> fileLst = new ArrayList<String>(); 
//    	fileLst.add(folder +"bed1.dat.dp0.1.idx11");
//    	fileLst.add(folder +"bed1.dat.dp0.1.idx12");
//    	fileLst.add(folder +"bed1.dat.dp0.1.idx13");
//    	fileLst.add(folder +"bed1.dat.dp0.1.idx14");
//    	fileLst.add(folder +"bed1.dat.dp0.1.idx15");
//    	fileLst.add(folder +"bed1.dat.dp0.1.idx16");
//    	fileLst.add(folder +"bed1.dat.dp0.1.idx17");
//    	fileLst.add(folder +"bed1.dat.dp0.1.idx18");
//    	String chartName = "test";
//    	displayByFileList(fileLst,  chartName);

    }

	public static void showTemplate() {
		String filepath = "/Users/super_handsome_jason/jasoncode/FinalProject/data/tmp_esc_voltage/good.dat";
		TimeSeries tsGood = DataLoader.loadByfile(filepath);
    	
    	filepath = "/Users/super_handsome_jason/jasoncode/FinalProject/data/tmp_esc_voltage/bed1.dat";
		TimeSeries tsBed1 = DataLoader.loadByfile(filepath);
		
    	//filepath = "/Users/super_handsome_jason/jasoncode/FinalProject/data/tmp_esc_voltage/good.dat.pk-5-0.15-7.idx1";
		filepath = "/Users/super_handsome_jason/jasoncode/FinalProject/data/tmp_esc_voltage/bed2.dat";
		TimeSeries tsBed2 = DataLoader.loadByfile(filepath);
		
    	 TimeSeriesCollection dataset = new TimeSeriesCollection();
         dataset.addSeries(tsGood);
         dataset.addSeries(tsBed1);
         dataset.addSeries(tsBed2);
    	
    	
    	TimeSeriesChart demo = new TimeSeriesChart("chart");
    	demo.createNewChart(dataset);
    	//demo.createNewChart(createDataset());
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
	}
    
    public static void displayByFileList(List<String> fileLst, String chartName){
    	 TimeSeriesCollection dataset = new TimeSeriesCollection();
    	 TimeSeries tsTmp = null;
    	for(String fname: fileLst){
    		tsTmp = DataLoader.loadByfile(fname);
            dataset.addSeries(tsTmp);
    	}
    	
    	TimeSeriesChart demo = new TimeSeriesChart(chartName);
    	demo.createNewChart(dataset);
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);

    }
}
