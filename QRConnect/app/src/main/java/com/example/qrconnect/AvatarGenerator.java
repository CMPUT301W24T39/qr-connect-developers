package com.example.qrconnect;

import android.graphics.Bitmap;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Utility class for deterministically generating user profiles
 */
public class AvatarGenerator {
    /**
     * This function returns a deterministically generated bitmap based on a hash of the user's name
     * to be used as the user's profile picture if they do not upload one themselves
     * @param user UserProfile from which the name and ID is used as the seed for generating the bitmap.
     * @return Bitmap To be used as the profile picture
     */
    public static Bitmap generateAvatar(UserProfile user) {
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String userID = user.getUserID();

        String name = firstName + " " + lastName + userID;

        try {
            // generate hash of user's name and convert to byte array
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] sha = md.digest(name.getBytes());

            // convert bytes to string of their binary representation
            // pad with zeros to keep it 16x16. this pads on the wrong side but it doesn't make a difference
            String binaryString = "";
            for (byte b : sha) {
                String s = Integer.toBinaryString(b & 0xFF);
                while (s.length() < 8) {
                    s += "0";
                }
                binaryString += s;
            }

            // create bitmap and then fill its pixels in with the values of the binary string
            Bitmap bitmap = Bitmap.createBitmap(256, 256, Bitmap.Config.RGB_565);
            for (int i=0; i<16; i++) {
                for (int j=0; j<16; j++) {
                    int pixel;
                    if (binaryString.charAt(16*i + j) == '1') {
                        pixel = 0xFFFFFF;
                    } else {
                        pixel = 0x000000;
                    }
                    // this sets each 16x16 grid in the 256x256 bitmap to the given pixel
                    // same as scaling the image up, but without a loss in quality
                    for (int ii = 16*i; ii < 16*i + 16; ii++) {
                        for (int jj = 16*j; jj < 16*j + 16; jj++) {
                            bitmap.setPixel(ii,jj,pixel);
                        }
                    }
                    // alternative approach: a 16x16 grid of 16x16 bitmaps
//                    for (int ii=0; ii<16; ii++) {
//                        for (int jj=0; jj<16; jj++) {
//                            bitmap.setPixel(ii*16 + i, jj*16+j,pixel);
//                        }
//                    }
                    // just a 16x16 bitmap
                    //bitmap.setPixel(i,j,pixel);
                }
            }
            Log.d("byte array", Arrays.toString(sha));
            return bitmap;
        } catch (NoSuchAlgorithmException e){
            Log.e("SHA error", "NoSuchAlgorithmException");
        }
        return null;
    }
}
