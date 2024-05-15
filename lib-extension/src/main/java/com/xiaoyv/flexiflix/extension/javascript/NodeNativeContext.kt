package com.xiaoyv.flexiflix.extension.javascript

object NodeNativeContext {
    @Volatile
    private var inited = false

    init {
        init()
    }

    fun init() {
        if (!inited) {
            inited = true
            try {
                System.loadLibrary("node")
                System.loadLibrary("node-extension")
            } catch (e: UnsatisfiedLinkError) {
                e.printStackTrace()
            }
        }
    }
}
