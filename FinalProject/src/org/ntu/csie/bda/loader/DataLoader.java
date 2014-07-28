package org.ntu.csie.bda.loader;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;

public class DataLoader {
	static Millisecond basemillisecond;
	static {

		basemillisecond = new Millisecond();
	}

	public static void main(String[] args) {
//		String filepath = "C:/Users/allen/Desktop/tool/workspace/FinalProject/data/tmp_esc_voltage/good.dat";
//		TimeSeries ts = loadByfile(filepath);
//		System.out.println(ts.getKey());
		

		String folderpath = "C:/Users/allen/Desktop/tool/workspace/FinalProject/data/tmp_esc_voltage/sample/";
		List<TimeSeries> tsLst = loadByFolder(folderpath);
		System.out.println(tsLst.size());
		System.out.println(tsLst.get(0).getKey());
	}
	
	public static List<TimeSeries> loadByFolder(String folder) {
		List<TimeSeries> tsLst = new ArrayList<TimeSeries>(); 
		File srcfolder = new File(folder);
		for(File f:srcfolder.listFiles()){
			TimeSeries ts = loadByfile(folder+f.getName());
			tsLst.add(ts);
		}
		return tsLst;
	}

	public static TimeSeries loadByfile(String filepath) {
		String key = filepath.substring(filepath.lastIndexOf("/")+1);
		//System.out.println("key:" + key);
		TimeSeries ts = new TimeSeries(key);
		try {
			List<String> data = read(filepath);
			// long l = System.currentTimeMillis();
			Millisecond millisecond = basemillisecond;
			for (String d : data) {
				Double dvalue = Double.parseDouble(d);
				millisecond = (Millisecond) millisecond.next();
				ts.add(millisecond, dvalue);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ts;

	}

	public static List<String> read(String filepath) throws IOException {
		List<String> data = new ArrayList<String>();

		try {
			// Open the file that is the first
			// command line parameter
			FileInputStream fstream = new FileInputStream(filepath);
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			// Read File Line By Line
			while ((strLine = br.readLine()) != null) {
				// Print the content on the console
				data.add(strLine);
			}
			// Close the input stream
			in.close();
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
		return data;
	}
}
