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

package com.hamo.styrixviz.comp.LibraryQueueUI.Containers.Base;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.ViewGroup;

import com.hamo.styrixviz.Common.MultiList;
import com.hamo.styrixviz.Common.Tuple2;
import com.hamo.styrixviz.comp.LibraryQueueUI.Containers.Adapter.IAdapter;
import com.hamo.styrixviz.comp.LibraryQueueUI.Containers.Adapter.ViewAdapter;
import com.hamo.styrixviz.comp.LibraryQueueUI.ViewHolders.ViewHolderFactory;

import com.hamo.styrixviz.Common.Assert;

import java.lang.ref.WeakReference;
import java.util.List;

public abstract class MultiListContainerBase<T1, T2> extends ContainerBase implements ViewAdapter.IAdapterDataProvider {

    private WeakReference<IAdapter> associatedAdapter = new WeakReference<>(null);
    private MultiList<T1, T2> list;

    protected MultiListContainerBase(Context context,
                                     MultiList<T1, T2> list,
                                     String libraryAddress,
                                     String displayName,
                                     int displayIconResId,
                                     int pageIndex) {
        super(context,
                libraryAddress,
                displayName,
                displayIconResId,
                pageIndex);

        Assert.assertNotNull(list);
        this.list = list;
    }

    protected IAdapter getAssociatedAdapter() {
        return associatedAdapter.get();
    }

    @Override
    public void onAdapterInitialized(IAdapter adapter) {
        associatedAdapter = new WeakReference<>(adapter);
    }

    @Override
    public void onAdapterDispose() {
    }

    @Override
    public abstract int getItemViewType(int position);

    @Override
    public int getItemCount() {
        return list.size();
    }

    public Tuple2<T1, T2> getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return ViewHolderFactory.newInstance(viewGroup.getContext(), viewGroup, viewType);
    }

    @Override
    public void onItemsMoved(int from, int to, List<Integer> itemOffsets) {

    }

    @Override
    public int dataPositionToPosition(int position) {
        return position;
    }

    protected MultiList<T1, T2> getList() {
        return list;
    }

    public void setDataAndNotifyDataSetChanged(MultiList<T1, T2> list, String itemsIdent) {

        if (checkItemIdent(itemsIdent)) return;

        Assert.assertNotNull(list);
        this.list = list;
        IAdapter adapter = getAssociatedAdapter();
        if (adapter != null)
            adapter.myNotifyDataSetChanged();
    }


}
