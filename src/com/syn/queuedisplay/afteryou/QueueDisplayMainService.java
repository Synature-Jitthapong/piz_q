package com.syn.queuedisplay.afteryou;

import org.ksoap2.serialization.PropertyInfo;
import android.content.Context;
import com.j1tth4.mobile.util.DotNetWebServiceTask;

public class QueueDisplayMainService extends DotNetWebServiceTask {
	public static final String PARAM_SHOP_ID = "iShopID";
	public static final String PARAM_DEVICE_CODE = "szDeviceCode";
	public static final String GET_CURR_ALL_QUEUE_METHOD = "WSiQueue_JSON_GetCurrentAllQueueDisplay";
	
	public QueueDisplayMainService(Context c, String method) {
		super(c, method);
		
		property = new PropertyInfo();
		property.setName(PARAM_SHOP_ID);
		property.setValue(QueueApplication.getShopId());
		property.setType(int.class);
		soapRequest.addProperty(property);
		
		property = new PropertyInfo();
		property.setName(PARAM_DEVICE_CODE);
		property.setValue(QueueApplication.getDeviceCode());
		property.setType(String.class);
		soapRequest.addProperty(property);
	}
}
