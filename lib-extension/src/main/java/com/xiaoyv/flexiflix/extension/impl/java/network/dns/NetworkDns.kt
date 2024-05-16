package com.xiaoyv.flexiflix.extension.impl.java.network.dns

import okhttp3.Dns
import okio.IOException
import java.net.InetAddress

/**
 * 自定义 DNS 解析器
 *
 * Class: [NetworkDns]
 */
class NetworkDns : Dns {

    override fun lookup(hostname: String): List<InetAddress> {
        // 先尝试从 hosts 文件中查找
        val ip = NetworkManager.getSourceDnsMap()[hostname].orEmpty()
        if (ip.isEmpty()) {
            // 如果 hosts 文件中没有找到，则使用系统默认的 DNS 解析
            try {
                return Dns.SYSTEM.lookup(hostname)
            } catch (e: Exception) {
                throw IOException(e)
            }
        }

        return listOf(InetAddress.getByName(ip))
    }
}