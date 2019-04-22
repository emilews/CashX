/*
 * Copyright 2018 the bitcoinj-cash developers
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.bitcoinj.pow.rule;

import org.bitcoinj.core.*;
import org.bitcoinj.pow.AbstractPowRulesChecker;
import org.bitcoinj.store.BlockStore;
import org.bitcoinj.store.BlockStoreException;

import java.math.BigInteger;

/**
 * Walk backwards until we find a block that doesn't have the easiest proof of work,
 * then check that difficulty is equal to that one.
 */
public class LastNonMinimalDifficultyRuleChecker extends AbstractPowRulesChecker {

    public LastNonMinimalDifficultyRuleChecker(NetworkParameters networkParameters) {
        super(networkParameters);
    }

    @Override
    public void checkRules(StoredBlock storedPrev, Block nextBlock, BlockStore blockStore,
                           AbstractBlockChain blockChain) throws VerificationException, BlockStoreException {
        Block prevBlock = storedPrev.getHeader();
        if (isUnderPeriod(prevBlock, nextBlock)) {
            checkLastNonMinimalDifficultyIsSet(storedPrev, blockStore, nextBlock);
        }
    }

    private boolean isUnderPeriod(Block prevBlock, Block nextBlock) {
        final long timeDelta = nextBlock.getTimeSeconds() - prevBlock.getTimeSeconds();
        return timeDelta >= 0 && timeDelta <= NetworkParameters.TARGET_SPACING * 2;
    }

    private void checkLastNonMinimalDifficultyIsSet(StoredBlock storedPrev, BlockStore blockStore, Block nextBlock) throws BlockStoreException {
        try {
            Block lastNotEasiestPowBlock = findLastNotEasiestPowBlock(storedPrev, blockStore);
            if (!hasEqualDifficulty(lastNotEasiestPowBlock, nextBlock))
                throw new VerificationException("Testnet block transition that is not allowed: " +
                        Long.toHexString(lastNotEasiestPowBlock.getDifficultyTarget()) + " vs " +
                        Long.toHexString(nextBlock.getDifficultyTarget()));
        } catch (BlockStoreException ex) {
            // we don't have enough blocks, yet
        }

    }

    private Block findLastNotEasiestPowBlock(StoredBlock storedPrev, BlockStore blockStore) throws BlockStoreException {
        StoredBlock cursor = storedPrev;
        BigInteger easiestDifficulty = networkParameters.getMaxTarget();
        while (!cursor.getHeader().equals(networkParameters.getGenesisBlock()) &&
                cursor.getHeight() % networkParameters.getInterval() != 0 &&
                hasEqualDifficulty(cursor.getHeader().getDifficultyTarget(), easiestDifficulty)) {
            cursor = cursor.getPrev(blockStore);
        }
        return cursor.getHeader();
    }

}
