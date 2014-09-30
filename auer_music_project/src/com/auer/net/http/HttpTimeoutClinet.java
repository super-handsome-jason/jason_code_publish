package com.auer.net.http;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import com.auer.util.StringTools;

/******************************************************************************
 * <p>Title: Auer Package - This is all java package for auer cop.</p>
 *
 * <p>Description: 這是有 timeout 功能的 HttpClient</p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: auer.com.tw</p>
 *
 * @author JasonHsu
 * @version 1.0
 *****************************************************************************/
public class HttpTimeoutClinet
{
  //此為不可變更的參數, 且不需要清除.
  private final long timeout;
  
  //以下為每次使用前皆需要清空的參數.
  private String url = "";
  private String rtn_data = "";
  private HashMap<String,String> http_header;
  private String params = "";
  private String uid = "";
  private String pwd = "";
  private String charset_name = "";
  
  //以下為外部可以存取的變數;
/******************************************************************************
 * 此部份為此次 connect 所取得的 http status code, 等同於 
 * java.net.HttpURLConnection.getResponseCode() 所得到的回傳值.
 * @see java.net.HttpURLConnection
 *****************************************************************************/
  public int http_stauts_code = 0;
  
/******************************************************************************
 * 此部份為此次 connect 如果有錯誤的話, 此部分會包含錯誤的內容...
 *****************************************************************************/
  public String http_error_message = "";

  //清空相關需要清空的資料...
  private void cleanData()
  {
    this.url = "";
    this.rtn_data = "";
    this.http_header = null;
    this.params = "";
    this.uid = "";
    this.pwd = "";
    this.http_stauts_code = 0;
    this.http_error_message = "";
  }
  
/******************************************************************************
 * 此為較常用的建構子. 
 * @param timeout_sec 預設的timeout毫秒數. eg:如果希望10秒timeout, 則輸入10000L.
 * 於程式執行中, 每0.5秒會檢查一次連線狀況.
 *****************************************************************************/
  public HttpTimeoutClinet(long timeout_sec)
  {
    this.timeout = timeout_sec;
  }
  
/******************************************************************************
 * 此為預設的建構子, default timeout 30秒.
 *****************************************************************************/
  public HttpTimeoutClinet()
  {
    this.timeout = 30000L;
  }

/******************************************************************************
 * 此方法主要是使用GET Method來取回該網址所回傳的資料.
 * @param site_url String, 為網址的url. eg:http://www.auer.com.tw 請特別注意, 
 * 如果參數中有包含非url規範內可以包含之字元, 記得使用
 * com.auer.net.http.HttpClient.encodeParams(byte[]).
 * @param header_data HashMap, 為欲帶入的http request header.
 * @param charset_name String 網頁的編碼. Eg: UTF-8
 * @return String. 回傳該網址所可以取得的原始資料. 如果有發生問題, 則回傳空字串.
 *****************************************************************************/
  public String getSite(String site_url, HashMap<String,String> header_data, 
  																												String charset_name)
  {
    if (site_url==null || site_url.equals(""))
      return "";
    
    //清空參數
    this.cleanData();
    
    //將參數傳入
    this.url = site_url;
    this.http_header = header_data;
    this.charset_name = charset_name;
    
    //如果是 https, 設定 Handler ...
    if (site_url.toLowerCase().startsWith("https"))
      HttpTimeoutClinet.setupHandler();
    
    try
    {
      Thread td = new Thread(new GetSite());
      td.start();
      for (long i = 0L; i < timeout; i += 500L)
      {
        if (td.isAlive())
          Thread.sleep(500L);
        else
          break;
      }
      //如果超過時間還沒有中斷, 就中斷...
      if (td.isAlive())
      {
        td.interrupt();
        this.rtn_data = "";
        this.http_error_message = "Gateway Timeout.";
        this.http_stauts_code = HttpURLConnection.HTTP_GATEWAY_TIMEOUT;
      } 
      return this.rtn_data;
    }
    catch (Exception e)
    {
      e.printStackTrace();
      //清空回傳值
      this.rtn_data = "";
      this.http_error_message = "Exception : "+e.getMessage();
      this.http_stauts_code = HttpURLConnection.HTTP_INTERNAL_ERROR;
      return this.rtn_data;
    }
  }

/******************************************************************************
 * 此方法主要是使用GET Method來取回該網址所回傳的資料.
 * @param site_url String, 為網址的url. eg:http://www.auer.com.tw
 * @return String. 回傳該網址所可以取得的原始資料. 如果有發生問題, 則回傳空字串.
 *****************************************************************************/
  public String getSite(String site_url)
  {
    return getSite(site_url, null, "UTF-8");
  }
  
/******************************************************************************
 * 此方法主要是使用POST Method來取回該網址所回傳的資料.
 * @param site_url String, 為網址的url. eg:http://www.auer.com.tw
 * @param header_data HashMap, 為欲帶入的http request header.
 * @param param_str String, 此為欲post的資料. eg : id=jason&pwd=12345.請特別注意, 
 * 如果參數中有包含非url規範內可以包含之字元, 記得使用
 * com.auer.net.http.HttpClient.encodeParams(byte[]). 而且請分別做, 切忌把整個
 * param_str做一次性的encode. 如果沒有需要輸入的參數, 請帶入空字串.
 * @return String. 回傳該網址所可以取得的原始資料. 如果有發生問題, 則回傳空字串.
 *****************************************************************************/
  public String postSite(String site_url, HashMap<String,String> header_data, String param_str)
  {
    if (site_url==null || site_url.equals("") || param_str==null)
      return "";
    
    //清空參數
    this.cleanData();
    
    //將參數傳入
    this.url = site_url;
    this.http_header = header_data;
    this.params = param_str;

    //如果是 https, 設定 Handler ...
    if (site_url.toLowerCase().startsWith("https"))
      HttpTimeoutClinet.setupHandler();
    try
    {
      Thread td = new Thread(new PostSite());
      td.start();
      for (long i = 0L; i < timeout; i += 500L)
      {
        if (td.isAlive())
          Thread.sleep(500L);
        else
          break;
      }
      //如果超過時間還沒有中斷, 就中斷...
      if (td.isAlive())
      {
        td.interrupt();
        this.rtn_data = "";
        this.http_error_message = "Gateway Timeout.";
        this.http_stauts_code = HttpURLConnection.HTTP_GATEWAY_TIMEOUT;
      }
      return this.rtn_data;
    }
    catch (Exception e)
    {
      e.printStackTrace();
      //清空回傳值
      this.rtn_data = "";
      this.http_error_message = "Exception : "+e.getMessage();
      this.http_stauts_code = HttpURLConnection.HTTP_INTERNAL_ERROR;
      return this.rtn_data;
    }
  }
  
/******************************************************************************
 * 此方法主要是使用POST Method來取回該網址所回傳的資料.
 * @param site_url String, 為網址的url. eg:http://www.auer.com.tw
 * @param param_str String, 此為欲post的資料. eg : id=jason&pwd=12345.請特別注意, 
 * 如果參數中有包含非url規範內可以包含之字元, 記得使用
 * com.auer.net.http.HttpClient.encodeParams(byte[]). 而且請分別做, 切忌把整個
 * param_str做一次性的encode. 如果沒有需要輸入的參數, 請帶入空字串.
 * @return String. 回傳該網址所可以取得的原始資料. 如果有發生問題, 則回傳空字串.
 *****************************************************************************/
  public String postSite(String site_url, String param_str)
  {
    return postSite(site_url, null, param_str);
  }
  
/******************************************************************************
 * 此方法主要使用POST Method, 另外再加上簡單認證的帳號密碼, 來取回該網址所回傳的資料.
 * @param site_url String, 為網址的url. eg:http://www.auer.com.tw
 * @param header_data HashMap, 為欲帶入的http request header.
 * @param param_str String, 此為欲post的資料. eg : id=jason&pwd=12345.請特別注意, 
 * 如果參數中有包含非url規範內可以包含之字元, 記得使用
 * com.auer.net.http.HttpClient.encodeParams(byte[]). 而且請分別做, 切忌把整個
 * param_str做一次性的encode. 如果沒有需要輸入的參數, 請帶入空字串.
 * @param user_id String, 使用者帳號.
 * @param user_pwd String, 使用者密碼.
 * @return String. 回傳該網址所可以取得的原始資料. 如果有發生問題, 則回傳空字串.
 *****************************************************************************/
  public String postAuthSite(String site_url, HashMap<String,String> header_data, 
      						String param_str, String user_id, String user_pwd)
  {
    if (user_id!=null && user_pwd!=null)
    {
      this.uid = user_id;
      this.pwd = user_pwd;
    }
    return postSite(site_url, header_data, param_str);
  }
  
/******************************************************************************
 * 抓取此次 Connect 所取得的 http status code.<br>
 * <b>請特別注意!! 要抓取前一定要先執行過以下三個 method 任一個 :</b><br>
 * <li>getSite()</li>
 * <li>postSite()</li>
 * <li>postAuthSite()</li><br>
 * 且每次執行前都會還原成 0.
 * @return int Http Status Code, 等同於java.net.HttpURLConnection.getResponseCode()
 * @see java.net.HttpURLConnection
 *****************************************************************************/
  public int getHttpStatusCode()
  {
    return this.http_stauts_code;
  }
  
/******************************************************************************
 * 此部分會抓取此次 Connect 的錯誤訊息.<br>
 * <b>請特別注意!! 要抓取前一定要先執行過以下三個 method 任一個 :</b><br>
 * <li>getSite()</li>
 * <li>postSite()</li>
 * <li>postAuthSite()</li><br>
 * 且每次執行前都會還原成 0.
 * @return String 此次 Connect 所取得的錯誤訊息, 倘若沒有, 則會是空字串.
 *****************************************************************************/
  public String getErrorMessage()
  {
    return this.http_error_message;
  }
  
/******************************************************************************
 * This method is used to encode the input string for url transfer.
 * @param src The byte array which you want to encode for url.
 * @return The string which after encode for url transfering.
 *****************************************************************************/
  public static String encodeParams(byte[] src) 
  {
    if (src==null || src.length<1)
      return "";
    String retstr = "";
    for (int i = 0; i < src.length; i++) 
    {
      if (src[i] < 0) { // src[i] >= 0x80
        String hh = Integer.toHexString( (int) (src[i] & 0xff));
        retstr += "%" + hh;
      }
      else {
        char cc = (char) src[i];
        if (Character.isLetterOrDigit(cc))
          retstr += cc;
        else if (cc == ' ')
          retstr += '+';
        else if (cc == '.' || cc == '-' || cc == '*' || cc == '_')
          retstr += cc;
        else {
          String hh = Integer.toHexString( (int) (src[i] & 0xff));
          if (src[i] < 0x10)
            retstr += "%0" + hh;
          else
            retstr += "%" + hh;
        }
      }
    }
    return retstr;
  }  
  
  //針對 SSL 所作的修改...
  private static void setupHandler()
  {
    java.util.Properties p = System.getProperties();
    String s = p.getProperty("java.protocol.handler.pkgs");
    if (s == null)
      s = "weblogic.net";
    else if (s.indexOf("weblogic.net") == -1)
      s += "|weblogic.net";
    p.put("java.protocol.handler.pkgs", s);
    System.setProperties(p);
  }
  

  
  public static void main(String[] args)
  {
    try
    {
//      HttpTimeoutClinet htc = new HttpTimeoutClinet(200000L);
//      System.out.println("AP Start!!");
//      StringBuffer sb = new StringBuffer("mbrid=89&musicid=38663,38664,38665,38666,38667,38668,38669,38670,38671,38672,38673,38674,38675,38676,38677,38678,38679,38680,38681,38682,38683,38684,38685,38686,38688,38689,38690,38691,38692,38693,38694,38695,38696,38697,38698,38699,38700,38701,38702,38703,38704,38705,38706,38707,38708,38709,38710,38711,38712,38713,38714,38715,38716,38717,38718,38719,38721,38722,38723,38724,38725,38726,38727,38728,38729,38730,38731,38732,38733,38734,38735,38736,38737,38738,38739,38740,38742,38743,38744,38745,38746,38747,38748,38749,38750,38751,38752,38753,38754,38755,38756,38758,38759,38760,38761,38762,38763,38764,38765,38767,38768,38769,38770,38771,38772,38773,38774,38775,38776,38777,38778,38779,38780,38781,38782,38783,38784,38786,38787,38788,38789,38790,38791,38792,38793,38794,38795,38797,38806,38807,38808,38809,38810,38811,38813,38814,38815,38816,38817,38818,38819,38820,38822,38823,38824,38837,38838,38839,38840,38844,38845,38846,38847,38848,38849,38850,38851,38852,38853,38854,38855,38856,38857,38858,38859,38860,38861,38862,38863,38864,38865,38866,38867,38868,38869,38870,38871,38872,38873,38874,38875,38877,38878,38879,38880,38881,38882,38883,38884,38885,38886,38887,38888,38889,38890,38891,38892,38893,38894,38895,38896,38897,38898,38899,38900,38903,38904,38905,38906,38907,38908,38909,38910,38911,38912,38913,38914,38915,38916,38917,38918,38919,38920,38921,38922,38923,38924,38925,38926,38927,38928,38929,38930,38931,38932,38933,38934,38935,38936,38938,38939,38940,38941,38942,38943,38944,38945,38946,38947,38948,38949,38950,38951,38952,38953,38954,38956,38957,38958,38959,38960,38961,38962,38963,38964,38965,38966,38967,38968,38969,38970,38971,38972,38973,38974,38975,38976,38977,38978,38979,38980,38981,38982,38983,38984,38985,38986,38987,38988,38989,38990,38991,38992,38993,38994,38995,38997,38998,38999,39000,39001,39002,39003,39004,39005,39006,39007,39008,39009,39010,39011,39012,39013,39014,39015,39016,39017,39018,39019,39020,39021,39022,39023,39024,39025,39026,39027,39028,39029,39030,39031,39032,39033,39034,39035,39036,39037,39038,39039,39040,39041,39042,39043,39044,39045,39046,39047,39048,39049,39050,39051,39052,39053,39054,39055,39056,39057,39058,39059,39060,39062,39063,39064,39065,39066,39067,39068,39069,39070,39071,39072,39073,39074,39075,39076,39077,39078,39079,39080,39081,39082,39083,39084,39085,39086,39087,39088,39089,39090,39091,39092,39093,39094,39095,39096,39097,39098,39099,39100,39101,39102,39103,39104,39105,39106,39107,39108,39109,39110,39111,39112,39113,39114,39115,39116,39117,39118,39119,39120,39121,39122,39123,39124,39125,39126,39127,39128,39129,39130,39131,39132,39133,39134,39135,39136,39137,39138,39139,39140,39141,39142,39143,39144,39145,39146,39147,39148,39149,39150,39151,39152,39153,39154,39155,39156,39157,39158,39159,39160,39161,39162,39163,39164,39165,39166,39168,39169,39170,39171,39172,39173,39174,39175,39177,39178,39179,39180,39181,39182,39183,39184,39185,39186,39187,39188,39189,39190,39191,39192,39193,39194,39195,39197,39198,39199,39200,39201,39202,39203,39204");
//      long time_tag1 = System.currentTimeMillis();
//      String return_str = htc.postSite(
//          "http://css.inmusic.com.tw/iNmusicClientService/songlist/getxmltest.aspx", 
//      sb.toString());
//      long time_tag2 = System.currentTimeMillis();
//      System.out.println("Run "+(time_tag2 - time_tag1)+" miliseconds!!");
      System.out.println(HttpTimeoutClinet.encodeParams("123,456l,7898".getBytes()));
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  private class GetSite implements Runnable
  {
    public void run()
    {
      InputStreamReader isr = null;
      try
      {
        URL u = new URL(url);
        HttpURLConnection uc = (HttpURLConnection) u.openConnection();
        uc.setDoInput(true);
        uc.setDoOutput(true);
        uc.setUseCaches(false);
        //增加request header參數...
        if (http_header!=null && http_header.size() > 0) 
        {
          String[] keys = http_header.keySet().toArray(new String[]{});
          for (String key : keys)
            uc.setRequestProperty(key, http_header.get(key));
        }
        //讀取資料...
        http_stauts_code = uc.getResponseCode();
        if (http_stauts_code == HttpURLConnection.HTTP_OK)
        {
        	isr = new InputStreamReader(uc.getInputStream(), charset_name);
          StringBuffer sb = new StringBuffer();
          char[] char_array = new char[512];
          int length = 0;
          while ((length = isr.read(char_array, 0, char_array.length)) != -1)
            sb.append(char_array, 0, length);
          isr.close();
          uc.disconnect();
          rtn_data = sb.toString();
        }
        else
        {
          rtn_data = "";
          http_error_message = uc.getResponseMessage();
        }
      }
      catch (Exception ex)
      {
        ex.printStackTrace();
        rtn_data = "";
        http_error_message = "Exception : "+ex.getMessage();
        http_stauts_code = HttpURLConnection.HTTP_INTERNAL_ERROR;
      }
      finally
      {
        try{isr.close();}catch (IOException e){};
      }
    }
  }
  
  private class PostSite implements Runnable
  {
    public void run()
    {
      BufferedInputStream bin = null;
      OutputStreamWriter dos = null;
      try
      {
        URL u = new URL(url);
        HttpURLConnection uc = (HttpURLConnection) u.openConnection();
        uc.setDoInput(true);
        uc.setDoOutput(true);
        uc.setUseCaches(false);
        uc.setRequestMethod("POST");
        //看是否需要增加request header參數...
        if (http_header!=null && http_header.size() > 0) 
        {
          String[] keys = http_header.keySet().toArray(new String[]{});
          for (String key : keys)
            uc.setRequestProperty(key, http_header.get(key));
        }
        //看是否需要認證...
        if (uid!=null && !uid.equals(""))
        {
          uc.setRequestProperty("Authorization", "BASIC "
              + StringTools.toBase64((uid+":"+pwd).getBytes()));
          byte[] paramsbyte = params.getBytes();
          String paramsHex = StringTools.toHex(paramsbyte);
          int paramsLength = paramsHex.length() / 2;
          uc.setRequestProperty("CONTENT-LENGTH", "" + paramsLength);
        }
        //寫出資料...
        dos = new OutputStreamWriter(uc.getOutputStream(), 
            											"ASCII");
        dos.write(params);
        dos.write("\r\n");
        dos.flush();
        
        //讀取資料...
        http_stauts_code = uc.getResponseCode();
        if (http_stauts_code == HttpURLConnection.HTTP_OK)
        {
          bin = new BufferedInputStream(uc.getInputStream());
          StringBuffer sb = new StringBuffer();
          int c;
          while ((c = bin.read()) != -1)
            sb.append((char)c);
          bin.close();
          dos.close();
          uc.disconnect();
          rtn_data = sb.toString();
        }
        else
        {
          rtn_data = "";
          http_error_message = uc.getResponseMessage();
        }
      }
      catch (Exception ex)
      {
        ex.printStackTrace();
        rtn_data = "";
        http_error_message = "Exception : "+ex.getMessage();
        http_stauts_code = HttpURLConnection.HTTP_INTERNAL_ERROR;
      }
      finally
      {
        try{bin.close();}catch (IOException e){};
        try{dos.close();}catch (IOException e){};
      }
    }
  }
}
