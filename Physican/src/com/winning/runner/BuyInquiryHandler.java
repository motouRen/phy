/**
 * 
 */
package com.winning.runner;

import java.util.ArrayList;
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
import com.winning.pregnancy.util.ThreadPoolUtils;
import com.winning.pregnancy.util.URLUtils;

/**
 * 项目名称：EcareAndroid 类名称：q 类描述： 创建人：ZJJ 创建时间：2015-5-29 上午8:36:02 修改人：ZJJ
 * 修改时间：2015-5-29 上午8:36:02 修改备注：
 * 
 * @version
 */
public class BuyInquiryHandler
{
    private Context context;
    private static final int WHAT_DID_LOGIN_SUCC = 1;
    private static final int WHAT_DID_LOGIN_FAIL = 2;

    public BuyInquiryHandler(Context context)
    {
        super();
        this.context = context;
    }

    public void handingBusiness(final HandingBusinessInf businessInf, final String gravidaID, final String doctorID)
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
                    if (businessInf != null)
                    {
                        businessInf.onHandingSuccess((String) msg.obj);
                    }
                    break;
                }
                case WHAT_DID_LOGIN_FAIL:
                {
                    if (businessInf != null)
                    {
                        Map<String, String> map = (Map<String, String>) msg.obj;
                        businessInf.onHandingFail(map.get("errcode"), map.get("err"));
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
                params.add(new BasicNameValuePair("gravidaID", gravidaID));
                params.add(new BasicNameValuePair("doctorID", doctorID));
                JSONObject json = HTTPGetTool.getTool().post(URLUtils.HOSTMAIN + URLUtils.URLORDER, params);
                try
                {
                    if (json != null)
                    {
                        if (json.getIntValue("success") == 0)
                        {
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

}
