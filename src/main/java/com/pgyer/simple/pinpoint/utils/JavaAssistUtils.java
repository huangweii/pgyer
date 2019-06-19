package com.pgyer.simple.pinpoint.utils;

import javassist.CtBehavior;
import javassist.Modifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaAssistUtils {

    private static final String ARRAY = "[]";
    private static final String[] EMPTY_STRING_ARRAY = new String[0];
    private static final Pattern PARAMETER_SIGNATURE_PATTERN = Pattern.compile("\\[*L[^;]+;|\\[*[ZBCSIFDJ]|[ZBCSIFDJ]");

    public static boolean isStaticBehavior(CtBehavior behavior) {
        if (behavior == null) {
            throw new NullPointerException("behavior must not be null");
        }
        int modifiers = behavior.getModifiers();
        return Modifier.isStatic(modifiers);
    }

    public static String[] parseParameterSignature(String signature) {
        if (signature == null) {
            throw new NullPointerException("signature must not be null");
        }
        final List<String> parameterSignatureList = splitParameterSignature(signature);
        if (parameterSignatureList.isEmpty()) {
            return EMPTY_STRING_ARRAY;
        }
        final String[] objectType = new String[parameterSignatureList.size()];
        for (int i = 0; i < parameterSignatureList.size(); i++) {
            final String parameterSignature = parameterSignatureList.get(i);
            objectType[i] = byteCodeSignatureToObjectType(parameterSignature, 0);
        }
        return objectType;
    }

    private static List<String> splitParameterSignature(String signature) {
        final String parameterSignature = getParameterSignature(signature);
        if (parameterSignature.isEmpty()) {
            return Collections.emptyList();
        }
        final Matcher matcher = PARAMETER_SIGNATURE_PATTERN.matcher(parameterSignature);
        final List<String> parameterTypeList = new ArrayList<String>();
        while (matcher.find()) {
            parameterTypeList.add(matcher.group());
        }
        return parameterTypeList;
    }

    private static String getParameterSignature(String signature) {
        int start = signature.indexOf('(');
        if (start == -1) {
            throw new IllegalArgumentException("'(' not found. signature:" + signature);
        }
        final int end = signature.indexOf(')', start + 1);
        if (end == -1) {
            throw new IllegalArgumentException("')' not found. signature:" + signature);
        }
        start = start + 1;
        if (start == end) {
            return "";
        }
        return signature.substring(start, end);
    }

    private static String byteCodeSignatureToObjectType(String signature, int startIndex) {
        final char scheme = signature.charAt(startIndex);
        switch (scheme) {
            case 'B':
                return "byte";
            case 'C':
                return "char";
            case 'D':
                return "double";
            case 'F':
                return "float";
            case 'I':
                return "int";
            case 'J':
                return "long";
            case 'S':
                return "short";
            case 'V':
                return "void";
            case 'Z':
                return "boolean";
            case 'L':
                return toObjectType(signature, startIndex + 1);
            case '[': {
                return toArrayType(signature);
            }
        }
        throw new IllegalArgumentException("invalid signature :" + signature);
    }

    private static String toObjectType(String signature, int startIndex) {
        // Ljava/lang/String;
        final String assistClass = signature.substring(startIndex, signature.length() - 1);
        final String objectName = jvmNameToJavaName(assistClass);
        if (objectName.isEmpty()) {
            throw new IllegalArgumentException("invalid signature. objectName not found :" + signature);
        }
        return objectName;
    }

    /**
     * java/lang/String -> java.lang.String
     * @param jvmName
     * @return
     */
    public static String jvmNameToJavaName(String jvmName) {
        if (jvmName == null) {
            throw new NullPointerException("jvmName must not be null");
        }
        return jvmName.replace('/', '.');
    }

    private static String toArrayType(String description) {
        final int arraySize = getArraySize(description);
        final String objectType = byteCodeSignatureToObjectType(description, arraySize);
        return arrayType(objectType, arraySize);
    }

    private static String arrayType(String objectType, int arraySize) {
        final int arrayStringLength = ARRAY.length() * arraySize;
        StringBuilder sb = new StringBuilder(objectType.length() + arrayStringLength);
        sb.append(objectType);
        for (int i = 0; i < arraySize; i++) {
            sb.append(ARRAY);
        }
        return sb.toString();
    }

    private static int getArraySize(String description) {
        if (description == null || description.isEmpty()) {
            return 0;
        }
        int arraySize = 0;
        for (int i = 0; i < description.length(); i++) {
            final char c = description.charAt(i);
            if (c == '[') {
                arraySize++;
            } else {
                break;
            }
        }
        return arraySize;
    }
}
