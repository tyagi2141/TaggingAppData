package rahultyag.in.taggingapp.apiInterface;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import rahultyag.in.taggingapp.model.LocationEntity;


@Dao
public interface TaskDao {
	
	
	@Query("SELECT * FROM LocationData")
	LiveData<List<LocationEntity>>  getAllLocation();

	
	@Query("SELECT * FROM LocationData WHERE id LIKE :id")
	LiveData<List<LocationEntity>>  getAllLocation(int id);
	
	
	@Insert
	void insertAllArea(LocationEntity task);
	
	@Query("DELETE FROM LocationData")
	void deleteLocation();
	
	@Update
	void updateLocation(LocationEntity area);

	@Query("SELECT COUNT(*) FROM LocationData ORDER BY id ASC LIMIT 1")
	LiveData<Integer> getCount();
}
