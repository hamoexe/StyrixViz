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
import com.hamo.styrixviz.Common.UtilsUI;
import com.hamo.styrixviz.comp.LibraryQueueUI.Containers.Base.ContainerBase;
import com.hamo.styrixviz.R;

import java.lang.ref.WeakReference;

public class Footer1ViewHolder
        extends BaseViewHolder
        implements ContainerBase.IContainerStatusListener {

    private TextView textInfo, textInfoItems;

    public Footer1ViewHolder(Context context, ViewGroup parent) {
        super(UtilsUI.getInflaterFromContext(context).inflate(R.layout.footer_item, parent, false));

        View view = this.itemView;

        textInfo = (TextView) view.findViewById(R.id.textInfo);
        textInfoItems = (TextView) view.findViewById(R.id.textInfoItems);

        View navBarPlaceHolder = view.findViewById(R.id.navbar_placeholder);
        ViewGroup.LayoutParams params = navBarPlaceHolder.getLayoutParams();
        params.height = UtilsUI.getNavBarHeightIgnoreOrienCached(context);
        navBarPlaceHolder.setLayoutParams(params);

    }

    public void onBind(final IContainerData containerData, int position) {
        textInfo.setText("");
        textInfoItems.setText("");
        containerData.setContainerStatusListener(new WeakReference<ContainerBase.IContainerStatusListener>(this));

        onDataItemCountChanged();
    }

    private void onDataItemCountChanged() {

    }

    @Override
    public void onItemCountChanged(int itemCount, int totalTime, boolean searchingActive) {

        int itemCountVisible = itemCount;

        if (searchingActive) {
            textInfo.setText(R.string.searching);
            String txt = textInfoItems.getResources().getQuantityString(
                    R.plurals.showing_x_of_y_items3_item_count, itemCountVisible, itemCountVisible);
            textInfoItems.setText(txt);
        } else {

            if (itemCountVisible == 0) {
                textInfo.setText(R.string.nothing_to_show);
                textInfoItems.setText("");
            } else {
                String txt = textInfoItems.getResources().getQuantityString(
                        R.plurals.showing_x_of_y_items3_item_count, itemCountVisible, itemCountVisible);
                textInfoItems.setText(txt);
            }
        }
    }

}