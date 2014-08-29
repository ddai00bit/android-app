package com.appshed.appstore.tasks;

import static com.appshed.appstore.utils.SystemUtils.*;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.appshed.appstore.entities.App;
import com.appshed.appstore.fragments.AppsByCategoryFragment;
import com.appshed.appstore.utils.SniRequestUtils;
import com.appshed.appstore.utils.SystemUtils;
import com.rightutils.rightutils.collections.RightList;
import com.rightutils.rightutils.tasks.BaseTask;

import ch.boye.httpclientandroidlib.HttpResponse;
import ch.boye.httpclientandroidlib.HttpStatus;
import ch.boye.httpclientandroidlib.util.EntityUtils;

/**
 * Created by Anton Maniskevich on 8/8/14.
 */
public class RetrieveCategoriesApps extends BaseTask {

	private static final String TAG = RetrieveCategoriesApps.class.getSimpleName();
	private AppsByCategoryFragment fragment;
	private String error;
	private AppData appData;
	private String category;
	private Long sinceId;
	private Long maxId;

	public RetrieveCategoriesApps(Context context, View progressBar, AppsByCategoryFragment fragment) {
		super(context, progressBar);
		this.fragment = fragment;
	}

	@Override
	protected Boolean doInBackground(String... params) {
		try {
			String resultUrl = APPS_URL;
			if (!category.equals(SystemUtils.GENERAL)) {
				resultUrl += "?category="+category;
			}
			if (sinceId != null) {
				resultUrl += "&since_id=" + sinceId;
			}
			if (maxId != null) {
				resultUrl += "&max_id=" + maxId;
			}
			Log.i(TAG, resultUrl);
			HttpResponse response = SniRequestUtils.getHttpResponse(resultUrl);
			int status = response.getStatusLine().getStatusCode();
			Log.i(TAG, "status code: " + String.valueOf(status));
			if (status == HttpStatus.SC_OK) {
				appData = MAPPER.readValue(response.getEntity().getContent(), AppData.class);
				return true;
			} else {
				error = EntityUtils.toString(response.getEntity());
			}
		} catch (Exception e) {
			Log.e(TAG, "run", e);
		}
		return false;
	}

	private static class AppData {
		public RightList<App> apps;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		if (result) {
			fragment.addApps(appData.apps);
		} else {
			fragment.addApps(new RightList<App>());
			if (error != null) {
				Toast.makeText(context, error, Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(context, "Unknown error", Toast.LENGTH_LONG).show();
			}
		}
		super.onPostExecute(result);
	}

	public RetrieveCategoriesApps setCategory(String category) {
		this.category = category;
		return this;
	}

	public RetrieveCategoriesApps setSinceId(Long sinceId) {
		this.sinceId = sinceId;
		return this;
	}

	public RetrieveCategoriesApps setMaxId(Long maxId) {
		this.maxId = maxId;
		return this;
	}
}
