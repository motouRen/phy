/**
 * 
 */
package com.winning.pregnancy.dao.impl;

import android.content.Context;

import com.winning.pregnancy.ahibernate.dao.impl.BaseDaoImpl;
import com.winning.pregnancy.dao.EMMessageLocalDao;
import com.winning.pregnancy.model.EMMessageLocal;
import com.winning.pregnancy.util.DBHelper;

/**
 * @author ZJJ
 *
 */
public class EMMessageLocalImpl extends BaseDaoImpl<EMMessageLocal> implements EMMessageLocalDao
{

	public EMMessageLocalImpl(Context context) {
		super(new DBHelper(context),EMMessageLocal.class);
	}


}
