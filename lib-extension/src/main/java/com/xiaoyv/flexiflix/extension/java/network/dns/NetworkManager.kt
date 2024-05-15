package com.xiaoyv.flexiflix.extension.java.network.dns

import java.util.concurrent.ConcurrentHashMap

/**
 * [NetworkManager]
 *
 * @author why
 * @since 5/11/24
 */
class NetworkManager private constructor() {
    private val map = ConcurrentHashMap<String, String>()

    companion object {
        private val instance by lazy { NetworkManager() }

        @JvmStatic
        fun put(host: String, ip: String) {
            instance.map[host] = ip
        }

        @JvmStatic
        fun putAll(map: Map<String, String>) {
            instance.map.putAll(map)
        }

        @JvmStatic
        fun getSourceDnsMap() = instance.map
    }
}