package com.xiaoyv.comic.flexiflix

import android.os.Bundle
import android.util.Log
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
import com.whl.quickjs.wrapper.QuickJSContext
import com.xiaoyv.comic.flexiflix.ui.component.DelegateSnackbarHostState
import com.xiaoyv.comic.flexiflix.ui.component.LocalPopupHostState
import com.xiaoyv.comic.flexiflix.ui.component.LocalSnackbarHostState
import com.xiaoyv.comic.flexiflix.ui.component.PopupHostScreen
import com.xiaoyv.comic.flexiflix.ui.component.ScaffoldWrap
import com.xiaoyv.comic.flexiflix.ui.component.rememberPopupHostState
import com.xiaoyv.comic.flexiflix.ui.theme.AppTheme
import com.xiaoyv.flexiflix.common.utils.copyAssetsFolder
import com.xiaoyv.flexiflix.extension.java.utils.runCatchingPrint
import com.xiaoyv.flexiflix.extension.javascript.QuickJsBridge
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
    }

    override fun onResume() {
        super.onResume()

//        lifecycleScope.launch(Dispatchers.IO) {
//            runNodeTest()
//        }
//        lifecycleScope.launch(Dispatchers.IO) {
//            runNodeTest()
//        }
        lifecycleScope.launch(Dispatchers.IO) {
            val nodeDir = File(filesDir.absolutePath + "/node")
            nodeDir.deleteRecursively()
            nodeDir.mkdirs()

            copyAssetsFolder("nodejs-project", nodeDir.absolutePath)
            //runNodeTest()

//            runQuickJs()

            val index =
                File(filesDir.absolutePath + "/node/extension-samples/extension-hanime/dist/index.js")

            QuickJsBridge.run(index, "info") {
                Log.e("QuickJs", it)
                // destroy()
            }
        }
    }

    private fun runQuickJs() {
        Log.e(javaClass.simpleName, "QuickJs Start!")
        val moduleDir = filesDir.absolutePath + "/node/extension-samples"
    }

    private suspend fun runNodeTest() {
        val time = System.currentTimeMillis()
        Log.e(javaClass.simpleName, "Node Start!")

        runCatchingPrint {
            val libraryDir = packageManager.getApplicationInfo(packageName, 0)
                .nativeLibraryDir
            val moduleDir = filesDir.absolutePath + "/node/extension-samples"
            val index = filesDir.absolutePath + "/node/setup/dist/index.js"

            val process = Runtime.getRuntime().exec(
                arrayOf(
                    "$libraryDir/libnode-extension.so", index,
                    "-d=${moduleDir}", "-i=${moduleDir}/install.json",
//                    "console.log('The node project has started.');"
                ),
                arrayOf("LD_LIBRARY_PATH=$libraryDir")
            )

            val i = process.waitFor()

            Log.e(javaClass.simpleName, "退出码：$i")

            i
        }

        Log.e(javaClass.simpleName, "结束进程：${System.currentTimeMillis() - time} ms")


//        NodeService.run(this, emptyArray())

        /*     NodeExtension.startNodeWithArguments(
                 arrayOf(
                     "node", "-e",
                     "var http = require('http'); " +
                             "var versions_server = http.createServer( (request, response) => { " +
                             "  response.end('Versions: ' + JSON.stringify(process.versions)); " +
                             "}); " +
                             "versions_server.listen(3000);"
                 )
             )*/

//        if (NodeService.isServiceRunning(this)) {
//            NodeService.stop(this)
//        }
//        NodeService.start(this)
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
