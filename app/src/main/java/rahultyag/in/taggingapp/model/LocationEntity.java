package rahultyag.in.taggingapp.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "LocationData")
public class LocationEntity {
	
	
	@PrimaryKey(autoGenerate = true)
	public int id;
	
	private String latitude;
	private String longitude;
	private String images;
	private String address;
	
	
	public LocationEntity(String latitude, String longitude, String images, String address) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.images = images;
		this.address = address;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	
	public String getImages() {
		return images;
	}
	
	public void setImages(String images) {
		this.images = images;
	}
	
	public String getLatitude() {
		return latitude;
	}
	
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	
	public String getLongitude() {
		return longitude;
	}
	
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	

}
