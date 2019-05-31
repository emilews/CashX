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

package bisq.core.bch;

import org.bitcoincashj.core.NetworkParameters;
import org.bitcoincashj.params.MainNetParams;
import org.bitcoincashj.params.RegTestParams;
import org.bitcoincashj.params.TestNet3Params;

import lombok.Getter;

public enum BaseCurrencyNetwork {
    BCH_MAINNET(MainNetParams.get(), "BTC", "MAINNET", "Bitcoin Cash"),
    BCH_TESTNET(TestNet3Params.get(), "BCH", "TESTNET", "Bitcoin Cash"),
    BCH_REGTEST(RegTestParams.get(), "BCH", "REGTEST", "Bitcoin Cash");
    @Getter
    private final NetworkParameters parameters;
    @Getter
    private final String currencyCode;
    @Getter
    private final String network;
    @Getter
    private String currencyName;

    BaseCurrencyNetwork(NetworkParameters parameters, String currencyCode, String network, String currencyName) {
        this.parameters = parameters;
        this.currencyCode = currencyCode;
        this.network = network;
        this.currencyName = currencyName;
    }

    public boolean isMainnet() {
        return "BCH_MAINNET".equals(name());
    }

    public boolean isTestnet() {
        return "BCH_TESTNET".equals(name());
    }

    public boolean isRegtest() {
        return "BCH_REGTEST".equals(name());
    }

    public long getDefaultMinFeePerByte() {
        return 1;
    }
}
