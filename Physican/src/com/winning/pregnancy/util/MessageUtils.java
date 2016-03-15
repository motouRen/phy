package com.winning.pregnancy.util;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MessageUtils
{
    private TextView txt_title;
    private TextView txt_msg;

    private static MessageUtils me = null;

    protected Context appContext = null;

    protected final static String BROADCAST = "com.winning.pregnancy.widget.MessageReceiver";

    public MessageUtils()
    {
        me = this;
    }

    public static void showToast(Context context, String str)
    {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }

    public static void showMsgToastCenter(Context context, String str)
    {
        Toast toast = Toast.makeText(context, str, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void showMsgToastBottom(Context context, String str)
    {
        Toast toast = Toast.makeText(context, str, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.show();
    }

    // 遍历循环Viewgroup内的所有View元素
    public interface ViewTask
    {
        void Excute(View v);
    }

    public static void test(ViewGroup view_group, ViewTask task)
    {
        int k = view_group.getChildCount();
        for (int i = 0; i < k; i++)
        {
            View v = view_group.getChildAt(i);
            task.Excute(v);
        }
    }

    public static void showMsgDialog(Context context, String str)
    {
        new AlertDialog(context).builder().setMsg(str).setNegativeButton("确定", new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        }).show();
    }

    public static void redirectToRecevier(Context context, String message, final CommonClickCallBack callBack)
    {
        new AlertDialog(context).builder().setMsg(message).setNegativeButton("确定", new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                callBack.onCommonClickCallBack();
            }
        }).show();
    }

    public static void redirectToRecevier(Context context, String message)
    {
        Intent intent = new Intent(BROADCAST); // 对应setAction()
        intent.putExtra("msg", message);
        context.sendBroadcast(intent);
    }

    // public static void redirectToRecevierDialog(Context context, Map<String,
    // String> map)
    // {
    // Intent intent = new Intent(BROADCAST); // 对应setAction()
    // intent.putExtra("msg", JsonBuilder.getInstance().toJson(map));
    // intent.putExtra("type", "1");
    // context.sendBroadcast(intent);
    // }

    public static void showMsgDialogClick(Context context, String title, String message,
            OnClickListener clickListener1, OnClickListener clickListener2)
    {
        if (StringUtil.isEmpty(title))
        {
            title = "提示";
        }
        new AlertDialog(context).builder().setTitle(title).setMsg(message).setPositiveButton("确定", clickListener1)
                .setNegativeButton("取消", clickListener2).show();
    }

    public static void showMsgDialogClick(Context context, String title, String message, OnClickListener clickListener1)
    {
        if (StringUtil.isEmpty(title))
        {
            title = "提示";
        }
        new AlertDialog(context).builder().setTitle(title).setMsg(message).setPositiveButton("确定", clickListener1)
                .show();
    }

    public static MessageUtils getInstance()
    {
        return me;
    }

    public void onInit(Context context)
    {
        appContext = context;
    }

    public interface CommonClickCallBack
    {
        public void onCommonClickCallBack();
    }
}
