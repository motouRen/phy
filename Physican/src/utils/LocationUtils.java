package utils;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;

public class LocationUtils implements BDLocationListener {

	@Override
	public void onReceiveLocation(BDLocation arg0) {
		// TODO Auto-generated method stub
		arg0.getProvince();
		arg0.getCity();
		
	}

}
