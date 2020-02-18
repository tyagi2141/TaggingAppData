package rahultyag.in.taggingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
	private RecyclerView recyclerView;
	private LocationAdapter mAdapter;
	List<LocationEntity> mLocationEntities=new ArrayList<>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		recyclerView =  findViewById(R.id.recycle);
		getAllLocation().observe(MainActivity.this, new Observer<List<LocationEntity>>() {
			@Override
			public void onChanged(List<LocationEntity> locationEntities) {
				mLocationEntities=locationEntities;
				mAdapter = new LocationAdapter(locationEntities,getApplicationContext());
				RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
				recyclerView.setLayoutManager(mLayoutManager);
				recyclerView.setItemAnimator(new DefaultItemAnimator());
				recyclerView.setAdapter(mAdapter);
				mAdapter.setOnItemClickListener(onItemClickListener);
			}
		});




	}
	public LiveData<List<LocationEntity>> getAllLocation(){
		return 	DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
				.taskDao()
				.getAllArea();
	}

	View.OnClickListener onItemClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {

			RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
			int position = viewHolder.getAdapterPosition();
			LocationEntity thisItem = mLocationEntities.get(position);

			Toast.makeText(MainActivity.this, "You Clicked: " + thisItem.getAddress(), Toast.LENGTH_SHORT).show();
		}
	};
}
