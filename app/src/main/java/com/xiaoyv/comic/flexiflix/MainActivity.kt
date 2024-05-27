package com.xiaoyv.comic.flexiflix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import com.xiaoyv.comic.flexiflix.ui.component.AppThemeState
import com.xiaoyv.comic.flexiflix.ui.component.DelegateSnackBarHostState
import com.xiaoyv.comic.flexiflix.ui.component.LocalPopupHostState
import com.xiaoyv.comic.flexiflix.ui.component.LocalSnackBarHostState
import com.xiaoyv.comic.flexiflix.ui.component.LocalThemeConfigState
import com.xiaoyv.comic.flexiflix.ui.component.PopupHostScreen
import com.xiaoyv.comic.flexiflix.ui.component.rememberPopupHostState
import com.xiaoyv.comic.flexiflix.ui.theme.AppTheme
import com.xiaoyv.flexiflix.common.utils.debugLog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val popupHostState = rememberPopupHostState()
            val snackBarState = remember { SnackbarHostState() }
            val delegateSnackBarState = remember { DelegateSnackBarHostState(snackBarState) }
            val themeState = remember { AppThemeState() }

            CompositionLocalProvider(
                LocalPopupHostState provides popupHostState,
                LocalSnackBarHostState provides delegateSnackBarState,
                LocalThemeConfigState provides themeState
            ) {
                AppTheme(themeState = themeState) {
                    Box {
                        MainRoute()

                        PopupHostScreen(
                            visible = popupHostState.visible,
                            onDismissRequest = popupHostState::hide,
                            content = popupHostState.content,
                        )

                        SnackbarHost(
                            modifier = Modifier
                                .padding(bottom = 60.dp)
                                .align(Alignment.BottomCenter),
                            hostState = snackBarState,
                        )
                    }
                }
            }
        }

        lifecycleScope.launch(Dispatchers.IO) { runTest() }
        lifecycleScope.launch(Dispatchers.IO) { runNodeJsTest() }
    }

    private fun runNodeJsTest() {
//        NodeExtension.instacne.startServer()
    }

    private fun runTest() {
        val pythonDir = File(filesDir, "python").apply {
            if (!exists()) mkdirs()
        }
        val file = File(pythonDir, "test.py")
        assets.open("python/test.py")
            .use { FileOutputStream(file).use { out -> it.copyTo(out) } }

        Python.getInstance()
            .getModule("sys")["path"]?.callAttr("append", pythonDir.absolutePath)


        val source: PyObject = Python.getInstance()
            .getModule("test")
            .callAttr("defineSource")

        // json.dumps(source.fetch_home_sections(), default=source.serializable())
        val sections = source.callAttr("fetch_home_sections")
        val string = source.callAttr("serializable", sections).toString()
        debugLog { "string:$string" }
    }
}
