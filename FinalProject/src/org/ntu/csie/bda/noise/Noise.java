package org.ntu.csie.bda.noise;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.jfree.data.time.TimeSeries;

public class Noise {
	private String name;
	private String desc;
	
	public Noise(String name, String desc){
		this.name = name;
		this.desc = desc;
	}
	
	public TimeSeries addNoise(TimeSeries tsSrc){
		return tsSrc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Set<Integer> randomdrop(int iTotalCount, float percent) {
		Random rand = new Random();

		Set<Integer> dropset = new HashSet<Integer>();
		int points = (int) (iTotalCount * percent);
		while (dropset.size() != points) {
			int num = (int) (Math.random() * iTotalCount);
			dropset.add(num);
		}

		//System.out.print("dropset.size():" + dropset.size());
		return dropset;
	}
	

	public Set<Integer> randomdrop(int iTotalCount, int points) {
		Random rand = new Random();

		Set<Integer> dropset = new HashSet<Integer>();
		while (dropset.size() != points) {
			int num = (int) (Math.random() * iTotalCount);
			dropset.add(num);
		}

		//System.out.print("dropset.size():" + dropset.size());
		return dropset;
	}
	
}
