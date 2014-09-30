package com.auer.util;

/******************************************************************************
 * <p>Title: Auer Package - This is all java package for auer cop.</p>
 * <p>Description: 此類別用來產生一個循序序號的抓取器, 目前僅支援整數.在抓取至 max 值之後,
 * 還會循環至 start 的值. 使用方式如下 : 
 * <code>
 *   CircleSequence cs = null;
 *   try
 *   {
 *     cs = new CircleSequence(1, 4);
 *   }
 *   catch (Exception e)
 *   {
 *     return null;
 *   }
 * </code>
 * </p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: auer.com.tw</p>
 *
 * @author JasonHsu
 * @version 1.0
 *****************************************************************************/
public class CircleSequence
{
  private int start = 0;
  private int max = 0;
  private int now = 0;
  
/******************************************************************************
 * 產生一個序號器. 
 * @param start_no int 此序號的開始號碼.
 * @param max_no int 此序號器的最大號碼.
 * @throws Exception 當最大號碼小於起始號碼時, 或是起始號碼跟最大號碼都等於零的時候,
 * 會拋出 Exception.
 *****************************************************************************/
  public CircleSequence(int start_no, int max_no) throws Exception
  {
    if (max_no<start_no || (start_no==max_no && start_no==0))
    {
      throw new Exception(
          "com.auer.util.CircleSequence() error : Parameters are invalid!!");
    }
    this.start = start_no;
    this.max = max_no;
  }
  
/******************************************************************************
 * 這是抓取現在值的方法.
 * @return int 現在的號碼.
 *****************************************************************************/
  public int getValue()
  {
    if (this.start == this.max)
    {
      this.now = this.start;
      return this.now;
    }
    if ((this.now==0 && this.start!=0) || this.now < this.start)
    {
      this.now = this.start;
      return this.now;
    }
    this.now++;
    
    if (this.now > this.max)//循環的部份~
      this.now = this.start;
    
    return this.now;
  }
  
  /**
   * @param args
   */
  public static void main(String[] args)
  {
    try
    {
      CircleSequence cs = new CircleSequence(1, 0);
      for (int i = 0; i < 200; i++)
      {
        System.out.println("value = "+cs.getValue());
      }
    }
    catch (Exception e)
    {
      // TODO 自動產生 catch 區塊
      e.printStackTrace();
    }

  }

}
