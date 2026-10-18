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
import com.hamo.styrixviz.comp.LibraryQueueUI.LibraryQueueFragmentBase;
import com.hamo.styrixviz.Common.UtilsUI;
import com.hamo.styrixviz.ContextData;
import com.hamo.styrixviz.R;

public class HeaderFoldersViewHolder
        extends BaseHeaderViewHolder {

    private TextView[] buttonTexts = new TextView[1];
    private View imgBtnPlay;

    public HeaderFoldersViewHolder(Context context, ViewGroup parent) {
        super(UtilsUI.getInflaterFromContext(context).inflate(R.layout.header_folder_item, parent, false));

        View view = this.itemView;

        imgBtnPlay = view.findViewById(R.id.group1);
        buttonTexts[0] = (TextView) view.findViewById(R.id.txt1);

        updateShowButtonTexts(buttonTexts, context);
    }

    public void onBind(final IContainerData containerData, int position) {
        super.onBind(containerData, position);

        HeaderFoldersViewHolder holder = this;

        holder.imgBtnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAction.invoke(new ContextData(v), LibraryQueueFragmentBase.ACTION_AddFolder);
            }
        });
    }

}