/*
 * This file is part of Bisq.
 *
 * Bisq is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 *
 * Bisq is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Bisq. If not, see <http://www.gnu.org/licenses/>.
 */

package bisq.network;

import org.bitcoincashj.core.NetworkParameters;
import org.bitcoincashj.net.discovery.PeerDiscovery;
import org.bitcoincashj.net.discovery.PeerDiscoveryException;

import com.runjva.sourceforge.jsocks.protocol.Socks5Proxy;

import java.net.InetSocketAddress;

import java.util.concurrent.TimeUnit;

/**
 * Socks5SeedOnionDiscovery provides a list of known Bitcoin .onion seeds.
 * These are nodes running as hidden services on the Tor network.
 */
public class Socks5SeedOnionDiscovery implements PeerDiscovery {
    private InetSocketAddress[] seedAddrs;

    /**
     * Supports finding peers by hostname over a socks5 proxy.
     *
     * @param proxy  proxy the socks5 proxy to connect over.
     * @param params param to be used for seed and port information.
     */
    public Socks5SeedOnionDiscovery(@SuppressWarnings("UnusedParameters") Socks5Proxy proxy, NetworkParameters params) {
        // We do this because NetworkParameters does not contain any .onion
        // seeds.  Perhaps someday...
        String[] seedAddresses = {};
        switch (params.getId()) {
            case NetworkParameters.ID_MAINNET:
                seedAddresses = mainNetSeeds();
                break;
            case NetworkParameters.ID_TESTNET:
                seedAddresses = testNet3Seeds();
                break;
        }

        this.seedAddrs = convertAddrsString(seedAddresses, params.getPort());
    }

    /**
     * returns .onion nodes available on mainnet
     */
    private String[] mainNetSeeds() {
        // List copied from bitcoin-core on 2017-11-03
        // https://raw.githubusercontent.com/bitcoin/bitcoin/master/contrib/seeds/nodes_main.txt

        return new String[]{
                "5.39.61.239:8333",
                "13.229.113.143:8333",
                "18.208.61.185:8333",
                "34.217.29.57:8333",
                "35.227.56.27:8333",
                "37.48.125.237:8333",
                "38.87.54.163:8334",
                "39.107.225.178:8333",
                "43.239.157.253:8333",
                "45.32.46.198:8333",
                "47.90.12.238:8333",
                "47.94.198.98:8333",
                "54.210.151.7:8333",
                "60.191.106.150:8333",
                "62.210.110.181:8333",
                "66.96.199.249:8333",
                "66.187.65.6:8333",
                "67.239.3.146:8333",
                "69.181.246.223:8333",
                "70.184.247.44:8333",
                "73.26.3.156:8333",
                "74.91.21.114:8333",
                "78.97.206.149:8333",
                "81.237.206.224:8353",
                "82.75.64.15:8333",
                "82.200.205.30:8331",
                "83.172.69.154:8333",
                "88.208.3.195:8331",
                "89.179.247.236:8333",
                "91.148.141.242:8333",
                "92.206.113.127:8333",
                "94.237.73.18:8333",
                "94.247.134.76:8333",
                "95.79.35.133:7333",
                "95.216.0.251:8333",
                "98.165.34.4:8333",
                "100.1.209.114:8333",
                "101.92.34.239:8334",
                "104.154.189.24:8333",
                "104.238.131.116:8333",
                "107.175.46.159:8334",
                "107.191.117.175:8333",
                "109.70.144.105:8333",
                "113.10.152.126:8333",
                "141.239.183.148:8333",
                "142.68.56.141:8333",
                "159.89.178.179:18080",
                "162.213.252.3:8333",
                "173.212.202.187:8335",
                "173.214.244.102:8333",
                "176.9.114.109:8333",
                "188.20.184.122:8333",
                "188.134.90.224:8333",
                "188.214.30.3:8333",
                "188.214.30.232:8333",
                "193.169.244.189:8333",
                "194.14.247.116:8333",
                "195.154.168.129:8333",
                "195.211.5.228:8333",
                "198.27.66.168:8333",
                "198.204.229.34:8333",
                "209.97.181.74:18080",
                "212.107.44.171:10333"
        };
    }

    /**
     * returns .onion nodes available on testnet3
     */
    private String[] testNet3Seeds() {
        // this list copied from bitcoin-core on 2017-01-19
        //   https://github.com/bitcoin/bitcoin/blob/57b34599b2deb179ff1bd97ffeab91ec9f904d85/contrib/seeds/nodes_test.txt
        return new String[]{
                "thfsmmn2jbitcoin.onion",
                "it2pj4f7657g3rhi.onion",
                "nkf5e6b7pl4jfd4a.onion",
                "4zhkir2ofl7orfom.onion",
                "t6xj6wilh4ytvcs7.onion",
                "i6y6ivorwakd7nw3.onion",
                "ubqj4rsu3nqtxmtp.onion"
        };
    }

    /**
     * Returns an array containing all the Bitcoin nodes within the list.
     */
    @Override
    public InetSocketAddress[] getPeers(long services, long timeoutValue, TimeUnit timeoutUnit) throws PeerDiscoveryException {
        if (services != 0)
            throw new PeerDiscoveryException("DNS seeds cannot filter by services: " + services);
        return seedAddrs;
    }

    /**
     * Converts an array of hostnames to array of unresolved InetSocketAddress
     */
    private InetSocketAddress[] convertAddrsString(String[] addrs, int port) {
        InetSocketAddress[] list = new InetSocketAddress[addrs.length];
        for (int i = 0; i < addrs.length; i++) {
            list[i] = InetSocketAddress.createUnresolved(addrs[i], port);
        }
        return list;
    }

    @Override
    public void shutdown() {
        //TODO should we add a DnsLookupTor.shutdown() ?
    }
}
