package com.example.demo.utils;

import com.neurotec.biometrics.NFPosition;
import java.util.Base64;

/**
 * @author David
 */
public class Utils {

    public static byte[] decodeImage(String imageDataString) {
        return Base64.getDecoder().decode(imageDataString);
    }

    public static NFPosition obtenerPosicionDedo(int i) {
        switch (i) {
            case 1:
                return NFPosition.RIGHT_RING_FINGER;
            case 2:
                return NFPosition.LEFT_RING_FINGER;
            case 3:
                return NFPosition.RIGHT_INDEX_FINGER;
            case 4:
                return NFPosition.LEFT_INDEX_FINGER;
            case 5:
                return NFPosition.RIGHT_MIDDLE_FINGER;
            case 6:
                return NFPosition.LEFT_MIDDLE_FINGER;
            case 7:
                return NFPosition.RIGHT_LITTLE_FINGER;
            case 8:
                return NFPosition.LEFT_LITTLE_FINGER;
            case 9:
                return NFPosition.RIGHT_THUMB;
            case 10:
                return NFPosition.LEFT_THUMB;
        }
        return NFPosition.UNKNOWN;
    }
}
