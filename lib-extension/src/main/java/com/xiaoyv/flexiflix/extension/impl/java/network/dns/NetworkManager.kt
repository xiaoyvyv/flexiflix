package com.xiaoyv.flexiflix.extension.impl.java.network.dns

import java.util.concurrent.ConcurrentHashMap

/**
 * [NetworkManager]
 *
 * @author why
 * @since 5/11/24
 */
class NetworkManager private constructor() {
    /**
     * HOST
     *
     * HostName - IP
     */
    private val extensionDnsMap = ConcurrentHashMap<String, String>()

    companion object {
        private val instance by lazy { NetworkManager() }

        @JvmStatic
        fun put(host: String, ip: String) {
            instance.extensionDnsMap[host] = ip
        }

        @JvmStatic
        fun putAll(map: Map<String, String>) {
            instance.extensionDnsMap.putAll(map)
        }

        @JvmStatic
        fun getSourceDnsMap() = instance.extensionDnsMap
    }
}