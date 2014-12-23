package com.auer.ap;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.auer.net.http.HttpTimeoutClinet;

/******************************************************************************
 * <p>Title: Auer Package - This is all java package for auer cop.</p>
 *
 * <p>Description: 這是有 timeout 功能的 HttpClient</p>
 *
 * <p>Copyright: Copyright (c) 2014</p>
 *
 * <p>Company: auer.com.tw</p>
 *
 * @author JasonHsu
 * @version 1.0
 *****************************************************************************/
public class WebContentClawer
{
	public final static String SONG_HEADER = "http://www.mymusic.net.tw/song/show?song_id=";
	
	public String[] loadMusicKeys(String file_name)
	{
		try
    {
	    ArrayList<String> al =  new ArrayList<String>(8000);
			BufferedReader br = new BufferedReader(new FileReader(file_name));
	    String ss = null;
			while ((ss = br.readLine()) != null)
			{
	    	if (ss!=null && !ss.equals(""))
	    		al.add(ss);
			}
	    
			return al.toArray(new String[]{});
    }
    catch (Exception e)
    {
	    e.printStackTrace();
	    return null;
    }
	}
	
	public String[] findDataString(String source_string)
	{
		if (source_string==null || source_string.equals(""))
			return null;
		
		if (source_string.indexOf("此歌曲未授權") > -1)
			return null;
		
		String[] music_data = new String[5];//歌名, 語言, 發行時間, 專輯名, 歌手
		
		int start_index = source_string.indexOf("<div id=\"album_block\">");
		start_index = source_string.indexOf("<h2>", start_index) + 4;
		music_data[0] = source_string.substring(start_index, 
																	source_string.indexOf("</h2>", start_index));
		start_index = source_string.indexOf("<p>", start_index);
		start_index = source_string.indexOf("</p>", start_index);//跳過作詞作曲
		
		start_index = source_string.indexOf("<p>", start_index) + 3;
		String language_time = source_string.substring(start_index, 
																	source_string.indexOf("</p>", start_index));
		String[] str_array = language_time.split("：");
		music_data[1] = str_array[0];
		music_data[2] = str_array[1];
		
		start_index = source_string.indexOf("<p>", start_index) + 3;
		String album_total = source_string.substring(start_index, 
																	source_string.indexOf("</p>", start_index));
		int temp_start = album_total.indexOf(">") + 1;
		music_data[3] = album_total.substring(temp_start, 
																				album_total.indexOf("<", temp_start));
		
		start_index = source_string.indexOf("<p>", start_index) + 3;
		String singer_total = source_string.substring(start_index, 
																	source_string.indexOf("</p>", start_index));
		temp_start = singer_total.indexOf(">") + 1;
		music_data[4] = singer_total.substring(temp_start, 
																				singer_total.indexOf("<", temp_start));
		
		return music_data;
	}
	
	public void writeFile(String file_name, String[][] write_data)
	{
		FileWriter fw = null;
		try
    {
	    fw = new FileWriter(file_name);
			for (String[] row_str : write_data)
	    {
	    	StringBuffer row = new StringBuffer();
	    	for (String column_str : row_str)
	    		row.append(column_str).append("\t");
	    	fw.write(row.toString().trim());
	    	fw.write("\n");
	    	fw.flush();
	    }
    }
    catch (IOException e)
    {
	    e.printStackTrace();
    }
		finally
		{
			try
      {
	      fw.close();
      }
      catch (IOException e)
      {
	      e.printStackTrace();
      }
		}
	}

	public static void main(String[] args)
	{
		System.out.println("程式執行開始!!");
		args = new String[]{"/Users/superhandsomejason/Downloads/keys.txt", 
												"/Users/superhandsomejason/Downloads/music_data.csv"};
		WebContentClawer wcc = new WebContentClawer();
		String[] music_keys = wcc.loadMusicKeys(args[0]);
		System.out.println("共有" + music_keys.length + "筆資料...");
		HttpTimeoutClinet htc = new HttpTimeoutClinet();
		
		FileWriter fw = null;
		try
    {
	    fw = new FileWriter(args[1]);
	    fw.write("\uFEFF");
	    
			for (int i = 0; i < music_keys.length; ++i)
			{
				try
	      {
  				System.out.println("處理第" + (i+1) + "筆資料...");
  				String html_source = htc.getSite(WebContentClawer.SONG_HEADER + music_keys[i]);
  				//System.out.println(html_source);
  				String[] music_data = wcc.findDataString(html_source);
  				StringBuffer row = new StringBuffer(music_keys[i]);
  				row.append("\t");
  				if (music_data == null)
  				{
  					row.append("此歌曲未授權");
  				}
  				else
  				{
  					for (String column_str : music_data)
    	    		row.append(column_str).append("\t");
  				}
  				
  	    	fw.write(row.toString().trim());
  	    	fw.write("\n");
  	    	fw.flush();
		      Thread.sleep(1000);
	      }
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
    }
    catch (IOException e)
    {
	    e.printStackTrace();
    }
		finally
		{
			try
      {
	      fw.close();
	      System.out.println("寫出檔案...");
      }
      catch (IOException e)
      {
	      e.printStackTrace();
      }
		}
		System.out.println("程式結束!!");
//		String html_source = htc.getSite(WebContentClawer.SONG_HEADER + music_keys[0]);
//		System.out.println(html_source);
//		String[] music_data = wcc.findDataString(html_source);
//		for (String data : music_data)
//			System.out.println(data);
	}
}
