/**
 * 
 */
package com.winning.runner;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.winning.pregnancy.model.PregnancyUser;
import com.winning.pregnancy.util.HTTPGetTool;
import com.winning.pregnancy.util.MyTimeUtil;
import com.winning.pregnancy.util.PreferencesUtils;
import com.winning.pregnancy.util.StringUtil;
import com.winning.pregnancy.util.ThreadPoolUtils;
import com.winning.pregnancy.util.URLUtils;
import com.winning.pregnancy.widget.CustomProgressDialog;

/**
 * 项目名称：EcareAndroid 类名称：q 类描述： 创建人：ZJJ 创建时间：2015-5-29 上午8:36:02 修改人：ZJJ
 * 修改时间：2015-5-29 上午8:36:02 修改备注：
 * 
 * @version
 */
public class UserSaveHandler
{
    private Context context;
    private PregnancyUser user;
    private static final int WHAT_DID_LOGIN_SUCC = 1;
    private static final int WHAT_DID_LOGIN_FAIL = 2;
    protected CustomProgressDialog proDialog;// 加载框

    public UserSaveHandler(Context context)
    {
        super();
        this.context = context;
    }

    public UserSaveHandler(Context context, PregnancyUser user)
    {
        super();
        this.context = context;
        this.user = user;
    }

    protected void openProDialog(String str)
    {
        if (StringUtil.isEmpty(str))
        {
            str = "数据加载中。。。";
        }
        proDialog = CustomProgressDialog.createDialog(context);
        proDialog.setMessage(str);
        proDialog.show();
        proDialog.setCanceledOnTouchOutside(false);
    }

    protected void closeProDialog()
    {
        if (proDialog != null && proDialog.isShowing())
        {
            proDialog.dismiss();
        }
    }

    public void userSave(final UserSaveCallBack userSaveCallBack)
    {

        final Handler handler = new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                super.handleMessage(msg);
                switch (msg.what)
                {

                case WHAT_DID_LOGIN_SUCC:
                {
                    closeProDialog();
                    if (userSaveCallBack != null)
                    {
                        userSaveCallBack.onUserSaveSuccess((String) msg.obj);
                    }
                    break;
                }
                case WHAT_DID_LOGIN_FAIL:
                {
                    closeProDialog();
                    if (userSaveCallBack != null)
                    {
                        Map<String, String> map = (Map<String, String>) msg.obj;
                        userSaveCallBack.onUserSaveFail(map.get("errcode"), map.get("err"));
                    }
                    break;
                }
                }
            }
        };
        Thread thread = new Thread()
        {
            @Override
            public void run()
            {
                Looper.prepare();
                Map<String, String> map = new HashMap<String, String>();
                Message msg = handler.obtainMessage();
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("gravidaView", JSON.toJSONString(user)));
                JSONObject json = HTTPGetTool.getTool().post(URLUtils.HOSTMAIN + URLUtils.URLXGYFZL, params);
                try
                {
                    if (json != null)
                    {
                        if (json.getIntValue("success") == 0)
                        {
                            PreferencesUtils.putString(
                                    context,
                                    "dueDate",
                                    StringUtil.isNotEmpty(json.getJSONArray("data").getJSONObject(0)
                                            .getString("dueDate")) ? json.getJSONArray("data").getJSONObject(0)
                                            .getString("dueDate").substring(0, 10) : MyTimeUtil.getPlusDays(
                                            MyTimeUtil.yyyy_MM_dd, new Date(), 280));
                            PreferencesUtils.putString(context, "user", json.getJSONArray("data").get(0).toString());
                            msg.obj = json.getJSONArray("data").get(0).toString();
                            msg.what = WHAT_DID_LOGIN_SUCC;
                        }
                        else
                        {
                            map.put("errcode", json.getString("success"));
                            map.put("err", json.getString("err"));
                            msg.obj = map;
                            msg.what = WHAT_DID_LOGIN_FAIL;
                        }
                    }
                    else
                    {
                        msg.what = WHAT_DID_LOGIN_FAIL;
                        map.put("errcode", "10000");
                        map.put("err", "连接服务器失败！");
                        msg.obj = map;
                    }
                }
                catch (Exception e)
                {
                    msg.what = WHAT_DID_LOGIN_FAIL;
                    map.put("errcode", "10000");
                    map.put("err", "连接服务器失败！");
                    msg.obj = map;
                }
                handler.sendMessage(msg);
                Looper.loop();
            }
        };
        openProDialog("保存中");
        ThreadPoolUtils.execute(thread);
    }

    /**
     * 用户主信息下载完成回调接口
     */
    public interface UserSaveCallBack
    {
        void onUserSaveSuccess(String str);

        void onUserSaveFail(String errcode, String err);
    }
}
