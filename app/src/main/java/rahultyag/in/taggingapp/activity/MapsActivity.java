package rahultyag.in.taggingapp.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import es.dmoral.toasty.Toasty;
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;
import io.reactivex.functions.Consumer;
import rahultyag.in.taggingapp.R;
import rahultyag.in.taggingapp.database.DatabaseClient;
import rahultyag.in.taggingapp.helper.Connectivity;
import rahultyag.in.taggingapp.helper.GPSTracker;
import rahultyag.in.taggingapp.model.LocationEntity;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener,
		GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener {
	GPSTracker gps;
	double latitude, longitude;
	private GoogleMap mMap;
	public String currentLatLng;
	static final int REQUEST_TAKE_PHOTO1 = 1;
	String mCurrentPhotoPath;
	String imageId;
	public static int type = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maps);
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);
		StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
		StrictMode.setVmPolicy(builder.build());
		builder.detectFileUriExposure();
		gps = new GPSTracker(MapsActivity.this);
		
	}
	
	
	@Override
	public void onMapReady(final GoogleMap googleMap) {
		mMap = googleMap;
		
		onMapReady();
		
	}
	
	public LiveData<Integer> getCount() {
		return DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
				.taskDao()
				.getCount();
	}
	
	public LiveData<List<LocationEntity>> getAllLocation() {
		return DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
				.taskDao()
				. getAllLocation();
	}
	
	public LiveData<List<LocationEntity>> getAllLocationById(int id) {
		return DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
				.taskDao()
				. getAllLocation(id);
	}
	
	@Override
	public void onMapClick(LatLng point) {
		
		
	}
	
	@Override
	public void onMapLongClick(final LatLng point) {
		
		
		if (Connectivity.isConnected(this)) {
			mMap.clear();
			String strAddress = null;
			List<Address> addresses;
			mMap.addMarker(new MarkerOptions().position(new LatLng(point.latitude, point.longitude)).title(point.latitude + "," + point.longitude)).setTag(point);
			try {
				Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
				
				addresses = geocoder.getFromLocation(
						point.latitude,
						point.longitude,
						1);
				
				if (addresses == null || addresses.size() == 0) {
					Toasty.error(MapsActivity.this, "No address found", Toasty.LENGTH_LONG).show();
				} else {
					Address address = addresses.get(0);
					for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
						strAddress = address.getAddressLine(i);
					}
					
				}
				assert strAddress != null;
				if (!strAddress.equalsIgnoreCase("")) {
					ViewDialog alert = new ViewDialog();
					alert.showPolygonDialog(MapsActivity.this, strAddress, String.valueOf(point.latitude), String.valueOf(point.longitude));
				} else {
					Toasty.error(MapsActivity.this, "No address found", Toasty.LENGTH_LONG).show();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			Toasty.error(MapsActivity.this, "No internet", Toasty.LENGTH_LONG).show();
		}
		
		
	}
	
	@Override
	public boolean onMarkerClick(Marker marker) {
		
		try {
			Object pos = marker.getTag();
			if (pos!=null) {
				startActivity(new Intent(MapsActivity.this, MainActivity.class).putExtra("position", Integer.parseInt(pos.toString())));
			}else {
				Toasty.error(MapsActivity.this,"No data found",Toasty.LENGTH_LONG).show();
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	public class ViewDialog {
		
		
		void showPolygonDialog(final Activity activity,
							   final String address, final String mLat, final String mLng) {
			final Dialog dialog = new Dialog(activity);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setCancelable(false);
			dialog.setContentView(R.layout.marker_row);
			dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
			
			TextView mDialogNo = dialog.findViewById(R.id.frmNo);
			final ImageView mImageView = dialog.findViewById(R.id.imgcapture);
			final TextView tvAddress = dialog.findViewById(R.id.address);
			TextView location = dialog.findViewById(R.id.location);
			tvAddress.setText(address);
			location.setText(mLat + " || " + mLng);
			
			new SimpleTooltip.Builder(MapsActivity.this)
					.anchorView(mImageView)
					.text("click take image")
					.gravity(Gravity.START)
					.animated(true)
					.transparentOverlay(false)
					.build()
					.show();
			mMap.setMyLocationEnabled(true);
			
			mImageView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					imageId = generateMyNumber();
					if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
						dispatchTakePictureIntent1(mImageView);
					}
				}
			});
			
			mDialogNo.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					
				}
			});
			
			TextView mDialogOk = dialog.findViewById(R.id.frmOk);
			mDialogOk.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					
					if (mImageView.getDrawable() != null) {
						LocationEntity locationEntity = new LocationEntity(mLat, mLng, mCurrentPhotoPath, address);
						SaveTask st = new SaveTask();
						st.execute(locationEntity);
						startActivity(new Intent(MapsActivity.this, MainActivity.class));
						mImageView.setImageDrawable(null);
						dialog.dismiss();
					} else {
						Toasty.error(getApplicationContext(), "Click Image", Toasty.LENGTH_LONG).show();
					}
				}
			});
			
			dialog.show();
		}
	}
	
	private void dispatchTakePictureIntent1(ImageView mImageView) {
		try {
			Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			
			if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
				
				File photoFile = null;
				try {
					photoFile =  imageDetail(mImageView);
				} catch (Exception ex) {
					
				}
				if (photoFile != null) {
					takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
							Uri.fromFile(photoFile));
					startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO1);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private File imageDetail(ImageView mImageView) {
		
		File image = null;
		try {
			
			String imageFileName = imageId;
			String storageDir = Environment.getExternalStorageDirectory() + "/picupload/";
			image = new File(storageDir + imageFileName + ".jpg");
			File dir = new File(storageDir);
			if (!dir.exists())
				dir.mkdirs();
			
			
			Glide.with(this)
					.load(image)
					.apply(new RequestOptions())
					.diskCacheStrategy(DiskCacheStrategy.NONE
					)
					.skipMemoryCache(true)
					.into(mImageView);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return image;
	}
	
	public String generateMyNumber() {
		
		int aNumber = (int) ((Math.random() * 9000000) + 1000000);
		String randn = String.valueOf(aNumber);
		return randn;
	}
	
	@SuppressLint("MissingSuperCall")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		super.onActivityResult(requestCode, resultCode, data);
		try {
			
			
			if (requestCode == REQUEST_TAKE_PHOTO1 && resultCode == Activity.RESULT_OK) {
				
				String dirString = Environment.getExternalStorageDirectory() + "/picupload/";
				File dir = new File(dirString);
				File image = new File(dir + "/" + imageId + ".jpg");
				
				mCurrentPhotoPath = image.getAbsolutePath();
				
				
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	class SaveTask extends AsyncTask<LocationEntity, Void, Void> {
		
		@Override
		protected Void doInBackground(LocationEntity... voids) {
			
			DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
					.taskDao()
					.insertAllArea(voids[0]);
			
			
			return null;
		}
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		MenuItem dataView = menu.add(R.string.app_name);
		dataView.setShowAsAction(MenuItem.SHOW_AS_ACTION_WITH_TEXT | MenuItem.SHOW_AS_ACTION_IF_ROOM);
		dataView.setIcon(R.drawable.view_data);
		
		dataView.setOnMenuItemClickListener(
				new MenuItem.OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(MenuItem dataView) {
						startActivity(new Intent(MapsActivity.this, MainActivity.class));
						return true;
					}
				}
		);
	
		return true;
	}
	
	
	public void initStep() {
		mMap.setMyLocationEnabled(true);
		
		gps=new GPSTracker(this);
		if (gps.canGetLocation()) {
			latitude = gps.getLatitude();
			longitude = gps.getLongitude();
			currentLatLng = latitude + "||" + longitude;
		}
		
		LatLng latLng = new LatLng(latitude, longitude);
		
		mMap.addMarker(new MarkerOptions().position(latLng).title(currentLatLng)).setDraggable(true);
		mapExtraFields();
		mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.0f));
	}
	
	public void mapExtraFields() {
		mMap.getUiSettings().setZoomControlsEnabled(true);
		mMap.getUiSettings().setCompassEnabled(true);
		mMap.getUiSettings().setMyLocationButtonEnabled(true);
		mMap.getUiSettings().setMapToolbarEnabled(true);
		mMap.getUiSettings().setZoomGesturesEnabled(true);
		mMap.getUiSettings().setScrollGesturesEnabled(true);
		mMap.getUiSettings().setTiltGesturesEnabled(true);
		mMap.getUiSettings().setRotateGesturesEnabled(true);
		mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
	}
	
	
	@Override
	protected void onStop() {
		
		gps.stopUsingGPS();
		super.onStop();
	}
	
	@SuppressLint("CheckResult")
	private void onMapReady() {
		
		mMap.setOnMapLongClickListener(this);
		mMap.setOnMarkerClickListener(this);
		mMap.setOnMapClickListener(this);
		Intent intent = getIntent();
		
		if (intent.getExtras() != null) {
			type = intent.getIntExtra("type", 0);
		} else {
			type = 0;
		}
		RxPermissions rxPermissions = new RxPermissions(this);
		
		 rxPermissions.request(Manifest.permission.ACCESS_FINE_LOCATION,
				Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE) // ask single or multiple permission once
				.subscribe(new Consumer<Boolean>() {
					@Override
					public void accept(Boolean granted) {
						if (granted) {
							if (gps.isGPSEnabled) {
								if (type == 0) {
									getCount().observe(MapsActivity.this, new Observer<Integer>() {
										@Override
										public void onChanged(Integer integer) {
											if (integer > 0) {
												getAllLocation().observe(MapsActivity.this, new Observer<List<LocationEntity>>() {
													@Override
													public void onChanged(List<LocationEntity> locationEntities) {
														for (LocationEntity locationEntity : locationEntities) {
															
															mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(locationEntity.getLatitude()),
																	Double.parseDouble(locationEntity.getLongitude()))).title(locationEntity.getAddress())
															).setTag(locationEntity.getId());
															
															mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(locationEntity.getLatitude()), Double.parseDouble(locationEntity.getLongitude())), 17.0f));
															mapExtraFields();
															
														}
														
													}
												});
											} else {
												initStep();
											}
										}
									});
								} else {
									
									getAllLocationById(type).observe(MapsActivity.this, new Observer<List<LocationEntity>>() {
										@Override
										public void onChanged(List<LocationEntity> locationEntities) {
											for (LocationEntity locationEntity : locationEntities) {
												mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(locationEntity.getLatitude()),
														Double.parseDouble(locationEntity.getLongitude()))).title(locationEntity.getAddress())
												).setTag(locationEntity.getId());
												mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(locationEntity.getLatitude()), Double.parseDouble(locationEntity.getLongitude())), 17.0f));
												mapExtraFields();
											}
										}
									});
								}
							} else {
								gps.showSettingsAlert();
								Toasty.error(MapsActivity.this, "Enable Gps", Toasty.LENGTH_LONG).show();
							}
						}
						
					}
				});
		
	}
}