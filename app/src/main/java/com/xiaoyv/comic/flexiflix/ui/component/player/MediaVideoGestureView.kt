package com.xiaoyv.comic.flexiflix.ui.component.player

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.media.AudioManager
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.TranslateAnimation
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.annotation.OptIn
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.media3.common.C.TIME_UNSET
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.DefaultTimeBar
import androidx.media3.ui.PlayerView
import com.xiaoyv.comic.flexiflix.R
import com.xiaoyv.flexiflix.common.utils.vibrate
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.math.roundToLong

/**
 * 手势操作覆盖层
 */
@OptIn(UnstableApi::class)
class MediaVideoGestureView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private val root = LayoutInflater.from(context).inflate(R.layout.video_player_overlay, this)

    private val gestureDetector = GestureDetector(context, GestureListener())

    internal var playerView: PlayerView? = null

    private var scrubbing: Boolean = false
    private var longPressedForward: Boolean = false

    val brightnessState = MutableLiveData<Float>()
    val volumeState = MutableLiveData<Int>()

    private val handler = Handler(Looper.getMainLooper())

    /**
     * 亮度提示面板
     */
    private val brightnessPanel: View
        get() = root.findViewById(R.id.panel_brightness)
    private val brightnessBar: ProgressBar
        get() = root.findViewById(R.id.pb_brightness)
    private val brightnessIcon: ImageView
        get() = root.findViewById(R.id.iv_brightness)


    private val brightnessIcons = listOf(
        R.drawable.ic_brightness_1,
        R.drawable.ic_brightness_2,
        R.drawable.ic_brightness_3,
        R.drawable.ic_brightness_4,
        R.drawable.ic_brightness_5,
        R.drawable.ic_brightness_6,
        R.drawable.ic_brightness_7,
    )

    /**
     * 音量提示面板
     */
    private val volumePanel: View
        get() = root.findViewById(R.id.panel_volume)
    private val volumeBar: ProgressBar
        get() = root.findViewById(R.id.pb_volume)
    private val volumeIcon: ImageView
        get() = root.findViewById(R.id.iv_volume)

    /**
     * 快进提示面板
     */
    private val fastForwardPanel: View
        get() = root.findViewById(R.id.panel_fast)

    private val fastForwardIcon: View
        get() = root.findViewById(R.id.iv_fast)

    private val fastForwardText: View
        get() = root.findViewById(R.id.tv_fast)

    private val volumeIcons = listOf(
        R.drawable.ic_volume_mute,
        R.drawable.ic_volume_down,
        R.drawable.ic_volume_up,
    )

    private val player: Player?
        get() = playerView?.player


    /**
     * TimeBar
     */
    private val timeBar: DefaultTimeBar?
        get() = playerView?.findViewById(androidx.media3.ui.R.id.exo_progress)

    /**
     * 播放暂停按钮
     */
    private val playPause: View?
        get() = playerView?.findViewById(androidx.media3.ui.R.id.exo_play_pause)

    /**
     * 绑定生命周期
     */
    var lifecycleOwner: LifecycleOwner? = null
        set(value) {
            field = value
            if (value != null) {
                brightnessState.observe(value) { ratio ->
                    screenBrightness = ratio

                    volumePanel.isVisible = false
                    brightnessPanel.isVisible = true
                    brightnessBar.max = 255
                    brightnessBar.progress = (ratio * 255).roundToInt()

                    val index = (ratio * brightnessIcons.size.toFloat())
                        .roundToInt()
                        .coerceIn(0, brightnessIcons.size - 1)
                    brightnessIcon.setImageResource(brightnessIcons[index])
                }

                volumeState.observe(value) { data ->
                    volume = data

                    brightnessPanel.isVisible = false
                    volumePanel.isVisible = true

                    volumePanel.isVisible = true
                    volumeBar.max = maxVolume
                    volumeBar.progress = data

                    val ratio = data / maxVolume.toFloat()
                    val index = (ratio * volumeIcons.size.toFloat())
                        .roundToInt()
                        .coerceIn(0, volumeIcons.size - 1)
                    volumeIcon.setImageResource(if (data <= 0) R.drawable.ic_volume_off else volumeIcons[index])
                }
            }
        }

    /**
     * 最大音量
     */
    private val maxVolume: Int
        get() {
            val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            return audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        }

    /**
     * 获取或设置音量
     */
    private var volume: Int
        set(value) {
            runCatching {
                val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
                audioManager.setStreamVolume(
                    AudioManager.STREAM_MUSIC,
                    value.coerceIn(0, maxVolume),
                    0
                )
            }
        }
        get() {
            val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            return audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        }

    /**
     * 获取或设置视图亮度
     */
    private var screenBrightness: Float
        set(value) {
            val window = (context as? Activity)?.window ?: return
            val layoutParams = window.attributes
            layoutParams.screenBrightness = value.coerceIn(0f, 1f)
            window.attributes = layoutParams
        }
        get() {
            val window = (context as? Activity)?.window ?: return 0.5f
            val screenBrightness = window.attributes.screenBrightness
            if (screenBrightness in 0f..1f) return screenBrightness
            return try {
                val brightness = Settings.System.getFloat(
                    context.contentResolver,
                    Settings.System.SCREEN_BRIGHTNESS
                )
                brightness / 255f
            } catch (e: Settings.SettingNotFoundException) {
                0.5f
            }
        }

    /**
     * 最大媒体进度
     */
    private val mediaDuration: Long
        get() = requireNotNull(player).duration.let {
            if (it == TIME_UNSET) 0 else it
        }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) {
            // 停止拖拽进度
            if (scrubbing) {
                stopScrubbing(event.action == MotionEvent.ACTION_CANCEL)
            }

            // 停止快进提示面板动画
            fastForwardPanel.isVisible = false
            fastForwardIcon.clearAnimation()
            fastForwardText.clearAnimation()

            // 如果有长按倍数中，则停止倍数播放
            if (longPressedForward) {
                requireNotNull(player).apply {
                    if (playbackParameters.speed != 1f) {
                        playbackParameters = playbackParameters.withSpeed(1f)
                        context.vibrate(50)
                    }
                }
            }

            // 隐藏亮度和音量面板
            volumePanel.isVisible = false
            brightnessPanel.isVisible = false
        }

        return gestureDetector.onTouchEvent(event)
    }

    private fun startScrubbing(mills: Long) {
        val method = DefaultTimeBar::class.java.getDeclaredMethod(
            "startScrubbing",
            Long::class.javaPrimitiveType
        ).also {
            it.isAccessible = true
        }
        method.invoke(timeBar, mills)
    }

    private fun updateScrubbing(mills: Long) {
        val method = DefaultTimeBar::class.java.getDeclaredMethod(
            "updateScrubbing",
            Long::class.javaPrimitiveType
        ).also {
            it.isAccessible = true
        }
        method.invoke(timeBar, mills)
    }

    private fun stopScrubbing(boolean: Boolean) {
        val method = DefaultTimeBar::class.java.getDeclaredMethod(
            "stopScrubbing",
            Boolean::class.javaPrimitiveType
        ).also {
            it.isAccessible = true
        }
        method.invoke(timeBar, boolean)
    }


    private fun update() {
        val method = DefaultTimeBar::class.java.getDeclaredMethod("update")
            .also { it.isAccessible = true }
        method.invoke(timeBar)
    }

    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        private var initialX: Float = 0f
        private var initialY: Float = 0f
        private var initialBrightness: Float = 0f
        private var initialVolume: Int = 0
        private var initialProgress: Long = 0

        /**
         * 判断滑动方向
         */
        private var isScrollingHorizontally = false
        private var isScrollingVertically = false
        private val threshold = 10


        override fun onDown(e: MotionEvent): Boolean {
            initialX = e.x
            initialY = e.y

            initialBrightness = screenBrightness
            initialVolume = volume
            initialProgress = requireNotNull(player).currentPosition

            isScrollingVertically = false
            isScrollingHorizontally = false

            scrubbing = false
            longPressedForward = false
            return true
        }

        override fun onScroll(
            e1: MotionEvent?,
            e2: MotionEvent,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            e1 ?: return false
            if (!isScrollingHorizontally && !isScrollingVertically) {
                if (abs(distanceX) > abs(distanceY)) {
                    if (abs(distanceX) > threshold) {
                        isScrollingHorizontally = true
                    }
                } else {
                    if (abs(distanceY) > threshold) {
                        isScrollingVertically = true
                        onScrollVertically(e1, e2)
                    }
                }
            }

            if (isScrollingHorizontally) {
                onScrollHorizontally(e1, e2)
            }

            if (isScrollingVertically) {
                onScrollVertically(e1, e2)
            }
            return true
        }

        /**
         * 水平滑动手势
         */
        fun onScrollHorizontally(e1: MotionEvent, e2: MotionEvent) {
            val deltaX = e2.x - e1.x

            if (!scrubbing) {
                scrubbing = true
                startScrubbing(initialProgress)
                playerView?.showController()
            }

            val newProgress = (initialProgress + (deltaX / width.toFloat()) * mediaDuration / 2f)

            val progress = newProgress.coerceIn(0f, mediaDuration.toFloat())
                .roundToLong()

            updateScrubbing(progress)
            update()
        }

        /**
         * 垂直滑动手势
         */
        fun onScrollVertically(e1: MotionEvent, e2: MotionEvent) {
            val deltaY = e1.y - e2.y

            when {
                // 左屏滑动控制亮度
                initialX < width / 2f -> {
                    val newBrightness = (initialBrightness + (deltaY / height.toFloat()) * 2)
                        .coerceIn(0f, 1f)
                    brightnessState.value = newBrightness
                }
                // 右屏滑动控制音量
                initialX > width / 2f -> {
                    val newVolume =
                        (initialVolume + (deltaY / height.toFloat()) * maxVolume).toInt()
                    volumeState.value = newVolume
                }
            }
        }

        override fun onLongPress(e: MotionEvent) {
            requireNotNull(player).apply {
                requireNotNull(playerView).hideController()

                if (isPlaying) {
                    longPressedForward = true

                    // 定义左右来回移动动画
                    val translateAnimation = TranslateAnimation(
                        Animation.RELATIVE_TO_SELF, -0.1f,
                        Animation.RELATIVE_TO_SELF, 0.1f,
                        Animation.RELATIVE_TO_SELF, 0f,
                        Animation.RELATIVE_TO_SELF, 0f
                    ).apply {
                        duration = 300
                        repeatCount = Animation.INFINITE
                        repeatMode = Animation.REVERSE
                    }

                    // 定义渐变的 AlphaAnimation
                    val alphaAnimation = AlphaAnimation(1.0f, 0.5f).apply {
                        duration = 300
                        repeatCount = Animation.INFINITE
                        repeatMode = Animation.REVERSE
                    }

                    // 创建 AnimationSet 来组合动画
                    val animationSet = AnimationSet(true).apply {
                        addAnimation(translateAnimation)
                        addAnimation(alphaAnimation)
                    }

                    fastForwardPanel.isVisible = true

                    fastForwardIcon.clearAnimation()
                    fastForwardIcon.startAnimation(animationSet)
                    fastForwardText.clearAnimation()
                    fastForwardText.startAnimation(alphaAnimation)

                    playbackParameters = playbackParameters.withSpeed(3f)
                    context.vibrate(50)
                }
            }
        }

        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
            requireNotNull(playerView).apply {
                if (isControllerFullyVisible) {
                    hideController()
                } else {
                    showController()
                }
            }
            return true
        }

        override fun onDoubleTap(e: MotionEvent): Boolean {
            playPause?.performClick()
            return true
        }
    }
}
