package org.util.nanolog.internals;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public final class EncryptionProvider {

	private static final byte[] key    = new byte[24];
	private static final String DESede = "DESede";

	//@formatter:off
	private static final ThreadLocal<Cipher> ciphers = new ThreadLocal<Cipher>(){
		@Override
		public final Cipher initialValue() {
			try {
				final Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
				cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, DESede));
				return cipher;
			} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {e.printStackTrace();}
			return null;
		}
	};
	
	public static final String encrypt(final byte[] bytes) {
		try {
			return ByteHexUtil.byteToHex(ciphers.get().doFinal(bytes));
		} catch (Exception e) {}
		return null;
	}
}
