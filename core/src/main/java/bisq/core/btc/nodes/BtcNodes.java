/*
 * This file is part of Bisq.
 *
 * bisq is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 *
 * bisq is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with bisq. If not, see <http://www.gnu.org/licenses/>.
 */

package bisq.core.btc.nodes;

import bisq.core.app.BisqEnvironment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;

import static com.google.common.base.Preconditions.checkArgument;

// Managed here: https://github.com/bisq-network/roles/issues/39
@Slf4j
public class BtcNodes {

    public enum BitcoinNodesOption {
        PROVIDED,
        CUSTOM,
        PUBLIC
    }

    // For other base currencies or testnet we ignore provided nodes
    public List<BtcNode> getProvidedBtcNodes() {
        return useProvidedBtcNodes() ?
                Arrays.asList(
                        //AndreaJ
                        new BtcNode("Test", null, "35.187.34.1",8333, "AndreaJ"),
                        new BtcNode("Test", null, "13.230.45.190",8333, "AndreaJ"),
                        new BtcNode("Test", null, "89.163.204.53",8333, "AndreaJ"),
                        new BtcNode("Test", null, "165.227.134.178",8333, "AndreaJ"),
                        new BtcNode("Test", null, "157.230.240.58",8333, "AndreaJ"),
                        new BtcNode("Test", null, "96.126.117.5",8333, "AndreaJ"),
                        new BtcNode("Test", null, "172.104.146.199",8333, "AndreaJ"),
                        new BtcNode("Test", null, "13.211.124.209",8333, "AndreaJ"),
                        new BtcNode("Test", null, "37.221.198.57",28333, "AndreaJ"),
                        new BtcNode("Test", null, "18.179.32.86",8333, "AndreaJ"),
                        new BtcNode("Test", null, "211.27.135.253",8333, "AndreaJ"),
                        new BtcNode("Test", null, "3.80.119.4",8333, "AndreaJ"),
                        new BtcNode("Test", null, "198.13.60.36",8333, "AndreaJ"),
                        new BtcNode("Test", null, "188.214.30.138",8333, "AndreaJ"),
                        new BtcNode("Test", null, "195.122.150.173",8333, "AndreaJ"),
                        new BtcNode("Test", null, "96.44.173.109",8333, "AndreaJ"),
                        new BtcNode("Test", null, "45.79.176.90",8333, "AndreaJ"),
                        new BtcNode("Test", null, "5.9.142.55",8333, "AndreaJ"),
                        new BtcNode("Test", null, "209.97.148.253",8333, "AndreaJ"),
                        new BtcNode("Test", null, "50.39.245.26",8333, "AndreaJ"),
                        new BtcNode("Test", null, "80.179.226.48",8333, "AndreaJ"),
                        new BtcNode("Test", null, "51.15.179.106",8335, "AndreaJ"),
                        new BtcNode("Test", null, "212.32.230.219",8333, "AndreaJ"),
                        new BtcNode("Test", null, "209.188.18.204",8334, "AndreaJ"),
                        new BtcNode("Test", null, "159.89.131.10",8888, "AndreaJ"),
                        new BtcNode("Test", null, "35.211.37.184",8333, "AndreaJ"),
                        new BtcNode("Test", null, "173.230.147.162",8333, "AndreaJ"),
                        new BtcNode("Test", null, "60.205.149.204",8333, "AndreaJ"),
                        new BtcNode("Test", null, "139.162.252.62",8333, "AndreaJ"),
                        new BtcNode("Test", null, "82.118.17.190",8433, "AndreaJ"),
                        new BtcNode("Test", null, "144.76.94.143",8333, "AndreaJ"),
                        new BtcNode("Test", null, "165.227.155.208",8333, "AndreaJ"),
                        new BtcNode("Test", null, "145.239.94.183",8333, "AndreaJ"),
                        new BtcNode("Test", null, "148.251.12.252",8333, "AndreaJ"),
                        new BtcNode("Test", null, "198.199.86.165",8333, "AndreaJ"),
                        new BtcNode("Test", null, "79.135.200.90",8333, "AndreaJ"),
                        new BtcNode("Test", null, "45.79.209.223",8333, "AndreaJ"),
                        new BtcNode("Test", null, "128.199.241.171",8333, "AndreaJ"),
                        new BtcNode("Test", null, "94.247.134.76",8333, "AndreaJ"),
                        new BtcNode("Test", null, "47.254.153.230",8333, "AndreaJ"),
                        new BtcNode("Test", null, "47.99.170.250",8333, "AndreaJ"),
                        new BtcNode("Test", null, "138.68.57.69",8333, "AndreaJ"),
                        new BtcNode("Test", null, "23.239.22.239",8333, "AndreaJ"),
                        new BtcNode("Test", null, "170.106.66.186",8333, "AndreaJ"),
                        new BtcNode("Test", null, "52.66.202.53",8333, "AndreaJ"),
                        new BtcNode("Test", null, "139.59.99.164",8333, "AndreaJ"),
                        new BtcNode("Test", null, "45.77.74.52",8333, "AndreaJ"),
                        new BtcNode("Test", null, "185.247.119.31",8333, "AndreaJ"),
                        new BtcNode("Test", null, "100.11.114.130",8333, "AndreaJ"),
                        new BtcNode("Test", null, "198.58.125.203",8333, "AndreaJ"),
                        new BtcNode("Test", null, "47.89.177.134",8333, "AndreaJ"),
                        new BtcNode("Test", null, "173.212.242.147",8333, "AndreaJ"),
                        new BtcNode("Test", null, "45.32.244.156",8333, "AndreaJ"),
                        new BtcNode("Test", null, "136.243.24.159",8333, "AndreaJ"),
                        new BtcNode("Test", null, "45.63.115.252",8333, "AndreaJ")
                                        ) :
                new ArrayList<>();
    }

    public boolean useProvidedBtcNodes() {
        return BisqEnvironment.getBaseCurrencyNetwork().isMainnet();
    }

    public static List<BtcNodes.BtcNode> toBtcNodesList(Collection<String> nodes) {
        return nodes.stream()
                .filter(e -> !e.isEmpty())
                .map(BtcNodes.BtcNode::fromFullAddress)
                .collect(Collectors.toList());
    }

    @EqualsAndHashCode
    @Getter
    public static class BtcNode {
        private static final int DEFAULT_PORT = BisqEnvironment.getParameters().getPort(); //8333

        @Nullable
        private final String onionAddress;
        @Nullable
        private final String hostName;
        @Nullable
        private final String operator; // null in case the user provides a list of custom btc nodes
        @Nullable
        private final String address; // IPv4 address
        private int port = DEFAULT_PORT;

        /**
         * @param fullAddress [IPv4 address:port or onion:port]
         * @return BtcNode instance
         */
        public static BtcNode fromFullAddress(String fullAddress) {
            String[] parts = fullAddress.split(":");
            checkArgument(parts.length > 0);
            final String host = parts[0];
            int port = DEFAULT_PORT;
            if (parts.length == 2)
                port = Integer.valueOf(parts[1]);

            return host.contains(".onion") ? new BtcNode(null, host, null, port, null) : new BtcNode(null, null, host, port, null);
        }

        public BtcNode(@Nullable String hostName, @Nullable String onionAddress, @Nullable String address, int port, @Nullable String operator) {
            this.hostName = hostName;
            this.onionAddress = onionAddress;
            this.address = address;
            this.port = port;
            this.operator = operator;
        }

        public boolean hasOnionAddress() {
            return onionAddress != null;
        }

        public String getHostNameOrAddress() {
            if (hostName != null)
                return hostName;
            else
                return address;
        }

        public boolean hasClearNetAddress() {
            return hostName != null || address != null;
        }

        @Override
        public String toString() {
            return "onionAddress='" + onionAddress + '\'' +
                    ", hostName='" + hostName + '\'' +
                    ", address='" + address + '\'' +
                    ", port='" + port + '\'' +
                    ", operator='" + operator;
        }
    }
}
