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
import android.widget.ImageButton;
import android.widget.TextView;

import com.hamo.styrixviz.comp.LibraryQueueUI.Containers.Base.IContainerData;
import com.hamo.styrixviz.Common.UtilsUI;
import com.hamo.styrixviz.comp.LibraryQueueUI.Containers.Base.ContainerBase;
import com.hamo.styrixviz.R;

class SectionViewHolder
        extends BaseViewHolder implements
        ContainerBase.IContainerStatusListener {

    private ImageButton btnCollapse;
    private TextView textTitle;

    public SectionViewHolder(Context context, ViewGroup parent) {
        super(UtilsUI.getInflaterFromContext(context).inflate(R.layout.section_item, parent, false));

        View view = this.itemView;
        btnCollapse = (ImageButton) view.findViewById(R.id.btnCollapse);
        textTitle = (TextView) view.findViewById(R.id.txtTitle);
    }

    public void onBind(final IContainerData containerData, int position) {
        textTitle.setText(containerData.getDisplayName());
        updateButtonState(containerData.getSectionOpened());

        btnCollapse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                containerData.setSectionOpenedState(!containerData.getSectionOpened());
                updateButtonState(containerData.getSectionOpened());
            }
        });
    }

    @Override
    public void onItemCountChanged(int itemCount, int totalTime, boolean searchingActive) {

    }

    void updateButtonState(boolean state) {
        if (state)
            btnCollapse.setImageResource(R.drawable.ic_minus);
        else
            btnCollapse.setImageResource(R.drawable.ic_expand2);
    }

}