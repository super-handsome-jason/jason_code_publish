package org.ntu.csie.bda.datagen;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesDataItem;
import org.ntu.csie.bda.loader.DataLoader;
import org.ntu.csie.bda.noise.DropNoise;
import org.ntu.csie.bda.noise.Noise;
import org.ntu.csie.bda.noise.PeakNoise;

public class Generator {

	public Generator() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {

		String sampleFolder = "/Users/super_handsome_jason/jasoncode/FinalProject/data/tmp_esc_voltage/sample.dp0.03/";

		String filepath = "/Users/super_handsome_jason/jasoncode/FinalProject/data/tmp_esc_voltage/good.dat";
		TimeSeries ts1 = DataLoader.loadByfile(filepath);

		int good_datasize = 30;
		int bed1_datasize = 20;
		int bed2_datasize = 20;
		
		DropNoise dn = new DropNoise("", "", (float) 0.08);
		for (int i = 1; i <= good_datasize; i++) {
		genTSWithNoise(sampleFolder, ts1, dn, i);
		}

		//PeakNoise pn = new PeakNoise("", "", 5, (double) 0.05, 4);
		//for (int i = 1; i <= good_datasize; i++) {
		//	genTSWithNoise(sampleFolder, ts1, pn, i);
		//}
		
		filepath = "/Users/super_handsome_jason/jasoncode/FinalProject/data/tmp_esc_voltage/bed1.dat";
		ts1 = DataLoader.loadByfile(filepath);

		for (int i = 1; i <= bed1_datasize; i++) {
			genTSWithNoise(sampleFolder, ts1, dn, i);
		}

		// (int i = 1; i <= bed1_datasize; i++) {
		//	genTSWithNoise(sampleFolder, ts1, pn, i);
		//}
		
		filepath = "/Users/super_handsome_jason/jasoncode/FinalProject/data/tmp_esc_voltage/bed2.dat";
		ts1 = DataLoader.loadByfile(filepath);
		
		
		for (int i = 1; i <= bed2_datasize; i++) {
			genTSWithNoise(sampleFolder, ts1, dn, i);
		}

//		for (int i = 1; i <= bed2_datasize; i++) {
//			genTSWithNoise(sampleFolder, ts1, pn, i);
//		}


	}

	public static void genTSWithNoise(String sampleFolder, TimeSeries ts1,
			Noise nos, int index) {
		TimeSeries ts2 = nos.addNoise(ts1);
		ts2.setKey(ts2.getKey() + ".idx" + index);
		output(sampleFolder, ts2);
	}

	public static void output(String sampleFolder, TimeSeries ts) {
		// System.out.println(ts.getKey());
		try {

			File file = new File(sampleFolder + ts.getKey().toString());

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			for (Object item : ts.getItems()) {
				bw.write(((TimeSeriesDataItem) item).getValue().doubleValue()
						+ "\n");
			}
			bw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
