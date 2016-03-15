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

import com.alibaba.fastjson.JSONObject;
import com.winning.pregnancy.util.HTTPGetTool;
import com.winning.pregnancy.util.MyTimeUtil;
import com.winning.pregnancy.util.PreferencesUtils;
import com.winning.pregnancy.util.StringUtil;
import com.winning.pregnancy.util.ThreadPoolUtils;
import com.winning.pregnancy.util.URLUtils;

/**
 * 项目名称：EcareAndroid 类名称：q 类描述： 创建人：ZJJ 创建时间：2015-5-29 上午8:36:02 修改人：ZJJ
 * 修改时间：2015-5-29 上午8:36:02 修改备注：
 * 
 * @version
 */
public class UserLoginHandler
{
    private Context context;
    private String dlzh;
    private String dlmm;
    private static final int WHAT_DID_LOGIN_SUCC = 1;
    private static final int WHAT_DID_LOGIN_FAIL = 2;

    public UserLoginHandler(Context context)
    {
        super();
        this.context = context;
    }

    public UserLoginHandler(Context context, String dlzh, String dlmm)
    {
        super();
        this.context = context;
        this.dlzh = dlzh;
        this.dlmm = dlmm;
    }

    public void userLogin(final UserLoginCallBack userLoginCallBack)
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
                    if (userLoginCallBack != null)
                    {
                        userLoginCallBack.onUserLoginSuccess((String) msg.obj);
                    }
                    break;
                }
                case WHAT_DID_LOGIN_FAIL:
                {
                    if (userLoginCallBack != null)
                    {
                        Map<String, String> map = (Map<String, String>) msg.obj;
                        userLoginCallBack.onUserLoginFail(map.get("errcode"), map.get("err"));
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
                params.add(new BasicNameValuePair("mobileNo", dlzh));
                params.add(new BasicNameValuePair("password", dlmm));
                JSONObject json = HTTPGetTool.getTool().post(URLUtils.HOSTMAIN + URLUtils.URLLOGIN, params);
                try
                {
                    if (json != null)
                    {
                        if (json.getIntValue("success") == 0)
                        {
                            PreferencesUtils.putString(context, "dlzh", dlzh);
                            PreferencesUtils.putString(context, "dlmm", dlmm);
                            PreferencesUtils.putString(context, "integralBalance", json.getJSONArray("data").getJSONObject(0)
                                    .getString("integralBalance") );
                            PreferencesUtils.putString(
                                    context,
                                    "dueDate",
                                    StringUtil.isNotEmpty(json.getJSONArray("data").getJSONObject(0)
                                            .getString("dueDate")) ? json.getJSONArray("data").getJSONObject(0)
                                            .getString("dueDate").substring(0, 10) : MyTimeUtil.getPlusDays(
                                            MyTimeUtil.yyyy_MM_dd, new Date(), 280));
                            PreferencesUtils.putString(context, "yydm", json.getJSONArray("data").getJSONObject(0)
                                    .getString("hospitalCode"));
                            PreferencesUtils.putString(context, "yymc", json.getJSONArray("data").getJSONObject(0)
                                    .getString("hospitalName"));
                            PreferencesUtils.putString(context, "visiturl", json.getJSONArray("data").getJSONObject(0)
                                    .getString("hospitalHost"));
                            PreferencesUtils.putString(context, "yyid", json.getJSONArray("data").getJSONObject(0)
                                    .getString("hospitalID"));
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
        ThreadPoolUtils.execute(thread);
    }

    /**
     * 用户主信息下载完成回调接口
     */
    public interface UserLoginCallBack
    {
        void onUserLoginSuccess(String str);

        void onUserLoginFail(String errcode, String err);
    }
}
