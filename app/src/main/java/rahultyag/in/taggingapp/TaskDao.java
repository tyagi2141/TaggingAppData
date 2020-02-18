package rahultyag.in.taggingapp;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


@Dao
public interface TaskDao {
	
	//Area
	@Query("SELECT * FROM LocationData")
	LiveData<List<LocationEntity>> getAllArea();

	
	@Query("SELECT * FROM LocationData WHERE id LIKE :id")
	LiveData<List<LocationEntity>> getAllArea(int id);
	
	
	@Insert
	void insertAllArea(LocationEntity task);
	
	@Query("DELETE FROM LocationData")
	void deleteArea();
	
	@Update
	void updateArea(LocationEntity area);

	@Query("SELECT COUNT(*) FROM LocationData ORDER BY id ASC LIMIT 1")
	LiveData<Integer> getCount();
}
