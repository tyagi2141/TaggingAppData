package rahultyag.in.taggingapp.database;

import android.content.Context;

import androidx.room.Room;
import rahultyag.in.taggingapp.apiInterface.AppDatabase;

public class DatabaseClient {
	
	private static DatabaseClient mInstance;
	
	private AppDatabase appDatabase;
	
	private DatabaseClient(Context mCtx) {
		
		appDatabase = Room.databaseBuilder(mCtx, AppDatabase.class, "LocationData").allowMainThreadQueries().build();
	}
	
public 	static synchronized DatabaseClient getInstance(Context mCtx) {
		if (mInstance == null) {
			mInstance = new DatabaseClient(mCtx);
		}
		return mInstance;
	}
	
	public AppDatabase getAppDatabase() {
		return appDatabase;
	}
}
