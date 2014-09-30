package com.auer.util;

import java.io.*;
import java.text.SimpleDateFormat;
//import java.util.*;
import java.math.BigInteger;

import java.util.HashMap;
import java.util.Random;
import java.util.Date;
import java.util.zip.ZipOutputStream;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipEntry;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;

/******************************************************************************
 * <p>Title: Auer Package - This is all java package for auer cop.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: auer.com.tw</p>
 * @author JasonHsu
 * @version 1.0
 *****************************************************************************/
public class StringTools
{
/*****************************************************************************
 * 將整數的數字轉換成Hex.
 * @param nibble 要轉換的數字.
 * @return char 回傳Hex Char.
 *****************************************************************************/
  private static char toHex(int nibble)
  {
    return hexDigit[(nibble & 0xF)];
  }

/*****************************************************************************
 * 16進位的char array.
 *****************************************************************************/
  private static final char[] hexDigit = {
    '0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'
  };

/*****************************************************************************
 * Base64 所使用的 char 陣列.
 * 使用陣列比使用String 然後在使用 charAt() 還要有效率, 
 * 節省了 String 一開始初始化 char value[] 的動作.
 *****************************************************************************/  
  private static final char[] chars = new char[]{
    'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
    'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
    'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd',
    'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
    'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
    'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7',
    '8', '9', '+', '/'
  };

/*****************************************************************************
 * Base64 所使用的 char 對應 byte 的陣列.
 * 最大為z, ascii = 122.
 *****************************************************************************/  
  private static final int[] ints = new int[123];
  static 
  {
    for (int i = 0; i < 64; i++)
      ints[chars[i]] = i;
  }
  
/*****************************************************************************
 * 農曆的天干字串.
 *****************************************************************************/
  private static final String[] GAN_STR = new String[]{
			"甲","乙","丙","丁","戊","己","庚","辛","壬","癸"};

/*****************************************************************************
 * 農曆的地支字串.
 *****************************************************************************/
  private static final String[] ZHI_STR = new String[]{
			"子","丑","寅","卯","辰","巳","午","未","申","酉","戌","亥"};

/*****************************************************************************
 * 生肖字串.
 *****************************************************************************/
  private static final String[] ANIMAL_STR = new String[]{
			"鼠","牛","虎","兔","龍","蛇","馬","羊","猴","雞","狗","豬"};

/*****************************************************************************
 * 節氣字串.
 *****************************************************************************/
  private static final String[] SOLAR_TERMS = new String[]
  { "小寒", "大寒", "立春", "雨水", "驚蟄", "春分", "清明", "穀雨", "立夏", "小滿", "芒種", "夏至",
      "小暑", "大暑", "立秋", "處暑", "白露", "秋分", "寒露", "霜降", "立冬", "小雪", "大雪", "冬至" };


/******************************************************************************
 * <pre>農曆資訊。每個數位表示1年，第一個數字0x04bd8代表1900年。
 * 每個數字16bit，前12bit表示每月是否大月，1表示大月，30天，0表示小月，29天。
 * 最後4個bit表示是否有閏月和閏哪個月例如:
 * 0x04bd8展開為二進位
 * 0000 0100 1011 1101 1000按位整理得到
 *
 * 19   15         4     0<- 位置
 * ^    ^          ^     ^
 * |    |          |     |
 * 0000 010010111101 1000
 * P0       P1       P2
 *
 * P0表示當年閏月是閏大月還是閏小月
 * P1按位元表示當年每月的大小情況(從高到低)
 * P2表示當年閏的是哪個月份</pre>
 *****************************************************************************/
  public static final int[] LUNAR_DATA = {
    0x04BD8, 0x04AE0, 0x0A570, 0x054D5, 0x0D260, 0x0D950, 0x16554, 0x056A0,
    0x09AD0, 0x055D2, 0x04AE0, 0x0A5B6, 0x0A4D0, 0x0D250, 0x1D255, 0x0B540,
    0x0D6A0, 0x0ADA2, 0x095B0, 0x14977, 0x04970, 0x0A4B0, 0x0B4B5, 0x06A50,
    0x06D40, 0x1AB54, 0x02B60, 0x09570, 0x052F2, 0x04970, 0x06566, 0x0D4A0,
    0x0EA50, 0x06E95, 0x05AD0, 0x02B60, 0x186E3, 0x092E0, 0x1C8D7, 0x0C950,
    0x0D4A0, 0x1D8A6, 0x0B550, 0x056A0, 0x1A5B4, 0x025D0, 0x092D0, 0x0D2B2,
    0x0A950, 0x0B557, 0x06CA0, 0x0B550, 0x15355, 0x04DA0, 0x0A5B0, 0x14573,
    0x052B0, 0x0A9A8, 0x0E950, 0x06AA0, 0x0AEA6, 0x0AB50, 0x04B60, 0x0AAE4,
    0x0A570, 0x05260, 0x0F263, 0x0D950, 0x05B57, 0x056A0, 0x096D0, 0x04DD5,
    0x04AD0, 0x0A4D0, 0x0D4D4, 0x0D250, 0x0D558, 0x0B540, 0x0B6A0, 0x195A6,
    0x095B0, 0x049B0, 0x0A974, 0x0A4B0, 0x0B27A, 0x06A50, 0x06D40, 0x0AF46,
    0x0AB60, 0x09570, 0x04AF5, 0x04970, 0x064B0, 0x074A3, 0x0EA50, 0x06B58,
    0x055C0, 0x0AB60, 0x096D5, 0x092E0, 0x0C960, 0x0D954, 0x0D4A0, 0x0DA50,
    0x07552, 0x056A0, 0x0ABB7, 0x025D0, 0x092D0, 0x0CAB5, 0x0A950, 0x0B4A0,
    0x0BAA4, 0x0AD50, 0x055D9, 0x04BA0, 0x0A5B0, 0x15176, 0x052B0, 0x0A930,
    0x07954, 0x06AA0, 0x0AD50, 0x05B52, 0x04B60, 0x0A6E6, 0x0A4E0, 0x0D260,
    0x0EA65, 0x0D530, 0x05AA0, 0x076A3, 0x096D0, 0x04BD7, 0x04AD0, 0x0A4D0,
    0x1D0B6, 0x0D250, 0x0D520, 0x0DD45, 0x0B5A0, 0x056D0, 0x055B2, 0x049B0,
    0x0A577, 0x0A4B0, 0x0AA50, 0x1B255, 0x06D20, 0x0ADA0, 0x14B63 };

/******************************************************************************
 * 將一個byte[] 轉成hex 字串.
 * @param ba 想要轉換的byte[].
 * @param offset 轉換的起始的位置.
 * @param length 想要轉換多長的length in byte[].
 * @return String 回傳 hex 字串.
 *****************************************************************************/
  public static String toHex(byte[] ba, int offset, int length)
  {
    try
    {
      char[] buf = new char[length * 2];
      int j = 0;
      int k;
      for (int i = offset; i < offset + length; i++) {
        k = ba[i];
        buf[j++] = hexDigit[ (k >>> 4) & 0x0F];
        buf[j++] = hexDigit[k & 0x0F];
      }
      return new String(buf);
    }
    catch (Exception e)
    {
      e.printStackTrace();
      return "";
    }
  }

/******************************************************************************
 * 將整個輸入的byte[]都轉換成 hex 字串.
 * @param ba 想要轉換的byte[].
 * @return String 回傳 hex 字串.
 *****************************************************************************/
  public static String toHex(byte[] ba)
  {
    return toHex(ba, 0, ba.length);
  }

 /******************************************************************************
 * 此方法類似String.replace().
 * @param str 原始字串.
 * @param pattern 欲搜尋的字串.
 * @param replace 欲取代的字串.
 * @return 加工過後的字串.
 *****************************************************************************/
  public static String replace(String str, String pattern, String replace) {
    if (replace == null) {
      replace = "";
    }
    int s = 0, e = 0;
    StringBuffer result = new StringBuffer();
    while ((e = str.indexOf(pattern, s)) >= 0) {
      result.append(str.substring(s, e));
      result.append(replace);
      s = e + pattern.length();
    }
    result.append(str.substring(s));
    return result.toString();
  }

/******************************************************************************
 * 將字串針對html做encode.
 * @param tag 輸入的文字
 * @return String encode後的文字.
 *****************************************************************************/
  public static String toHTML(String tag)
  {
    if ( tag == null ) return "";
    tag = replace(tag, "&", "&amp;");
    tag = replace(tag, "<", "&lt;");
    tag = replace(tag, ">", "&gt;");
    tag = replace(tag, "\"", "&quot;");
    return tag;
  }

/******************************************************************************
 * 將整個檔案吃進來, 然後轉成 hex 字串.
 * @param filename 檔案的名字.請包含路徑.
 * @return String 回傳 hex 字串.
 *****************************************************************************/
  public static String toHex(String filename)
  {
    try
    {
      BufferedInputStream bis = new BufferedInputStream(
          new FileInputStream(filename));
      int bytenumber = bis.available();
      byte[] input = new byte[bytenumber];
      bis.read(input, 0, bytenumber);
      return StringTools.toHex(input);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      return "";
    }
  }

/******************************************************************************
 * 將來源的字串轉成指定長度大小的字串. 若長度不足, 將會在後面補上指定的填充字元. 若超過,
 * 則會在後面截斷, 使其滿足指定長度.
 * @param source String 原始字串.
 * @param length int 指定的長度.
 * @param fillstr char 填充的字元.
 * @return String 回傳加工後的字串. 當source==null, 或是 length < 1, 或是
 * 指定長度剛好等於原始字串長度, 都會回傳原始字串.
 *****************************************************************************/
  public static String toFixLengthStrByBackAdd(String source, int length,
                                               char fillstr)
  {
    try
    {
      int src_length = toHex(source.getBytes("Big5")).length() / 2;
      if (source == null || length < 1 || src_length == length)
        return source;
      if (src_length > length)
			{
        //當source.length() > length時, 要截斷字串.
        return new String(source.getBytes("Big5"), 0, length);
      }
      else {
        //當source.length() < length時, 在後面補上填充的字元
        StringBuffer sb = new StringBuffer(source);
        for (int i = src_length; i < length; i++)
          sb.append(fillstr);

        return sb.toString();
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
      return source;
    }
  }

/******************************************************************************
 * 將來源的字串轉成指定長度大小的字串. 若長度不足, 將會在前面補上指定的填充字元. 若超過,
 * 則會在後面截斷, 使其滿足指定長度.
 * @param source String 原始字串.
 * @param length int 指定的長度.
 * @param fillstr char 填充的字元.
 * @return String 回傳加工後的字串. 當source==null, 或是 length < 1, 或是
 * 指定長度剛好等於原始字串長度, 都會回傳原始字串.
 *****************************************************************************/
  public static String toFixLengthStrByFrontAdd(String source, int length,
                                               char fillstr)
  {
    try
    {
      int src_length = toHex(source.getBytes()).length() / 2;
      if (source == null || length < 1 || src_length == length)
        return source;
      if (src_length > length)
      {
        // 當source.length() > length時, 要截斷字串.
        return new String(source.getBytes("Big5"), 0, length);
      }
      else
      {
        // 當source.length() < length時, 在後面補上填充的字元
        StringBuffer sb = new StringBuffer();
        for (int i = src_length; i < length; i++)
          sb.append(fillstr);
        sb.append(source);
        return sb.toString();
      }
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      return source;
    }
  }

/******************************************************************************
 * 回傳那一年的<b>農曆</b>是否有閏月.
 * @param year int 西洋的年份, 必須大於1900, 小於2050.
 * @return int 當年的閏月份, 範圍為1-12, 若是沒有閏月, 則回傳0. 若是傳入的年份不正確,
 * 將回傳-1.
 * @deprecated 請使用 com.auer.util.DateTimeTools
 *****************************************************************************/
  public static int getLunarLeapMonth(int year)
  {
    if (year < 1900 || year > 2050)
      return -1;
    else
      return (int) (LUNAR_DATA[year - 1900] & 0xf);
  }

/******************************************************************************
 * 回傳那一年的<b>農曆</b>的閏月是多少天.
 * @param year int 西洋的年份, 必須大於1900, 小於2050.
 * @return int 當年的閏月份的天數, 不是29就是30, 若是沒有閏月, 則回傳0.
 * 若是傳入的年份不正確, 將回傳-1.
 * @deprecated 請使用 com.auer.util.DateTimeTools
 *****************************************************************************/
  public static int getLunarLeapDays(int year)
  {
    switch (getLunarLeapMonth(year))
    {
      case 0:
        return 0;
      case -1:
        return -1;
      default:
        if ((LUNAR_DATA[year - 1900] & 0x10000) != 0)
          return 30;
        else
          return 29;
    }
  }

/******************************************************************************
 * 回傳某年某月的<b>農曆</b>天數.
 * @param year int 西洋的年份, 必須大於1900, 小於2050.
 * @param month int 農曆的月份, 介於1~12.
 * @return int 指定年月的農曆天數, 不是29就是30.
 * 若是傳入的年月不正確, 將回傳-1.
 * @deprecated 請使用 com.auer.util.DateTimeTools
 *****************************************************************************/
  public static int getLunarMonthDays(int year, int month)
  {
    if (year < 1900 || year > 2050 || month < 1 || month > 12)
      return -1;
    if ((LUNAR_DATA[year - 1900] & (0x10000 >> month)) == 0)
      return 29;
    else
      return 30;
  }

/******************************************************************************
 * 此方法可以把 hex 字串轉回成 byte array.
 * @param hex_str String 欲轉換的 HEX 字串.
 * @return byte[] 回傳的 byte 陣列, 若是參數錯誤則回傳 null.
 *****************************************************************************/
  public static byte[] fromHexToByteArray(String hex_str)
  {
    if (hex_str == null || hex_str.equals(""))
      return null;
    return new BigInteger(hex_str, 16).toByteArray();
  }

/******************************************************************************
 * 將輸入的 byte array 轉成 Base64 編碼.
 * @param datas byte[] 欲轉換的 byte array.
 * @return String 轉成 Base64 以後的字串. 若輸入的 byte array 是 null, 將會回空字串.
 *****************************************************************************/
  public static String toBase64(byte[] datas)
  {
    if (datas==null || datas.length<1)
      return "";
    
    byte cur = 0x00;
    byte prev = 0x00;
    StringBuffer result = new StringBuffer();
    CircleSequence cs = null;
    try
    {
      cs = new CircleSequence(1, 3);
    }
    catch (Exception e)
    {
      return "";
    }
    for (byte data : datas)
    {
      cur = data;
      switch(cs.getValue())
      {
        case 1: //first byte
          result.append(StringTools.chars[cur >> 2]);
          break;
        
        case 2: //second byte
          result.append(StringTools.chars[(prev & 3) << 4 | (cur >> 4)]);
          break;
        
        case 3: //third byte
          result.append(StringTools.chars[(prev & 0x0f) << 2 | (cur >> 6)]);
          result.append(StringTools.chars[cur & 0x3f]);
          break;
      }
      prev = cur;
    }
    switch(cs.getValue())
    {
      case 2: //剩下一個 byte
        result.append(StringTools.chars[(prev & 3) << 4]);
        result.append("==");
        break;
      
      case 3: //剩下兩個 byte
        result.append(StringTools.chars[(prev & 0x0f) << 2]);
        result.append("=");
        break;
    }
    return result.toString();
  }

/******************************************************************************
 * 此方法會把 Base64 的字串反轉成 byte[].
 * @param data String 要 decode 的字串.
 * @return byte[] decode 的結果, 若是 null or 空字串, 將會收到 null.
 *****************************************************************************/
  public static byte[] fromBase64(String data)
  {
    if (data==null || data.equals("") || data.length() < 3)
      return null;

    char[] data_chars = data.toCharArray();
    ByteArrayOutputStream baos = new ByteArrayOutputStream(data.length());
    
    //初始化序號器...
    CircleSequence cs = null;
    try
    {
      cs = new CircleSequence(1, 4);
    }
    catch (Exception e)
    {
      return null;
    }
    
    int byte_6_bits = 0;
    int byte_temp = 0;
    for (char data_char : data_chars)
    {
      if (Character.isWhitespace(data_char) || data_char == '=') //跳過斷行或是等號
        continue;
      
      try
      {
        byte_6_bits = StringTools.ints[data_char];
      }
      catch (Exception e)
      {
        continue;
      }
      
      switch (cs.getValue())
      {
        case 1: //第一個 byte, 需要取右邊六位.
          byte_temp = byte_6_bits & 0x3f;
          break;
        case 2: //第二個 byte, 需要將左邊兩位跟上一個 byte_temp 進行結合, 然後剩下的再裝入 byte_temp.
          baos.write((byte_temp << 2) | ((byte_6_bits >> 4) & 0x03));
          byte_temp = byte_6_bits & 0x0f;
          break;
        case 3: //第三個 byte, 將此 byte 的中間四位跟上一個 byte_temp 進行結合, 然後剩下的再裝入 byte_temp.
          baos.write((byte_temp << 4) | ((byte_6_bits >> 2) & 0x0f));
          byte_temp = byte_6_bits & 0x03;
          break;
        case 4: //第四個 byte, 將此 byte 的右邊六位跟上一個 byte_temp 進行結合.
          baos.write((byte_temp << 6) | (byte_6_bits & 0x3f));
          break;
      }
    }
    
    //當byte數 mod 4 剩下一時, 必須把 byte_temp 裡面的值丟進去...
    if (cs.getValue() == 2)
      baos.write(byte_temp);    
    
    return baos.toByteArray();
  }

/******************************************************************************
 * 將輸入的 byte 資料作 zip 的壓縮, 然後回傳壓縮過後的 byte 陣列. 請注意,
 * 此方法會預設壓縮的檔名叫做"temp".
 * @param input byte[] 欲進行壓縮的 byte 資料.
 * @return byte[] 回傳壓縮過後的 byte 陣列.
 *****************************************************************************/
  public static byte[] toZipBytes(byte[] input)
  {
    if (input == null || input.length < 1)
      return null;
    ZipOutputStream zos = null;
    try
    {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      zos = new ZipOutputStream(baos);
      zos.setMethod(ZipOutputStream.DEFLATED);
      zos.putNextEntry(new ZipEntry("temp"));
      zos.write(input);
      zos.flush();
      zos.closeEntry();
      zos.close();
      byte[] output = baos.toByteArray();
      baos.close();
      return output;
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      return null;
    }
    finally{
      try{zos.close();}catch (Exception ex){};
    }
  }

/******************************************************************************
 * 將輸入的 byte 資料作 zip 的解壓縮, 然後回傳解壓縮過後的 byte 陣列.
 * @param input byte[] 欲進行解壓縮的 byte 資料.
 * @return byte[] 回傳解壓縮過後的 byte 陣列.
 *****************************************************************************/
  public static byte[] fromZipBytes(byte[] input)
  {
    if (input == null || input.length < 1)
      return null;
    ZipInputStream zis = null;
    try
    {
      ByteArrayInputStream bais = new ByteArrayInputStream(input);
      zis = new ZipInputStream(bais);
      ZipEntry ze = zis.getNextEntry();
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      while (ze != null && zis.available() > 0)
        baos.write(zis.read());
      zis.close();
      baos.flush();
      byte[] output = baos.toByteArray();
      baos.close();
      return output;
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      return null;
    }
    finally{
      try{zis.close();}catch (Exception ex){};
    }
  }
/******************************************************************************
 * 此部分用於回傳現在時間的 YYYYMMDD.
 * @return String YYYYMMDD 字串.
 * @deprecated 請使用 com.auer.DateTimeTools 替代.
 * @see com.auer.DateTimeTools
 *****************************************************************************/
  public static String getNowYYYYMMDD()
  {
    return new SimpleDateFormat("yyyyMMdd").format(new Date());
  }

/******************************************************************************
 * 此部分用於回傳現在時間的 YYYYMMDD.
 * @param delimiter 年月日的分隔符號...
 * @return String YYYYMMDD 字串.
 * @deprecated 請使用 com.auer.DateTimeTools 替代.
 * @see com.auer.DateTimeTools
 *****************************************************************************/
  public static String getNowYYYYMMDD(String delimiter)
  {
    if (delimiter == null || delimiter.equals(""))
      return new SimpleDateFormat("yyyyMMdd").format(new Date());
    else
      return new SimpleDateFormat("yyyy" + delimiter + "MM" + delimiter + "dd")
          .format(new Date());
  }

/******************************************************************************
 * 此部分用於回傳指定時間的 YYYYMMDD.
 * @param datetime 指定的時間...
 * @param delimiter 年月日的分隔符號...
 * @return String YYYYMMDD 字串.
 * @deprecated 請使用 com.auer.DateTimeTools 替代.
 * @see com.auer.DateTimeTools
 *****************************************************************************/
  public static String getYYYYMMDD(Date datetime, String delimiter)
  {
    if (delimiter == null || datetime == null)
      return new SimpleDateFormat("yyyyMMdd").format(datetime);
    else
      return new SimpleDateFormat("yyyy" + delimiter + "MM" + delimiter + "dd")
          .format(datetime);
  }

/******************************************************************************
 * 此部分用於回傳現在時間的 YYYY/M/D. 此類別最大的不同為, 若月份或日期是個位數字的話,
 * 則前面不會補0.
 * @param delimiter 年月日的分隔符號...
 * @return String YYYY/M/D 字串.
 * @deprecated 請使用 com.auer.DateTimeTools 替代.
 * @see com.auer.DateTimeTools
 *****************************************************************************/
  public static String getNowYYYYMD(String delimiter)
  {
    if (delimiter == null || delimiter.equals(""))
      return new SimpleDateFormat("yyyyMd").format(new Date());
    else
      return new SimpleDateFormat("yyyy" + delimiter + "M" + delimiter + "d")
          .format(new Date());
  }

/******************************************************************************
 * 此部分用於回傳現在時間的 YYYYMMDDHHMMSS.
 * @return String yyyyMMddHHmmss 字串.
 * @deprecated 請使用 com.auer.DateTimeTools 替代.
 * @see com.auer.DateTimeTools
 *****************************************************************************/
  public static String getNowYYYYMMDDHHMMSS()
  {
    return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
  }

/******************************************************************************
 * 此部分用於回傳現在時間的毫秒數.
 * @return String SSS 字串.
 * @deprecated 請使用 com.auer.DateTimeTools 替代.
 * @see com.auer.DateTimeTools
 *****************************************************************************/
  public static String getNowSSS()
  {
    return new SimpleDateFormat("SSS").format(new Date());
  }

/******************************************************************************
 * 這個方法會將輸入的陣列做一個亂湊, 產生新的陣列.
 * @param input Object[] 原始陣列
 * @return Object[] 亂排後的陣列
 * @deprecated 希望可以使用 com.auer.util.AuerArrays.randomSort()
 *****************************************************************************/
  public static Object[] randomSort(Object[] input)
  {
    if (input==null || input.length<1)
      return input;
    
    Object[] input2 = new Object[input.length];
    System.arraycopy(input, 0, input2, 0, input.length);
    
    HashMap<Integer,String> hm = new HashMap<Integer,String>(input.length);
    Random rd = new Random();

    for (Object input2_data : input2)
    {
      int index = rd.nextInt(input.length);
      while(hm.containsKey(new Integer(index)))
        index = rd.nextInt(input.length);
      input[index] = input2_data;
      hm.put(new Integer(index), "");
    }
    return input;
  }

/******************************************************************************
 * 此方法可以判斷輸入的字串是否是數字
 * @param input String 欲輸入判斷的字串...
 * @return boolean 回傳該字串是否為數字...
 * @deprecated 請使用 com.auer.util.StringChecker.isNumberic().
 *****************************************************************************/
  public static boolean isNumberic(String input)
  {
    try
    {
      Integer.parseInt(input);
      return true;
    }
    catch (Exception ex)
    {
      return false;
    }
  }

/******************************************************************************
 * 檢查字串中是否有 sql command 或是相關危險字串...
 * @param input String 輸入的欲檢查的字串...
 * @return boolean 如果安全則回傳<b>true</b>, 若有問題則回傳<b>false</b>.
 * @deprecated 希望大家使用 com.auer.util.StringChecker.checkSqlInjection(String input)
 *****************************************************************************/
  public static boolean checkSqlInjectionSafe(String input)
  {
    if (input == null || input.equals(""))
      return true;
    if (input.toLowerCase().indexOf("select") > -1
        || input.toLowerCase().indexOf("insert") > -1
        || input.toLowerCase().indexOf("update") > -1
        || input.toLowerCase().indexOf("delete") > -1
        || input.toLowerCase().indexOf("alter") > -1
        || input.toLowerCase().indexOf("varchar") > -1
        || input.toLowerCase().indexOf("is_srvrolemember") > -1
        || input.toLowerCase().indexOf("cast") > -1)
    {
      return false;
    }
    return true;
  }

/******************************************************************************
 * 此方法主要檢查 email 格式是否正確...
 * @param email String 欲檢查的 email address ...
 * @return boolean 回傳<b>true</b>表示格式正確, <b>false</b>表示格式錯誤...
 * @deprecated 請使用 com.auer.util.StringChecker.chkEmailFormat().
 *****************************************************************************/
  public static boolean chkEmailFormat(String email)
  {
    if (email == null || email.equals(""))
      return false;
    if (email.indexOf(".") == -1 || email.indexOf("@") == -1
        || email.startsWith("@") || email.endsWith(".") || email.endsWith("@"))
      return false;
    else
      return true;
  }

/******************************************************************************
 * 檢查手機電話號碼的格式是否正確...
 * @param phone String 欲檢查的電話號碼...
 * @return boolean 回傳<b>true</b>表示格式正確, <b>false</b>表示格式錯誤...
 * @deprecated 請使用 com.auer.util.StringChecker.chkMobilePhNumFormat().
 ******************************************************************************/
  public static boolean chkMobilePhNumFormat(String phone)
  {
		if (phone==null || phone.equals("") || phone.length()!=10)
			return false;
		return StringTools.isNumberic(phone);
  }

/******************************************************************************
 * 此為抓取隨機英文字母的方法...
 * @param length int 抓去英文字母的長度...
 * @param case_sense boolean 是否要大小寫參雜...
 * @return String 回傳亂數的字串... 若發生問題, 則回傳空字串...
 *****************************************************************************/
  public static String getRandomCode(int length, boolean case_sense)
  {
    if (length < 1)
      return "";
    int max = 26;
    if (case_sense)
      max = 58;
    StringBuffer sb = new StringBuffer();
    Random rd = new Random();
    while (sb.length() < length)
    {
      int num = rd.nextInt(max) + 65;
      if (num > 96 || num < 91)
        sb.append((char) num);
    }
    return sb.toString();
  }

/******************************************************************************
 * 此方法為檢查輸入的日期字串是否正確...
 * @param yyyy String 年份字串, 需要四位數
 * @param mm String 月份字串, 可一位可兩位
 * @param dd String 日期字串, 可一位可兩位
 * @return boolean 若是正確, 則回傳<b>true</b>, 反之, 則回傳<b>false</b>
 * @deprecated 請使用 com.auer.DateTimeTools.chkDateFormat().
 *****************************************************************************/
  public static boolean chkDateFormat(String yyyy, String mm, String dd)
  {
    if (yyyy == null || !StringTools.isNumberic(yyyy) || mm == null
        || !StringTools.isNumberic(mm) || dd == null
        || !StringTools.isNumberic(dd))
      return false;
    try
    {
      SimpleDateFormat dFormat = new SimpleDateFormat("yyyyMMdd");
      dFormat.setLenient(false);
      if (mm.length() == 1)
        mm = "0" + mm;
      if (dd.length() == 1)
        dd = "0" + dd;
      return true;
    }
    catch (Exception ex)
    {
      // ex.printStackTrace();
      return false;
    }
  }

/******************************************************************************
 * 這是用來分割字串成兩個 String[] element... 他只會比對出第一個 pattern, 就直接分割.
 * @param input String 即將被分割的字串.
 * @param pattern 尋找的樣板, 會依此樣板來分割字串.
 * @return String[] 第一個 element 為樣板左邊的字串, 第二個 element 為樣板右邊的字串,
 * 都不包含樣板本身. 如果參數錯誤或是有 Exception, 則會回傳原始字串的陣列.
 * eg : output[0] = input.
 *****************************************************************************/
  public static String[] splitOne(String input, String pattern)
  {
    if (input==null)
      return null;
    if (input.equals("") || pattern==null || pattern.equals("") || input.indexOf(pattern)<0)
      return new String[]{input};
    return new String[]{input.substring(0, input.indexOf(pattern)).trim(), 
        input.substring(input.indexOf(pattern)+pattern.length()).trim()};
  }


/**
 * Main method...
 * @param args String[]
 */
  public static void main(String[] args)
  {
    String[] output = StringTools.splitOne("singer_id  >=    '12>=34'", ">=");
    for (String s : output)
      System.out.println(s);
  }
}
