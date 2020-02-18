package rahultyag.in.taggingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

public class MainActivity extends AppCompatActivity {
	private RecyclerView recyclerView;
	private LocationAdapter mAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		recyclerView = (RecyclerView) findViewById(R.id.recycle);
		getArea();
	}
	
	
	private void getArea() {
		class GetTasks extends AsyncTask<Void, Void, List<LocationEntity>> {
			
			@Override
			protected List<LocationEntity> doInBackground(Void... voids) {
				List<LocationEntity> taskList = DatabaseClient
						.getInstance(getApplicationContext())
						.getAppDatabase()
						.taskDao()
						.getAllArea();
				
				return taskList;
			}
			@Override
			protected void onPostExecute(List<LocationEntity> areas) {
				super.onPostExecute(areas);
				Log.e("jdfbvjbjfbfb",areas.toString());
				mAdapter = new LocationAdapter(areas,getApplicationContext());
				RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
				recyclerView.setLayoutManager(mLayoutManager);
				recyclerView.setItemAnimator(new DefaultItemAnimator());
				recyclerView.setAdapter(mAdapter);
				
			}
		}
		
		GetTasks gt = new GetTasks();
		gt.execute();
	}
	
}
