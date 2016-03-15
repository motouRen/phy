package com.winning.pregnancy.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.graphics.Bitmap;

public class StringUtil
{

    /**
     * 比较两个String
     * 
     * @param actual
     * @param expected
     * @return <ul>
     *         <li>若两个字符串都为null，则返回true</li>
     *         <li>若两个字符串都不为null，且相等，则返回true</li>
     *         <li>否则返回false</li>
     *         </ul>
     */
    public static boolean isEquals(String actual, String expected)
    {
        return ObjectUtils.isEquals(actual, expected);
    }

    /**
     * null字符串转换为长度为0的字符串
     * 
     * @param str
     *            待转换字符串
     * @return
     * @see <pre>
     * nullStrToEmpty(null) = &quot;&quot;;
     * nullStrToEmpty(&quot;&quot;) = &quot;&quot;;
     * nullStrToEmpty(&quot;aa&quot;) = &quot;aa&quot;;
     * </pre>
     */
    public static String nullStrToEmpty(String str)
    {
        return (str == null ? "" : str);
    }

    /**
     * 将字符串首字母大写后返回
     * 
     * @param str
     *            原字符串
     * @return 首字母大写后的字符串
     * 
     *         <pre>
     *      capitalizeFirstLetter(null)     =   null;
     *      capitalizeFirstLetter("")       =   "";
     *      capitalizeFirstLetter("2ab")    =   "2ab"
     *      capitalizeFirstLetter("a")      =   "A"
     *      capitalizeFirstLetter("ab")     =   "Ab"
     *      capitalizeFirstLetter("Abc")    =   "Abc"
     * </pre>
     */
    public static String capitalizeFirstLetter(String str)
    {
        if (isEmpty(str))
        {
            return str;
        }

        char c = str.charAt(0);
        return (!Character.isLetter(c) || Character.isUpperCase(c)) ? str : new StringBuilder(str.length())
                .append(Character.toUpperCase(c)).append(str.substring(1)).toString();
    }

    /**
     * 判断字符串是否是整数
     * 
     * @param number
     * @return
     */
    public static boolean isInteger(String number)
    {
        boolean isNumber = false;
        if (StringUtil.isNotEmpty(number))
        {
            isNumber = number.matches("^([1-9]\\d*)|(0)$");
        }
        return isNumber;
    }

    /**
     * 判断字符串不为空
     * 
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str)
    {
        boolean isNotEmpty = false;
        if (str != null && !str.trim().equals("") && !str.trim().equalsIgnoreCase("NULL"))
        {
            isNotEmpty = true;
        }
        return isNotEmpty;
    }

    /**
     * 判断字符串为空
     * 
     * @param str
     * @return
     */
    public static boolean isEmpty(String str)
    {
        return !isNotEmpty(str);
    }

    /**
     * 判断字符串不为空
     * 
     * @param str
     * @return
     */
    public static boolean isObjNotEmpty(Object obj)
    {
        boolean isNotEmpty = false;
        if (obj != null)
        {
            isNotEmpty = true;
        }
        return isNotEmpty;
    }

    /**
     * 判断字符串为空
     * 
     * @param str
     * @return
     */
    public static boolean isObjEmpty(Object obj)
    {
        return !isObjNotEmpty(obj);
    }

    /**
     * 将数组转成SQL认识的字符串 123,432,2312 id in('123','432','2312')
     * 
     * @param ids
     * @return
     */
    public static String fromArrayToStr(String[] ids)
    {
        StringBuffer str = new StringBuffer();
        for (int i = 0; i < ids.length; i++)
        {
            str.append("'" + ids[i] + "',");
        }
        if (ids.length > 0)
        {
            str.deleteCharAt(str.length() - 1);
        }
        return str.toString();
    }

    public static String strFormat2(String str)
    {
        try
        {
            if (str.length() <= 1)
            {
                str = "0" + str;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 手机号验证
     * 
     * @param str
     * @return 验证通过返回true
     */
    public static boolean isMobile(String str)
    {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号
        m = p.matcher(str);
        b = m.matches();
        return b;
    }

    /**
     * 计算某人的生日到当前一共有几个闰年，此方法是为了更精确考虑闰年，从而计算出生日。
     * 
     * @param data
     *            数据库传入的 生日
     */
    public static int getLY(String data)
    {
        int leapYear = 0;
        if (!("".equals(data)))
        {
            SimpleDateFormat myFormat = new SimpleDateFormat("yyyy");
            Date date = new Date();
            int birthYear = Integer.parseInt(data.substring(0, 4)); // 获取出生日期，解析为Date类型
            int currYear = Integer.parseInt(myFormat.format(date)); // 获取当前日期
            for (int year = birthYear; year <= currYear; year++)
            {
                if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0))
                {
                    leapYear++;// 从出生年到当前年，只有是闰年就+1
                }
            }
        }
        return leapYear;
    }

    /**
     * 通过生日减去当前日期求年龄。
     * 
     * @param data
     *            接收数据库传来的birthDate：1980-08-09
     * @return 返回年龄
     */
    public static int transToAge(String data) throws Exception
    {
        int age = 0;
        long leapYear = (long) getLY(data);// 其实会自动转成int
        if (!("".equals(data)))
        {
            SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            Date birthDate = myFormat.parse(data);// 获取出生日期，解析为Date类型
            Date currDate = myFormat.parse(myFormat.format(date)); // 获取当前日期
            if (birthDate.getTime() <= currDate.getTime())
            { // 以来此 Date 对象表示的毫秒数
                age = (int) (((currDate.getTime() - birthDate.getTime()) / (24 * 60 * 60 * 1000) - leapYear) / 365);
                // 365L表示长整型
            }
        }
        return age;
    }

    public static byte[] File2byte(String filePath)
    {
        byte[] buffer = null;
        try
        {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1)
            {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return buffer;
    }

    /**
     * 获取图片名称 规则： 以当前时间 + 一个随机数
     * 
     * @param modNam
     *            图片命名均以这个参数开始
     * @param postfix
     *            图片格式
     * @return 图片名称
     */
    public static String getPicName(String modNam, String postfix)
    {
        StringBuffer sb = new StringBuffer(modNam);
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        String mDateTime = formatter.format(cal.getTime());
        int fa = (int) (Math.random() * 5000 + 1000);
        sb.append(mDateTime).append(fa).append(".").append(postfix);// 重新构建文件名

        return sb.toString();
    }

    public static byte[] Bitmap2Bytes(Bitmap bm)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 25, baos);
        return baos.toByteArray();
    }

    public static void main(String[] args)
    {
        try
        {
            System.out.println(transToAge("1985-04-26") + "");
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
