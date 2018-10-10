package com.develop.mnemonic;

import io.github.novacrypto.bip32.ExtendedPrivateKey;
import io.github.novacrypto.bip32.Network;
import io.github.novacrypto.bip32.networks.Bitcoin;
import io.github.novacrypto.bip44.AddressIndex;
import io.github.novacrypto.bip44.BIP44;

/**
 * 生成bip32 PrivateKey ， bip44 + bip32
 *
 * @author Angus
 */
public class KeyPairUtils {
    /**
     * 币种类型
     */
    public static class CoinTypes {
        public static final int BTC = 0;
        public static final int BTCTEST = 1;
        public static final int LTC = 2;
        public static final int ETH = 60;
        public static final int EOS = 194;
    }

    /**
     * 生成 a bip32 private key
     *
     * @param mnemonic
     * @param coinType
     * @return
     */
    public static byte[] generatePrivateKey(String mnemonic, int coinType) {
        byte[] seed = MnemonicUtils.generateSeed(mnemonic, "");
        return generatePrivateKey(seed, coinType, Bitcoin.MAIN_NET);
    }

    public static byte[] generatePrivateKey(byte[] seed, int coinType) {
        return generatePrivateKey(seed, coinType, Bitcoin.MAIN_NET);
    }

    public static byte[] generatePrivateKey(byte[] seed, int coinType, final Network network) {
        // 1. we just need eth wallet for now
        AddressIndex addressIndex = BIP44
                .m()
                .purpose44()
                .coinType(coinType)
                .account(0)
                .external()
                .address(0);
        // 2. calculate seed from mnemonics , then get master/root key ; Note that the bip39 passphrase we set "" for common
        ExtendedPrivateKey rootKey = ExtendedPrivateKey.fromSeed(seed, network);
        // 3. get child private key deriving from master/root key
        ExtendedPrivateKey childPrivateKey = rootKey.derive(addressIndex, AddressIndex.DERIVATION);
        // 4. get key pair
        byte[] privateKeyBytes = childPrivateKey.getKey();
        return privateKeyBytes;
    }
}
