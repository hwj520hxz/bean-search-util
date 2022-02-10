package com.bean.search.util;

/**
 * @author ：hwj
 * @version 版本号：V1.0
 * @Description ：
 */
public class StringUtils {
    public StringUtils() {
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    public static boolean isBlank(String str) {
        if (str == null) {
            return true;
        } else {
            int len = str.length();
            if (len == 0) {
                return true;
            } else {
                int i = 0;

                while(i < len) {
                    switch(str.charAt(i)) {
                        case '\t':
                        case '\n':
                        case '\r':
                        case ' ':
                            ++i;
                            break;
                        default:
                            return false;
                    }
                }

                return true;
            }
        }
    }

    public static String firstCharToUpperCase(String str) {
        if (str != null && str.length() != 0) {
            char first = str.charAt(0);
            if (first >= 'a' && first <= 'z') {
                first = (char)(first - 32);
                return str.length() == 1 ? String.valueOf(first) : first + str.substring(1);
            } else {
                return str;
            }
        } else {
            return str;
        }
    }

    public static String firstCharToLoweCase(String str) {
        if (str != null && str.length() != 0) {
            char first = str.charAt(0);
            if (first >= 'A' && first <= 'Z') {
                first = (char)(first + 32);
                return str.length() == 1 ? String.valueOf(first) : first + str.substring(1);
            } else {
                return str;
            }
        } else {
            return str;
        }
    }

    public static int containCount(String src, int from, int to, char[] targets) {
        int count = 0;
        if (src != null) {
            from = Math.max(from, 0);
            to = Math.min(to, src.length());

            for(int i = from; i < to; ++i) {
                char c = src.charAt(i);
                boolean contained = false;
                char[] var8 = targets;
                int var9 = targets.length;

                for(int var10 = 0; var10 < var9; ++var10) {
                    char target = var8[var10];
                    if (c == target) {
                        contained = true;
                        break;
                    }
                }

                if (contained) {
                    ++count;
                }
            }
        }

        return count;
    }

    public static String toHyphenation(String src, String hyphenation) {
        StringBuilder sb = new StringBuilder(src);
        int cnt = 0;

        for(int i = 1; i < src.length(); ++i) {
            if (Character.isUpperCase(src.charAt(i))) {
                sb.insert(i + cnt, hyphenation);
                cnt += hyphenation.length();
            }
        }

        return sb.toString().toLowerCase();
    }

    public static String toUnderline(String src) {
        return toHyphenation(src, "_");
    }
}
