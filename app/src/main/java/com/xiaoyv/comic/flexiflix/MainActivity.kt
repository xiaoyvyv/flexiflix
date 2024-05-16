package com.xiaoyv.comic.flexiflix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.chaquo.python.Python
import com.xiaoyv.comic.flexiflix.ui.component.DelegateSnackbarHostState
import com.xiaoyv.comic.flexiflix.ui.component.LocalPopupHostState
import com.xiaoyv.comic.flexiflix.ui.component.LocalSnackbarHostState
import com.xiaoyv.comic.flexiflix.ui.component.PopupHostScreen
import com.xiaoyv.comic.flexiflix.ui.component.ScaffoldWrap
import com.xiaoyv.comic.flexiflix.ui.component.rememberPopupHostState
import com.xiaoyv.comic.flexiflix.ui.theme.AppTheme
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
            AppTheme {
                val popupHostState = rememberPopupHostState()
                val snackbarState = remember { SnackbarHostState() }
                val delegateSnackbarState = remember { DelegateSnackbarHostState(snackbarState) }

                CompositionLocalProvider(
                    LocalPopupHostState provides popupHostState,
                    LocalSnackbarHostState provides delegateSnackbarState
                ) {
                    ScaffoldWrap(
                        snackbarHost = {
                            SnackbarHost(
                                modifier = Modifier.navigationBarsPadding(),
                                hostState = snackbarState,
                            )
                        }
                    ) {
                        MainRoute()

                        PopupHostScreen(
                            visible = popupHostState.visible,
                            onDismissRequest = popupHostState::hide,
                            content = popupHostState.content,
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

        Python.getInstance()
            .getModule("test")
            .callAttr("main")
    }
}
