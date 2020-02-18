package rahultyag.in.taggingapp;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


@Dao
public interface TaskDao {
	
	//Area
	@Query("SELECT * FROM LocationData")
	List<LocationEntity> getAllArea();

	
	
	@Query("SELECT * FROM LocationData WHERE id LIKE :id")
	List<LocationEntity> getAllArea(int id);
	
	
	@Insert
	void insertAllArea(LocationEntity task);
	
	@Query("DELETE FROM LocationData")
	void deleteArea();
	
	@Update
	void updateArea(LocationEntity area);
	

}
