package com.winning.pregnancy.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Hashtable;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class BitmapUtils
{

    /***
     * 图片的缩放方法
     * 
     * @param bgimage
     *            ：源图片资源
     * @param newWidth
     *            ：缩放后宽度
     * @param newHeight
     *            ：缩放后高度
     * @return
     */
    public static Bitmap zoomImage(Bitmap bgimage, double newWidth, double newHeight)
    {
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width, (int) height, matrix, true);
        return bitmap;
    }

    /**
     * 以最省内存的方式读取本地资源的图片
     * 
     * @param context
     * @param resId
     * @return
     */
    public static Bitmap readBitMap(Context context, int resId)
    {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        // 获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }

    public static Bitmap qr_code(String string, BarcodeFormat format) throws WriterException
    {
        MultiFormatWriter writer = new MultiFormatWriter();
        Hashtable<EncodeHintType, String> hst = new Hashtable<EncodeHintType, String>();
        hst.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        BitMatrix matrix = writer.encode(string, format, 400, 400, hst);
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                if (matrix.get(x, y))
                {
                    pixels[y * width + x] = 0xff000000;
                }

            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        // 通过像素数组生成bitmap,具体参考api
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    public static Bitmap getBitmapFormSrc(String src)
    {
        Bitmap bit = null;
        try
        {
            bit = BitmapFactory.decodeStream(BitmapUtils.class.getResourceAsStream(src));
        }
        catch (Exception e)
        {
        }
        return bit;
    }

    public static Bitmap getBitmapFromSD(File file, int type, int newWidth, int newHeight)
    {
        Bitmap bit = null;
        try
        {
            if (!isCanUseSD())
            {
                return null;
            }

            if (!file.exists())
            {
                return null;
            }

            if (type == 0)
            {
                bit = cutImg(file, newWidth, newHeight);
            }
            else
            {
                bit = scaleImg(file, newWidth, newHeight);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return bit;
    }

    public static boolean isCanUseSD()
    {
        try
        {
            return Environment.getExternalStorageState().equals("mounted");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    public static Bitmap cutImg(File file, int newWidth, int newHeight)
    {
        Bitmap newBitmap = null;
        BitmapFactory.Options opts = new BitmapFactory.Options();

        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getPath(), opts);
        if ((newWidth != -1) && (newHeight != -1))
        {

            int srcWidth = opts.outWidth;
            int srcHeight = opts.outHeight;
            int destWidth = 0;
            int destHeight = 0;
            int cutSrcWidth = newWidth * 2;
            int cutSrcHeight = newHeight * 2;

            double ratio = 0.0D;
            if ((srcWidth < cutSrcWidth) || (srcHeight < cutSrcHeight))
            {
                ratio = 0.0D;
                destWidth = srcWidth;
                destHeight = srcHeight;
            }
            else if (srcWidth > srcHeight)
            {
                ratio = srcWidth / cutSrcWidth;
                destWidth = cutSrcWidth;
                destHeight = (int) (srcHeight / ratio);
            }
            else
            {
                ratio = srcHeight / cutSrcHeight;
                destHeight = cutSrcHeight;
                destWidth = (int) (srcWidth / ratio);
            }

            opts.inSampleSize = ((int) ratio + 1);

            opts.outHeight = destHeight;
            opts.outWidth = destWidth;
        }
        else
        {
            opts.inSampleSize = 1;
        }

        opts.inJustDecodeBounds = false;

        opts.inDither = false;
        Bitmap resizeBmp = BitmapFactory.decodeFile(file.getPath(), opts);
        if (resizeBmp != null)
        {
            newBitmap = cutImg(resizeBmp, newWidth, newHeight);
        }
        if (newBitmap != null)
        {
            return newBitmap;
        }
        return resizeBmp;
    }

    public static Bitmap cutImg(Bitmap bitmap, int newWidth, int newHeight)
    {
        if (bitmap == null)
        {
            return null;
        }
        Bitmap newBitmap = null;
        if ((newHeight <= 0) || (newWidth <= 0))
        {
            return bitmap;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        if ((width <= 0) || (height <= 0))
        {
            return null;
        }
        int offsetX = 0;
        int offsetY = 0;

        if (width > newWidth)
        {
            offsetX = (width - newWidth) / 2;
        }
        if (height > newHeight)
        {
            offsetY = (height - newHeight) / 2;
        }

        newBitmap = Bitmap.createBitmap(bitmap, offsetX, offsetY, newWidth, newHeight);
        return newBitmap;
    }

    public static Bitmap scaleImg(File file, int newWidth, int newHeight)
    {
        Bitmap resizeBmp = null;
        BitmapFactory.Options opts = new BitmapFactory.Options();

        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getPath(), opts);
        if ((newWidth != -1) && (newHeight != -1))
        {

            int srcWidth = opts.outWidth;
            int srcHeight = opts.outHeight;
            int destWidth = 0;
            int destHeight = 0;

            double ratio = 0.0D;
            if ((srcWidth < newWidth) || (srcHeight < newHeight))
            {
                ratio = 0.0D;
                destWidth = srcWidth;
                destHeight = srcHeight;
            }
            else if (srcWidth > srcHeight)
            {
                ratio = srcWidth / newWidth;
                destWidth = newWidth;
                destHeight = (int) (srcHeight / ratio);
            }
            else
            {
                ratio = srcHeight / newHeight;
                destHeight = newHeight;
                destWidth = (int) (srcWidth / ratio);
            }

            opts.inSampleSize = ((int) ratio + 1);

            opts.outHeight = destHeight;
            opts.outWidth = destWidth;
        }
        else
        {
            opts.inSampleSize = 1;
        }

        opts.inJustDecodeBounds = false;

        opts.inDither = false;
        resizeBmp = BitmapFactory.decodeFile(file.getPath(), opts);
        return resizeBmp;
    }

    public static Bitmap compressImage(Bitmap image)
    {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 25, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100)
        { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

}
