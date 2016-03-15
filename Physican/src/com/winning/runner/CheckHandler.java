/**
 * 
 */
package com.winning.runner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.winning.pregnancy.model.Node;
import com.winning.pregnancy.util.StringUtil;
import com.winning.pregnancy.util.ThreadPoolUtils;

/**
 * 项目名称：EcareAndroid 类名称：q 类描述： 创建人：ZJJ 创建时间：2015-5-29 上午8:36:02 修改人：ZJJ
 * 修改时间：2015-5-29 上午8:36:02 修改备注：
 * 
 * @version
 */
public class CheckHandler
{
    private Context context;
    private static final int WHAT_DID_LOGIN_SUCC = 1;
    private static final int WHAT_DID_LOGIN_FAIL = 2;

    public CheckHandler(Context context)
    {
        super();
        this.context = context;
    }

    public void handingBusiness(final HandingCheck businessInf, final int type)
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
                        businessInf.onHandingSuccess((List<Node>) msg.obj);
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
        if (type == 1)
        {
            Thread thread = new Thread()
            {
                @Override
                public void run()
                {
                    Looper.prepare();
                    Message msg = handler.obtainMessage();
                    List<Node> templist = new ArrayList<Node>();
                    Node n = new Node();
                    n.setId("1");
                    n.setCode("1");
                    n.setText("非常清楚");
                    templist.add(n);
                    n = new Node();
                    n.setId("2");
                    n.setCode("2");
                    n.setText("很专业认真");
                    templist.add(n);
                    n = new Node();
                    n.setId("3");
                    n.setCode("3");
                    n.setText("回复很及时");
                    templist.add(n);
                    n = new Node();
                    n.setId("4");
                    n.setCode("4");
                    n.setText("意见很有帮助");
                    templist.add(n);
                    n = new Node();
                    n.setId("5");
                    n.setCode("5");
                    n.setText("态度非常好");
                    templist.add(n);
                    n = new Node();
                    n.setId("6");
                    n.setCode("6");
                    n.setText("非常敬业");
                    templist.add(n);
                    msg.what = WHAT_DID_LOGIN_SUCC;
                    msg.obj = templist;
                    handler.sendMessage(msg);
                    Looper.loop();
                }
            };
            ThreadPoolUtils.execute(thread);
        }
        else if (type == 2)
        {
            Thread thread = new Thread()
            {
                @Override
                public void run()
                {
                    Looper.prepare();
                    Message msg = handler.obtainMessage();
                    List<Node> templist = new ArrayList<Node>();
                    Node n = new Node();
                    n.setId("1");
                    n.setCode("1");
                    n.setText("希望更热情");
                    templist.add(n);
                    n = new Node();
                    n.setId("2");
                    n.setCode("2");
                    n.setText("希望讲得更透彻");
                    templist.add(n);
                    n = new Node();
                    n.setId("3");
                    n.setCode("3");
                    n.setText("希望更细致");
                    templist.add(n);
                    n = new Node();
                    n.setId("4");
                    n.setCode("4");
                    n.setText("希望回复更快");
                    templist.add(n);
                    msg.what = WHAT_DID_LOGIN_SUCC;
                    msg.obj = templist;
                    handler.sendMessage(msg);
                    Looper.loop();
                }
            };
            ThreadPoolUtils.execute(thread);
        }
        else
        {
            Thread thread = new Thread()
            {
                @Override
                public void run()
                {
                    Looper.prepare();
                    Message msg = handler.obtainMessage();
                    List<Node> templist = new ArrayList<Node>();
                    Node n = new Node();
                    n.setId("1");
                    n.setCode("1");
                    n.setText("不细致");
                    templist.add(n);
                    n = new Node();
                    n.setId("2");
                    n.setCode("2");
                    n.setText("等好久没回复");
                    templist.add(n);
                    n = new Node();
                    n.setId("3");
                    n.setCode("3");
                    n.setText("感觉不专业");
                    templist.add(n);
                    n = new Node();
                    n.setId("4");
                    n.setCode("4");
                    n.setText("不友好");
                    templist.add(n);
                    n = new Node();
                    n.setId("5");
                    n.setCode("5");
                    n.setText("没有帮助");
                    templist.add(n);
                    n = new Node();
                    n.setId("6");
                    n.setCode("6");
                    n.setText("完全听不懂");
                    templist.add(n);
                    msg.what = WHAT_DID_LOGIN_SUCC;
                    msg.obj = templist;
                    handler.sendMessage(msg);
                    Looper.loop();
                }
            };
            ThreadPoolUtils.execute(thread);
        }

    }

    public void handingBusinessDetail(final HandingCheck businessInf, final String commentTag)
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
                        businessInf.onHandingSuccess((List<Node>) msg.obj);
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
                Message msg = handler.obtainMessage();
                List<Node> templist = new ArrayList<Node>();
                if (StringUtil.isNotEmpty(commentTag))
                {
                    String[] str = commentTag.split(",");
                    for (int i = 0; i < str.length; i++)
                    {
                        Node n = new Node();
                        n.setText(str[i]);
                        templist.add(n);
                    }
                }
                msg.what = WHAT_DID_LOGIN_SUCC;
                msg.obj = templist;
                handler.sendMessage(msg);
                Looper.loop();
            }
        };
        ThreadPoolUtils.execute(thread);

    }

    public interface HandingCheck
    {
        void onHandingSuccess(List<Node> list);

        void onHandingFail(String errcode, String err);

    }
}
