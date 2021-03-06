/*
 * DtwTest.java   Jul 14, 2004
 *
 * Copyright (c) 2004 Stan Salvador
 * stansalvador@hotmail.com
 */

package com;

import com.timeseries.TimeSeries;
import com.util.DistanceFunction;
import com.util.DistanceFunctionFactory;
import com.dtw.TimeWarpInfo;


public class DtwTest
{

   // PUBLIC FUNCTIONS
   public static void main(String[] args)
   {
//      if (args.length!=2 && args.length!=3)
//      {
//         System.out.println("USAGE:  java DtwTest timeSeries1 timeSeries2 [EuclideanDistance|ManhattanDistance|BinaryDistance]");
//         System.exit(1);
//      }
//      else
//      {
         final TimeSeries tsI = new TimeSeries("C:/Users/allen/Desktop/tool/workspace/FinalProject/data/tmp_esc_voltage/001.dat", false, false, ',');
         final TimeSeries tsJ = new TimeSeries("C:/Users/allen/Desktop/tool/workspace/FinalProject/data/tmp_esc_voltage/001.dat.drp0.1", false, false, ',');
         
         final DistanceFunction distFn;
//         if (args.length < 3)
//         {
            distFn = DistanceFunctionFactory.getDistFnByName("EuclideanDistance");  //ManhattanDistance   , BinaryDistance
//         }
//         else
//         {
//            distFn = DistanceFunctionFactory.getDistFnByName(args[2]);
//         }   // end if
         
         final TimeWarpInfo info = com.dtw.DTW.getWarpInfoBetween(tsI, tsJ, distFn);

         System.out.println("Warp Distance: " + info.getDistance());
         System.out.println("Warp Path:     " + info.getPath());
//      }  // end if

   }  // end main()

}  // end class DtwTest
