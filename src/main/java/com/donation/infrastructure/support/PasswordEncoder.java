package com.donation.infrastructure.support;

import com.donation.global.exception.DonationInvalidateException;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoder {

    public String encode(final String password){
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public boolean compare(final String password, final String encodePassword){
        if (!BCrypt.checkpw(password, encodePassword))
            throw new DonationInvalidateException("패스워드가 일치하지 않습니다.");
        return true;
    }
}
