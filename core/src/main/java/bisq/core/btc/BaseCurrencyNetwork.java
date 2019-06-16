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

package bisq.core.btc;

import org.bitcoincashj.core.NetworkParameters;
import org.bitcoincashj.params.MainNetParams;
import org.bitcoincashj.params.RegTestParams;
import org.bitcoincashj.params.TestNet3Params;

import lombok.Getter;

public enum BaseCurrencyNetwork {
    BTC_MAINNET(MainNetParams.get(), "BCH", "MAINNET", "Bitcoin Cash"),
    BTC_TESTNET(TestNet3Params.get(), "BCH", "TESTNET", "Bitcoin Cash"),
    BTC_REGTEST(RegTestParams.get(), "BCH", "REGTEST", "Bitcoin Cash"),
    BTC_DAO_TESTNET(RegTestParams.get(), "BCH", "REGTEST", "Bitcoin Cash"), // server side regtest until v0.9.5
    BTC_DAO_BETANET(MainNetParams.get(), "BCH", "MAINNET", "Bitcoin Cash"), // mainnet test genesis
    BTC_DAO_REGTEST(RegTestParams.get(), "BCH", "REGTEST", "Bitcoin Cash"); // server side regtest after v0.9.5, had breaking code changes so we started over again

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
        return "BTC_MAINNET".equals(name());
    }

    public boolean isTestnet() {
        return "BTC_TESTNET".equals(name());
    }

    public boolean isDaoTestNet() {
        return "BTC_DAO_TESTNET".equals(name());
    }

    public boolean isDaoRegTest() {
        return "BTC_DAO_REGTEST".equals(name());
    }

    public boolean isDaoBetaNet() {
        return "BTC_DAO_BETANET".equals(name());
    }

    public boolean isRegtest() {
        return "BTC_REGTEST".equals(name());
    }

    public long getDefaultMinFeePerByte() {
        return 1;
    }
}
