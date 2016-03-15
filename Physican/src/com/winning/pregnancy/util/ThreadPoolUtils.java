package com.winning.pregnancy.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 
*    
* 项目名称：EcareAndroid   
* 类名称：ThreadPoolUtils   
* 类描述： 线程辅助类  
* 创建人：ZJJ   
* 创建时间：2015-2-16 下午3:54:38   
* 修改人：ZJJ   
* 修改时间：2015-2-16 下午3:54:38   
* 修改备注：   
* @version    
*
 */
public class ThreadPoolUtils
{

	private ThreadPoolUtils()
	{

	}

	private static ExecutorService service;

	static
	{
		if (service == null)
		{
			service = Executors.newCachedThreadPool();
		}
	}

	/**
	 * 从线程池中抽取线程，执行指定的Runnable对象
	 * 
	 * @param runnable
	 */
	public static void execute(Runnable runnable)
	{
		service.execute(runnable);
	}

}