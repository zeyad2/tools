package org.os.minisocial.config;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.security.enterprise.identitystore.PasswordHash;
import org.mindrot.jbcrypt.BCrypt;

@ApplicationScoped
public class BCryptPasswordHash implements PasswordHash {

    @Override
    public String generate(char[] password) {
        return BCrypt.hashpw(new String(password), BCrypt.gensalt());
    }

    @Override
    public boolean verify(char[] password, String hashedPassword) {
        return BCrypt.checkpw(new String(password), hashedPassword);
    }
}