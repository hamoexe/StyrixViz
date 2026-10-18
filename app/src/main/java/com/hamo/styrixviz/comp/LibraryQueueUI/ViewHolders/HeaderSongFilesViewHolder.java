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
import android.widget.ImageView;
import android.widget.TextView;

import com.hamo.styrixviz.comp.LibraryQueueUI.Containers.Base.IContainerData;
import com.hamo.styrixviz.comp.LibraryQueueUI.LibraryQueueFragmentBase;
import com.hamo.styrixviz.Common.Events.WeakEvent1;
import com.hamo.styrixviz.Common.UtilsUI;
import com.hamo.styrixviz.ContextData;
import com.hamo.styrixviz.R;

import java.util.LinkedList;
import java.util.List;

public class HeaderSongFilesViewHolder extends BaseHeaderViewHolder {

    private ImageView[] buttonIcons = new ImageView[4];
    private TextView[] buttonTexts = new TextView[4];
    private View[] buttons = new View[4];
    private int btnColor0;
    private List<Object> listenerRefHolder = new LinkedList<>();

    public HeaderSongFilesViewHolder(final Context context, ViewGroup parent) {
        super(UtilsUI.getInflaterFromContext(context).inflate(R.layout.header_songs_item, parent, false));
        View view = this.itemView;

        btnColor0 = view.getResources().getColor(R.color.black_alpha_1);

        buttonTexts[0] = (TextView) view.findViewById(R.id.txt1);
        buttonTexts[1] = (TextView) view.findViewById(R.id.txt2);
        buttonTexts[2] = (TextView) view.findViewById(R.id.txt5);
        buttonTexts[3] = (TextView) view.findViewById(R.id.txt6);

        buttonIcons[3] = (ImageView) view.findViewById(R.id.btn6);

        updateShowButtonTexts(buttonTexts, context);

        buttons[0] = view.findViewById(R.id.group1);
        buttons[1] = view.findViewById(R.id.group2);
        buttons[2] = view.findViewById(R.id.group5);
        buttons[3] = view.findViewById(R.id.group6);
    }

    public void onBind(final IContainerData containerData, int position) {
        super.onBind(containerData, position);

        IContainerData.ILibraryContainerDataListener libraryContainerDataListener = containerData.getLibraryContainerDataListener();
        if(libraryContainerDataListener != null)
            libraryContainerDataListener.subscribeWeakShowAlbumArtValueChanged(new WeakEvent1.Handler<Boolean>() {
                @Override
                public void invoke(Boolean showAlbumArt) {
                    HeaderSongFilesViewHolder.this.onShowAlbumArtInsteadChanged(showAlbumArt);
                }
            }, listenerRefHolder);

        buttons[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                containerData.executeItemActionHeader(new ContextData(v), 0);
            }
        });

        buttons[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                containerData.executeItemActionHeader(new ContextData(v), 1);
            }
        });

        buttons[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                BaseHeaderViewHolder.onActionChooseSortFiles.invoke(new ContextData(v));
            }
        });

        buttons[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                onAction.invoke(new ContextData(v), LibraryQueueFragmentBase.ACTION_ShowAlbumArt);
            }
        });

        boolean showAlbumArtInstead = containerData.getShowAlbumArtValue();

        onShowAlbumArtInsteadChanged(showAlbumArtInstead);
    }

    public void onShowAlbumArtInsteadChanged(boolean state) {
        if (state)
            buttonIcons[3].setColorFilter(UtilsUI.getAttrColor(buttonIcons[3], R.attr.highlight_color_2));
        else
            buttonIcons[3].setColorFilter(btnColor0);
    }
}