 
package eu.blky.log4j.sec;


public class Base64
{

    public Base64()
    {
    }

    public static boolean isBase64(byte b)
    {
        return b == 61 || b >= 0 && base64Alphabet[b] >= 0;
    }

    public static boolean isArrayByteBase64(byte arrayOctect[])
    {
        arrayOctect = discardWhitespace(arrayOctect);
        int length = arrayOctect.length;
        if(length == 0)
            return true;
        for(int i = 0; i < length; i++)
            if(!isBase64(arrayOctect[i]))
                return false;

        return true;
    }

    public static byte[] encodeBase64(byte binaryData[])
    {
        return encodeBase64(binaryData, false);
    }

    public static byte[] encodeBase64Chunked(byte binaryData[])
    {
        return encodeBase64(binaryData, true);
    }

    public Object decode(Object pObject)
        throws IllegalArgumentException
    {
        if(!(pObject instanceof byte[]))
            throw new IllegalArgumentException("Parameter supplied to Base64 decode is not a byte[]");
        else
            return decode((byte[])pObject);
    }

    public byte[] decode(byte pArray[])
    {
        return decodeBase64(pArray);
    }

    public static byte[] encodeBase64(byte binaryData[], boolean isChunked)
    {
        int lengthDataBits = binaryData.length * 8;
        int fewerThan24bits = lengthDataBits % 24;
        int numberTriplets = lengthDataBits / 24;
        int nbrChunks = 0;
        int encodedDataLength;
        if(fewerThan24bits != 0)
            encodedDataLength = (numberTriplets + 1) * 4;
        else
            encodedDataLength = numberTriplets * 4;
        if(isChunked)
        {
            nbrChunks = CHUNK_SEPARATOR.length != 0 ? (int)Math.ceil((float)encodedDataLength / 76F) : 0;
            encodedDataLength += nbrChunks * CHUNK_SEPARATOR.length;
        }
        byte encodedData[] = new byte[encodedDataLength];
        int encodedIndex = 0;
        int nextSeparatorIndex = 76;
        int chunksSoFar = 0;
        int i;
        int dataIndex;
        for(i = 0; i < numberTriplets; i++)
        {
            dataIndex = i * 3;
            byte b1 = binaryData[dataIndex];
            byte b2 = binaryData[dataIndex + 1];
            byte b3 = binaryData[dataIndex + 2];
            byte l = (byte)(b2 & 0xf);
            byte k = (byte)(b1 & 3);
            byte val1 = (b1 & 0xffffff80) != 0 ? (byte)(b1 >> 2 ^ 0xc0) : (byte)(b1 >> 2);
            byte val2 = (b2 & 0xffffff80) != 0 ? (byte)(b2 >> 4 ^ 0xf0) : (byte)(b2 >> 4);
            byte val3 = (b3 & 0xffffff80) != 0 ? (byte)(b3 >> 6 ^ 0xfc) : (byte)(b3 >> 6);
            encodedData[encodedIndex] = lookUpBase64Alphabet[val1];
            encodedData[encodedIndex + 1] = lookUpBase64Alphabet[val2 | k << 4];
            encodedData[encodedIndex + 2] = lookUpBase64Alphabet[l << 2 | val3];
            encodedData[encodedIndex + 3] = lookUpBase64Alphabet[b3 & 0x3f];
            encodedIndex += 4;
            if(isChunked && encodedIndex == nextSeparatorIndex)
            {
                System.arraycopy(CHUNK_SEPARATOR, 0, encodedData, encodedIndex, CHUNK_SEPARATOR.length);
                chunksSoFar++;
                nextSeparatorIndex = 76 * (chunksSoFar + 1) + chunksSoFar * CHUNK_SEPARATOR.length;
                encodedIndex += CHUNK_SEPARATOR.length;
            }
        }

        dataIndex = i * 3;
        if(fewerThan24bits == 8)
        {
            byte b1 = binaryData[dataIndex];
            byte k = (byte)(b1 & 3);
            byte val1 = (b1 & 0xffffff80) != 0 ? (byte)(b1 >> 2 ^ 0xc0) : (byte)(b1 >> 2);
            encodedData[encodedIndex] = lookUpBase64Alphabet[val1];
            encodedData[encodedIndex + 1] = lookUpBase64Alphabet[k << 4];
            encodedData[encodedIndex + 2] = 61;
            encodedData[encodedIndex + 3] = 61;
        } else
        if(fewerThan24bits == 16)
        {
            byte b1 = binaryData[dataIndex];
            byte b2 = binaryData[dataIndex + 1];
            byte l = (byte)(b2 & 0xf);
            byte k = (byte)(b1 & 3);
            byte val1 = (b1 & 0xffffff80) != 0 ? (byte)(b1 >> 2 ^ 0xc0) : (byte)(b1 >> 2);
            byte val2 = (b2 & 0xffffff80) != 0 ? (byte)(b2 >> 4 ^ 0xf0) : (byte)(b2 >> 4);
            encodedData[encodedIndex] = lookUpBase64Alphabet[val1];
            encodedData[encodedIndex + 1] = lookUpBase64Alphabet[val2 | k << 4];
            encodedData[encodedIndex + 2] = lookUpBase64Alphabet[l << 2];
            encodedData[encodedIndex + 3] = 61;
        }
        if(isChunked && chunksSoFar < nbrChunks)
            System.arraycopy(CHUNK_SEPARATOR, 0, encodedData, encodedDataLength - CHUNK_SEPARATOR.length, CHUNK_SEPARATOR.length);
        return encodedData;
    }

    public static byte[] decodeBase64(byte base64Data[])
    {
        base64Data = discardNonBase64(base64Data);
        if(base64Data.length == 0)
            return new byte[0];
        int numberQuadruple = base64Data.length / 4;
        int encodedIndex = 0;
        int lastData;
        for(lastData = base64Data.length; base64Data[lastData - 1] == 61;)
            if(--lastData == 0)
                return new byte[0];

        byte decodedData[] = new byte[lastData - numberQuadruple];
        for(int i = 0; i < numberQuadruple; i++)
        {
            int dataIndex = i * 4;
            byte marker0 = base64Data[dataIndex + 2];
            byte marker1 = base64Data[dataIndex + 3];
            byte b1 = base64Alphabet[base64Data[dataIndex]];
            byte b2 = base64Alphabet[base64Data[dataIndex + 1]];
            if(marker0 != 61 && marker1 != 61)
            {
                byte b3 = base64Alphabet[marker0];
                byte b4 = base64Alphabet[marker1];
                decodedData[encodedIndex] = (byte)(b1 << 2 | b2 >> 4);
                decodedData[encodedIndex + 1] = (byte)((b2 & 0xf) << 4 | b3 >> 2 & 0xf);
                decodedData[encodedIndex + 2] = (byte)(b3 << 6 | b4);
            } else
            if(marker0 == 61)
            {
                decodedData[encodedIndex] = (byte)(b1 << 2 | b2 >> 4);
            } else
            {
                byte b3 = base64Alphabet[marker0];
                decodedData[encodedIndex] = (byte)(b1 << 2 | b2 >> 4);
                decodedData[encodedIndex + 1] = (byte)((b2 & 0xf) << 4 | b3 >> 2 & 0xf);
            }
            encodedIndex += 3;
        }

        return decodedData;
    }

    static byte[] discardWhitespace(byte data[])
    {
        byte groomedData[] = new byte[data.length];
        int bytesCopied = 0;
        int i = 0;
        do
            if(i < data.length)
            {
                switch(data[i])
                {
                default:
                    groomedData[bytesCopied++] = data[i];
                    // fall through

                case 9: // '\t'
                case 10: // '\n'
                case 13: // '\r'
                case 32: // ' '
                    i++;
                    break;
                }
            } else
            {
                byte packedData[] = new byte[bytesCopied];
                System.arraycopy(groomedData, 0, packedData, 0, bytesCopied);
                return packedData;
            }
        while(true);
    }

    static byte[] discardNonBase64(byte data[])
    {
        byte groomedData[] = new byte[data.length];
        int bytesCopied = 0;
        for(int i = 0; i < data.length; i++)
            if(isBase64(data[i]))
                groomedData[bytesCopied++] = data[i];

        byte packedData[] = new byte[bytesCopied];
        System.arraycopy(groomedData, 0, packedData, 0, bytesCopied);
        return packedData;
    }

    public Object encode(Object pObject)
        throws IllegalArgumentException
    {
        if(!(pObject instanceof byte[]))
            throw new IllegalArgumentException("Parameter supplied to Base64 encode is not a byte[]");
        else
            return encode((byte[])pObject);
    }

    public byte[] encode(byte pArray[])
    {
        return encodeBase64(pArray, false);
    }

    static final int CHUNK_SIZE = 76;
    static final byte CHUNK_SEPARATOR[] = "\r\n".getBytes();
    static final int BASELENGTH = 255;
    static final int LOOKUPLENGTH = 64;
    static final int EIGHTBIT = 8;
    static final int SIXTEENBIT = 16;
    static final int TWENTYFOURBITGROUP = 24;
    static final int FOURBYTE = 4;
    static final int SIGN = -128;
    static final byte PAD = 61;
    private static byte base64Alphabet[];
    private static byte lookUpBase64Alphabet[];

    static 
    {
        base64Alphabet = new byte[255];
        lookUpBase64Alphabet = new byte[64];
        int i;
        for(i = 0; i < 255; i++)
            base64Alphabet[i] = -1;

        for(i = 90; i >= 65; i--)
            base64Alphabet[i] = (byte)(i - 65);

        for(i = 122; i >= 97; i--)
            base64Alphabet[i] = (byte)((i - 97) + 26);

        for(i = 57; i >= 48; i--)
            base64Alphabet[i] = (byte)((i - 48) + 52);

        base64Alphabet[43] = 62;
        base64Alphabet[47] = 63;
        for(i = 0; i <= 25; i++)
            lookUpBase64Alphabet[i] = (byte)(65 + i);

        i = 26;
        for(int j = 0; i <= 51; j++)
        {
            lookUpBase64Alphabet[i] = (byte)(97 + j);
            i++;
        }

        i = 52;
        for(int j = 0; i <= 61; j++)
        {
            lookUpBase64Alphabet[i] = (byte)(48 + j);
            i++;
        }

        lookUpBase64Alphabet[62] = 43;
        lookUpBase64Alphabet[63] = 47;
    }
}
