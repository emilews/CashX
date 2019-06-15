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
                        //EmilioWong
                        new BtcNode("Test #1", null, "54.90.7.240", BtcNode.DEFAULT_PORT, "@ABC:0.14.6"),
                        new BtcNode("Test #2", null, "104.199.220.66", BtcNode.DEFAULT_PORT, "@ABC:0.19.7"),
                        new BtcNode("Test #3", null,"138.197.133.102", BtcNode.DEFAULT_PORT, "@BCHD:0.14.3"),
                        new BtcNode("Test #4", null, "173.249.16.82",BtcNode.DEFAULT_PORT,"@BCHD:0.14.4"),
                        new BtcNode("Test #6", null, "185.247.119.31", BtcNode.DEFAULT_PORT, "@BCHD:0.14.4"),
                        new BtcNode("Test #7", null, "35.202.172.160", BtcNode.DEFAULT_PORT,"@BCHD:0.14.4"),
                        new BtcNode("Test #8", null, "51.254.136.188", BtcNode.DEFAULT_PORT, "@BCHD:0.14.4"),
                        new BtcNode("Test #9", null,"89.163.204.53", BtcNode.DEFAULT_PORT,"@BCHD:0.14.4")

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
