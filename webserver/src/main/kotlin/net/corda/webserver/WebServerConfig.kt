package net.corda.webserver

import com.typesafe.config.Config
import net.corda.core.utilities.NetworkHostAndPort
import net.corda.nodeapi.internal.config.NodeSSLConfiguration
import net.corda.nodeapi.internal.config.User
import net.corda.nodeapi.internal.config.getValue
import java.nio.file.Path

/**
 * [baseDirectory] is not retrieved from the config file but rather from a command line argument.
 */
class WebServerConfig(override val baseDirectory: Path, val config: Config) : NodeSSLConfiguration {
    override val keyStorePassword: String by config
    override val trustStorePassword: String by config
    val exportJMXto: String get() = "http"
    val useHTTPS: Boolean by config
    val myLegalName: String by config
    val rpcAddress: NetworkHostAndPort by lazy {
        if (config.hasPath("rpcSettings.address")) {
            return@lazy NetworkHostAndPort.parse(config.getConfig("rpcSettings").getString("address"))
        }
        if (config.hasPath("rpcAddress")) {
            return@lazy NetworkHostAndPort.parse(config.getString("rpcAddress"))
        }
        throw Exception("Missing rpc address property. Either 'rpcSettings' or 'rpcAddress' must be specified.")
    }
    val webAddress: NetworkHostAndPort by config
    val rpcUsers: List<User> by config
}
