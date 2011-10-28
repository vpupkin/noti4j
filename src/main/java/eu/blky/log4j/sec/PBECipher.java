// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   PBECipher.java

package eu.blky.log4j.sec;

import java.security.*;
import java.util.Random;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

// Referenced classes of package org.sonatype.plexus.components.cipher:
//            PlexusCipherException, Base64

public class PBECipher
{

    public PBECipher()
        throws PlexusCipherException
    {
        _onLinux = false;
        try
        {
            _digester = MessageDigest.getInstance("SHA-256");
            if(System.getProperty("os.name", "blah").toLowerCase().indexOf("linux") != -1)
                _onLinux = true;
            if(_onLinux)
                System.setProperty("securerandom.source", "file:/dev/./urandom");
            else
                _secureRandom = new SecureRandom();
        }
        catch(NoSuchAlgorithmException e)
        {
            throw new PlexusCipherException(e);
        }
    }

    private byte[] getSalt(int sz)
        throws NoSuchAlgorithmException, NoSuchProviderException
    {
        byte res[] = null;
        if(_secureRandom != null)
        {
            _secureRandom.setSeed(System.currentTimeMillis());
            res = _secureRandom.generateSeed(sz);
        } else
        {
            res = new byte[sz];
            Random r = new Random(System.currentTimeMillis());
            r.nextBytes(res);
        }
        return res;
    }

    public String encrypt64(String clearText, String password)
        throws PlexusCipherException
    {
        try
        {
            byte clearBytes[] = clearText.getBytes();
            byte salt[] = getSalt(8);
            if(_secureRandom != null)
                (new SecureRandom()).nextBytes(salt);
            Cipher cipher = createCipher(password.getBytes("UTF8"), salt, 1);
            byte encryptedBytes[] = cipher.doFinal(clearBytes);
            int len = encryptedBytes.length;
            byte padLen = (byte)(16 - (8 + len + 1) % 16);
            int totalLen = 8 + len + padLen + 1;
            byte allEncryptedBytes[] = getSalt(totalLen);
            System.arraycopy(salt, 0, allEncryptedBytes, 0, 8);
            allEncryptedBytes[8] = padLen;
            System.arraycopy(encryptedBytes, 0, allEncryptedBytes, 9, len);
            byte encryptedTextBytes[] = Base64.encodeBase64(allEncryptedBytes);
            String encryptedText = new String(encryptedTextBytes, "UTF8");
            return encryptedText;
        }
        catch(Exception e)
        {
            throw new PlexusCipherException(e);
        }
    }

    public String decrypt64(String encryptedText, String password)
        throws PlexusCipherException
    {
        try
        {
            byte allEncryptedBytes[] = Base64.decodeBase64(encryptedText.getBytes());
            int totalLen = allEncryptedBytes.length;
            byte salt[] = new byte[8];
            System.arraycopy(allEncryptedBytes, 0, salt, 0, 8);
            byte padLen = allEncryptedBytes[8];
            byte encryptedBytes[] = new byte[totalLen - 8 - 1 - padLen];
            System.arraycopy(allEncryptedBytes, 9, encryptedBytes, 0, encryptedBytes.length);
            Cipher cipher = createCipher(password.getBytes("UTF8"), salt, 2);
            byte clearBytes[] = cipher.doFinal(encryptedBytes);
            String clearText = new String(clearBytes, "UTF8");
            return clearText;
        }
        catch(Exception e)
        {
            throw new PlexusCipherException(e);
        }
    }

    private Cipher createCipher(byte pwdAsBytes[], byte salt[], int mode)
        throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException
    {
        _digester.reset();
        byte keyAndIv[] = new byte[32];
        if(salt == null || salt.length == 0)
            salt = null;
        int currentPos = 0;
        do
        {
            if(currentPos >= keyAndIv.length)
                break;
            _digester.update(pwdAsBytes);
            if(salt != null)
                _digester.update(salt, 0, 8);
            byte result[] = _digester.digest();
            int stillNeed = keyAndIv.length - currentPos;
            if(result.length > stillNeed)
            {
                byte b[] = new byte[stillNeed];
                System.arraycopy(result, 0, b, 0, b.length);
                result = b;
            }
            System.arraycopy(result, 0, keyAndIv, currentPos, result.length);
            currentPos += result.length;
            if(currentPos < keyAndIv.length)
            {
                _digester.reset();
                _digester.update(result);
            }
        } while(true);
        byte key[] = new byte[16];
        byte iv[] = new byte[16];
        System.arraycopy(keyAndIv, 0, key, 0, key.length);
        System.arraycopy(keyAndIv, key.length, iv, 0, iv.length);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(mode, new SecretKeySpec(key, "AES"), new IvParameterSpec(iv));
        return cipher;
    }

    protected static final String STRING_ENCODING = "UTF8";
    protected static final int SPICE_SIZE = 16;
    protected static final int SALT_SIZE = 8;
    protected static final int CHUNK_SIZE = 16;
    protected static final byte WIPER = 0;
    protected static final String DIGEST_ALG = "SHA-256";
    protected static final String KEY_ALG = "AES";
    protected static final String CIPHER_ALG = "AES/CBC/PKCS5Padding";
    protected static int PBE_ITERATIONS = 1000;
    protected MessageDigest _digester;
    protected SecureRandom _secureRandom;
    protected boolean _onLinux;

}
