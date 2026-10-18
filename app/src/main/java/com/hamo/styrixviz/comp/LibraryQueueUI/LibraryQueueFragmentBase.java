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

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hamo.styrixviz.Common.Events.WeakEvent1;
import com.hamo.styrixviz.Common.Events.WeakEvent2;
import com.hamo.styrixviz.Common.Events.WeakEvent3;
import com.hamo.styrixviz.Common.Events.WeakEvent4;
import com.hamo.styrixviz.Common.Events.WeakEventR;
import com.hamo.styrixviz.Common.Events.WeakEventR1;
import com.hamo.styrixviz.Common.Events.WeakEventR2;
import com.hamo.styrixviz.Common.Events.WeakEventR3;
import com.hamo.styrixviz.Common.MultiList;
import com.hamo.styrixviz.Common.UtilsUI;
import com.hamo.styrixviz.comp.AlbumArt.AlbumArtRequest;
import com.hamo.styrixviz.comp.Common.IGeneralItemContainerIdentifier;
import com.hamo.styrixviz.comp.Common.IItemIdentifier;
import com.hamo.styrixviz.comp.ContextualActionBar.ActionListenerBase;
import com.hamo.styrixviz.comp.ContextualActionBar.ItemSelection;
import com.hamo.styrixviz.comp.GlobalSearch.SearchEntryOptions;
import com.hamo.styrixviz.comp.LibraryQueueUI.Containers.Adapter.ViewAdapter;
import com.hamo.styrixviz.comp.LibraryQueueUI.Containers.Base.ContainerBase;
import com.hamo.styrixviz.comp.LibraryQueueUI.Containers.Base.IContainerData;
import com.hamo.styrixviz.comp.LibraryQueueUI.ContextualActions.ItemActionsFolders;
import com.hamo.styrixviz.comp.LibraryQueueUI.ContextualActions.ItemActionsPlaylist;
import com.hamo.styrixviz.comp.LibraryQueueUI.ContextualActions.ItemActionsQueue;
import com.hamo.styrixviz.comp.LibraryQueueUI.ContextualActions.ItemActionsSongs;
import com.hamo.styrixviz.comp.LibraryQueueUI.ViewHolders.BaseHeaderViewHolder;
import com.hamo.styrixviz.comp.LibraryQueueUI.ViewHolders.BaseViewHolder;
import com.hamo.styrixviz.comp.LibraryQueueUI.ViewHolders.ContentItemViewHolder;
import com.hamo.styrixviz.comp.playback.MediaPlaybackServiceDefs;
import com.hamo.styrixviz.comp.playback.Song.PlaylistSong;
import com.hamo.styrixviz.comp.PlaybackQueue.IPlaylistSongContainerIdentifier;
import com.hamo.styrixviz.ContextData;
import com.hamo.styrixviz.Design.SortDesign;
import com.google.android.gms.ads.AdView;

import java.io.File;
import java.util.Collection;
import java.util.List;

public class LibraryQueueFragmentBase extends Fragment implements IContainerData.ILibraryContainerDataListener {

    public static final int ACTION_AddByLink = 110;
    public static final int ACTION_ClearQueue = 111;
    public static final int ACTION_SaveAs = 112;
    public static final int ACTION_Shuffle = 113;
    public static final int ACTION_FollowCurrent = 114;
    public static final int ACTION_ShowAlbumArt = 115;
    public static final int ACTION_AddFolder = 120;

    public static WeakEventR<Integer> onRequestShuffleMode = new WeakEventR<>();
    public static WeakEvent1<String> onNavigateLibraryAddress = new WeakEvent1<>();
    public static WeakEvent4<Integer /*index*/, Boolean /*enabled*/, String /*hint*/, IGeneralItemContainerIdentifier /*containerIdentifier*/> onUpdateSearchOptions = new WeakEvent4<>();

    //ItemActionsSongs
    public static WeakEventR<IPlaylistSongContainerIdentifier> onRequestSongContainerIdentifier = ItemActionsSongs.onRequestSongContainerIdentifier;
    public static WeakEventR<MultiList<PlaylistSong, IItemIdentifier>> onRequestQueueList = ItemActionsSongs.onRequestQueueList;
    public static WeakEvent1<Integer /*position*/> onQueuePositionChanged = ItemActionsSongs.onQueuePositionChanged;
    public static WeakEvent4<Context /*context*/, List<PlaylistSong> /*songs*/, Boolean /*overwritePL*/, ContextData /*contextData*/> onLibraryQueueUI_ActionSongSendToPlaylist = ItemActionsSongs.onLibraryQueueUI_ActionSongSendToPlaylist;
    public static WeakEvent2<Collection<PlaylistSong> /*list*/, Integer /*action*/> onEnqueue = ItemActionsSongs.onEnqueue;
    public static WeakEvent3<List<PlaylistSong> /*list*/, Integer /*startPlayPosition*/, IPlaylistSongContainerIdentifier /*songContainerIdentifier*/> onOpen2 = ItemActionsSongs.onOpen2;
    public static WeakEvent2<ContextData /*contextData*/, List<ItemActionsSongs.ItemsDetails> /*itemDetails*/> onActionViewDetails = ItemActionsSongs.onActionViewDetails;

    //ItemActionsQueue
    public static WeakEvent1<IItemIdentifier /*item*/> onSetCurrentQueueItem = ItemActionsQueue.onSetCurrentQueueItem;
    public static WeakEvent1<List<IItemIdentifier> /*itemIdentifiers*/> onRemoveQueueItems = ItemActionsQueue.onRemoveQueueItems;
    public static WeakEventR1<Integer /*tipId*/, Boolean> onRequestShowTipState = ItemActionsQueue.onRequestShowTipState;
    public static WeakEvent1<ContextData> onActionShowReorderTip = ItemActionsQueue.onActionShowReorderTip;

    //ItemActionsPlaylist
    public static WeakEvent4<Context /*context*/, String /*idhash*/, String /*path*/, ContextData /*contextData*/> onLibraryQueue2UI_ActionRemoveStandalonePlaylist = ItemActionsPlaylist.onLibraryQueue2UI_ActionRemoveStandalonePlaylist;
    public static WeakEvent4<Context /*context*/, Long /*playlistId*/, String /*currentName*/, ContextData /*mainDat*/> onLibraryQueueUI_ActionRenamePlaylist = ItemActionsPlaylist.onLibraryQueueUI_ActionRenamePlaylist;
    public static WeakEvent4<Context /*context*/, Long /*playlistId*/, String /*name*/, ContextData /*mainDat*/> onLibraryQueueUI_ActionDeletePlaylist = ItemActionsPlaylist.onLibraryQueueUI_ActionDeletePlaylist;

    //ItemActionsFolders
    public static WeakEvent3<Context /*context*/, String /*idhash*/, String /*path*/> onActionRemoveFolder = ItemActionsFolders.onActionRemoveFolder;

    //ContentItemViewHolder
    public static WeakEvent3<ActionListenerBase[] /*itemActions*/, Boolean /*select*/, ItemSelection.One<Object> /*itemSelection*/> onItemSelected = ContentItemViewHolder.onItemSelected;
    public static WeakEventR<Boolean> onRequestIsSelectingEnabled = ContentItemViewHolder.onRequestIsSelectingEnabled;

    //BaseHeaderViewHolder
    public static WeakEvent2<ContextData /*contextData*/, Integer /*action*/> onAction = BaseHeaderViewHolder.onAction;
    public static WeakEvent1<ContextData /*contextData*/> onLibraryQueue2UI_ActionScanStandalonePlaylist = BaseHeaderViewHolder.onLibraryQueue2UI_ActionScanStandalonePlaylist;
    public static WeakEvent3<long[] /*addSongsNativePL*/, List<String> /*addSongDataSources*/, ContextData /*contextData*/> onActionCreatePlaylist = BaseHeaderViewHolder.onActionCreatePlaylist;
    public static WeakEventR<Boolean> onUIRequestFollowCurrentValue = BaseHeaderViewHolder.onUIRequestFollowCurrentValue;
    public static WeakEvent1<ContextData> onActionChooseSortFiles = BaseHeaderViewHolder.onActionChooseSortFiles;
    public static WeakEvent1<ContextData> onActionChooseSort = BaseHeaderViewHolder.onActionChooseSort;
    public static WeakEvent3<AdView /*adView*/, Integer /*id*/, Integer /*containerItemCount*/> onShowAdView = BaseViewHolder.onShowAdView;

    //ContainerBase
    public static WeakEventR2<Integer /*pageIndex*/, IGeneralItemContainerIdentifier /*containerIdentifier*/, String> onRequestSearchQuery = ContainerBase.onRequestSearchQuery;
    public static WeakEventR2<Integer /*pageIndex*/, IGeneralItemContainerIdentifier /*containerIdentifier*/, SortDesign.SortDesc> onRequestCurrentSortDesc = ContainerBase.onRequestCurrentSortDesc;
    public static WeakEventR3<Integer /*pageIndex*/, IGeneralItemContainerIdentifier /*containerIdentifier*/, File /*file,*/, Boolean> onRequestFilterFileResult = ContainerBase.onRequestFilterFileResult;
    public static WeakEventR1<Class<?> /*cls*/, Boolean> onRequestSectionOpenedState = ContainerBase.onRequestSectionOpenedState;
    public static WeakEvent2<Boolean /*state*/, Class<?> /*cls*/> onSetSectionOpened = ContainerBase.onSetSectionOpened;
    public static WeakEvent3<Integer /*from*/, Integer /*to*/, List<Integer> /*itemOffsets*/> onMoveQueueItems = ContainerBase.onMoveQueueItems;
    public static WeakEvent2<String /*url*/, ImageView /*imageView*/> onRequestAlbumArtSimple = ContainerBase.onRequestAlbumArtSimple;
    public static WeakEvent4<AlbumArtRequest /*artRequest*/, ImageView /*imageView*/, Boolean /*fitCenterInside*/, Boolean /*preferLarge*/> onRequestAlbumArt = ContainerBase.onRequestAlbumArt;
    public static WeakEventR<Boolean> onRequestShowAlbumArtValue = ContainerBase.onRequestShowAlbumArtValue;
    public static WeakEventR1<ItemSelection.One /*itemSelection*/, Boolean> onRequestContainsItemSelection = ContainerBase.onRequestContainsItemSelection;
    public static WeakEventR2<AsyncTask /*task*/, Integer /*pageIndex*/, Boolean> onCompareSearchTask = ContainerBase.onCompareSearchTask;
    public static WeakEvent3<AsyncTask /*task*/, Integer /*pageIndex*/, Object /*taskParam*/> onStartingSearchTask = ContainerBase.onStartSearchTask;
    public static WeakEvent1<Integer /*pageIndex*/> onContainerDataSetChanged = ContainerBase.onContainerDataSetChanged;

    private static WeakEvent1<Integer /*shuffleMode*/> internalOnShuffleModeChanged = new WeakEvent1<>();
    private static WeakEvent1<Boolean /*followCurrent*/> internalOnFollowCurrentValueChanged = new WeakEvent1<>();
    private static WeakEvent1<Boolean /*showAlbumArt*/> internalOnShowAlbumArtValueChanged = new WeakEvent1<>();

    public LibraryQueueFragmentBase() {
    }

    public static void onShuffleModeChanged(int shuffleMode) {
        internalOnShuffleModeChanged.invoke(shuffleMode);
    }

    public static void onFollowCurrentValueChanged(boolean followCurrent) {
        internalOnFollowCurrentValueChanged.invoke(followCurrent);
    }

    public static void onShowAlbumArtValueChanged(boolean showAlbumArt) {
        internalOnShowAlbumArtValueChanged.invoke(showAlbumArt);
    }

    @Override
    public void subscribeWeakShuffleModeChanged(WeakEvent1.Handler<Integer> listener, List<Object> listenerRefHolder) {
        internalOnShuffleModeChanged.subscribeWeak(listener, listenerRefHolder);
    }

    @Override
    public void subscribeWeakFollowCurrentValueChanged(WeakEvent1.Handler<Boolean> listener, List<Object> listenerRefHolder) {
        internalOnFollowCurrentValueChanged.subscribeWeak(listener, listenerRefHolder);
    }

    @Override
    public void subscribeWeakShowAlbumArtValueChanged(WeakEvent1.Handler<Boolean> listener, List<Object> listenerRefHolder) {
        internalOnShowAlbumArtValueChanged.subscribeWeak(listener, listenerRefHolder);
    }

    public static SearchEntryOptions getSearchEntryOptions(Context contex, ViewAdapter adapter) {
        if (adapter == null)
            return SearchEntryOptions.refuse;

        String[] outSearchHint = new String[1];
        IGeneralItemContainerIdentifier[] outContainerIdentifier = new IGeneralItemContainerIdentifier[1];
        adapter.getContainerData().getSearchOptions(contex, outSearchHint, outContainerIdentifier);

        if (outSearchHint[0] == null || outSearchHint[0].isEmpty()) {

            SearchEntryOptions result = new SearchEntryOptions();
            result.enabled = false;
            result.hint = "";
            result.containerIdentifier = null;

            return result;

        } else {
            IGeneralItemContainerIdentifier containerIdentifier = outContainerIdentifier[0];
            String searchHint = outSearchHint[0];

            SearchEntryOptions result = new SearchEntryOptions();
            result.enabled = true;
            result.hint = searchHint;
            result.containerIdentifier = containerIdentifier;

            return result;
        }
    }

    @Override
    public void onNavigateForward(String currentAddress, String relativeAddress) {
        onNavigateLibraryAddress.invoke(relativeAddress);
    }

    @Override
    public String makeChildAddress(String currentAddress, String childItemAddress) {
        if (childItemAddress.length() <= 0)
            return currentAddress;

        if (currentAddress.length() <= 0)
            return currentAddress + childItemAddress;

        String addr;

        int len0 = currentAddress.length();
        if (currentAddress.charAt(len0 - 1) == '/') {
            if (childItemAddress.charAt(0) != '/')
                addr = currentAddress + childItemAddress;
            else
                addr = currentAddress + childItemAddress.substring(1);
        } else {
            if (childItemAddress.charAt(0) != '/')
                addr = currentAddress + "/" + childItemAddress;
            else
                addr = currentAddress + childItemAddress;
        }

        int len = addr.length();
        if (len > 0 && addr.charAt(len - 1) == '/')
            return addr;
        else
            return addr + "/";
    }

    @Override
    public int onRequestShuffleMode() {
        return onRequestShuffleMode.invoke(MediaPlaybackServiceDefs.SHUFFLE_NONE);
    }

    protected static void setStatusBarDimensions(View view) {
        if (view == null) return;

        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = UtilsUI.getStatusBarHeight(view.getContext());
    }
}
