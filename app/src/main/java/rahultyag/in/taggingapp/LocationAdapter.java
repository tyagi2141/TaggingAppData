package rahultyag.in.taggingapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Movie;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.MyViewHolder> {
	
	private List<LocationEntity> moviesList;
	Context context;
	public class MyViewHolder extends RecyclerView.ViewHolder {
		public TextView title, strAddress, strLatLng;
		ImageView mphotos;
		
		public MyViewHolder(View view) {
			super(view);
			mphotos =  view.findViewById(R.id.ImgVw_Letters);
			strLatLng = (TextView) view.findViewById(R.id.latlng_id);
			strAddress = (TextView) view.findViewById(R.id.address_id);
		}
	}
	
	
	public LocationAdapter(List<LocationEntity> moviesList,Context mContext) {
		this.moviesList = moviesList;
		this.context=mContext;
	}
	
	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View itemView = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.list_row, parent, false);
		
		return new MyViewHolder(itemView);
	}
	
	@Override
	public void onBindViewHolder(MyViewHolder holder, int position) {
		LocationEntity movie = moviesList.get(position);
	
		File file = new File(movie.getImages());
		Uri imageUri = Uri.fromFile(file);
	
		Glide.with(context.getApplicationContext()).asBitmap()
				.load(imageUri)
				.diskCacheStrategy(DiskCacheStrategy.NONE
				)
				.skipMemoryCache(true)
				.into(holder.mphotos);
		holder.strLatLng.setText(movie.getLatitude()+" || "+movie.getLongitude());
		holder.strAddress.setText(movie.getAddress());
	}
	
	@Override
	public int getItemCount() {
		return moviesList.size();
	}
}