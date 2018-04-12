/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.test.xg.mytvtest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/*
 * MainActivity class that loads MainFragment
 */
public class MainActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button startFloatWindow = (Button) findViewById(R.id.start_float_window);
        startFloatWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
//                MyWindowManager.getFloatBallManager(BaseApplication.getmCtx()).createSmallWindow();
                Intent intent = new Intent(MainActivity.this, FloatWindowService.class);
                startService(intent);
                finish();
            }
        });
    }
}
