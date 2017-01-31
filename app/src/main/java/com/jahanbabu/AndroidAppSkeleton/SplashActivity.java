package com.jahanbabu.AndroidAppSkeleton;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

import com.orhanobut.hawk.Hawk;

import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SplashActivity extends Activity {
	
	private VideoView videoView;
	
	private Button btnSkip;
	Context context;

	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		context = this;
		Hawk.init(context).build();

		if (BuildConfig.DEBUG) {
			Timber.plant(new Timber.DebugTree());
		} else {
			Timber.plant(new CrashReportingTree());
		}

		CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
//                .setDefaultFontPath("fonts/Raleway-Light.ttf")
						.setDefaultFontPath("fonts/ClassicRobot.otf")
//                .setDefaultFontPath("fonts/Roboto-Light.ttf")
						.setFontAttrId(R.attr.fontPath)
						.build()
		);

		Timber.e("Testing Timber is working or not !!!");

//		RunAnimation();
    }

	private void RunAnimation()
	{
		Animation a = AnimationUtils.loadAnimation(this, R.anim.app_name_alpha);
		a.reset();
		TextView tv = (TextView) findViewById(R.id.appNameTextView);
		tv.clearAnimation();
		tv.startAnimation(a);
		a.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				startActivity(new Intent(SplashActivity.this, SigninActivity.class));
				finish();
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}
		});
	}

	public static boolean isNetworkAvailable(Context context){
		ConnectivityManager cm =
				(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		boolean isConnected = activeNetwork != null &&
				activeNetwork.isConnectedOrConnecting();

		return isConnected;
	}

	public static void showNoInternetDialog(final Context context, final Activity activity) {
		AlertDialog dailog;
		AlertDialog.Builder build = new AlertDialog.Builder(context);
		build.setMessage("This application requires Internet connection.\nWould you connect to internet ?");
		build.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				activity.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
			}
		});
		build.setNegativeButton("No", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				AlertDialog dailog;
//				ContextThemeWrapper ctw = new ContextThemeWrapper(context, R.style.MyAlertDialogStyle);
				AlertDialog.Builder build = new AlertDialog.Builder(context);
				build.setMessage("Are sure you want to exit?");
				build.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						activity.finish();
					}
				});
				build.setNegativeButton("No", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				dailog = build.create();
				dailog.show();
			}
		});
		dailog = build.create();
		dailog.show();
	}
	/** A tree which logs important information for crash reporting. */
	private static class CrashReportingTree extends Timber.Tree {
		@Override
		protected void log(int priority, String tag, String message, Throwable t) {
			if (priority == Log.VERBOSE || priority == Log.DEBUG) {
				return;
			}

			FakeCrashLibrary.log(priority, tag, message);

			if (t != null) {
				if (priority == Log.ERROR) {
					FakeCrashLibrary.logError(t);
				} else if (priority == Log.WARN) {
					FakeCrashLibrary.logWarning(t);
				}
			}
		}
	}
}
