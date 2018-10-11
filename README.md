# mnemonic-sdk
Mnemonic bip39 bip32 bip44 

支持 BIP39 助记词
支持 BIP32 子私钥
支持 BIP44 多币种管理

# Install
``` 
Gradle：

Add dependency:

dependencies {
    implementation 'com.lgann.develop:mnemonic-sdk:1.0.0'
}

Maven：

<dependency>
  <groupId>com.lgann.develop</groupId>
  <artifactId>mnemonic-sdk</artifactId>
  <version>1.0.0</version>
  <type>pom</type>
</dependency>
```
# Usage
## 生成助记词
``` Java
// 默认生成12个单词的助记词
String mnemonic = MnemonicUtils.generateMnemonic();
System.out.println("mnemonic = " + mnemonic);
```
助记词：exchange throw faculty fiction require father prefer mask organ crumble journey cricket<br>

## 生成种子
``` Java
// 根据助记词生成种子
byte[] seed = MnemonicUtils.generateSeed(mnemonic, "");
System.out.println("seed = " + Numeric.toHexString(seed));
```

种子： 0x7eaedb7137ef3c3b9da8c2bd976d639455133ef76be73fda9c8342c922c98ca910fe195a5db88c43fa526b3504569f6aa7476d738a6e11f8feb48aa03ae0eac0<br>

## 生成一个bip32 私钥
``` Java
byte[] privateKeyBytes = KeyPairUtils.generatePrivateKey(seed, KeyPairUtils.CoinTypes.EOS);
System.out.println("privateKeyBytes:"+ Numeric.toHexString(privateKeyBytes));
```

私钥:0x05b04396cf928446dd14be3d58cad3f64ff7b61730462e14a0722caaaf6a1d49<br>

generatePrivateKey具体实现如下<br>
说明：生成的助记词 通过bip32 bip44 转换生成的私钥 可以让同一个 seed 可以支援多币种、多帐户等<br>
``` Java
// "m/44'/60'/0'/0/0"
// 1. we just need eth wallet for now
AddressIndex addressIndex = BIP44
        .m()
        .purpose44()
        .coinType(coinType)
        .account(0)
        .external()
        .address(0);
// 2. calculate seed from mnemonics , then get master/root key ; 
// Note that the bip39 passphrase we set "" for common
ExtendedPrivateKey rootKey = ExtendedPrivateKey.fromSeed(seed, network);
// 3. get child private key deriving from master/root key
ExtendedPrivateKey childPrivateKey = rootKey.derive(addressIndex, AddressIndex.DERIVATION);
// 4. get key pair
byte[] privateKeyBytes = childPrivateKey.getKey();
```

如果是ETH钱包开发的话导入了web3j的库 ，可使用ECKeyPair 生成私钥和公钥。<br>
ETH、EOS等账户体系会有所不同，生成私钥和公钥有所区别<br>
``` Java
// 生成私钥和公钥
ECKeyPair keyPair = ECKeyPair.create(privateKeyBytes);
```

## 说明
`BIP32`：定义 Hierarchical Deterministic wallet (简称 “HD Wallet”)，是一个系统可以从单一个 seed 产生一树状结构储存多组 keypairs（私钥和公钥）。好处是可以方便的备份、转移到其他相容装置（因为都只需要 seed），以及分层的权限控制等。<br>
`BIP39`：将 seed 用方便记忆和书写的单字表示。一般由 12 个单字组成，称为 mnemonic code(phrase)，中文称为助记词或助记码。<br>
`BIP44`：基于 BIP32 的系统，赋予树状结构中的各层特殊的意义。让同一个 seed 可以支援多币种、多帐户等。各层定义如下：<br>
``` Java
m / purpose' / coin_type' / account' / change / address_index
//purporse': 固定值44', 代表是BIP44
//coin_type': 这个代表的是币种, 可以兼容很多种币, 比如BTC是0', ETH是60'
//btc一般是 m/44'/0'/0'/0
//eth一般是 m/44'/60'/0'/0
``` 

如果需要测试助记词, 和校验助记词生成的地址, 那么可以访问这个网站: https://iancoleman.io/bip39/<br>
<br>
[Bip44 注册币种列表](https://github.com/satoshilabs/slips/blob/master/slip-0044.md)