package rahultyag.in.taggingapp.apiInterface;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import rahultyag.in.taggingapp.model.LocationEntity;


@Database(entities = { LocationEntity.class}, version = 3,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
	public abstract TaskDao taskDao();
	
	
}