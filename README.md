# jch-jingtum-lib-android
基于[jingtum-lib-java](https://github.com/swtcpro/jingtum-lib-java)，适用Android钱包应用开发。

## 变更点
1. 删除了挂单类和关系类的方法，修改了部分代码以适用Android
2. 添加了KeyStore和二维码的导入导出
3. 助记词导入导入和ED25519模式支持

## 接口说明
### keyStore生成
```android
Wallet wallet = new Wallet("shExMjiMqza4DdMaSg3ra9vxWPZsQ");
try {
        KeyStoreFile keyStoreFile = KeyStore.createLight("Key123456", jtKeyPair);
        System.out.println(keyStoreFile.toString());
    } catch (CipherException e) {
        e.printStackTrace();
    }
```

```json
{
	"address": "jHY6aRcs7J6KnfgqD4FVwTQ247boj9nbDZ",
	"id": "1c1bf720-82fd-4ed3-bddf-72ebbc7b4262",
	"version": 3,
	"crypto": {
		"cipher": "aes-128-ctr",
		"ciphertext": "0bc63928ace81eb82869d5008372830191bad7706ef2101665d009a9e6",
		"cipherparams": {
			"iv": "2ae846f498bbb6ff6a7d572d51cdd74b"
		},
		"kdf": "scrypt",
		"kdfparams": {
			"dklen": 32,
			"n": 4096,
			"p": 6,
			"r": 8,
			"salt": "944611340b628e66850eff427ec0df006788d2aa7e3809b383dbe05282edd723"
		},
		"mac": "ad1343750c048c96b019dc09dd6a5b93d5664cfd5147dd052ec040546d53617f"
	}
}
```

### 解析keyStore
```java
String data = "{"address":"jHY6aRcs7J6KnfgqD4FVwTQ247boj9nbDZ","id":"1c1bf720-82fd-4ed3-bddf-72ebbc7b4262","version":3,"crypto":{"cipher":"aes-128-ctr","ciphertext":"0bc63928ace81eb82869d5008372830191bad7706ef2101665d009a9e6","cipherparams":{"iv":"2ae846f498bbb6ff6a7d572d51cdd74b"},"kdf":"scrypt","kdfparams":{"dklen":32,"n":4096,"p":6,"r":8,"salt":"944611340b628e66850eff427ec0df006788d2aa7e3809b383dbe05282edd723"},"mac":"ad1343750c048c96b019dc09dd6a5b93d5664cfd5147dd052ec040546d53617f"}}"
KeyStoreFile keyStoreFile = KeyStoreFile.parse(data);
Wallet wallet = KeyStore.decrypt(pwd, keyStoreFile);
```

### 生成二维码
```java
Bitmap bitmap = QRGenerator.getQrCodeImage(data, 800, Color.BLACK);
```

### 解析二维码
```java
String data = QRGenerator.decodeQrImage(bitmap);
```

### 助记词、ED25519

#### 随机助记词生成
```java
	Wallet wallet = new Bip44WalletGenerator(Chinese_simplified.INSTANCE, WordCount.TWELVE).generateWallet(null, false);
	System.out.println(wallet.getAddress());
	System.out.println(wallet.getMnemonics());
	System.out.println(wallet.getSecret());
```

#### 导入助记词
```java
	String mnemonic = "头 玄 秒 站 致 源 省 却 景 熙 奇 将";
	Wallet wallet = Bip44WalletGenerator.fromMnemonic(mnemonic, null, false);
	Assert.assertEquals("jHJSCzjAeZWJrR1MKzU3JdWSTrivHCccLV", wallet.getAddress());
	Assert.assertEquals("头 玄 秒 站 致 源 省 却 景 熙 奇 将", wallet.getMnemonics());
	Assert.assertEquals("shc9m9iNVPcnhxfedr5kVa2vg2Xov", wallet.getSecret());
```

#### BIP44助记词生成
```
	AddressIndex addressIndex = BIP44.m()
		.purpose44()
		.coinType(CoinTypes.SWTC)
		.account(0)
		.external()
		.address(0);
	Wallet wallet = new Bip44WalletGenerator(Chinese_simplified.INSTANCE, WordCount.TWELVE).generateBip44Wallet(null, 		addressIndex, false);
	System.out.println(wallet.getAddress());
	System.out.println(wallet.getMnemonics());
	System.out.println(wallet.getSecret());
```


#### BIP44助记词导入
```
	AddressIndex addressIndex = BIP44.m()
		.purpose44()
		.coinType(CoinTypes.SWTC)
		.account(0)
		.external()
		.address(0);
	String mnemonic = "馏 标 骗 孩 挑 蒸 座 希 原 门 挖 序";
	Wallet wallet = Bip44WalletGenerator.fromMnemonicWithPath(mnemonic, null, addressIndex, false);
	Assert.assertEquals("j41M6Qjp7k9CcrRMA4vZrX37HorymBHUaB", wallet.getAddress());
	Assert.assertEquals("馏 标 骗 孩 挑 蒸 座 希 原 门 挖 序", wallet.getMnemonics());
	Assert.assertEquals("005570847FC13CC17B93637BC54A200748EC2538385FB4E5DFDDDCE3703A0E873D", wallet.getSecret().toUpperCase());
```

### jingtum-lib-java部分
请参照[jingtum-lib-java](https://github.com/swtcpro/jingtum-lib-java/blob/master/README.md)，这里不再赘述。




