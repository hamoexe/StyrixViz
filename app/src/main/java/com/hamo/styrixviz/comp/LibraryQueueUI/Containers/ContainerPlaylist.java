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

package com.hamo.styrixviz.comp.LibraryQueueUI.Containers;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.hamo.styrixviz.Common.MediaStoreUtils;
import com.hamo.styrixviz.Common.MultiList;
import com.hamo.styrixviz.Common.Tuple2;
import com.hamo.styrixviz.Common.Utils;
import com.hamo.styrixviz.Common.UtilsMusic;
import com.hamo.styrixviz.comp.Common.IGeneralItemContainerIdentifier;
import com.hamo.styrixviz.comp.ContextualActionBar.ActionListenerBase;
import com.hamo.styrixviz.comp.LibraryQueueUI.Containers.Adapter.HeaderFooterAdapterData;
import com.hamo.styrixviz.comp.LibraryQueueUI.Containers.Adapter.ViewAdapter;
import com.hamo.styrixviz.comp.LibraryQueueUI.Containers.Base.CursorContainerBase;
import com.hamo.styrixviz.comp.LibraryQueueUI.ContextualActions.ItemActionsPlaylist;
import com.hamo.styrixviz.comp.LibraryQueueUI.ContextualActions.ItemActionsSongs;
import com.hamo.styrixviz.comp.LibraryQueueUI.ViewHolders.ContentItemViewHolder;
import com.hamo.styrixviz.comp.LibraryQueueUI.ViewHolders.ViewHolderFactory;
import com.hamo.styrixviz.comp.playback.Song.PlaylistSong;
import com.hamo.styrixviz.Design.SortDesign;
import com.hamo.styrixviz.MainActivity;
import com.hamo.styrixviz.R;

import java.util.ArrayList;
import java.util.List;

public class ContainerPlaylist extends CursorContainerBase {

    private static final int dispIconResId = R.drawable.ic_playlist4;
    private static final int primaryActionIndex = -1;
    private static final int defaultActionIndex = 0;

    ActionListenerBase[] itemListenerActions = new ActionListenerBase[]
            {

                    new ItemActionsSongs.PlaySingleItemAction.PlaySingleActionListener2() {
                        @Override
                        protected void onPlaySingle(Context context, Object objItem, List<PlaylistSong> songsOut) {
                            ThisItemIdentifier item = (ThisItemIdentifier) objItem;
                            getChildItems(context, "" + item.id, songsOut);
                        }
                    },

                    new ItemActionsSongs.PlayMultiItemAction.PlayMultiActionListener2() {
                        @Override
                        protected void onPlayMulti(Context context, Object objItem, List<PlaylistSong> songsOut) {
                            ThisItemIdentifier item = (ThisItemIdentifier) objItem;
                            getChildItems(context, "" + item.id, songsOut);
                        }
                    },

                    new ItemActionsSongs.ItemActionEnqueue.EnqueueActionListener2() {
                        @Override
                        protected void onEnqueue(Context context, Object objItem, List<PlaylistSong> songsOut) {
                            ThisItemIdentifier item = (ThisItemIdentifier) objItem;
                            getChildItems(context, "" + item.id, songsOut);
                        }
                    },

                    new ItemActionsSongs.ItemActionEnqueueNext.EnqueueNextActionListener2() {
                        @Override
                        protected void onEnqueue(Context context, Object objItem, List<PlaylistSong> songsOut) {
                            ThisItemIdentifier item = (ThisItemIdentifier) objItem;
                            getChildItems(context, "" + item.id, songsOut);
                        }
                    },

                    new ItemActionsSongs.SendToItemAction.SendToActionListener() {
                        @Override
                        protected void onSendTo(Context context, Object objItem, List<PlaylistSong> songsOut) {
                            ThisItemIdentifier item = (ThisItemIdentifier) objItem;
                            getChildItems(context, "" + item.id, songsOut);
                        }
                    },

                    new ItemActionsPlaylist.RenamePlaylistAction.RenamePlaylistActionListener() {
                        @Override
                        protected void onRenamePlaylist(Context context, Object objItem, MultiList<Long, String> playlistOut) {
                            ThisItemIdentifier item = (ThisItemIdentifier) objItem;
                            playlistOut.add(item.id, item.name);
                        }
                    },

                    new ItemActionsPlaylist.DeletePlaylistAction.DeletePlaylistActionListener() {
                        @Override
                        protected void onDeletePlaylist(Context context, Object objItem, MultiList<Long, String> playlistOut) {
                            ThisItemIdentifier item = (ThisItemIdentifier) objItem;
                            playlistOut.add(item.id, item.name);
                        }
                    },
            };

    public ContainerPlaylist(Context context, String libraryAddress, int pageIndex) {
        super(context, libraryAddress, context.getResources().getString(R.string.section_playlist_system), dispIconResId, pageIndex);
        init(context);
    }

    static Tuple2<Cursor, String> makeCursor(Context context, final IGeneralItemContainerIdentifier containerIdentifier, final int pageIndex) {
        ContentResolver cr = context.getContentResolver();

        String where = null;
        String[] whereVal = null;

        String searchText = onRequestSearchQuery.invoke(pageIndex, containerIdentifier, "");

        if (searchText != null && !searchText.isEmpty()) {
            where = MediaStore.Audio.Genres.NAME + " LIKE ?";
            String whereValue[] = {
                    "%" + searchText + "%"
            };
            whereVal = whereValue;
        } else {
            searchText = "";//original items
        }

        final Uri uri = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;
        final String[] columns = {
                MediaStore.Audio.Playlists._ID,
                MediaStore.Audio.Playlists.NAME,
                MediaStore.Audio.Playlists.DATA};

        final Cursor cursor = MediaStoreUtils.querySafeEmpty(cr, uri, columns, where, whereVal, columns[1]);

        return new Tuple2<>(cursor, searchText);
    }

    @Override
    public ViewAdapter createAdapter(Context context, int type) {
        ViewAdapter.IAdapterDataProvider adapterDataProvider = new HeaderFooterAdapterData(this, this, ViewHolderFactory.VIEW_HOLDER_playLists, ViewHolderFactory.VIEW_HOLDER_footer1);
        return new ViewAdapter(adapterDataProvider, this);
    }

    List<PlaylistSong> getChildItems(Context context, String playlistId, List<PlaylistSong> dest) {
        ContentResolver cr = context.getContentResolver();

        //have to query both columns or else we get ..some unnecessary rows
        String[] columns = {
                MediaStore.Audio.Playlists.Members._ID,
                MediaStore.Audio.Playlists.Members.AUDIO_ID,
                MediaStore.Audio.Media.DATA};
        String where = MediaStore.Audio.Playlists.Members.PLAYLIST_ID + "=?";
        String whereVal[] = {playlistId};
        SortDesign.SortDesc sortDesc = onRequestCurrentSortDesc.invoke(pageIndex, getSelectionContainerIdentifier(), null);
        String orderBy = MediaStoreUtils.getOrderBy(sortDesc);

        Cursor cursor = MediaStoreUtils.querySafe(cr,
                MediaStore.Audio.Playlists.Members.getContentUri("external", Utils.strToLongSafe(playlistId)), columns, where,
                whereVal, orderBy);

        if (cursor != null) {
            dest = UtilsMusic.songListFromCursor(cursor, dest);
            cursor.close();
        } else {
            dest = new ArrayList<>();
        }

        return dest;
    }

    @Override
    public String getItemPositionToItemAddress(int position) {
        final Cursor item = this.getItem(position);
        return item.getString(0);
    }

    @Override
    public ViewAdapter createChildAdapter(Context context, String playlistId)//, ViewAdapter _adapter
    {
        ContentResolver cr = context.getContentResolver();

        String dispName = "";
        {
            Cursor cursor2;

            final Uri uri = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;
            final String[] columns2 = {
                    MediaStore.Audio.Playlists.NAME};

            String where = MediaStore.Audio.Playlists._ID + "=?";
            String whereVal[] = {playlistId};

            cursor2 = MediaStoreUtils.querySafe(cr, uri, columns2, where, whereVal, null);
            if (cursor2 != null) {
                cursor2.moveToFirst();
                dispName = MediaStoreUtils.CursorGetStringSafe(cursor2, 0);
                cursor2.close();
            }
        }

        ContainerSongs containerData = new ContainerSongs(context,
                getChildItems(context, playlistId, null),
                makeChildAddress(playlistId),
                dispName,
                0,
                pageIndex,
                false);

        containerData.setLibraryContainerDataListener(libraryContainerDataListenerWeak);
        return containerData.createOrGetAdapter(context, MainActivity.LIBRARY_PAGE_INDEX);
    }

    public void getView(final Cursor item, int position, final ContentItemViewHolder holder) {
        holder.setToDefault(this, new ThisItemIdentifier(item.getLong(0), item.getString(1)), this.getSelectionContainerIdentifier());
        boolean selected = onRequestContainsItemSelection.invoke(holder.itemSelection, false);
        holder.viewItemBg.setSelected(selected);
        holder.setItemActions2(itemListenerActions, primaryActionIndex, defaultActionIndex, this);
        holder.imgArt.setVisibility(View.VISIBLE);
        holder.imgArt.setColorFilter(this.colorM1);
        holder.setImgResource(R.drawable.ic_playlist4);
        holder.txtNum.setVisibility(View.GONE);
        holder.txtItemLine1.setText(item.getString(1));
        holder.txtItemLine1.setTextColor(this.color);
        holder.txtItemLine2.setVisibility(View.GONE);
        holder.txtItemLine2.setText("");
        holder.txtItemDuration.setText("");
    }

    public void getSearchOptions(Context context, String[] outSearchHint, IGeneralItemContainerIdentifier[] outContainerIdentifier) {
        outSearchHint[0] = context.getResources().getString(R.string.libContainer_Playlists_search);
        outContainerIdentifier[0] = getSelectionContainerIdentifier();
    }

    @Override
    public Tuple2<Cursor, String> createOrGetCursor(Context context, String query) {
        return makeCursor(context, this.getSelectionContainerIdentifier(), pageIndex);
    }

    @Override
    public Tuple2<Cursor, String> createOrGetCursor(Context context) {
        return makeCursor(context, this.getSelectionContainerIdentifier(), pageIndex);
    }

    @Override
    public int getItemViewType(int position)
    {
        return ViewHolderFactory.VIEW_HOLDER_libContent;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        Cursor _item = this.getItem(position);
        ContentItemViewHolder holder = (ContentItemViewHolder) viewHolder;
        holder.itemPosition = position;
        getView(_item, position, holder);
    }

    @Override
    public boolean getSectionOpened() {
        return onRequestSectionOpenedState.invoke(ContainerPlaylist.class, false);
    }

    @Override
    public void setSectionOpenedState(final boolean state) {
        onSetSectionOpened.invoke(state, ContainerPlaylist.class);
    }

    static class ThisItemIdentifier {
        final long id;
        final String name;

        public ThisItemIdentifier(long id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public int hashCode() {
            return (int) id;
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof ThisItemIdentifier && id == ((ThisItemIdentifier) o).id;
        }
    }
}
