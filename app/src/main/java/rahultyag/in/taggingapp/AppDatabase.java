package rahultyag.in.taggingapp;

import android.graphics.Region;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;


@Database(entities = { LocationEntity.class}, version = 3,exportSchema = true)
public abstract class AppDatabase extends RoomDatabase {
	public abstract TaskDao taskDao();
	
	final Migration MIGRATION_2_3 =
			new Migration(1, 2) {
				@Override
				public void migrate(@NonNull final SupportSQLiteDatabase database) {
					//database.execSQL("CREATE TABLE IF NOT EXISTS `Pet` (`name` TEXT NOT NULL, PRIMARY KEY(`name`))");
				}
			};
}