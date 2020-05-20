/*
 * Copyright 2019 Web3 Labs Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package com.android.jtblk.BIP44;

import com.android.jtblk.BIP39.Bip32ECKeyPair;
import com.android.jtblk.BIP39.RandomSeed;
import com.android.jtblk.BIP39.WordCount;
import com.android.jtblk.BIP39.WordList;
import com.android.jtblk.client.Wallet;
import com.android.jtblk.crypto.ecdsa.Seed;
import com.android.jtblk.utils.MnemonicGenerator;

import org.web3j.utils.Numeric;

import java.util.Arrays;

import static com.android.jtblk.utils.Utils.bigHex;
import static org.web3j.utils.Numeric.toHexString;

public class Bip44WalletGenerator {

    private static WordList wordList;
    private static WordCount wordCount;

    public Bip44WalletGenerator(final WordList wordList, final WordCount wordCount) {
        this.wordList = wordList;
        this.wordCount = wordCount;
    }

    /**
     * Generates a BIP-44 compatible SWTC wallet on top of BIP-39 generated seed.
     *
     * @param password     Will be used for both wallet encryption and passphrase for BIP-39 seed
     * @param addressIndex BIP-44 path
     * @return A BIP-39 compatible SWTC wallet
     */
    public static Wallet generateBip44Wallet(
            String password, AddressIndex addressIndex) {
        byte[] initialEntropy = RandomSeed.random(wordCount);
        String mnemonic = new MnemonicGenerator(wordList).generateMnemonic(initialEntropy);
        byte[] seed = new MnemonicGenerator(wordList).generateSeed(mnemonic, password);

        Bip32ECKeyPair masterKeypair = Bip32ECKeyPair.generateKeyPair(seed);
        Bip32ECKeyPair bip44Keypair = Bip32ECKeyPair.deriveKeyPair(masterKeypair, addressIndex);

        Wallet wallet = new Wallet();
        wallet.setKeypairs(Seed.fromPrivateKey(bip44Keypair.getPrivateKey()));
        wallet.setMnemonics(mnemonic);
        return wallet;
    }

    /**
     * Generates a BIP-44 compatible SWTC wallet by mnemonic.
     *
     * @param mnemonic
     * @param password     Will be used for both wallet encryption and passphrase for BIP-39 seed
     * @param addressIndex BIP-44 path
     * @return
     */
    public static Wallet fromMnemonic(
            String mnemonic, String password, AddressIndex addressIndex) {
        byte[] seed = MnemonicGenerator.generateSeed(mnemonic, password);
        Bip32ECKeyPair masterKeypair = Bip32ECKeyPair.generateKeyPair(seed);
        Bip32ECKeyPair bip44Keypair = Bip32ECKeyPair.deriveKeyPair(masterKeypair, addressIndex);

        Wallet wallet = new Wallet();
        wallet.setKeypairs(Seed.fromPrivateKey(bip44Keypair.getPrivateKey()));
        wallet.setMnemonics(mnemonic);
        return wallet;
    }
}
