package com.xiaoyv.flexiflix.extension.impl.java.network.dns

import com.xiaoyv.flexiflix.extension.config.settings.AppSettings
import okhttp3.Dns
import java.net.InetAddress
import java.util.concurrent.ConcurrentHashMap

/**
 * 自定义 DNS 解析器
 *
 * Class: [NetworkDns]
 */
class NetworkDns : Dns {

    override fun lookup(hostname: String): List<InetAddress> {
        // 不启用自定义 Host
        if (!AppSettings.Network.hostEnable) {
            return Dns.SYSTEM.lookup(hostname)
        }

        // 启用插件配置的 Host
        if (AppSettings.Network.hostExtensionEnable) {
            val ip = NetworkManager.getSourceDnsMap()[hostname].orEmpty()
            if (ip.isNotBlank()) {
                return listOf(InetAddress.getByName(ip))
            }
        }

        // 自定义配置的 Host
        val ip = AppSettings.Network.hostContent.parseHost()[hostname].orEmpty()
        if (ip.isNotBlank()) {
            return listOf(InetAddress.getByName(ip))
        }

        return Dns.SYSTEM.lookup(hostname)
    }

    private fun String.parseHost(): Map<String, String> {
        return emptyMap()
    }
}