package rahultyag.in.taggingapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;
import rahultyag.in.taggingapp.R;
import rahultyag.in.taggingapp.adapter.LocationAdapter;
import rahultyag.in.taggingapp.database.DatabaseClient;
import rahultyag.in.taggingapp.model.LocationEntity;

public class MainActivity extends AppCompatActivity {
	private RecyclerView recyclerView;
	private LocationAdapter mAdapter;
	List<LocationEntity> mLocationEntities = new ArrayList<>();
	int position = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		recyclerView = findViewById(R.id.recycle);
		Intent intent = getIntent();
		if (intent.getExtras() != null) {
			position = intent.getIntExtra("position", 0);
		}
		position = position - 1;
		getAllLocation().observe(MainActivity.this, new Observer<List<LocationEntity>>() {
			@Override
			public void onChanged(List<LocationEntity> locationEntities) {
				mLocationEntities = locationEntities;
				
				final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
				linearLayoutManager.setStackFromEnd(true);
				mAdapter = new LocationAdapter(locationEntities, getApplicationContext(), position);
				RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
				recyclerView.setLayoutManager(mLayoutManager);
				recyclerView.setItemAnimator(new DefaultItemAnimator());
				recyclerView.setAdapter(mAdapter);
				if (position >= 0) {
					
					recyclerView.smoothScrollToPosition(position);
				}
				mAdapter.setOnItemClickListener(onItemClickListener);
				mAdapter.notifyDataSetChanged();
			}
		});
		
		
	}
	
	public LiveData<List<LocationEntity>> getAllLocation() {
		return DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
				.taskDao()
				. getAllLocation();
	}
	
	View.OnClickListener onItemClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
			int position = viewHolder.getAdapterPosition();
			LocationEntity thisItem = mLocationEntities.get(position);
			startActivity(new Intent(MainActivity.this, MapsActivity.class).putExtra("type", thisItem.id));
			finish();
			Toasty.info(MainActivity.this,  "You Clicked: " + thisItem.getAddress(), Toasty.LENGTH_LONG).show();
		}
	};
}
