package com.example.progresscircle;

import java.math.BigDecimal;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {

	private float mLaptime = 0.0f;
	private float totalMSecond = 0.25f * 60 * 1000;
	private float progressMSecond = 0.0f;
	private Timer mTimer = null;
	private Handler mHandler = new Handler();
	private ProgressCircle mProgressCircle = null;

	public void onClick(View v) {

		Button btn = (Button) v;

		switch (btn.getId()) {

		// スタートボタンが押されたとき
		case R.id.btn_start:

			if (mTimer == null) {
				// タイマーの初期化処理
				mTimer = new Timer(true);
				mTimer.schedule(new TimerTask() {
					@Override
					public void run() {
						// mHandlerを通じてUI Threadへ処理をキューイング
						mHandler.post(new Runnable() {
							public void run() {
								// 実行間隔分を加算処理
								mLaptime += 0.1f;
								progressMSecond += 100.0f;

								// 計算にゆらぎがあるので小数点第1位で丸める
								BigDecimal bi = new BigDecimal(mLaptime);
								float outputValue = bi.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();

								// 現在のLapTime
								mProgressCircle.setText(Float.toString(outputValue) + "S");
								mProgressCircle.setProgress(progressMSecond);
								mProgressCircle.invalidate();

								if (totalMSecond <= progressMSecond) {
									mTimer.cancel();
									mTimer.purge();
									mTimer = null;
									mLaptime = 0.0f;
									progressMSecond = 0.0f;
								}
							}
						});
					}
				}, 100, 100);
			}
			break;

		// ストップボタンが押されたとき
		case R.id.btn_stop:
			if (mTimer != null) {
				mTimer.cancel();
				mTimer = null;
			}
			break;

		default:
			break;

		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mProgressCircle = (ProgressCircle) findViewById(R.id.progressCircle);
		mProgressCircle.setMax(totalMSecond);
		mProgressCircle.setProgress(progressMSecond);

		// タイマー開始ボタンの処理
		Button btnStart = (Button) findViewById(R.id.btn_start);
		btnStart.setOnClickListener(this);

		// タイマー停止ボタンの処理
		Button btnStop = (Button) findViewById(R.id.btn_stop);
		btnStop.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * 終了処理
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();

		cancelTimer();
	}

	/**
	 * タイマーをキャンセルする。
	 */
	private void cancelTimer() {
		if (mTimer != null) {
			mTimer.cancel();
		}
	}
}
