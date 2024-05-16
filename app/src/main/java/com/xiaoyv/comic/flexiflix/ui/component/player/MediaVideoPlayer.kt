@file:OptIn(UnstableApi::class)

package com.xiaoyv.comic.flexiflix.ui.component.player

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.annotation.OptIn
import androidx.appcompat.widget.AppCompatImageView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.media3.common.AudioAttributes
import androidx.media3.common.DeviceInfo
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.PlaybackException
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import androidx.media3.common.Timeline
import androidx.media3.common.TrackSelectionParameters
import androidx.media3.common.Tracks
import androidx.media3.common.VideoSize
import androidx.media3.common.text.CueGroup
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.datasource.okhttp.OkHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.ui.PlayerView
import androidx.media3.ui.PlayerView.ControllerVisibilityListener
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.xiaoyv.comic.flexiflix.R
import com.xiaoyv.comic.flexiflix.application
import com.xiaoyv.flexiflix.common.utils.debugLog
import com.xiaoyv.flexiflix.extension.MediaSourceFactory
import com.xiaoyv.flexiflix.extension.model.FlexMediaPlaylistUrl

internal val sampleCache by lazy {
    SimpleCache(
        application.cacheDir,
        LeastRecentlyUsedCacheEvictor(500 * 1024 * 1024L),
        StandaloneDatabaseProvider(application)
    )
}


/**
 * [MediaVideoPlayer]
 *
 * @author why
 * @since 5/9/24
 */
@Composable
fun MediaVideoPlayer(
    modifier: Modifier = Modifier,
    poster: Any,
    playlistUrl: FlexMediaPlaylistUrl?,
    onFullscreenButtonClick: (Context, Boolean) -> Unit = { _, _ -> },
    onControllerVisibilityChanged: (Int) -> Unit = {},
    onEvents: (Player, Player.Events) -> Unit = { _, _ -> },
    onMediaItemTransition: (MediaItem?, reason: Int) -> Unit = { _, _ -> },
    onTracksChanged: (Tracks) -> Unit = {},
    onMediaMetadataChanged: (MediaMetadata) -> Unit = {},
    onPlaylistMetadataChanged: (MediaMetadata) -> Unit = {},
    onIsLoadingChanged: (Boolean) -> Unit = {},
    onAvailableCommandsChanged: (Player.Commands) -> Unit = {},
    onTrackSelectionParametersChanged: (TrackSelectionParameters) -> Unit = {},
    onPlaybackStateChanged: (Player, Int) -> Unit = { _, _ -> },
    onPlayWhenReadyChanged: (Boolean, Int) -> Unit = { _, _ -> },
    onPlaybackSuppressionReasonChanged: (Int) -> Unit = {},
    onIsPlayingChanged: (Boolean) -> Unit = {},
    onRepeatModeChanged: (Int) -> Unit = {},
    onShuffleModeEnabledChanged: (Boolean) -> Unit = {},
    onPlayerError: (PlaybackException) -> Unit = {},
    onPlayerErrorChanged: (PlaybackException?) -> Unit = {},
    onPositionDiscontinuity: (Player.PositionInfo, Player.PositionInfo, Int) -> Unit = { _, _, _ -> },
    onPlaybackParametersChanged: (PlaybackParameters) -> Unit = {},
    onSeekBackIncrementChanged: (Long) -> Unit = {},
    onSeekForwardIncrementChanged: (Long) -> Unit = {},
    onMaxSeekToPreviousPositionChanged: (Long) -> Unit = {},
    onAudioAttributesChanged: (AudioAttributes) -> Unit = {},
    onVolumeChanged: (Float) -> Unit = {},
    onSkipSilenceEnabledChanged: (Boolean) -> Unit = {},
    onDeviceInfoChanged: (DeviceInfo) -> Unit = {},
    onDeviceVolumeChanged: (Int, Boolean) -> Unit = { _, _ -> },
    onVideoSizeChanged: (VideoSize) -> Unit = {},
    onSurfaceSizeChanged: (Int, Int) -> Unit = { _, _ -> },
    onRenderedFirstFrame: () -> Unit = {},
    onCues: (CueGroup) -> Unit = {},
    onTimelineChanged: (Timeline, reason: Int) -> Unit = { _, _ -> }
) {
    val context = LocalContext.current

    debugLog { "MediaPlayer Compose!" }

    val exoPlayer = remember {
        val httpDataSourceFactory = OkHttpDataSource.Factory(MediaSourceFactory.okhttp)
            .setUserAgent("Jetpack-Media3")

        val defaultDataSourceFactory = DefaultDataSource.Factory(context, httpDataSourceFactory)

        val factory = CacheDataSource.Factory()
            .setUpstreamDataSourceFactory(defaultDataSourceFactory)
            .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
            .setCache(sampleCache)

        val mediaSourceFactory: MediaSource.Factory =
            DefaultMediaSourceFactory(context)
                .setDataSourceFactory(factory)

        val trackSelector = DefaultTrackSelector(context)
            .apply {
                setParameters(
                    buildUponParameters()
                        .setAllowVideoMixedMimeTypeAdaptiveness(true)
                )
            }

        ExoPlayer.Builder(context)
            .setTrackSelector(trackSelector)
            .setMediaSourceFactory(mediaSourceFactory)
            .build().apply {
                this.playWhenReady = false
                this.repeatMode = Player.REPEAT_MODE_ALL
                this.prepare()
                this.addListener(object : Player.Listener {
                    override fun onEvents(player: Player, events: Player.Events) {
                        onEvents(player, events)
                    }

                    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                        onMediaItemTransition(mediaItem, reason)
                    }

                    override fun onTracksChanged(tracks: Tracks) {
                        onTracksChanged(tracks)
                    }

                    override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
                        onMediaMetadataChanged(mediaMetadata)
                    }

                    override fun onPlaylistMetadataChanged(mediaMetadata: MediaMetadata) {
                        onPlaylistMetadataChanged(mediaMetadata)
                    }

                    override fun onIsLoadingChanged(isLoading: Boolean) {
                        onIsLoadingChanged(isLoading)
                    }

                    override fun onAvailableCommandsChanged(availableCommands: Player.Commands) {
                        onAvailableCommandsChanged(availableCommands)
                    }

                    override fun onTrackSelectionParametersChanged(parameters: TrackSelectionParameters) {
                        onTrackSelectionParametersChanged(parameters)
                    }

                    override fun onPlaybackStateChanged(playbackState: Int) {
                        onPlaybackStateChanged(this@apply, playbackState)
                    }

                    override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
                        onPlayWhenReadyChanged(playWhenReady, reason)
                    }

                    override fun onPlaybackSuppressionReasonChanged(playbackSuppressionReason: Int) {
                        onPlaybackSuppressionReasonChanged(playbackSuppressionReason)
                    }

                    override fun onIsPlayingChanged(isPlaying: Boolean) {
                        onIsPlayingChanged(isPlaying)
                    }

                    override fun onRepeatModeChanged(repeatMode: Int) {
                        onRepeatModeChanged(repeatMode)
                    }

                    override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
                        onShuffleModeEnabledChanged(shuffleModeEnabled)
                    }

                    override fun onPlayerError(error: PlaybackException) {
                        onPlayerError(error)
                    }

                    override fun onPlayerErrorChanged(error: PlaybackException?) {
                        onPlayerErrorChanged(error)
                    }

                    override fun onPositionDiscontinuity(
                        oldPosition: Player.PositionInfo,
                        newPosition: Player.PositionInfo,
                        reason: Int
                    ) {
                        onPositionDiscontinuity(oldPosition, newPosition, reason)
                    }

                    override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {
                        onPlaybackParametersChanged(playbackParameters)
                    }

                    override fun onSeekBackIncrementChanged(seekBackIncrementMs: Long) {
                        onSeekBackIncrementChanged(seekBackIncrementMs)
                    }

                    override fun onSeekForwardIncrementChanged(seekForwardIncrementMs: Long) {
                        onSeekForwardIncrementChanged(seekForwardIncrementMs)
                    }

                    override fun onMaxSeekToPreviousPositionChanged(maxSeekToPreviousPositionMs: Long) {
                        onMaxSeekToPreviousPositionChanged(maxSeekToPreviousPositionMs)
                    }

                    override fun onAudioAttributesChanged(audioAttributes: AudioAttributes) {
                        onAudioAttributesChanged(audioAttributes)
                    }

                    override fun onVolumeChanged(volume: Float) {
                        onVolumeChanged(volume)
                    }

                    override fun onSkipSilenceEnabledChanged(skipSilenceEnabled: Boolean) {
                        onSkipSilenceEnabledChanged(skipSilenceEnabled)
                    }

                    override fun onDeviceInfoChanged(deviceInfo: DeviceInfo) {
                        onDeviceInfoChanged(deviceInfo)
                    }

                    override fun onDeviceVolumeChanged(volume: Int, muted: Boolean) {
                        onDeviceVolumeChanged(volume, muted)
                    }

                    override fun onVideoSizeChanged(videoSize: VideoSize) {
                        onVideoSizeChanged(videoSize)
                    }

                    override fun onSurfaceSizeChanged(width: Int, height: Int) {
                        onSurfaceSizeChanged(width, height)
                    }

                    override fun onRenderedFirstFrame() {
                        onRenderedFirstFrame()
                    }

                    override fun onCues(cueGroup: CueGroup) {
                        onCues(cueGroup)
                    }

                    override fun onTimelineChanged(timeline: Timeline, reason: Int) {
                        onTimelineChanged(timeline, reason)
                    }
                })
            }
    }

    LaunchedEffect(playlistUrl) {
        if (playlistUrl != null && playlistUrl.mediaUrl.orEmpty().isNotBlank()) {
            exoPlayer.setMediaItem(
                MediaItem.Builder()
                    .setUri(playlistUrl.mediaUrl)
                    .setMediaId(playlistUrl.title)
                    .setTag(playlistUrl)
                    .setMediaMetadata(
                        MediaMetadata.Builder()
                            .setDisplayTitle(playlistUrl.title + "[${playlistUrl.size}]")
                            .build()
                    )
                    .build()
            )
            exoPlayer.prepare()
            exoPlayer.playWhenReady = false

            debugLog { "SetMediaItem: ${playlistUrl.mediaUrl}" }
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current

    Box(modifier = modifier) {
        AndroidView(
            modifier = Modifier
                .testTag("VideoPlayer")
                .fillMaxSize(),
            factory = {
                createPlayerView(
                    context = it,
                    lifecycleOwner = lifecycleOwner,
                    exoPlayer = exoPlayer,
                    poster = poster,
                    onFullscreenButtonClick = onFullscreenButtonClick,
                    onControllerVisibilityChanged = onControllerVisibilityChanged
                )
            },
            onRelease = {
                exoPlayer.release()

                debugLog { "Release Media Player" }
            }
        )
    }

}

/**
 * 插件播放器
 */
@SuppressLint("InflateParams")
fun createPlayerView(
    context: Context,
    lifecycleOwner: LifecycleOwner,
    exoPlayer: ExoPlayer,
    poster: Any,
    onFullscreenButtonClick: (Context, Boolean) -> Unit = { _, _ -> },
    onControllerVisibilityChanged: (Int) -> Unit = {},
): View {
    val playerView = LayoutInflater.from(context).inflate(R.layout.video_player, null) as PlayerView

    // 上一个和下一个按钮
    playerView.setShowNextButton(false)
    playerView.setShowPreviousButton(false)

    // 缓冲提示和字幕按钮、暂停不显示控制器
    playerView.setShowBuffering(PlayerView.SHOW_BUFFERING_ALWAYS)
    playerView.setShowSubtitleButton(true)
    playerView.setKeepContentOnPlayerReset(false)

    //快进15s 和 快退5s
    playerView.setShowFastForwardButton(false)
    playerView.setShowRewindButton(false)

    playerView.player = exoPlayer
    playerView.layoutParams = FrameLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT
    )
    playerView.setControllerVisibilityListener(ControllerVisibilityListener { visibility ->
        onControllerVisibilityChanged(visibility)
    })
    playerView.setFullscreenButtonClickListener {
        onFullscreenButtonClick(context, it)
    }

    // 注入封面
    playerView.overlayFrameLayout?.apply {
        val imageView = AppCompatImageView(context)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        addView(
            imageView, FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
        )

        val drawableCrossFadeFactory = DrawableCrossFadeFactory.Builder(300)
            .setCrossFadeEnabled(true)
            .build()

        Glide.with(this)
            .load(poster)
            .apply(RequestOptions().placeholder(R.color.black))
            .transition(DrawableTransitionOptions.with(drawableCrossFadeFactory))
            .into(imageView)

        exoPlayer.addListener(object : Player.Listener {
            override fun onPlayWhenReadyChanged(
                playWhenReady: Boolean,
                reason: Int
            ) {
                if (imageView.isVisible) {
                    imageView.isGone = playWhenReady
                }
            }
        })

        val overlayView = MediaVideoGestureView(context)
        overlayView.playerView = playerView
        overlayView.lifecycleOwner = lifecycleOwner
        addView(
            overlayView, FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
        )
    }

    return playerView
}

