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

package org.bitcoinj.pow.factory;

import org.bitcoinj.core.Block;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.StoredBlock;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.pow.AbstractRuleCheckerFactory;
import org.bitcoinj.pow.RulesPoolChecker;
import org.bitcoinj.pow.rule.MinimalDifficultyRuleChecker;
import org.bitcoinj.pow.rule.NewDifficultyAdjustmentAlgorithmRulesChecker;

public class DAARuleCheckerFactory extends AbstractRuleCheckerFactory {

    public DAARuleCheckerFactory(NetworkParameters parameters) {
        super(parameters);
    }

    @Override
    public RulesPoolChecker getRuleChecker(StoredBlock storedPrev, Block nextBlock) {
        RulesPoolChecker rulesChecker = new RulesPoolChecker(networkParameters);
        if (isTestNet() && TestNet3Params.isValidTestnetDateBlock(nextBlock)) {
            rulesChecker.addRule(new MinimalDifficultyRuleChecker(networkParameters));
        } else {
            rulesChecker.addRule(new NewDifficultyAdjustmentAlgorithmRulesChecker(networkParameters));
        }
        return rulesChecker;
    }

}
