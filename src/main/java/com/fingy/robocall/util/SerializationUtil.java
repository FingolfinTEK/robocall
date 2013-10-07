package com.fingy.robocall.util;

import org.apache.commons.codec.binary.Base64;

import java.io.*;

public class SerializationUtil {

    public static String serializeToString(Object toSerialize) throws IOException {
        try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
             ObjectOutputStream outputStream = new ObjectOutputStream(byteStream)) {
            outputStream.writeObject(toSerialize);
            return Base64.encodeBase64URLSafeString(byteStream.toByteArray());
        }
    }

    public static <T> T deserializeFromString(String toDeserialize) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream byteStream = new ByteArrayInputStream(Base64.decodeBase64(toDeserialize));
             ObjectInputStream outputStream = new ObjectInputStream(byteStream)) {
            return (T) outputStream.readObject();
        }
    }
}
