package com.appshed.store.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.appshed.store.R;
import com.appshed.store.entities.App;
import com.appshed.store.utils.BitmapUtils;
import com.appshed.store.utils.ImageLoadingListenerImpl;
import com.appshed.store.utils.SystemUtils;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.rightutils.rightutils.collections.RightList;

/**
 * Created by Anton Maniskevich on 8/8/14.
 */
public class AppAdapter extends ArrayAdapter<App> {

	private int layout;

	public AppAdapter(Context context, RightList<App> apps, int layout) {
		super(context, layout, apps);
		this.layout = layout;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = View.inflate(getContext(), layout, null);
		App app = getItem(position);
		((TextView) view.findViewById(R.id.txt_title)).setText(app.getName());
		((TextView) view.findViewById(R.id.txt_description)).setText(app.getDescription());
		if (layout == R.layout.item_tile_app) {
			ImageAware appBg = new ImageViewAware((ImageView) view.findViewById(R.id.img_app_bg), false);
			SystemUtils.IMAGELOADER.displayImage(app.getFeaturedImage(), appBg);
		}
		final ImageView icon = (ImageView) view.findViewById(R.id.img_icon);
		SystemUtils.IMAGELOADER.displayImage(app.getIcon(), icon, new ImageLoadingListenerImpl() {

			@Override
			public void onLoadingComplete(String imageUri, View view, final Bitmap loadedImage) {
				if (loadedImage != null) {
					icon.setImageBitmap(BitmapUtils.getCroppedRoundBitmap(loadedImage));
				} else {
					icon.setImageResource(R.drawable.ic_launcher);
				}
			}
		});
		return view;
	}

	public void changeLayout(int layout) {
		this.layout = layout;
		notifyDataSetInvalidated();
	}
}
