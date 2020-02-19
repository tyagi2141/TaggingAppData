package rahultyag.in.taggingapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import rahultyag.in.taggingapp.model.LocationEntity;
import rahultyag.in.taggingapp.R;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.MyViewHolder> {
	private View.OnClickListener mOnItemClickListener;
	private List<LocationEntity> mDataList;
	private Context context;
	private int itemViewPosition;
	private View itemView=null;
	class MyViewHolder extends RecyclerView.ViewHolder {
		TextView  strAddress, strLatLng;
		ImageView mphotos;
		CardView mCardView;
		
		public MyViewHolder(View view) {
			super(view);
			mphotos =  view.findViewById(R.id.ImgVw_Letters);
			strLatLng = (TextView) view.findViewById(R.id.latlng_id);
			strAddress = (TextView) view.findViewById(R.id.address_id);
			mCardView =  view.findViewById(R.id.cardview_id);
			itemView.setTag(this);
			itemView.setOnClickListener(mOnItemClickListener);
		}
	}
	
	
public 	LocationAdapter(List<LocationEntity> mDataList, Context mContext,int itemViewPositions) {
		this.mDataList = mDataList;
		this.context=mContext;
		this.itemViewPosition=itemViewPositions;
	}
	
	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		 itemView = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.list_row, parent, false);

		
		return new MyViewHolder(itemView);
	}
	
	@Override
	public void onBindViewHolder(MyViewHolder holder, int position) {
		LocationEntity entity = mDataList.get(position);
		if (itemViewPosition>=0  && itemViewPosition==position){
			holder.mCardView.setCardBackgroundColor(Color.parseColor("#e5ff9a"));
		}else {
			holder.mCardView.setCardBackgroundColor(Color.parseColor("#fefffd"));
		}
		
		
		File file = new File(entity.getImages());
		Uri imageUri = Uri.fromFile(file);
		Glide.with(context.getApplicationContext()).asBitmap()
				.load(imageUri)
				.diskCacheStrategy(DiskCacheStrategy.NONE
				)
				.skipMemoryCache(true)
				.into(holder.mphotos);
		holder.strLatLng.setText(entity.getLatitude()+" || "+entity.getLongitude());
		holder.strAddress.setText(entity.getAddress());
		
		
		
	}
	
	@Override
	public int getItemCount() {
		return mDataList.size();
	}
	public  void setOnItemClickListener(View.OnClickListener itemClickListener) {
		mOnItemClickListener = itemClickListener;
	}
}