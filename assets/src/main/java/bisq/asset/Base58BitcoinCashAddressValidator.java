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

package bisq.asset;

import org.bitcoincashj.core.Address;
import org.bitcoincashj.core.AddressFormatException;
import org.bitcoincashj.core.NetworkParameters;
import org.bitcoincashj.params.MainNetParams;

/**
 * {@link AddressValidator} for Base58-encoded Bitcoin addresses.
 *
 * @author Chris Beams
 * @since 0.7.0
 * @see Address#fromCashAddr(NetworkParameters, String)
 */
public class Base58BitcoinCashAddressValidator implements AddressValidator {

    private final NetworkParameters networkParameters;

    public Base58BitcoinCashAddressValidator() {
        this(MainNetParams.get());
    }

    public Base58BitcoinCashAddressValidator(NetworkParameters networkParameters) {
        this.networkParameters = networkParameters;
    }

    @Override
    public AddressValidationResult validate(String address) {
        try {
            Address.fromBase58(networkParameters, address);
        } catch (AddressFormatException ex) {
            try{
                Address.fromCashAddr(networkParameters, address);
            }catch (AddressFormatException ex2){
                return AddressValidationResult.invalidAddress(ex2);
            }
        }

        return AddressValidationResult.validAddress();
    }
}
