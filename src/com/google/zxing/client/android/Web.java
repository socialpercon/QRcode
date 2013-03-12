package com.google.zxing.client.android;

import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

public class Web extends Activity {
	private String url = null;
	WebView browser;
	private boolean mActivityInPause = true;

	DefaultHttpClient client;
	private final Handler handler = new Handler();

	final Activity activity = this;
	ProgressDialog mProgress;

	Intent intent = null;
	Bitmap bitmapLogo = null;
	BitmapDrawable bitmapDrawableLogo = null;
	ImageView iv = null;
	ImageView iv1 = null;
	ImageView iv2 = null;
	ImageView iv3 = null;
	ImageView iv4 = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		CookieSyncManager.createInstance(this);

		super.onCreate(savedInstanceState);
		System.gc();
		getWindow().requestFeature(Window.FEATURE_PROGRESS);
		if (getParent() != null) {
			getParent().setProgressBarIndeterminateVisibility(true);
		}
		setContentView(R.layout.web);

		browser = (WebView) findViewById(R.id.webkit);

		final Activity activity = this;

		browser.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				// TODO Auto-generated method stub
				activity.setProgress(newProgress * 100);
			}
		});

		Intent in = getIntent();
		Bundle extras = in.getExtras();

		String targetUrl = extras.getString("target");

		url = targetUrl;
		Log.v("url1: ", String.valueOf(url));

		browser.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
				case MotionEvent.ACTION_UP:
					if (!v.hasFocus()) {
						v.requestFocus();
					}
					break;
				}
				return false;
			}
		});

		browser.getSettings().setJavaScriptEnabled(true);
		browser.loadUrl(url); // url ?∏ÏûêÎ°?Î∏åÎùº?∞Ï†∏ Î°úÎìú
		browser.setWebViewClient(new WebView1Client());

	}

	private void callUrl(String url) {

		browser.getSettings().setJavaScriptEnabled(true);
		browser.loadUrl(url); // url ?∏ÏûêÎ°?Î∏åÎùº?∞Ï†∏ Î°úÎìú
		browser.setWebViewClient(new WebView1Client());
	}

	private class WebView1Client extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			Log.v("overind", url);
			// if (url.startsWith("http://")) {
			// return false;
			// }
			// else

			if (url.startsWith("tel:")) {
				Intent call_phone = new Intent(Intent.ACTION_VIEW,
						Uri.parse(url));
				startActivity(call_phone);
				return true;
			}

			return false;

			// view.loadUrl(url);
			// return true;
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			if (mProgress == null) {

				mProgress = new ProgressDialog(activity);
				mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				mProgress.setMessage("∑ŒµÂ¡ﬂ..");
				mProgress.show();
			}
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			if (mProgress != null) {
				if (mProgress.isShowing()) {
					mProgress.dismiss();
				}
			}
		}

	}

	public boolean goBack() {
		browser.goBack();
		return true;
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && browser.canGoBack()) {
			browser.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (!mActivityInPause) {
			return;
		}

		mActivityInPause = false;
		if (this.browser != null) {
			this.browser.resumeTimers();
		}

		browser.reload();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// CookieSyncManager.getInstance().stopSync();
		if (mActivityInPause) {
			return;
		}
		if (this.browser != null) {
			this.browser.pauseTimers();
		}
		mActivityInPause = true;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}

	private void clearApplicationCache(java.io.File dir) {
		if (dir == null)
			dir = getCacheDir();
		else
			;
		if (dir == null)
			return;
		else
			;
		java.io.File[] children = dir.listFiles();
		try {
			for (int i = 0; i < children.length; i++)
				if (children[i].isDirectory())
					clearApplicationCache(children[i]);
				else
					children[i].delete();
		} catch (Exception e) {
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		clearApplicationCache(null);

	}

}