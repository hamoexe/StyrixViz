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

package com.hamo.styrixviz.comp.LibraryQueueUI.ViewHolders;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hamo.styrixviz.comp.LibraryQueueUI.Containers.Base.IContainerData;
import com.hamo.styrixviz.Common.Events.WeakEvent1;
import com.hamo.styrixviz.Common.Events.WeakEvent2;
import com.hamo.styrixviz.Common.Events.WeakEvent3;
import com.hamo.styrixviz.Common.Events.WeakEventR;
import com.hamo.styrixviz.comp.AppPreferences.AppPreferences;
import com.hamo.styrixviz.ContextData;
import com.hamo.styrixviz.R;

import java.util.List;

public abstract class BaseHeaderViewHolder extends BaseViewHolder {

    public static WeakEventR<Boolean> onUIRequestFollowCurrentValue = new WeakEventR<>();
    public static WeakEvent1<ContextData> onActionChooseSortFiles = new WeakEvent1<>();
    public static WeakEvent1<ContextData> onActionChooseSort = new WeakEvent1<>();
    public static WeakEvent2<ContextData /*contextData*/, Integer /*action*/> onAction = new WeakEvent2<>();
    public static WeakEvent1<ContextData /*contextData*/> onLibraryQueue2UI_ActionScanStandalonePlaylist = new WeakEvent1<>();
    public static WeakEvent3<long[] /*addSongsNativePL*/, List<String> /*addSongDataSources*/, ContextData /*contextData*/> onActionCreatePlaylist = new WeakEvent3<>();

    public BaseHeaderViewHolder(View itemView) {
        super(itemView);

    }

    protected static void updateShowButtonTexts(TextView[] buttonTexts, Context context) {
        boolean showButtonTexts = AppPreferences.preferencesGetBoolSafe(AppPreferences.createOrGetInstance().getPreferences(context), "pref_toolButtonsShowTexts", true);

        if (showButtonTexts) {
            for (TextView txt : buttonTexts) {
                txt.setVisibility(View.VISIBLE);
            }
        } else {
            for (TextView txt : buttonTexts) {
                txt.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onBind(IContainerData containerData, int position) {
    }

}
