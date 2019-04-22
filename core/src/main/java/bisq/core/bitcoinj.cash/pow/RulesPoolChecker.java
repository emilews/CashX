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

package org.bitcoinj.pow;

import org.bitcoinj.core.*;
import org.bitcoinj.store.BlockStore;
import org.bitcoinj.store.BlockStoreException;

import java.util.ArrayList;
import java.util.List;

public class RulesPoolChecker extends AbstractPowRulesChecker {

    private List<AbstractPowRulesChecker> rules;

    public RulesPoolChecker(NetworkParameters networkParameters) {
        super(networkParameters);
        this.rules = new ArrayList<AbstractPowRulesChecker>();
    }

    @Override
    public void checkRules(StoredBlock storedPrev, Block nextBlock, BlockStore blockStore, AbstractBlockChain blockChain) throws VerificationException, BlockStoreException {
        for (AbstractPowRulesChecker rule : rules) {
            rule.checkRules(storedPrev, nextBlock, blockStore, blockChain);
        }
    }

    public void addRule(AbstractPowRulesChecker rule) {
        this.rules.add(rule);
    }

}
