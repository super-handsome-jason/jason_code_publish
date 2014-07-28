package org.ntu.csie.bda.noise;

import java.util.Set;
import java.util.UUID;

import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesDataItem;

public class DropNoise extends Noise {
	private float percent;

	public DropNoise(String name, String desc, float percent) {
		super(name, desc);
		// TODO Auto-generated constructor stub
		this.percent = percent;
	}

	@Override
	public TimeSeries addNoise(TimeSeries tsSrc) {
		// TODO Auto-generated method stub
		TimeSeries tsDt = new TimeSeries(UUID.randomUUID());
		int iCount = tsSrc.getItemCount();
		Set<Integer> dropset = randomdrop(iCount, percent);

		int index = 0;
		for (Object item : tsSrc.getItems()) {
			if (!dropset.contains(index)) {
				tsDt.add((TimeSeriesDataItem) item);
			}else{
				//System.out.println("drop:" + index);
			}
			index++;
		}
		
		tsDt.setKey(tsSrc.getKey() + ".dp"+ percent);
		return tsDt;
	}


}
