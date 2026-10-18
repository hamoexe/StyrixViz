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
import android.view.ViewGroup;
import android.widget.SearchView;

import com.hamo.styrixviz.comp.LibraryQueueUI.Containers.Base.IContainerData;
import com.hamo.styrixviz.Common.UtilsUI;
import com.hamo.styrixviz.R;

public class HeaderArtistsViewHolder
        extends BaseHeaderViewHolder implements SearchView.OnQueryTextListener {

    public HeaderArtistsViewHolder(Context context, ViewGroup parent) {
        super(UtilsUI.getInflaterFromContext(context).inflate(R.layout.header_artists_item, parent, false));
    }

    public void onBind(final IContainerData containerData, int position) {
        super.onBind(containerData, position);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}