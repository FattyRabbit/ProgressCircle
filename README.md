ProgressCircle
==============

## What is a ProgressCircle

ProgressCircle is a Custom View for show progress in a circular.


<div align="center">
  <img width="348px" src="https://github.com/FattyRabbit/ProgressCircle/raw/master/screenshot1.png"/>
</div>


## Usage

### Use it in your own Code

Add the View in your Layout

    <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:progresscircle="http://schemas.android.com/apk/res/com.example.progresscircle"
        ・・・・ >

    <com.example.progresscircle.ProgressCircle
        android:id="@+id/progressCircle"
        android:layout_width="100dp"
        android:layout_height="100dp"
        progresscircle:text="1M"
        progresscircle:textColor="#FF33B5E5"
        progresscircle:textSize="20dp"
        progresscircle:rimColor="#FF0A0A0A"
        progresscircle:rimWidth="5dp"
        progresscircle:barColor="#FF33B5E5"
        progresscircle:barWidth="10dp"
        progresscircle:barAlign="ALIGN_OUTER"
        progresscircle:max="100"
        progresscircle:progress="0" />

    </LinearLayout>

Add the View in code

	ProgressCircle mProgressCircle = new ProgressCircle();
	mProgressCircle.setText("1M");
	mProgressCircle.setTextColor(Color.parseColor("#FF33B5E5"));
	mProgressCircle.setTextSize(100);
	mProgressCircle.setRimColor(Color.parseColor("#FF0A0A0A"));
	mProgressCircle.setRimWidth(5);
	mProgressCircle.setBarColor(Color.parseColor("#FF33B5E5"));
	mProgressCircle.setBarWidth(10);
	mProgressCircle.setBarAlign(ProgressCircle.ALIGN_OUTER);
	mProgressCircle.setMax(100);
	mProgressCircle.setProgress(0);

	LayoutParams params = new LayoutParams(100, 100);
	this.addContentView(mProgressCircle, params);

## License

    Copyright 2013 Kugyon I

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.


Author: [Kugyon I](https://plus.google.com/107785783803600614558/posts)
