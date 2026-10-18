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


package com.hamo.styrixviz.Design;

import com.hamo.styrixviz.Common.Events.WeakEvent4;
import com.hamo.styrixviz.comp.AlbumArt.AlbumArtCore;
import com.hamo.styrixviz.comp.AlbumArt.AlbumArtRequest;
import com.hamo.styrixviz.comp.AlbumArt.ImageLoadedListener;
import com.hamo.styrixviz.comp.playback.view.MediaPlaybackNotification;

import java.util.ArrayList;
import java.util.List;

public class WidgetAndNotificationDesign {

    static WidgetAndNotificationDesign instance = new WidgetAndNotificationDesign();
    private List<Object> listenerRefHolder = new ArrayList<>();

    public WidgetAndNotificationDesign() {

        MediaPlaybackNotification.onRequestAlbumArtLarge.subscribeWeak(new WeakEvent4.Handler<AlbumArtRequest, ImageLoadedListener, Integer, Integer>() {
            @Override
            public void invoke(AlbumArtRequest albumArtRequest, ImageLoadedListener imageLoadedListener, Integer targetW, Integer targetH) {
                AlbumArtCore albumArtCore = AlbumArtCore.createInstance();
                if (albumArtCore != null)
                    albumArtCore.loadAlbumArtLarge(
                            albumArtRequest.videoThumbDataSource,
                            albumArtRequest.path0,
                            albumArtRequest.path1,
                            albumArtRequest.genStr,
                            imageLoadedListener,
                            targetW,
                            targetH);
            }
        }, listenerRefHolder);

    }

    public static WidgetAndNotificationDesign createInstance() {
        return instance;
    }

}
