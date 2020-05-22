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
import com.android.jtblk.crypto.ecdsa.IKeyPair;
import com.android.jtblk.crypto.ecdsa.Seed;
import com.android.jtblk.utils.MnemonicGenerator;
import com.android.jtblk.utils.Utils;

import java.util.Arrays;

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
     * @param isED25519    Generates a SWTC wallet by ed25519
     * @return A BIP-39 compatible SWTC wallet
     */
    public static Wallet generateBip44Wallet(
            String password, AddressIndex addressIndex, boolean isED25519) {
        byte[] initialEntropy = RandomSeed.random(wordCount);
        String mnemonic = new MnemonicGenerator(wordList).generateMnemonic(initialEntropy);
        byte[] seedByte = new MnemonicGenerator(wordList).generateSeed(mnemonic, password);

        Bip32ECKeyPair masterKeypair = Bip32ECKeyPair.generateKeyPair(seedByte);
        Bip32ECKeyPair bip44Keypair = Bip32ECKeyPair.deriveKeyPair(masterKeypair, addressIndex);

        Wallet wallet = new Wallet();
        Seed seed = new Seed();
        if (isED25519) {
            seed.setEd25519();
        }
        wallet.setKeypairs(seed.fromPrivateKey(Utils.bigHex(bip44Keypair.getPrivateKey())));
        wallet.setMnemonics(mnemonic);
        return wallet;
    }

    /**
     * Generates a BIP-44 compatible SWTC wallet on top of BIP-39 generated seed.
     *
     * @param password  Will be used for both wallet encryption and passphrase for BIP-39 seed
     * @param isED25519 Generates a SWTC wallet by ed25519
     * @return A BIP-39 compatible SWTC wallet
     */
    public static Wallet generateWallet(
            String password, boolean isED25519) {
        byte[] initialEntropy = RandomSeed.random(wordCount);
        String mnemonic = new MnemonicGenerator(wordList).generateMnemonic(initialEntropy);
        byte[] seedByte = new MnemonicGenerator(wordList).generateSeed(mnemonic, password);

        Wallet wallet = new Wallet();
        Seed seed = new Seed();
        if (isED25519) {
            seed.setEd25519();
        }
        String secret = seed.encodeSeed(Arrays.copyOf(seedByte, 16));
        IKeyPair keypairs = Seed.fromBase58(secret).keyPair();
        wallet.setKeypairs(keypairs);
        wallet.setSecret(secret);
        wallet.setMnemonics(mnemonic);
        return wallet;
    }

    /**
     * Generates a BIP-44 compatible SWTC wallet by mnemonic.
     *
     * @param mnemonic
     * @param password  Will be used for both wallet encryption and passphrase for BIP-39 seed
     * @param isED25519 Generates a SWTC wallet by ed25519
     * @return
     */
    public static Wallet fromMnemonic(
            String mnemonic, String password, boolean isED25519) {
        byte[] seedByte = MnemonicGenerator.generateSeed(mnemonic, password);
        Seed seed = new Seed();
        if (isED25519) {
            seed.setEd25519();
        }
        Wallet wallet = new Wallet();
        String secret = seed.encodeSeed(Arrays.copyOf(seedByte, 16));
        IKeyPair keypairs = Seed.fromBase58(secret).keyPair();
        wallet.setKeypairs(keypairs);
        wallet.setSecret(secret);
        wallet.setMnemonics(mnemonic);
        return wallet;
    }

    /**
     * Generates a BIP-44 compatible SWTC wallet by mnemonic.
     *
     * @param mnemonic
     * @param password     Will be used for both wallet encryption and passphrase for BIP-39 seed
     * @param addressIndex BIP-44 path
     * @param isED25519    Generates a SWTC wallet by ed25519
     * @return
     */
    public static Wallet fromMnemonicWithPath(
            String mnemonic, String password, AddressIndex addressIndex, boolean isED25519) {
        byte[] seedByte = MnemonicGenerator.generateSeed(mnemonic, password);
        Bip32ECKeyPair masterKeypair = Bip32ECKeyPair.generateKeyPair(seedByte);
        Bip32ECKeyPair bip44Keypair = Bip32ECKeyPair.deriveKeyPair(masterKeypair, addressIndex);
        Seed seed = new Seed();
        if (isED25519) {
            seed.setEd25519();
        }
        Wallet wallet = new Wallet();
        wallet.setKeypairs(seed.fromPrivateKey(Utils.bigHex(bip44Keypair.getPrivateKey())));
        wallet.setMnemonics(mnemonic);
        return wallet;
    }
}
