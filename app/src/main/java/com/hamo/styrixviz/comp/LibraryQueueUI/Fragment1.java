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

package com.hamo.styrixviz.comp.LibraryQueueUI;

import android.content.Context;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hamo.styrixviz.Common.MultiList;
import com.hamo.styrixviz.Common.UtilsUI;
import com.hamo.styrixviz.comp.Common.IGeneralItemContainerIdentifier;
import com.hamo.styrixviz.comp.Common.IItemIdentifier;
import com.hamo.styrixviz.comp.GlobalSearch.SearchEntryOptions;
import com.hamo.styrixviz.comp.LibraryQueueUI.Containers.Adapter.ViewAdapter;
import com.hamo.styrixviz.comp.LibraryQueueUI.Containers.Base.ContainerBase;
import com.hamo.styrixviz.comp.LibraryQueueUI.Containers.Base.IContainerData;
import com.hamo.styrixviz.comp.LibraryQueueUI.Containers.ContainerSongs;
import com.hamo.styrixviz.comp.playback.Song.PlaylistSong;
import com.hamo.styrixviz.Design.PlaybackControlsDesign;
import com.hamo.styrixviz.MainActivity;
import com.hamo.styrixviz.R;
import com.emtronics.dragsortrecycler.DragSortRecycler;


import com.hamo.styrixviz.Common.Assert;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class Fragment1 extends LibraryQueueFragmentBase {

    private ContainerSongs dataAdapter;
    private RecyclerView recyclerViewItems;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;

    public Fragment1() {
    }

    public static Fragment1 newInstance() {
        Fragment1 fragment = new Fragment1();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroy() {
        onServiceDisconnected(false);
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_1, container, false);
        setStatusBarDimensions(rootView.findViewById(R.id.viewStatusBarBg));
        recyclerViewItems = (RecyclerView) rootView.findViewById(R.id.recyclerViewItems);
        {
            xyz.danoz.recyclerviewfastscroller.vertical.VerticalRecyclerViewFastScroller fastScroller =
                    (xyz.danoz.recyclerviewfastscroller.vertical.VerticalRecyclerViewFastScroller) rootView.findViewById(R.id.fast_scroller);
            // Connect the recycler to the scroller (to let the scroller scroll the list)
            fastScroller.setRecyclerView(recyclerViewItems);
            // Connect the scroller to the recycler (to let the recycler scroll the scroller's handle)
            recyclerViewItems.addOnScrollListener(fastScroller.getOnScrollListener());
        }

        recyclerViewLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewItems.setLayoutManager(recyclerViewLayoutManager);

        dataAdapter = new ContainerSongs(
                getActivity().getApplicationContext(),
                onRequestQueueList.invoke(new MultiList<PlaylistSong, IItemIdentifier>()),
                "nowplaying",
                "Playlist",
                true,
                MainActivity.QUEUE_PAGE_INDEX);

        dataAdapter.setLibraryContainerDataListener(new WeakReference<IContainerData.ILibraryContainerDataListener>(this));
        recyclerViewItems.setAdapter(dataAdapter.createOrGetAdapter(getActivity().getApplicationContext(), MainActivity.QUEUE_PAGE_INDEX));
        recyclerViewItems.addItemDecoration(new SpacesItemDecoration(1, UtilsUI.getAttrColor(recyclerViewItems, R.attr.containerBackgroundDark)));

        final DragSortRecycler dragSortRecycler = new DragSortRecycler();
        dragSortRecycler.setViewHandleId(R.id.btnItemMore);
        dragSortRecycler.setFloatingAlpha(0.4f);
        dragSortRecycler.setFloatingBgColor(UtilsUI.getAttrColor(this.getActivity().getTheme(), R.attr.highlight_color_1));
        dragSortRecycler.setAutoScrollSpeed(0.3f);
        dragSortRecycler.setAutoScrollWindow(0.1f);

        dragSortRecycler.setOnItemMovedListener(new DragSortRecycler.OnItemMovedListener() {
            @Override
            public void onItemMoved(final int from, final int to) {
                List<Integer> itemsOffsets = new ArrayList<>();
                itemsOffsets.add(0);
                ViewAdapter adapter = (ViewAdapter) recyclerViewItems.getAdapter();
                adapter.onItemsMoved(from, to, itemsOffsets);
            }
        });

        recyclerViewItems.addItemDecoration(dragSortRecycler);
        recyclerViewItems.addOnItemTouchListener(dragSortRecycler);
        recyclerViewItems.addOnScrollListener(dragSortRecycler.getScrollListener());

        dataAdapter.setOnDraggingListener(new ContainerBase.IOnDraggingListener() {
            @Override
            public void onStartDragging(View itemView) {
                dragSortRecycler.StartDragging(itemView);
            }
        });

        updateTrackList(onRequestQueueList.invoke(new MultiList<PlaylistSong, IItemIdentifier>()));
        updateTrackInfo(PlaybackControlsDesign.fieldQueuePosition,
                PlaybackControlsDesign.currentTrack,
                PlaybackControlsDesign.fieldsongData);
        updateSearchInfo();

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void onServiceDisconnected(boolean actualServiceDisconnect) {
    }

    public void updateQueueItems() {
        ViewAdapter adapter = (ViewAdapter) recyclerViewItems.getAdapter();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    public void updateTrackInfo(int queuePosition, PlaylistSong currentTrack, PlaylistSong.Data songData) {
        if (this.getActivity() == null) return;

        if (recyclerViewItems == null) return;
        ViewAdapter adapter = (ViewAdapter) recyclerViewItems.getAdapter();

        if (adapter != null) {
            adapter.notifyDataSetChanged();
            boolean followCurrentState = onUIRequestFollowCurrentValue.invoke(false);

            if (followCurrentState) {
                LinearLayoutManager layoutManager = ((LinearLayoutManager) recyclerViewItems.getLayoutManager());

                int overlapedItemsCnt = this.getResources().getInteger(R.integer.player_controls_height_in_items);

                int lastVisiblePosition = Math.max(layoutManager.findLastVisibleItemPosition() - (overlapedItemsCnt + 1), 0);

                if (queuePosition >= lastVisiblePosition)
                    recyclerViewItems.scrollToPosition(adapter.dataPositionToPosition(queuePosition + overlapedItemsCnt));
                else
                    recyclerViewItems.scrollToPosition(adapter.dataPositionToPosition(queuePosition));
            }
        }
    }


    public void updateTrackList(MultiList<PlaylistSong, IItemIdentifier> list) {
        if (dataAdapter != null)
            dataAdapter.setDataAndNotifyDataSetChanged(list.unmodifiableList(), null);
    }

    public void refreshTrackList(IGeneralItemContainerIdentifier containerIdentifier) {
        ViewAdapter adapter = (ViewAdapter) recyclerViewItems.getAdapter();
        if (adapter != null && adapter.getContainerData().containsContainerIdentifier(containerIdentifier))
            adapter.notifyDataSetChanged();
    }

    public void updateSearchQuery(Context context, String query) {
        if (recyclerViewItems != null)
            dataAdapter.updateSearchQuery(context, query);
    }

    public void updateSearchInfo() {
        Context context = this.getActivity();
        Assert.assertNotNull(context);
        SearchEntryOptions searchOptions = getSearchEntryOptions();
        onUpdateSearchOptions.invoke(MainActivity.QUEUE_PAGE_INDEX, searchOptions.enabled, searchOptions.hint, searchOptions.containerIdentifier);
    }

    public SearchEntryOptions getSearchEntryOptions() {
        Context context = this.getActivity();
        if (context == null) return SearchEntryOptions.refuse;

        if (recyclerViewItems == null)
            return SearchEntryOptions.refuse;

        ViewAdapter adapter = (ViewAdapter) recyclerViewItems.getAdapter();
        if (adapter == null)
            return SearchEntryOptions.refuse;

        return getSearchEntryOptions(context, adapter);
    }

}
