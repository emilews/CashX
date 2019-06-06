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

package bisq.core.bch.nodes;

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
                        new BtcNode("Default", null, "103.80.134.105", 8333, "ABC"),
                        new BtcNode("Second",null,"18.179.40.172", 8333, "ABC"),
                        new BtcNode("Third",null,"18.208.61.185", 8333, "ABC"),
                        new BtcNode("Fourth",null,"31.220.56.195", 8333, "ABC"),
                        new BtcNode("Fifth",null,"35.220.226.25", 8333, "ABC"),
                        new BtcNode("Sixth",null,"18.179.40.172", 8333, "ABC"),
                        new BtcNode("Seventh",null,"35.227.56.27", 8333, "ABC"),
                        new BtcNode("Eight",null,"37.48.83.207", 8333, "ABC"),
                        new BtcNode("Ninth",null,"38.87.54.163", 8334, "ABC"),
                        new BtcNode("Tenth",null,"38.143.66.14", 8333, "ABC"),
                        new BtcNode("Eleventh",null,"47.89.180.57", 8333, "ABC"),                        new BtcNode("Default", null, "103.80.134.105", 8333, "ABC"),
                        new BtcNode("",null,"162.242.168.36", 8333, "ABC"),
                        new BtcNode("",null,"162.242.168.55", 8333, "ABC"),
                        new BtcNode("",null,"163.172.142.149", 10020, "ABC"),
                        new BtcNode("",null,"172.96.161.245", 8333, "ABC"),
                        new BtcNode("",null,"172.249.77.148", 8333, "ABC"),
                        new BtcNode("",null,"173.82.103.250", 8333, "ABC"),
                        new BtcNode("",null,"37.48.83.207", 8333, "ABC"),
                        new BtcNode("",null,"38.87.54.163", 8334, "ABC"),
                        new BtcNode("",null,"38.143.66.14", 8333, "ABC"),
                        new BtcNode("",null,"47.89.180.57", 8333, "ABC")

                ) :
                new ArrayList<>();
    }

    public boolean useProvidedBtcNodes() {
        System.out.println(BisqEnvironment.getBaseCurrencyNetwork().isMainnet());
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
