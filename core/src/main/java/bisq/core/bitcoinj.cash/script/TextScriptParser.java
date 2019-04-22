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
package org.bitcoinj.script;

import org.bitcoinj.core.ECKey;

import java.util.HashMap;
import java.util.IllegalFormatException;
import java.util.Map;

import static org.bitcoinj.core.Utils.HEX;

/**
 * Created by shadders on 8/02/18.
 */
public class TextScriptParser {

    private final boolean enforceHexPrefix;
    private final Map<String, String> variables = new HashMap<String, String>();

    public static void main(String[] args) {

        ECKey keyPair = new ECKey();

        TextScriptParser parser = new TextScriptParser(false, null);
        parser.addVariable("barry", "0x00112233");
        Script script = parser.parse("<barry> 2 add 4 sub");
        System.out.println("script = " + script);
    }

    public TextScriptParser(boolean enforceHexPrefix) {
        this(enforceHexPrefix, null);
    }

    public TextScriptParser(boolean enforceHexPrefix, Map<String, String> variables) {
        this.enforceHexPrefix = enforceHexPrefix;
        if (variables != null)
            addVariables(variables);
    }

    public Script parse(String textScript) {

        ScriptBuilder builder = new ScriptBuilder();
        String[] parts = textScript.split("\\s+");

        for (int i = 0; i < parts.length; i++) {

            String part = parts[i].toUpperCase();

            if (part.startsWith("OP_"))
                part = part.substring(3);

            int opcode = ScriptOpCodes.getOpCode(part);
            if (opcode != ScriptOpCodes.OP_INVALIDOPCODE) {

                builder.op(opcode);

            } else {
                //must be a element be a data element
                if (part.startsWith("<") && part.endsWith(">")) {
                    //variable
                    String key = part.substring(1, part.length() - 1);
                    String data = variables.get(key);
                    builder.data(maybeDecodeHex(data));

                } else {
                    //assume hex encoded
                    builder.data(maybeDecodeHex(part));

                }
            }
        }
        return builder.build();
    }

    private byte[] maybeDecodeHex(String data) {
        if (data.startsWith("0X")) {
            data = data.substring(2);
        } else if (enforceHexPrefix) {
            throw new RuntimeException("Data element without hex prefix (0x).");
        }
        return HEX.decode(data); //will throw exception on bad data
    }

    public boolean isEnforceHexPrefix() {
        return enforceHexPrefix;
    }

    public String addVariable(String key, String value) {
        return variables.put(key.toUpperCase(), value.toUpperCase());
    }

    public void addVariables(Map<String, String> map) {
        for (Map.Entry<String, String> e: map.entrySet()) {
            addVariable(e.getKey(), e.getValue());
        }
    }
}
