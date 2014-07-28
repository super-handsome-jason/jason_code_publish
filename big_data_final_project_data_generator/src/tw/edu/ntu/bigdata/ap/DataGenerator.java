package tw.edu.ntu.bigdata.ap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Date;
import java.util.Random;

/******************************************************************************
 * 這是一個產生資料的類別
 * @author superhandsomejason
 * @since 1.7
 *****************************************************************************/
public class DataGenerator
{
	public static final Random rr = new Random((new Date()).getTime());
	
	public String[] processData(String[] original_data, NoiseType noise_type, 
										NoiseLevel noise_lv, int noise_count)
	{
		if (original_data==null || original_data.length<1 || noise_type==null ||
				noise_lv==null || noise_count<=0)
			return original_data;
		//轉換容器
		
		
		return null;
	}
	
	public double getNormalDistributionValue()
	{
		return rr.nextGaussian();
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		//預設參數
		args = new String[]{"~/Downloads/input_data1.txt", "Rough", "Lv2", "~/Downloads/output_data1.txt"};

		//檢查參數
		if (args==null || args.length!=4)
		{
			System.out.println("Please input 4 parameters to execute this application!!");
			System.out.println("1: The full path source file which contain within time serious data and separate with space.");
			System.out.println("2: The noise type keyword. eg: \"Rough\" or \"Smooth\"");
			System.out.println("3: The noise level keyword. eg: \"Lv1\" or \"Lv2\" or \"Lv3\"");
			System.out.println("4: The full path output file which contain within time serious data and separate with space.");
			System.exit(0);
		}
		
		//檢查輸入的資料檔案是否存在...
		File data_file = new File(args[0]);
		if (data_file==null || !data_file.exists())
		{
			System.out.println("Can not find the data input file!!");
			System.exit(0);
		}
		
		//讀取檔案裡面的資料...
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(data_file));
			String data_row = br.readLine();
			if (data_row==null || data_row.equals(""))
			{
				System.out.println("Data row is empty!!");
				System.exit(0);
			}
			
			String[] data_array = data_row.split(" ");
			if (data_array==null || data_array.length<1)
			{
				System.out.println("Data array is empty!!");
				System.exit(0);
			}
			
			String[] new_data_array = 
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.exit(0);
		}
	}
}

/******************************************************************************
 * 噪音的類型 : Rough(使用高斯分佈的隨機數值*前一個數值), Smooth(使用前後兩個數值的平均數);
 * @author super_handsome_jason
 *****************************************************************************/
enum NoiseType
{
	Rough, Smooth;
}

/******************************************************************************
 * 噪音的等級. 目前分別是 Lv1(1.0), Lv2(1.2), Lv3(1.5)
 * @author super_handsome_jason
 *****************************************************************************/
enum NoiseLevel
{
	Lv1(1.0), Lv2(1.2), Lv3(1.5);
	private double value;
	private NoiseLevel(double value)
	{
		this.value = value;
	}
	
	public double getValue()
	{
		return this.value;
	}
}
