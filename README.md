<p align="center">
  <img width="100" height="100" src="app/src/main/res/drawable-xxxhdpi/ic_launcher.png">
</p>

# Styrix Viz

Android music player with an OpenGL audio visualizer.

Styrix Viz is a fork of the open source **Avee Open Player (Avee 1.0.34)** source release, rebranded and maintained independently. It is not affiliated with or endorsed by Avee Player.

- Package / applicationId: `com.hamo.styrixviz`
- Audio playback: MediaPlayer API and ExoPlayer
- Visualizer: rendered with OpenGL, spectrum computed with FFT plus extra processing

## Features

* Most popular formats and network streams supported
* Shuffle and repeat modes
* Direct folder browsing with folder shortcuts
* Searchable library, queue, files
* Screen orientation lock
* Playlist and queue editing, item sorting
* Two playback engines (Native / ExoPlayer)
* Lockscreen and status bar widget
* Media and bluetooth controls
* Sleep timer
* Video fit, crop and stretch modes
* Audio visualizer: spectrum and waveform variants
* Add media by external links (including adaptive streaming)

## Building

Open the project in Android Studio or Code On the Go and run the `app` module.

- Toolchain: Gradle 9.6.1, Android Gradle Plugin 9.3.1 (the versions Code On the Go ships), compileSdk 36, minSdk 21, targetSdk 28
- Dependencies are AndroidX only; ExoPlayer and the fast scroller are prebuilt `.aar` files in `app/libs`
- If Gradle cannot resolve the AndroidX artifacts, turn off the `--offline` flag once (Code On the Go: Preferences > Build & Run > Additional Gradle flags) so they can be downloaded
- Ads were removed in this fork

## License

Apache License 2.0. See [LICENSE](LICENSE) and [NOTICE](NOTICE) for the original copyright, third-party attributions and a summary of changes.
