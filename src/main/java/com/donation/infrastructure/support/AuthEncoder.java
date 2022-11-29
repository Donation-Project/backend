package com.donation.infrastructure.support;

import com.donation.global.exception.DonationIOException;
import com.donation.global.exception.DonationInvalidateException;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class AuthEncoder {

    public String encode(final String Text){
        return BCrypt.hashpw(Text, BCrypt.gensalt());
    }
    public String encodeNoSalt(final String Text){
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(Text.getBytes());
            return bytesToHex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new DonationIOException("SHA-256");
        }
    }
    private String bytesToHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }

    public boolean compare(final String Text, final String encodeText){
        if (!BCrypt.checkpw(Text, encodeText))
            throw new DonationInvalidateException("인증정보가 일치하지 않습니다.");
        return true;
    }
}
