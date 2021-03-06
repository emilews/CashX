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

package bisq.core.trade;

import bisq.core.btc.wallet.BtcWalletService;
import bisq.core.offer.Offer;
import bisq.core.proto.CoreProtoResolver;
import bisq.core.trade.protocol.BuyerAsTakerProtocol;
import bisq.core.trade.protocol.TakerProtocol;

import bisq.network.p2p.NodeAddress;

import bisq.common.storage.Storage;

import io.bisq.generated.protobuffer.PB;

import org.bitcoincashj.core.Coin;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;

import static com.google.common.base.Preconditions.checkArgument;

@Slf4j
public final class BuyerAsTakerTrade extends BuyerTrade implements TakerTrade {

    ///////////////////////////////////////////////////////////////////////////////////////////
    // Constructor, initialization
    ///////////////////////////////////////////////////////////////////////////////////////////

    public BuyerAsTakerTrade(Offer offer,
                             Coin tradeAmount,
                             Coin txFee,
                             Coin takerFee,
                             boolean isCurrencyForTakerFeeBtc,
                             long tradePrice,
                             NodeAddress tradingPeerNodeAddress,
                             @Nullable NodeAddress arbitratorNodeAddress,
                             Storage<? extends TradableList> storage,
                             BtcWalletService btcWalletService) {
        super(offer, tradeAmount, txFee, takerFee, isCurrencyForTakerFeeBtc, tradePrice, tradingPeerNodeAddress,
                arbitratorNodeAddress, storage, btcWalletService);
    }


    ///////////////////////////////////////////////////////////////////////////////////////////
    // PROTO BUFFER
    ///////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public PB.Tradable toProtoMessage() {
        return PB.Tradable.newBuilder()
                .setBuyerAsTakerTrade(PB.BuyerAsTakerTrade.newBuilder()
                        .setTrade((PB.Trade) super.toProtoMessage()))
                .build();
    }

    public static Tradable fromProto(PB.BuyerAsTakerTrade buyerAsTakerTradeProto,
                                     Storage<? extends TradableList> storage,
                                     BtcWalletService btcWalletService,
                                     CoreProtoResolver coreProtoResolver) {
        PB.Trade proto = buyerAsTakerTradeProto.getTrade();
        return fromProto(new BuyerAsTakerTrade(
                        Offer.fromProto(proto.getOffer()),
                        Coin.valueOf(proto.getTradeAmountAsLong()),
                        Coin.valueOf(proto.getTxFeeAsLong()),
                        Coin.valueOf(proto.getTakerFeeAsLong()),
                        proto.getIsCurrencyForTakerFeeBtc(),
                        proto.getTradePrice(),
                        proto.hasTradingPeerNodeAddress() ? NodeAddress.fromProto(proto.getTradingPeerNodeAddress()) : null,
                        proto.hasArbitratorNodeAddress() ? NodeAddress.fromProto(proto.getArbitratorNodeAddress()) : null,
                        storage,
                        btcWalletService),
                proto,
                coreProtoResolver);
    }


    ///////////////////////////////////////////////////////////////////////////////////////////
    // API
    ///////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void createTradeProtocol() {
        tradeProtocol = new BuyerAsTakerProtocol(this);
    }

    @Override
    public void takeAvailableOffer() {
        checkArgument(tradeProtocol instanceof TakerProtocol, "tradeProtocol NOT instanceof TakerProtocol");
        ((TakerProtocol) tradeProtocol).takeAvailableOffer();
    }
}
