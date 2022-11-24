package com.donation.infrastructure.support;

import com.donation.global.exception.DonationInvalidateException;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class AuthEncoder {

    public String encode(final String Text){
        return BCrypt.hashpw(Text, BCrypt.gensalt());
    }

    public boolean compare(final String Text, final String encodeText){
        if (!BCrypt.checkpw(Text, encodeText))
            throw new DonationInvalidateException("인증정보가 일치하지 않습니다.");
        return true;
    }
}
