package rahultyag.in.taggingapp.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Connectivity {
	
	private static NetworkInfo getNetworkInfo(Context context){
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		assert cm != null;
		return cm.getActiveNetworkInfo();
	}
	
	
	public static boolean isConnected(Context context){
		NetworkInfo info = Connectivity.getNetworkInfo(context);
		return (info != null && info.isConnected());
	}

	
}