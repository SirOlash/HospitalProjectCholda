package org.HospitalProjectCholda.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {
    private final PasswordEncoder encryptedPassword;

    public PasswordService() {
        this.encryptedPassword = new BCryptPasswordEncoder();
    }
    public String hashPassword(String password){
        return encryptedPassword.encode(password);
    }
    public  boolean matchesPassword(String password, String hashedPassword){
        return encryptedPassword.matches(password, hashedPassword);
    }
}
