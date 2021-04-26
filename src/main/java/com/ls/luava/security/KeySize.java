package com.ls.luava.security;

/**
 * 单次RSA加密操作所允许的最大块长度，该值与 KEY_SIZE、padding方法有关。
 *  1024key->128,2048key->1256,512key->53,11个字节用于保存padding信息。
 *  
 * @author 杨志坚  Email: dib.yang@gmail.com
 * @since 0.2.0
 */
public enum KeySize {
	K512(512,53,64),
	K1024(1024,117,128),
	K2048(2048,245,256);
	public final int KEY_SIZE;
	public final int BLOCK_SIZE;
	public final int OUTPUT_BLOCK_SIZE;
	private KeySize(int KEY_SIZE,int BLOCK_SIZE,int OUTPUT_BLOCK_SIZE)
	{
    this.KEY_SIZE = KEY_SIZE;
		this.BLOCK_SIZE=BLOCK_SIZE;
		this.OUTPUT_BLOCK_SIZE=OUTPUT_BLOCK_SIZE;
	}
	public static KeySize getKeySize(int size)
	{
		for(KeySize cfg:KeySize.values())
		{
			if(cfg.KEY_SIZE==size)return cfg;
		}
		return null;
	}
	
}
