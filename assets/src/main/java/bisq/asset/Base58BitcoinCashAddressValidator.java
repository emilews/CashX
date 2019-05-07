package bisq.asset;

import org.bitcoincashj.params.MainNetParams;
import org.bitcoincashj.core.Address;
import org.bitcoincashj.core.AddressFormatException;
import org.bitcoincashj.core.NetworkParameters;

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
            Address.fromCashAddr(networkParameters, address);
        } catch (AddressFormatException ex) {
            return AddressValidationResult.invalidAddress(ex);
        }

        return AddressValidationResult.validAddress();
    }
}
