package org.ntu.csie.bda.noise;

import java.util.Set;
import java.util.UUID;

import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesDataItem;

public class PeakNoise extends Noise {
	private int number;
	private double height;
	private int width;
	private int cent;
	private double offset;

	public PeakNoise(String name, String desc, int number, double height,
			int width) {
		super(name, desc);
		// TODO Auto-generated constructor stub
		this.number = number;
		this.height = height;
		this.width = width;
		this.cent = (width / 2) + 1;
		this.offset = height / (cent - 1);
		// System.out.println("cent:" + cent);
		// System.out.println("offset:" + offset);

	}

	@Override
	public TimeSeries addNoise(TimeSeries tsSrc) {
		// TODO Auto-generated method stub
		TimeSeries tsDt = new TimeSeries(UUID.randomUUID());
		int iCount = tsSrc.getItemCount();

		Set<Integer> dropset = randomdrop(iCount, number);

		int index = 0;
		int noiseindex = 1;
		TimeSeriesDataItem tmp;
		for (Object item : tsSrc.getItems()) {
			tmp = (TimeSeriesDataItem) item;
			if (!dropset.contains(index)) {
				noiseindex = 1;
				tmp = addOffset(tmp, offset, noiseindex);
				tsDt.add(tmp);
			} else if (noiseindex <= width) {
				tmp = addOffset(tmp, offset, noiseindex);
				tsDt.add(tmp);
			} else {
				// System.out.println("drop:" + index);
			}
			noiseindex++;
			index++;
		}

		tsDt.setKey(tsSrc.getKey() + ".pk-" + number + "-" + height + "-"
				+ width);
		return tsDt;
	}

	private TimeSeriesDataItem addOffset(TimeSeriesDataItem src, double offset,
			int index) {
		double value = (double) src.getValue().doubleValue();
		value += offset * (index - 1);

		src.setValue(value);
		return src;
	}

}
