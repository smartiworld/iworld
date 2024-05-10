package iworld.rpc.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author gq.cai
 * @version 1.0
 * @description
 * @date 2024/5/10 14:17
 */
public class StringUtils {
    
    private static final int OX03 = 0x03;
    private static final int OX04 = 0x04;
    private static final int OX05 = 0x05;
    private static final int OX06 = 0x06;
    private static final int OX07 = 0x07;
    private static final int OX08 = 0x08;
    private static final int OX09 = 0x09;
    private static final int OX10 = 0x10;
    private static final int OX0A = 0x0A;
    private static final int OX0B = 0x0B;
    private static final int OX0C = 0x0C;
    private static final int OX0D = 0x0D;
    private static final int OX0E = 0x0E;
    private static final int OX0F = 0x0F;
    private static final int OX3F = 0x3F;
    private static final int OXFF = 0xff;
    
    private static final char ZERO_CHAR = '0';
    private static final char ONE_CHAR = '1';
    private static final char TWO_CHAR = '2';
    
    private static final String WHITE_SPACE = "　";
    private static final String NULL_STRING = "null";
    
    private static final String NEW_LINE = "\n";
    
    private static final int ZERO = 0;
    private static final int ONE = 1;
    private static final int TWO = 2;
    private static final int THREE = 3;
    private static final int FOUR = 4;
    private static final int FIVE = 5;
    private static final int SIX = 6;
    private static final int SEVEN = 7;
    private static final int TEN = 10;
    private static final int SIXTEEN = 16;
    private static final int SECOND_OF_MINUTE = 60;
    private static final int MILLION_SECOND_OF_SECOND = 1000;
    private static final int MINUTE_OF_HOUR = 60;
    private static final int HOUR_OF_DAY = 24;
    private static final int ONE_THOUSENT_NINE_HUNDRED = 1900;
    private static final int OXF0 = 0xf0;
    private static final int OXC0 = 0xc0;
    private static final int ONE_HUNDRED_EIGTH = 128;
    private static final int LENGTH = 8;
    private static final double ONE_POINT_THREE = 1.3;
    
    private static MessageDigest digest = null;
    private static final Logger LOGGER = LoggerFactory.getLogger(StringUtils.class);
    
    /**
     * Constants used by escapeHTMLTags
     */
    private static final char[] QUOTE_ENCODE = "&quot;".toCharArray();
    private static final char[] AMP_ENCODE = "&amp;".toCharArray();
    private static final char[] LT_ENCODE = "&lt;".toCharArray();
    private static final char[] GT_ENCODE = "&gt;".toCharArray();
    
    /**
     * 虚拟号码
     */
    private static Pattern p4 = Pattern.compile("(170)\\d{8}");
    
    /**
     * 171号码
     */
    private static Pattern p5 = Pattern.compile("(171)\\d{8}");
    
    private static final int FILL_CHAR = '=';
    private static final String CVT = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
            + "abcdefghijklmnopqrstuvwxyz" + "0123456789+/";
    
    /**
     * 用MD5给字符串加密
     *
     * @param data 要加密的串
     * @return 加密后的结果
     */
    public static synchronized String hash(String data) throws NoSuchAlgorithmException {
        if (data == null) {
            return "";
        }
        
        if (digest == null) {
            digest = MessageDigest.getInstance("MD5");
        }
        
        digest.update(data.getBytes(Charset.forName("UTF-8")));
        return encodeHex(digest.digest());
    }
    
    /**
     * 将字节数组转化为字符串
     *
     * @param bytes 字符数组
     * @return java.lang.String
     */
    public static String encodeHex(byte[] bytes) {
        StringBuilder buf = new StringBuilder(bytes.length * 2);
        int i;
        
        for (i = 0; i < bytes.length; i++) {
            if (((int) bytes[i] & OXFF) < OX10) {
                buf.append("0");
            }
            buf.append(Long.toString((int) bytes[i] & OXFF, SIXTEEN));
        }
        return buf.toString();
    }
    
    /**
     * Turns a hex encoded string into a byte array. It is specifically meant to
     * "reverse" the toHex(byte[]) method.
     *
     * @param hex a hex encoded String to transform into a byte array.
     * @return a byte array representing the hex String[
     */
    public static byte[] decodeHex(String hex) {
        char[] chars = hex.toCharArray();
        byte[] bytes = new byte[chars.length / 2];
        int byteCount = 0;
        for (int i = 0; i < chars.length; i += TWO) {
            byte newByte = 0x00;
            newByte |= hexCharToByte(chars[i]);
            newByte <<= FOUR;
            newByte |= hexCharToByte(chars[i + 1]);
            bytes[byteCount] = newByte;
            byteCount++;
        }
        return bytes;
    }
    
    /**
     * Returns the the byte value of a hexadecmical char (0-f). It's assumed
     * that the hexidecimal chars are lower case as appropriate.
     *
     * @param ch a hexedicmal character (0-f)
     * @return the byte value of the character (0x00-0x0F)
     */
    private static byte hexCharToByte(char ch) {
        switch (ch) {
            case '0':
                return 0x00;
            case '1':
                return 0x01;
            case '2':
                return 0x02;
            case '3':
                return OX03;
            case '4':
                return OX04;
            case '5':
                return OX05;
            case '6':
                return OX06;
            case '7':
                return OX07;
            case '8':
                return OX08;
            case '9':
                return OX09;
            case 'a':
                return OX0A;
            case 'b':
                return OX0B;
            case 'c':
                return OX0C;
            case 'd':
                return OX0D;
            case 'e':
                return OX0E;
            case 'f':
                return OX0F;
            default:
                return 0x00;
        }
    }
    
    // *********************************************************************
    // * Base64 - a simple base64 encoder and decoder.
    // *
    // * Copyright (c) 1999, Bob Withers - bwit@pobox.com
    // *
    // * This code may be freely used for any purpose, either personal
    // * or commercial, provided the authors copyright notice remains
    // * intact.
    // *********************************************************************
    
    /**
     * Encodes a String as a base64 String.
     *
     * @param data a String to encode.
     * @return a base64 encoded String.
     */
    public static String encodeBase64(String data) {
        return encodeBase64(data.getBytes(Charset.forName("UTF-8")));
    }
    
    /**
     * Encodes a byte array into a base64 String.
     *
     * @param data a byte array to encode.
     * @return a base64 encode String.
     */
    public static String encodeBase64(byte[] data) {
        int c;
        int len = data.length;
        StringBuilder ret = new StringBuilder(((len / THREE) + 1) * FOUR);
        for (int i = 0; i < len; ++i) {
            c = (data[i] >> 2) & OX3F;
            ret.append(CVT.charAt(c));
            c = (data[i] << FOUR) & OX3F;
            if (++i < len) {
                c |= (data[i] >> FOUR) & OX0F;
            }
            
            ret.append(CVT.charAt(c));
            if (i < len) {
                c = (data[i] << 2) & OX3F;
                if (++i < len) {
                    c |= (data[i] >> SIX) & OX03;
                }
                
                ret.append(CVT.charAt(c));
            } else {
                ++i;
                ret.append((char) FILL_CHAR);
            }
            
            if (i < len) {
                c = data[i] & OX3F;
                ret.append(CVT.charAt(c));
            } else {
                ret.append((char) FILL_CHAR);
            }
        }
        return ret.toString();
    }
    
    /**
     * Decodes a base64 String.
     *
     * @param data a base64 encoded String to decode.
     */
    public static String decodeBase64(String data) {
        return decodeBase64(data.getBytes(Charset.forName("UTF-8")));
    }
    
    /**
     * Decodes a base64 array of bytes.
     *
     * @param data a base64 encode byte array to decode.
     */
    public static String decodeBase64(byte[] data) {
        int c, c1;
        int len = data.length;
        StringBuilder ret = new StringBuilder((len * THREE) / FOUR);
        for (int i = 0; i < len; ++i) {
            c = CVT.indexOf(data[i]);
            ++i;
            c1 = CVT.indexOf(data[i]);
            c = ((c << 2) | ((c1 >> FOUR) & OX03));
            ret.append((char) c);
            if (++i < len) {
                c = data[i];
                if (FILL_CHAR == c) {
                    break;
                }
                
                c = CVT.indexOf((char) c);
                c1 = ((c1 << FOUR) & OXF0) | ((c >> 2) & OXF0);
                ret.append((char) c1);
            }
            
            if (++i < len) {
                c1 = data[i];
                if (FILL_CHAR == c1) {
                    break;
                }
                
                c1 = CVT.indexOf((char) c1);
                c = ((c << SIX) & OXC0) | c1;
                ret.append((char) c);
            }
        }
        return ret.toString();
    }
    
    public static String replaceWord(String str, String newStr, int len) {
        
        if (str == null) {
            return str;
        }
        
        char[] charArray = str.toCharArray();
        int sLen = str.length();
        
        if (str.length() < len) {
            return str;
        }
        
        return str.substring(0, len - 1) + newStr + str.substring(len, sLen);
        
    }
    
    /**
     * 去除字符串中的 空字符
     * 注：  \n 回车(\u000a)
     * \t 水平制表符(\u0009)
     * \s 空格(\u0008)
     * \r 换行(\u000d)
     *
     * @param str          源字符串
     * @param includeSpace 是否处理空格
     * @return 转换后的结果
     */
    public static String replaceBlank(String str, boolean includeSpace) {
        String dest = "";
        if (str != null) {
            String regexIncludeSpace = "\\s*|\t|\r|\n";
            String regexExcludeSpace = "\\t|\r|\n";
            
            String regex;
            if (includeSpace) {
                regex = regexIncludeSpace;
            } else {
                regex = regexExcludeSpace;
            }
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(str);
            
            dest = m.replaceAll("");
        }
        return dest;
    }
    
    
    public static int wordAtNum(String str, int num) {
        if ((str == null) || ("".equalsIgnoreCase(str))) {
            return 0;
        }
        
        char ch;
        ch = str.charAt(num - 1);
        if (ch == ONE_CHAR) {
            return 1;
        } else if (ch == TWO_CHAR) {
            return 2;
        } else if (ch == ZERO_CHAR) {
            return 0;
        } else {
            return 0;
        }
    }
    
    /**
     * 说明: 将字符串变量由null转换为""串
     *
     * @param str 需要处理的字符串
     * @return 处理后的字符串
     */
    public static String nullToStr(String str) {
        return str == null ? "" : str.trim();
    }
    
    /**
     * @param oldStr    原字符串
     * @param position  要被替换的字符位置从0开始
     * @param character 替换的字符
     * @return 替换后的字符串
     */
    public static String replaceChar(String oldStr, int position, char character) {
        
        StringBuilder stringBuilder = new StringBuilder(oldStr);
        stringBuilder.setCharAt(position, character);
        return stringBuilder.toString();
    }
    
    /**
     * 说明: ISO_8559_1字符转换为GBK字符
     *
     * @param strIn 需要转换的字符
     * @return 转换后的GBK字符
     */
    public static String isoToGbk(String strIn) {
        String strOut = null;
        if (strIn == null) {
            return "";
        }
        try {
            byte[] b = strIn.getBytes("ISO8859_1");
            strOut = new String(b, "GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        
        return strOut;
    }
    
    /**
     * 说明: GBK字符转换为ISO_8559_1字符
     *
     * @param strIn 需要转换的GBK字符
     * @return 转换后的ISO_8559_1字符
     */
    public static String gbkToIso(String strIn) {
        byte[] b;
        String strOut = null;
        if (strIn == null) {
            return "";
        }
        try {
            b = strIn.getBytes("GBK");
            
            strOut = new String(b, "ISO8859_1");
            
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        
        return strOut;
    }
    
    /**
     * 说明: ISO_8559_1字符转换为GB2312字符
     *
     * @param strIn 需要转换的字符
     * @return 转换后的GB2312字符
     */
    public static String isoToGB2312(String strIn) {
        String strOut = null;
        if (strIn == null) {
            return "";
        }
        try {
            byte[] b = strIn.getBytes("ISO8859_1");
            strOut = new String(b, "GB2312");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        
        return strOut;
    }
    
    /**
     * <p>Checks if a String is empty ("") or null.</p>
     *
     * <pre>
     * StringUtils.isEmpty(null)      = true
     * StringUtils.isEmpty("")        = true
     * StringUtils.isEmpty(" ")       = false
     * StringUtils.isEmpty("bob")     = false
     * StringUtils.isEmpty("  bob  ") = false
     * </pre>
     *
     * <p>NOTE: This method changed in Lang version 2.0.
     * It no longer trims the String.
     * That functionality is available in isBlank().</p>
     *
     * @param str the String to check, may be null
     * @return <code>true</code> if the String is empty or null
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }
    
    
    /**
     * <p>Checks if a String is whitespace, empty ("") or null.</p>
     *
     * <pre>
     * StringUtils.isBlank(null)      = true
     * StringUtils.isBlank("")        = true
     * StringUtils.isBlank(" ")       = true
     * StringUtils.isBlank("bob")     = false
     * StringUtils.isBlank("  bob  ") = false
     * </pre>
     *
     * @param str the String to check, may be null
     * @return <code>true</code> if the String is null, empty or whitespace
     * @since 2.0
     */
    public static boolean isBlank(String str) {
        if (str == null || str.length() == 0) {
            return true;
        }
        for (int i = 0; i < str.length(); i++) {
            if ((!Character.isWhitespace(str.charAt(i)))) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean equals(String str1, String str2) {
        return str1 == null ? str2 == null : str1.equals(str2);
    }
    
    /**
     * 说明: GB2312字符转换为ISO_8559_1字符
     *
     * @param strIn 需要转换的GB2312字符
     * @return 转换后的ISO_8559_1字符
     */
    public static String gb2312ToIso(String strIn) {
        byte[] b;
        String strOut = null;
        if (strIn == null) {
            return "";
        }
        try {
            b = strIn.getBytes("GB2312");
            
            strOut = new String(b, "ISO8859_1");
            
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        
        return strOut;
    }
    
    public static String gb2312ToUTF(String strIn) {
        byte[] b;
        String strOut = null;
        if (strIn == null) {
            return "";
        }
        try {
            b = strIn.getBytes("GB2312");
            
            strOut = new String(b, "UTF-16");
            
        } catch (UnsupportedEncodingException e) {
            LOGGER.error(e.getMessage(), e);
        }
        
        return strOut;
    }
    
    public static String utfToGB2312(String strIn) {
        byte[] b;
        String strOut = null;
        if (strIn == null) {
            return "";
        }
        try {
            b = strIn.getBytes("UTF-16");
            
            strOut = new String(b, "GB2312");
            
        } catch (UnsupportedEncodingException e) {
            LOGGER.error(e.getMessage(), e);
        }
        
        return strOut;
    }
    
    /**
     * Replaces all instances of oldString with newString in line.
     *
     * @param line      the String to search to perform replacements on
     * @param oldString the String that should be replaced by newString
     * @param newString the String that will replace all instances of oldString
     * @return a String will all instances of oldString replaced by newString
     */
    public static String replace(String line, String oldString,
                                 String newString) {
        if (line == null) {
            return null;
        }
        int i = 0;
        i = line.indexOf(oldString, i);
        if (i >= 0) {
            char[] line2 = line.toCharArray();
            char[] newString2 = newString.toCharArray();
            int oLength = oldString.length();
            StringBuilder buf = new StringBuilder(line2.length);
            buf.append(line2, 0, i).append(newString2);
            i += oLength;
            int j = i;
            while ((i = line.indexOf(oldString, i)) > 0) {
                buf.append(line2, j, i - j).append(newString2);
                i += oLength;
                j = i;
            }
            buf.append(line2, j, line2.length - j);
            return buf.toString();
        }
        return line;
    }
    
    /**
     * Replaces all instances of oldString with newString in line with the added
     * feature that matches of newString in oldString ignore case.
     *
     * @param line      the String to search to perform replacements on
     * @param oldString the String that should be replaced by newString
     * @param newString the String that will replace all instances of oldString
     * @return a String will all instances of oldString replaced by newString
     */
    public static String replaceIgnoreCase(String line, String oldString,
                                           String newString) {
        if (line == null) {
            return null;
        }
        String lcLine = line.toLowerCase();
        String lcOldString = oldString.toLowerCase();
        int i = 0;
        i = lcLine.indexOf(lcOldString, i);
        if (i >= 0) {
            char[] line2 = line.toCharArray();
            char[] newString2 = newString.toCharArray();
            int oLength = oldString.length();
            StringBuilder buf = new StringBuilder(line2.length);
            buf.append(line2, 0, i).append(newString2);
            i += oLength;
            int j = i;
            while ((i = lcLine.indexOf(lcOldString, i)) > 0) {
                buf.append(line2, j, i - j).append(newString2);
                i += oLength;
                j = i;
            }
            buf.append(line2, j, line2.length - j);
            return buf.toString();
        }
        return line;
    }
    
    /**
     * Replaces all instances of oldString with newString in line with the added
     * feature that matches of newString in oldString ignore case. The count
     * parameter is set to the number of replaces performed.
     *
     * @param line      the String to search to perform replacements on
     * @param oldString the String that should be replaced by newString
     * @param newString the String that will replace all instances of oldString
     * @param count     a value that will be updated with the number of replaces
     *                  performed.
     * @return a String will all instances of oldString replaced by newString
     */
    public static String replaceIgnoreCase(String line, String oldString, String newString, int[] count) {
        if (line == null) {
            return null;
        }
        String lcLine = line.toLowerCase();
        String lcOldString = oldString.toLowerCase();
        int i = 0;
        i = lcLine.indexOf(lcOldString, i);
        if (i >= 0) {
            int counter = 0;
            char[] line2 = line.toCharArray();
            char[] newString2 = newString.toCharArray();
            int oLength = oldString.length();
            StringBuilder buf = new StringBuilder(line2.length);
            buf.append(line2, 0, i).append(newString2);
            i += oLength;
            int j = i;
            while ((i = lcLine.indexOf(lcOldString, i)) > 0) {
                counter++;
                buf.append(line2, j, i - j).append(newString2);
                i += oLength;
                j = i;
            }
            buf.append(line2, j, line2.length - j);
            count[0] = counter;
            return buf.toString();
        }
        return line;
    }
    
    /**
     * Replaces all instances of oldString with newString in line. The count
     * Integer is updated with number of replaces.
     *
     * @param line      the String to search to perform replacements on
     * @param oldString the String that should be replaced by newString
     * @param newString the String that will replace all instances of oldString
     * @return a String will all instances of oldString replaced by newString
     */
    public static String replace(String line, String oldString, String newString, int[] count) {
        if (line == null) {
            return null;
        }
        int i = 0;
        i = line.indexOf(oldString, i);
        if (i >= 0) {
            int counter = 0;
            counter++;
            char[] line2 = line.toCharArray();
            char[] newString2 = newString.toCharArray();
            int oLength = oldString.length();
            StringBuilder buf = new StringBuilder(line2.length);
            buf.append(line2, 0, i).append(newString2);
            i += oLength;
            int j = i;
            while ((i = line.indexOf(oldString, i)) > 0) {
                counter++;
                buf.append(line2, j, i - j).append(newString2);
                i += oLength;
                j = i;
            }
            buf.append(line2, j, line2.length - j);
            count[0] = counter;
            return buf.toString();
        }
        return line;
    }
    
    /**
     * 说明：对字符串进行分割
     *
     * @param line      源字符串
     * @param newString 分割字符串
     * @return Vector
     */
    public static String[] split(String line, String newString) {
        int begin = 0;
        int end;
        ArrayList strList = new ArrayList();
        if (line == null) {
            return null;
        }
        if ("".equals(newString)) {
            int i;
            for (i = 0; i < line.length(); i++) {
                strList.add(line.substring(i, i + 1));
            }
            return (String[]) strList.toArray(new String[strList.size()]);
        }
        
        end = line.indexOf(newString);
        if (end == -1) {
            strList.add(line);
            return (String[]) strList.toArray(new String[strList.size()]);
        } else {
            while (end >= 0) {
                strList.add(line.substring(begin, end));
                begin = end + newString.length();
                end = line.indexOf(newString, begin);
            }
            strList.add(line.substring(begin, line.length()));
            return (String[]) strList.toArray(new String[strList.size()]);
        }
    }
    
    /**
     * 二进制数转化成十进制数,目前只支持正数
     *
     * @param str 一个由0，1组成的字符串
     * @return 转化后的十进制数值, 若返回-1则表明转化失败，有可能是输入字符串 不合法
     */
    public static int binsToDecs(String str) {
        int ret = 0;
        int v = 1;
        for (int i = 0; i < str.length(); i++) {
            if (i != 0) {
                v = v * 2;
            }
            if (str.charAt(i) == '0') {
                continue;
            } else if (str.charAt(i) == '1') {
                ret = ret + v;
            } else {
                return -1;
            }
        }
        return ret;
    }
    
    /**
     * Unescapes the String by converting XML escape sequences back into normal
     * characters.
     *
     * @param stringParam the string to unescape.
     * @return the string with appropriate characters unescaped.
     */
    public static String unescapeFromXML(String stringParam) {
        String string = stringParam;
        string = replace(string, "&lt;", "<");
        string = replace(string, "&gt;", ">");
        string = replace(string, "&quot;", "\"");
        return replace(string, "&amp;", "&");
    }
    
    /**
     * Escapes all necessary characters in the String so that it can be used in
     * an XML doc.
     *
     * @param string1 the string to escape.
     * @return the string with appropriate characters escaped.
     */
    public static String escapeForXML(String string1) {
        String string = processXMLMultLineText(string1);
        if (string == null) {
            return null;
        }
        char ch;
        int i = 0;
        int last = 0;
        char[] input = string.toCharArray();
        int len = input.length;
        StringBuilder out = new StringBuilder((int) (len * ONE_POINT_THREE));
        for (; i < len; i++) {
            ch = input[i];
            if (ch > '>') {
                continue;
            } else if (ch == '<') {
                if (i > last) {
                    out.append(input, last, i - last);
                }
                last = i + 1;
                out.append(LT_ENCODE);
            } else if (ch == '&') {
                if (i > last) {
                    out.append(input, last, i - last);
                }
                last = i + 1;
                out.append(AMP_ENCODE);
            } else if (ch == '"') {
                if (i > last) {
                    out.append(input, last, i - last);
                }
                last = i + 1;
                out.append(QUOTE_ENCODE);
            }
        }
        if (last == 0) {
            return string;
        }
        if (i > last) {
            out.append(input, last, i - last);
        }
        return out.toString();
        // return string;
    }
    
    /**
     * 对字符串进行截取 int subnum 要保留的字符数；String strCmd 要截取的字符串 返回值为截取后的字符串
     */
    public static String subStr(int subnumParam, String strCmd) {
        int subnum = subnumParam;
        String tempSub = "";
        if (subnum <= 0) {
            subnum = FIVE;
        }
        for (int i = 0; i < strCmd.length(); i++) {
            String tmpstr = strCmd.substring(i, i + 1);
            int codenum = tmpstr.hashCode();
            if (codenum >= ONE_HUNDRED_EIGTH) {
                subnum = subnum - 2;
            } else {
                subnum = subnum - 1;
            }
            tempSub += tmpstr;
            if (subnum <= 0) {
                tempSub += "...";
                break;
            }
        }
        return tempSub;
    }
    
    /**
     * 在新闻、展品介绍等多个地方，出现了多行文本，为便于系统统一处理，现提供统一接口， 在页面显示时，进行转换。
     *
     * @param strMultLineText GB2312/GBK编码
     * @return GB2312/GBK编码
     */
    public StringBuffer processMultLineText(StringBuffer strMultLineText) {
        String formatStr = StringUtils
                .replace(strMultLineText.toString(), "\\n", "\n");
        String beforeStr, nextStr, showStr;
        
        formatStr = StringUtils.replace(formatStr, "\n\r", "\n");
        beforeStr = formatStr;
        int index, before = 0;
        String head = "";
        
        index = beforeStr.indexOf("\n");
        if (index >= 0) {
            showStr = beforeStr.substring(0, index);
            beforeStr = beforeStr.substring(index);
            if (showStr.length() > FOUR) {
                head = showStr.substring(0, FOUR).trim();
                showStr = showStr.substring(FOUR);
                head = StringUtils.replace(head, "　", "");
            }
            showStr = "&nbsp;&nbsp;&nbsp;&nbsp;" + head + showStr;
        } else {
            showStr = beforeStr;
            if (showStr.length() > FOUR) {
                head = showStr.substring(0, FOUR).trim();
                showStr = showStr.substring(FOUR);
                head = StringUtils.replace(head, "　", "");
            }
            showStr = "&nbsp;&nbsp;&nbsp;&nbsp;" + head + showStr;
            beforeStr = "";
        }
        while ((index = beforeStr.indexOf(NEW_LINE)) >= 0) {
            showStr = showStr + beforeStr.substring(before, index);
            showStr = showStr + "<br>&nbsp;&nbsp;&nbsp;&nbsp;";
            nextStr = beforeStr.substring(index + 1).trim();
            while (nextStr.startsWith(WHITE_SPACE)) {
                nextStr = nextStr.substring(2);
            }
            beforeStr = nextStr;
        }
        showStr = showStr + beforeStr;
        return new StringBuffer(showStr);
    }
    
    public static String processXMLMultLineText(String strMultLineText) {
        String formatStr = replace(strMultLineText, "\\n", "\n");
        String beforeStr = "", nextStr = "", showStr = "";
        
        formatStr = replace(formatStr, "\r", "");
        formatStr = replace(formatStr, "\n", "<br>&nbsp;&nbsp;&nbsp;&nbsp;");
        return formatStr;
    }
    
    public static String processPageMultLineText(String strMultLineText) {
        String formatStr = replace(strMultLineText, "\\n", "\n");
        String beforeStr = "", nextStr = "", showStr = "";
        
        formatStr = replace(formatStr, "\r", "");
        formatStr = replace(formatStr, "\n", "<br>");
        
        return formatStr;
    }
    
    /**
     * 根据日期起始时间,得到每个月份 格式:2009-10
     */
    public static Vector<String> getDatePeriod(String sDate, String eDate) {
        Vector<String> v = new Vector<String>();
        Calendar ecalendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        
        String[] sDates = sDate.split("-");
        String[] eDates = eDate.split("-");
        
        if (sDate.equals(eDate)) {
            ecalendar.set(Calendar.YEAR, Integer.parseInt(sDates[0]));
            ecalendar.set(Calendar.MONTH, Integer.parseInt(sDates[1]) - 1);
            ecalendar.set(Calendar.DATE, 1);
            v.add(sdf.format(ecalendar.getTime()));
        } else {
            
            int syear = Integer.parseInt(sDates[0]);
            int smonth = Integer.parseInt(sDates[1]) - 1;
            
            ecalendar.set(Calendar.YEAR, Integer.parseInt(eDates[0]));
            ecalendar.set(Calendar.MONTH, Integer.parseInt(eDates[1]));
            ecalendar.set(Calendar.DATE, 1);
            String endDate = sdf.format(ecalendar.getTime());
            
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, syear);
            calendar.set(Calendar.MONTH, smonth);
            calendar.set(Calendar.DATE, 1);
            v.add(sdf.format(calendar.getTime()));
            
            while (true) {
                calendar.add(Calendar.MONTH, 1);
                if (sdf.format(calendar.getTime()).equals(endDate)) {
                    break;
                }
                v.add(sdf.format(calendar.getTime()));
            }
            
        }
        return v;
    }
    
    /**
     * 获得指定日期及指定天数之内的所有日期列表
     *
     * @param pDate 指定日期 格式:yyyy-MM-dd
     * @param count 取指定日期后的count天
     * @return
     * @throws ParseException
     */
    public static Vector<String> getDatePeriodDay(String pDate, int count)
            throws ParseException {
        Vector<String> v = new Vector<String>();
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sdf.parse(pDate));
        v.add(sdf.format(calendar.getTime()));
        
        for (int i = 0; i < count - 1; i++) {
            calendar.add(Calendar.DATE, 1);
            v.add(sdf.format(calendar.getTime()));
        }
        
        return v;
    }
    
    /**
     * 获得指定日期内的所有日期列表
     *
     * @param sDate 指定开始日期 格式:yyyy-MM-dd
     * @param sDate 指定开始日期 格式:yyyy-MM-dd
     * @return String[]
     * @throws ParseException
     */
    public static String[] getDatePeriodDay(String sDate, String eDate)
            throws ParseException {
        if (dateCompare(sDate, eDate)) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar.setTime(sdf.parse(sDate));
        long l1 = calendar.getTimeInMillis();
        calendar2.setTime(sdf.parse(eDate));
        long l2 = calendar2.getTimeInMillis();
        // 计算天数
        long days = (l2 - l1) / (HOUR_OF_DAY * MINUTE_OF_HOUR * SECOND_OF_MINUTE * MILLION_SECOND_OF_SECOND) + 1;
        
        String[] dates = new String[(int) days];
        dates[0] = (sdf.format(calendar.getTime()));
        for (int i = 1; i < days; i++) {
            calendar.add(Calendar.DATE, 1);
            dates[i] = (sdf.format(calendar.getTime()));
        }
        return dates;
    }
    
    /**
     * 比较日期大小
     *
     * @param compareDate   起始日期
     * @param toCompareDate 比较日期
     * @return 比较日期是否早于起始日期
     */
    public static boolean dateCompare(String compareDate, String toCompareDate) {
        boolean comResult = false;
        
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
        
        try {
            Date comDate = parser.parse(compareDate);
            Date toComDate = parser.parse(toCompareDate);
            
            if (comDate.after(toComDate)) {
                comResult = true;
            }
        } catch (ParseException e) {
            LOGGER.error(e.getMessage(), e);
        }
        
        return comResult;
    }
    
    public static String checkEmpty(String s) {
        return checkEmpty(s, null);
    }
    
    public static String checkEmpty(String s, String defaultValue) {
        if (s == null || NULL_STRING.equals(s) || "".equals(s.trim())) {
            return defaultValue;
        } else {
            return s;
        }
    }
    
    public static String castString(Integer s) {
        if (s == null) {
            return "";
        } else {
            return String.valueOf(s);
        }
    }
    
    public static String castString(Double s) {
        if (s == null) {
            return "";
        } else {
            return String.valueOf(s);
        }
    }
    
    public static Integer checkEmpty(Integer s) {
        return checkEmpty(s, null);
    }
    
    public static Integer checkEmpty(Integer s, Integer defaultValue) {
        if (s == null) {
            return defaultValue;
        } else {
            return s;
        }
    }
    
    public static int checkEmpty(Integer s, int defalutValue) {
        if (s == null) {
            return defalutValue;
        } else {
            return s.intValue();
        }
    }
    
    public static Double checkEmpty(Double s) {
        return checkEmpty(s, null);
    }
    
    public static Double checkEmpty(Double s, Double defaultValue) {
        if (s == null) {
            return defaultValue;
        } else {
            return s;
        }
    }
    
    public static double checkEmpty(Double s, double defaultValue) {
        if (s == null) {
            return defaultValue;
        } else {
            return s.doubleValue();
        }
    }
    
    public static String toCN(String s) {
        
        return checkNull(s);
    }
    
    public static String checkNull(Object s) {
        return checkNull(s, "");
    }
    
    public static String checkNull(Object s, String defaultValue) {
        if (s == null) {
            return defaultValue;
        } else {
            return s.toString().trim();
        }
    }
    
    /**
     * 获得指定格式的当前日期
     *
     * @param format 默认yyyy-MM-dd
     * @return 返回格式化后的日期字符串
     */
    public static String getCurrentDate(String format) {
        String f;
        if (format == null || "".equals(format)) {
            f = "yyyy-MM-dd";
        } else {
            f = format;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(f);
        return sdf.format(new Date());
    }
    
    public static String getDateFormat(String format, String dateTime) {
        String dateTimeFormat = "";
        String f;
        if (format == null || "".equals(format)) {
            f = "yyyy-MM-dd";
        } else {
            f = format;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(f);
        try {
            dateTimeFormat = sdf.format(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(dateTime));
        } catch (ParseException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return dateTimeFormat;
    }
    
    public static String getStringByEncoding(String string, String encoding) {
        String str = "";
        String encode = "utf-8";
        if (encoding != null && !"".equals(encoding)) {
            encode = encoding;
        }
        if (string != null && !"".equals(string)) {
            try {
                str = new String(string.getBytes("iso8859-1"), encode);
            } catch (UnsupportedEncodingException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
        return str;
    }
    
    /**
     * 根据年月返回当月所有日期列表
     *
     * @param year  年
     * @param month 月
     * @return 日期列表
     */
    public static Vector<String> createDateArray(int year, int month) {
        Vector<String> v = new Vector<String>();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ParsePosition pos = new ParsePosition(0);
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(year - ONE_THOUSENT_NINE_HUNDRED, month - 1, TEN));
        int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        String startTime = String.valueOf(year) + "-"
                + (month < TEN ? "0" + month : month) + "-01";
        Date st = formatter.parse(startTime, pos);
        for (int i = 0; i < maxDay; i++) {
            if (i > 0) {
                st.setDate(st.getDate() + 1);
            }
            v.add(formatter.format(st));
        }
        return v;
    }
    
    /**
     * 取得某年某月天数
     *
     * @param date 日期字符
     * @return 某年某月天数
     */
    public static int getDaysByMonth(String date) {
        int days = -1;
        Calendar cal = new GregorianCalendar();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        try {
            cal.setTime(sdf.parse(date));
            days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        } catch (ParseException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return days;
    }
    
    public static Date parseDate(String str, String parsePattern) {
        if (str == null || "".equals(str)) {
            return null;
        }
        SimpleDateFormat parser = new SimpleDateFormat(parsePattern);
        ParsePosition pos = new ParsePosition(0);
        Date date = parser.parse(str, pos);
        if (date != null) {
            return date;
        }
        throw new RuntimeException("日期错误: " + str);
    }
    
    /**
     * 返回指定日期加days天后的日期，yyyy-MM-DD
     *
     * @param dateStrParam 指定的日期
     * @param days         增加的天数
     * @return 计算后的结果
     */
    public static String getDateAdd(String dateStrParam, int days) {
        String date;
        String dateStr = dateStrParam;
        if (dateStr == null) {
            return null;
        }
        if (dateStr.length() > TEN) {
            dateStr = dateStr.substring(0, TEN);
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(parseDate(dateStr, "yyyy-MM-dd"));
        cal.add(Calendar.DAY_OF_MONTH, days);
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
        date = parser.format(cal.getTime());
        return date;
    }
    
    public static String getDateAdd(String dateStrParam, int days,
                                    String parsePattern) {
        String date;
        String dateStr = dateStrParam;
        if (dateStr == null) {
            return null;
        }
        if (dateStr.length() > TEN && parsePattern.length() <= TEN) {
            dateStr = dateStr.substring(0, TEN);
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(parseDate(dateStr, parsePattern));
        cal.add(Calendar.DAY_OF_MONTH, days);
        SimpleDateFormat parser = new SimpleDateFormat(parsePattern);
        date = parser.format(cal.getTime());
        return date;
    }
    
    public static Integer parseInt(String str) {
        return parseInt(str, null);
    }
    
    public static Integer parseInt(String str, Integer defValue) {
        if (str == null || "".equals(str.trim()) || NULL_STRING.equals(str)) {
            return defValue;
        } else {
            return Integer.parseInt(str);
        }
    }
    
    public static Double parseDouble(String str) {
        return parseDouble(str, null);
    }
    
    public static Double parseDouble(String str, Double defValue) {
        if (str == null || "".equals(str.trim()) || NULL_STRING.equals(str)) {
            return defValue;
        } else {
            return Double.parseDouble(str);
        }
    }
    
    public static Long parseLong(String str) {
        return parseLong(str, null);
    }
    
    public static Long parseLong(String str, Long defValue) {
        if (str == null || "".equals(str.trim()) || NULL_STRING.equals(str)) {
            return defValue;
        } else {
            return Long.parseLong(str);
        }
    }
    
    public static boolean parseBoolean(String str) {
        return !(isEmpty(str) || "null".equals(str)) && ("1".equals(str) || "true".equals(str));
    }
    
    public static String formatDouble(Double doubleValue) {
        return formatDouble(doubleValue, null);
    }
    
    public static String formatDouble(Double doubleValue, String patten) {
        DecimalFormat df;
        if (patten == null) {
            df = new DecimalFormat("#");
        } else {
            df = new DecimalFormat(patten);
        }
        return df.format(doubleValue);
    }
    
    
    /**
     * 转换字符串为金融数字
     *
     * @param param 要转换的字符串
     * @return 转换后的结果
     * @since 1.0.0
     */
    public static BigDecimal str2BigDecimal(String param) {
        BigDecimal dbNumber = new BigDecimal(-1);
        try {
            if (!isEmpty(param)) {
                dbNumber = new BigDecimal(param);
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        
        return dbNumber;
    }
    
    /**
     * 将字符串转换成数字
     *
     * @param str
     * @return
     */
    public static int convertInt(String str) {
        
        int number = 0;
        // 正则表达式正数匹配
        String matchStr = "^([+-]?)\\d*\\.?\\d+$";
        try {
            if (str != null && str.matches(matchStr)) {
                number = Integer.parseInt(str);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return number;
    }
    
    /**
     * 将数组字符串串连用于查询使用
     *
     * @param codes
     * @return
     */
    @Deprecated
    public static String conveArrayToStr(String[] codes) {
        return convertArrayToStr(codes);
    }
    
    /**
     * 将数组字符串串连用于查询使用
     *
     * @param codes
     * @return
     */
    public static String convertArrayToStr(String[] codes) {
        return convertArrayToStr(codes, ",");
    }
    
    /**
     * 将数组字符串串连
     *
     * @param codes     字符数组
     * @param separator 分隔符，如果不传，默认为英文逗号
     * @return
     */
    public static String convertArrayToStr(String[] codes, String separator) {
        if (codes == null || codes.length == 0) {
            return "";
        }
        
        String separatorStr = separator;
        
        if (isEmpty(separatorStr)) {
            separatorStr = ",";
        }
        StringBuilder str = new StringBuilder("");
        
        for (int i = 0; i < codes.length; i++) {
            if (i == codes.length - 1) {
                str.append(codes[i]);
            } else {
                str.append(codes[i]).append(separatorStr);
            }
        }
        
        return str.toString();
        
    }
    
    /**
     * 判断是否是数字型的字符
     *
     * @param str
     * @return
     */
    public static boolean isIntegerNum(String str) {
        
        return !isEmpty(str) && (str.matches("^-?\\d+$")
                || str.matches("^-?([1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*|0?\\.0+|0)$"));
    }
    
    /**
     * id值相互比较
     *
     * @param equalIds
     * @param equalToIds
     * @return
     */
    public static String constrast(String equalIds, String equalToIds) {
        String str;
        String[] equalId = equalIds.split(",");
        String[] equalToId = equalToIds.split(",");
        str = compareIds(equalId, equalToId);
        return str;
    }
    
    public static String compareIds(String[] equalId, String[] equalToId) {
        String str = "";
        boolean flag;
        // IDS比较
        for (int i = 0; i < equalId.length; i++) {
            flag = false;
            
            for (int j = 0; j < equalToId.length; j++) {
                if (equalId[i].equals(equalToId[j])) {
                    flag = true;
                    break;
                }
            }
            // 当没有相同id时
            if (!flag) {
                str += equalId[i] + ",";
            }
        }
        return str;
    }
    
    /**
     * id值相互比较
     *
     * @param equalIds
     * @param equalToIds
     * @return
     */
    public static String compareIds(String[] equalIds, String equalToIds) {
        String[] equalToId = equalToIds.split(",");
        return compareIds(equalIds, equalToId);
    }
    
    /**
     * id值相互比较
     *
     * @param equalIds
     * @param equalToIds
     * @return
     */
    public static String compareIds(String equalIds, String[] equalToIds) {
        String[] equalId = equalIds.split(",");
        return compareIds(equalId, equalToIds);
    }
    
    /**
     * 判断用户权限
     *
     * @param powerStr
     * @param authorities
     * @return
     */
    public static boolean hasAuthoritys(String powerStr, String[] authorities) {
        
        for (int i = 0; i < authorities.length; i++) {
            if (authorities[i] != null && !"".equals(authorities[i]) && authorities[i].equals(powerStr)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * 判断字符串是否为空
     */
    public static boolean checkStr(String str) {
        return !(str == null || "".equals(str.trim()));
    }
    
    public String dealString(String str) {
        if (str == null || "".equals(str.trim())) {
            return "";
        }
        return str.trim();
    }
    
    
    public static String dealSqlField(String str) {
        return str.trim().replace("'", "''");
    }
    
    /**
     * 取星期
     *
     * @param dt 日期
     * @return 取得的结果
     */
    public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"星期日SUN", "星期一MON", "星期二TUE", "星期三WED", "星期四THU",
                "星期五FRI", "星期六SAT"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return weekDays[w];
    }
    
    /**
     * 取星期 指定日期格式 yyyy-MM-dd
     *
     * @param date 日期
     * @return 取得的结果
     */
    public static String getWeekOfDate(String date) {
        String dt = date;
        if (StringUtils.checkEmpty(dt) == null) {
            dt = StringUtils.getCurrentDate("yyyy-MM-dd");
        }
        return StringUtils.getWeekOfDate(StringUtils
                .parseDate(dt, "yyyy-MM-dd"));
    }
    
    public static long dateTimeDifferenceHour(String format, String date1,
                                              String date2) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date d1 = sdf.parse(date1);
        Date d2 = sdf.parse(date2);
        long l = d1.getTime() - d2.getTime();
        long hour = (l / (HOUR_OF_DAY * MINUTE_OF_HOUR * MILLION_SECOND_OF_SECOND));
        
        return hour;
    }
    
    public static long dateTimeDifferenceMinute(String format, String date1,
                                                String date2) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date d1 = sdf.parse(date1);
        Date d2 = sdf.parse(date2);
        long l = d1.getTime() - d2.getTime();
        int min = (int) (l / (SECOND_OF_MINUTE * MILLION_SECOND_OF_SECOND));
        return min;
    }
    
    
    /**
     * Description: 格式化时间
     *
     * @param date 时间格式
     * @return yyyy-MM-dd HH:mm:ss时间字符串
     * @since 1.0 2012-3-13 上午10:32:11 by 王星皓（albertwxh@gmail.com）创建
     */
    public static String formatDate(Date date) {
        return StringUtils.formatDate(date, null);
    }
    
    public static String formatDate(Date date, String patternPram) {
        String pattern = patternPram;
        if (pattern == null) {
            pattern = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat simpleFormat = new SimpleDateFormat(pattern);
        return simpleFormat.format(date);
    }
    
    public static Integer getYearByDateString(String date) {
        Integer year = -1;
        if (StringUtils.checkEmpty(date) != null && date.length() >= SEVEN) {
            String y = date.split("-")[0];
            StringUtils.parseInt(y, -1);
        }
        
        return year;
    }
    
    public static Integer getMonthByDateString(String date) {
        Integer month = -1;
        if (StringUtils.checkEmpty(date) != null && date.length() >= SEVEN) {
            String m = date.split("-")[1];
            StringUtils.parseInt(m, -1);
        }
        return month;
    }
    
    public static int getWeekByDate(String date) throws ParseException {
        int week;
        Calendar cal = new GregorianCalendar();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        cal.setTime(sdf.parse(date));
        week = cal.get(Calendar.DAY_OF_WEEK) - 1;
        return week;
    }
    
    /**
     * 判断是否周末
     *
     * @param date 给定日期
     * @return 判断结果
     * @throws ParseException
     */
    public static boolean isWeekend(String date) throws ParseException {
        int week = StringUtils.getWeekByDate(date);
        return week == FIVE || week == SIX || week == 0;
    }
    
    /**
     * JAVA判断字符串数组中是否包含某字符串元素
     *
     * @param substring 某字符串
     * @param source    源字符串数组
     * @return 包含则返回true，否则返回false
     */
    public static boolean isIn(String substring, String[] source) {
        if (source == null || source.length == 0) {
            return false;
        }
        for (int i = 0; i < source.length; i++) {
            String aSource = source[i];
            if (aSource.equals(substring)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 指定日期加上指定天数
     *
     * @param date    Date
     * @param dateAdd date
     * @return Date
     */
    public static Date dateAddDay(Date date, int dateAdd) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, dateAdd);
        return c.getTime();
    }
    
    /**
     * @param s 获取字符串字节数
     * @return 字节数
     */
    public static int getWordCount(String s) {
        String result = s.replaceAll("[^\\x00-\\xff]", "**");
        return result.length();
    }
    
    public static int getDateField(int field) {
        return getDateField(new Date(), field);
    }
    
    public static int getDateField(Date date, int field) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        if (field == Calendar.MONTH) {
            return c.get(field) + 1;
        } else {
            return c.get(field);
        }
    }
    
    /**
     * 去除字符串中指定位置的字符，右侧字符串依次左移，并返回结果
     * <br/> Created on 2012-11-12 下午01:10:58
     *
     * @param str   要处理的字符串
     * @param index 字符位置,从0开始
     * @return 去除后的字符串结果
     * @author 李青原(liqingyuan1986 @ aliyun.com)
     * @since 1.0
     */
    public static String removeCharAt(String str, Integer index) {
        if (index < 0 || index > (str.length() - 1)) {
            throw new StringIndexOutOfBoundsException(index);
        } else {
            return str.substring(0, index) + str.substring(index + 1, str.length());
        }
    }
    
    
    /**
     * 检查指定的字符串列表是否不为空。
     */
    public static boolean areNotEmpty(String... values) {
        boolean result = true;
        if (values == null || values.length == 0) {
            result = false;
        } else {
            for (String value : values) {
                result &= !isEmpty(value);
            }
        }
        return result;
    }
    
    /**
     * 转换List所有元素转换为String, 中间以 separator分隔。
     * Description:
     *
     * @param list      要处理的字符串
     * @param separator 分隔符,如果不传，默认为英文逗号
     * @return 处理后的结果
     */
    public static String convertToString(final List list, String separator) {
        String separatorStr = separator;
        if (isEmpty(separatorStr)) {
            separatorStr = ",";
        }
        StringBuilder sb = new StringBuilder();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (i < list.size() - 1) {
                    sb.append(list.get(i) + separatorStr);
                } else {
                    sb.append(list.get(i));
                }
            }
        }
        return sb.toString();
    }
    
}
