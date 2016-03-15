package com.winning.pregnancy.util;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MyTimeUtil
{

    public static String yyyy = "yyyy";
    public static String yyyy_MM_dd = "yyyy-MM-dd";
    public static String yyyyMMdd = "yyyyMMdd";
    public static String yyyyMM = "yyyyMM";
    public static String yyyy_MM = "yyyy-MM";
    public static String yyyy_MM_dd_HH_mm = "yyyy-MM-dd HH:mm";
    public static String yyyyMMddHHmm = "yyyyMMddHHmm";
    public static String yyyyMMddHHmmss = "yyyyMMddHHmmss";
    public static String yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";
    /**
     * 年月日
     */
    public static String yyyyMMdd_HHmm = "yyyy年MM月dd日 HH:mm";
    public static String yyyyMMdd_HH_mm_ss = "yyyyMMddHH:mm:ss";

    public static String getCurrentTime()
    {
        Date now = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(yyyy_MM_dd_HH_mm_ss);
        String str1 = simpleDateFormat.format(now);
        return str1;
    }

    /**
     * 将字符串时间改成Date类型
     * 
     * @param format
     * @param dateStr
     * @return
     */
    public static Date strToDate(String format, String dateStr)
    {

        Date date = null;

        try
        {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            date = simpleDateFormat.parse(dateStr);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        return date;
    }

    /**
     * 将Date时间转成字符串
     * 
     * @param format
     * @param date
     * @return
     */
    public static String DateToStr(String format, Date date)
    {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);

        return simpleDateFormat.format(date);
    }

    /**
     * 获取2个字符日期的天数差
     * 
     * @param p_startDate
     * @param p_endDate
     * @return 天数差
     */
    public static long getDaysOfTowDiffDate(String p_startDate, String p_endDate)
    {

        Date l_startDate = MyTimeUtil.strToDate(MyTimeUtil.yyyy_MM_dd, p_startDate);
        Date l_endDate = MyTimeUtil.strToDate(MyTimeUtil.yyyy_MM_dd, p_endDate);
        long l_startTime = l_startDate.getTime();
        long l_endTime = l_endDate.getTime();
        long betweenDays = (long) ((l_endTime - l_startTime) / (1000 * 60 * 60 * 24));
        return betweenDays;
    }

    /**
     * 获取2个Date型日期的分钟数差值
     * 
     * @param p_startDate
     * @param p_endDate
     * @return 分钟数差值
     */
    public static long getMinutesOfTowDiffDate(Date p_startDate, Date p_endDate)
    {

        long l_startTime = p_startDate.getTime();
        long l_endTime = p_endDate.getTime();
        long betweenMinutes = (long) ((l_endTime - l_startTime) / (1000 * 60));
        return betweenMinutes;
    }

    /**
     * 获取2个字符日期的天数差
     * 
     * @param p_startDate
     * @param p_endDate
     * @return 天数差
     */
    public static long getDaysOfTowDiffDate(Date l_startDate, Date l_endDate)
    {

        long l_startTime = l_startDate.getTime();
        long l_endTime = l_endDate.getTime();
        long betweenDays = (long) ((l_endTime - l_startTime) / (1000 * 60 * 60 * 24));
        return betweenDays;
    }

    /**
     * 给出日期添加一段时间后的日期
     * 
     * @param dateStr
     * @param plus
     * @return
     */
    public static String getPlusDays(String format, String dateStr, long plus)
    {

        Date date = MyTimeUtil.strToDate(format, dateStr);

        long time = date.getTime() + plus * 24 * 60 * 60 * 1000;

        return MyTimeUtil.DateToStr(format, new Date(time));
    }

    /**
     * 给出日期添加一段时间后的日期
     * 
     * @param dateStr
     * @param plus
     * @return
     */
    public static String getPlusDays(String format, Date date, long plus)
    {

        long time = date.getTime() + plus * 24 * 60 * 60 * 1000;

        return MyTimeUtil.DateToStr(format, new Date(time));
    }

    /**
     * 获取当前日期是星期几
     * 
     * @param dt
     * @return 当前日期是星期几
     * @throws ParseException
     */
    public static String getWeekOfDate(String dt)
    {
        String[] weekDays =
        { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
        Calendar cal = Calendar.getInstance();
        try
        {
            cal.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(dt));
        }
        catch (ParseException e)
        {
            return "";
        }

        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;

        return weekDays[w];
    }

    /**
     * 根据出生日期计算年龄
     */

    public static String formatAge(String birth)
    {
        String age = "";
        int now = Integer.parseInt(DateToStr(yyyy, new Date()));
        int day = Integer.parseInt(birth.substring(0, 4));
        age = String.valueOf(now - day);
        return age;
    }

    /**
     * 增加几分钟后的时间
     */

    public static String addDateMinut(String day, int x)// 返回的是字符串型的时间，输入的
    // 是String day, int x
    {
        SimpleDateFormat format = new SimpleDateFormat(yyyyMMdd_HH_mm_ss);// 24小时制
        // 引号里面个格式也可以是 HH:mm:ss或者HH:mm等等，很随意的，不过在主函数调用时，要和输入的变
        // 量day格式一致
        Date date = null;
        try
        {
            date = format.parse(day);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        if (date == null)
            return "";
        // System.out.println("front:" + format.format(date)); //显示输入的日期
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, x);// 24小时制
        date = cal.getTime();
        // System.out.println("after:" + format.format(date)); //显示更新后的日期
        cal = null;
        return format.format(date);

    }

    /**
     * 得到本月的第一天
     * 
     * @return
     */
    public static String getMonthFirstDay()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));

        return MyTimeUtil.DateToStr(yyyy_MM_dd, calendar.getTime());
    }

    /**
     * 得到本月的最后一天
     * 
     * @return
     */
    public static String getMonthLastDay()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return MyTimeUtil.DateToStr(yyyy_MM_dd, calendar.getTime());
    }

    public static String GetSysDate(String format, String StrDate, int year, int month, int day)
    {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sFmt = new SimpleDateFormat(format);
        cal.setTime(sFmt.parse((StrDate), new ParsePosition(0)));

        if (day != 0)
        {
            cal.add(cal.DATE, day);
        }
        if (month != 0)
        {
            cal.add(cal.MONTH, month);
        }
        if (year != 0)
        {
            cal.add(cal.YEAR, year);

        }
        return sFmt.format(cal.getTime());
    }

    /**
     * 将字符串时间改成Date类型
     * 
     * @param format
     * @param dateStr
     * @return
     */
    public static java.sql.Date strToSqlDate(String dateStr)
    {

        java.sql.Date date = null;

        try
        {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(yyyy_MM_dd);
            date = new java.sql.Date(simpleDateFormat.parse(dateStr).getTime());
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        return date;
    }

    /**
     * 获得指定日期的后一天
     * 
     * @param specifiedDay
     * @return
     */
    public static String getSpecifiedDayAfter(String specifiedDay)
    {
        Calendar c = Calendar.getInstance();
        Date date = null;
        try
        {
            date = new SimpleDateFormat(yyyyMMdd).parse(specifiedDay);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day + 1);

        String dayAfter = new SimpleDateFormat(yyyyMMdd).format(c.getTime());
        return dayAfter;
    }

    /**
     * 将长整型数字转换为日期格式的字符串
     * 
     * @param time
     * @param format
     * @return
     */
    public static String convert2String(long time, String format)
    {
        if (time > 0l)
        {
            if (StringUtil.isNotEmpty(format))
            {
                SimpleDateFormat sf = new SimpleDateFormat(format);
                Date date = new Date(time);
                return sf.format(date);
            }
            else
            {
                return DateToStr(yyyyMMdd_HH_mm_ss, new Date());
            }
        }
        else
        {
            return DateToStr(yyyyMMdd_HH_mm_ss, new Date());
        }
    }
    
    public static long getWeeksMod(long between)
    {
        long week = 0;
        week = between % 7;
        return week;
    }
    
    public static long getWeeksCount(long between)
    {
        long week = 0;
        week = between / 7;
        return week;
    }


    public static void main(String[] args)
    {
        // System.out.println(getMonthLastDay());
    }
}
