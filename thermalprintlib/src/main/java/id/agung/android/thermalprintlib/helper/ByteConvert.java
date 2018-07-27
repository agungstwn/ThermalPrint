package id.agung.android.thermalprintlib.helper;

import android.support.v4.view.MotionEventCompat;

/**
 * Created by agung on 26/07/18.
 */

public class ByteConvert {
    public static final int DEFAULT_BUFFER_LENGTH = 3072;
    public static final int DEFAULT_TABLE_LENGTH = 256;
    private static String[] convertTable = null;
    static final String lineSeperate = "\r\n";

    static {
        convertTable = new String[DEFAULT_TABLE_LENGTH];
        int i = 0;
        while (i < 16) {
            convertTable[i] = "0" + Integer.toHexString(i) + " ";
            i++;
        }
        while (i < DEFAULT_TABLE_LENGTH) {
            convertTable[i] = Integer.toHexString(i) + " ";
            i++;
        }
    }

    public static int byte2int2(byte[] convertByte, int offset, boolean isBigIndian) {
        int byte0;
        int byte1;
        if (convertByte[offset + 0] < 0) {
            byte0 = convertByte[offset + 0] + DEFAULT_TABLE_LENGTH;
        } else {
            byte0 = convertByte[offset + 0];
        }
        if (convertByte[offset + 1] < 0) {
            byte1 = convertByte[offset + 1] + DEFAULT_TABLE_LENGTH;
        } else {
            byte1 = convertByte[offset + 1];
        }
        if (isBigIndian) {
            return (byte0 * DEFAULT_TABLE_LENGTH) + byte1;
        }
        return (byte1 * DEFAULT_TABLE_LENGTH) + byte0;
    }

    public static int byte2int2(byte[] convertByte) {
        return byte2int2(convertByte, 0, true);
    }

    private static int byte2int2(byte[] convertByte, boolean isBigIndian) {
        return byte2int2(convertByte, 0, isBigIndian);
    }

    private static int byte2int2(byte[] convertByte, int offset) {
        return byte2int2(convertByte, offset, true);
    }

    public static int byte2int4(byte[] convertByte, int offset, boolean bigIndian) {
        int byte0;
        int byte1;
        int byte2;
        int byte3;

        if (convertByte[offset + 0] < 0) {
            byte0 = convertByte[offset + 0] + DEFAULT_TABLE_LENGTH;
        } else {
            byte0 = convertByte[offset + 0];
        }

        if (convertByte[offset + 1] < 0) {
            byte1 = convertByte[offset + 1] + DEFAULT_TABLE_LENGTH;
        } else {
            byte1 = convertByte[offset + 1];
        }

        if (convertByte[offset + 2] < 0) {
            byte2 = convertByte[offset + 2] + DEFAULT_TABLE_LENGTH;
        } else {
            byte2 = convertByte[offset + 2];
        }

        if (convertByte[offset + 3] < 0) {
            byte3 = convertByte[offset + 3] + DEFAULT_TABLE_LENGTH;
        } else {
            byte3 = convertByte[offset + 3];
        }

        if (bigIndian) {
            return (((byte0 << 24) + (byte1 << 16)) + (byte2 << 8)) + byte3;
        }
        return (((byte3 << 24) + (byte2 << 16)) + (byte1 << 8)) + byte0;
    }

    public static int byte2int4(byte[] convertByte) {
        return byte2int4(convertByte, 0, true);
    }

    public static int byte2int4(byte[] convertByte, boolean isBigIndian) {
        return byte2int4(convertByte, 0, isBigIndian);
    }

    public static int byte2int4(byte[] convertByte, int offset) {
        return byte2int4(convertByte, offset, true);
    }

    public static byte[] int2byte2(int value) {
        return int2byte2(value, true);
    }

    public static byte[] int2byte2(int value, boolean isBigIndian) {
        byte[] byteValue = new byte[2];
        if (isBigIndian) {
            byteValue[1] = (byte) (value & MotionEventCompat.ACTION_MASK);
            byteValue[0] = (byte) ((value & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >>> 8);
        } else {
            byteValue[0] = (byte) (value & MotionEventCompat.ACTION_MASK);
            byteValue[1] = (byte) ((value & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >>> 8);
        }
        return byteValue;
    }

    public static void int2byte2(int value, byte[] fillByte) {
        int2byte2(value, fillByte, 0, true);
    }

    public static void int2byte2(int value, byte[] fillByte, boolean isBigIndian) {
        int2byte2(value, fillByte, 0, isBigIndian);
    }

    public static void int2byte2(int value, byte[] fillByte, int offset) {
        int2byte2(value, fillByte, offset, true);
    }

    public static void int2byte2(int value, byte[] fillByte, int offset, boolean isBigIndian) {
        if (isBigIndian) {
            fillByte[offset + 1] = (byte) (value & MotionEventCompat.ACTION_MASK);
            fillByte[offset + 0] = (byte) ((value & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >>> 8);
            return;
        }
        fillByte[offset + 0] = (byte) (value & MotionEventCompat.ACTION_MASK);
        fillByte[offset + 1] = (byte) ((value & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >>> 8);
    }

    public static byte[] int2byte4(int value) {
        return int2byte4(value, true);
    }

    public static byte[] int2byte4(int value, boolean isBigIndian) {
        byte[] byteValue = new byte[4];
        if (isBigIndian) {
            byteValue[3] = (byte) (value & MotionEventCompat.ACTION_MASK);
            byteValue[2] = (byte) ((MotionEventCompat.ACTION_POINTER_INDEX_MASK & value) >>> 8);
            byteValue[1] = (byte) ((16711680 & value) >>> 16);
            byteValue[0] = (byte) ((value & -16777216) >>> 24);
        } else {
            byteValue[0] = (byte) (value & MotionEventCompat.ACTION_MASK);
            byteValue[1] = (byte) ((MotionEventCompat.ACTION_POINTER_INDEX_MASK & value) >>> 8);
            byteValue[2] = (byte) ((16711680 & value) >>> 16);
            byteValue[3] = (byte) ((value & -16777216) >>> 24);
        }
        return byteValue;
    }

    public static void int2byte4(int value, byte[] fillByte) {
        int2byte4(value, fillByte, 0, true);
    }

    public static void int2byte4(int value, byte[] fillByte, boolean isBigIndian) {
        int2byte4(value, fillByte, 0, isBigIndian);
    }

    public static void int2byte4(int value, byte[] fillByte, int offset) {
        int2byte4(value, fillByte, offset, true);
    }

    public static void int2byte4(int value, byte[] fillByte, int offset, boolean isBigIndian) {
        if (isBigIndian) {
            fillByte[offset + 3] = (byte) (value & MotionEventCompat.ACTION_MASK);
            fillByte[offset + 2] = (byte) ((value & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >>> 8);
            fillByte[offset + 1] = (byte) ((value & 16711680) >>> 16);
            fillByte[offset + 0] = (byte) ((value & -16777216) >>> 24);
            return;
        }
        fillByte[offset + 0] = (byte) (value & MotionEventCompat.ACTION_MASK);
        fillByte[offset + 1] = (byte) ((value & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >>> 8);
        fillByte[offset + 2] = (byte) ((value & 16711680) >>> 16);
        fillByte[offset + 3] = (byte) ((value & -16777216) >>> 24);
    }

    public static String buf2String(String info, byte[] buf) {
        return buf2String(info, buf, 0, buf.length, true);
    }

    public static String buf2String(String info, byte[] buf, boolean isOneLine16) {
        return buf2String(info, buf, 0, buf.length, isOneLine16);
    }

    public static String buf2String(String info, byte[] buf, int offset, int length) {
        return buf2String(info, buf, offset, length, true);
    }

    public static String buf2String(String info, byte[] buf, int offset, int length, boolean oneLine16) {
        StringBuffer sBuf = new StringBuffer();
        sBuf.append(info);
        int i = offset + 0;
        while (i < length + offset) {
            if (i % 16 == 0) {
                if (oneLine16) {
                    sBuf.append(lineSeperate);
                }
            } else if (i % 8 == 0 && oneLine16) {
                sBuf.append("   ");
            }
            sBuf.append(convertTable[buf[i] < 0 ? buf[i] + DEFAULT_TABLE_LENGTH : buf[i]]);
            i++;
        }
        return sBuf.toString();
    }

    public static String buf2StringWithoutSpace(byte[] buf, int offset, int length) {
        StringBuffer sBuf = new StringBuffer();
        for (int i = offset + 0; i < length + offset; i++) {
            sBuf.append(changeIntoHex(buf[i], false));
        }
        return sBuf.toString();
    }

    private static String changeIntoHex(byte b, boolean hasSpace) {
        int index = 0;
        if (b < 0) {
            index = b + DEFAULT_TABLE_LENGTH;
        } else {
            byte index2 = b;
        }
        String hex = "";
        if (index < 16) {
            if (hasSpace) {
                return "0" + Integer.toHexString(index) + " ";
            }
            return "0" + Integer.toHexString(index);
        } else if (hasSpace) {
            return Integer.toHexString(index) + " ";
        } else {
            return Integer.toHexString(index);
        }
    }
}
