/*
 * Copyright 2019 Avee Player. All rights reserved.
 * Modified for Styrix Viz: package renamed to com.hamo.styrixviz.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hamo.styrixviz.Design;

import android.app.FragmentManager;

import com.hamo.styrixviz.Common.Events.WeakEvent;
import com.hamo.styrixviz.Common.Events.WeakEvent2;
import com.hamo.styrixviz.Common.Events.WeakEvent3;
import com.hamo.styrixviz.Common.Events.WeakEventR;
import com.hamo.styrixviz.comp.playback.EventsPlaybackService;
import com.hamo.styrixviz.comp.SleepTimer.SleepTimer;
import com.hamo.styrixviz.comp.SleepTimer.SleepTimerConfig;
import com.hamo.styrixviz.comp.SleepTimer.SleepTimerDialog;
import com.hamo.styrixviz.ContextData;
import com.hamo.styrixviz.MainActivity;

import java.util.LinkedList;
import java.util.List;

public class SleepTimerDesign {

    private List<Object> listenerRefHolder = new LinkedList<>();

    public SleepTimerDesign() {

        MainActivity.onMainUIAction.subscribeWeak(new WeakEvent2.Handler<Integer, ContextData>() {
            @Override
            public void invoke(Integer id, ContextData contextData) {
                FragmentManager fragmentManager = contextData.getFragmentManager();
                if (fragmentManager == null) return;

                if (id == 1) {
                    SleepTimerDialog.createAndShowSleepTimerDialog(fragmentManager);
                }

            }
        }, listenerRefHolder);

        MainActivity.onMainUIRequestSleepTimerConfig.subscribeWeak(new WeakEventR.Handler<SleepTimerConfig>() {
            @Override
            public SleepTimerConfig invoke() {
                SleepTimer sleepTimer = SleepTimer.createOrGetInstance();
                if (sleepTimer == null) return new SleepTimerConfig();

                return sleepTimer.getConfig();
            }
        }, listenerRefHolder);

        SleepTimer.onSleepTimerConfigChanged.subscribeWeak(new WeakEvent3.Handler<Boolean, Integer, Boolean>() {
            @Override
            public void invoke(Boolean enabled, Integer minutes, Boolean playLastSongToEnd) {
                final SleepTimerConfig config = new SleepTimerConfig();
                config.enabled = enabled;
                config.minutes = minutes;
                config.playLastSongToEnd = playLastSongToEnd;

                MainActivity mainActivity = MainActivity.getInstance();
                if (mainActivity != null)
                    mainActivity.updateSleepTimerIndicator(config.enabled, false);

            }
        }, listenerRefHolder);

        SleepTimer.onSleepTimerFire.subscribeWeak(new WeakEvent.Handler() {
            @Override
            public void invoke() {
                EventsPlaybackService.Receive.onAction.invoke(EventsPlaybackService.Receive.ACTION_Stop);
            }
        }, listenerRefHolder);

        SleepTimerDialog.onSleepTimerUISubmit.subscribeWeak(new WeakEvent3.Handler<Boolean, Integer, Boolean>() {
            @Override
            public void invoke(Boolean enabled, Integer minutes, Boolean playLastSongToEnd) {
                SleepTimer sleepTimer = SleepTimer.createOrGetInstance();
                if (sleepTimer == null) return;

                sleepTimer.configure(enabled, minutes, playLastSongToEnd);
            }
        }, listenerRefHolder);

        SleepTimerDialog.onSleepTimerUIRequestSleepTimerConfig.subscribeWeak(new WeakEventR.Handler<SleepTimerConfig>() {
            @Override
            public SleepTimerConfig invoke() {
                SleepTimer sleepTimer = SleepTimer.createOrGetInstance();
                if (sleepTimer == null) return new SleepTimerConfig();

                return sleepTimer.getConfig();
            }
        }, listenerRefHolder);

        SleepTimerDialog.onSleepTimerUIRequestRemainingSeconds.subscribeWeak(new WeakEventR.Handler<Integer>() {
            @Override
            public Integer invoke() {
                SleepTimer sleepTimer = SleepTimer.createOrGetInstance();
                if (sleepTimer == null) return 0;

                return sleepTimer.getRemainingSeconds();
            }
        }, listenerRefHolder);
    }

}
