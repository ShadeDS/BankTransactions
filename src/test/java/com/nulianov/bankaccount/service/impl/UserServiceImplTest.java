package com.nulianov.bankaccount.service.impl;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;

public class UserServiceImplTest {
    private final static Logger log = LoggerFactory.getLogger(UserServiceImplTest.class);
    private final static String validUsername = "niul-100@yahoo.com";
    private static final String[] invalidUsernames = new String[] { "niul", "niul@.com.my",
            "niul123@gmail.a", "niul123@.com", "niul123@.com.com", ".niul@niul.com",
            "niul()*@gmail.com", "niul@%*.com", "niul..2002@gmail.com", "niul.@gmail.com",
            "niul@niul@gmail.com", "niul@gmail.com.1a" };
    private final UserServiceImpl userService = new UserServiceImpl();

    @Test
    public void registerWithInvalidUsername() {
        boolean valid = false;
        for (String invalidUsername : invalidUsernames) {
            try {
                userService.register(invalidUsername, "password");
                valid = true;
            } catch (BadCredentialsException e) {
                log.debug("Username {} was considered as invalid which is ok", invalidUsername);
            } catch (Exception e) {
                valid = true;
            }
        }

        Assert.assertFalse(valid);
    }

    @Test(expected = BadCredentialsException.class)
    public void registerWithEmptyPassword() {
        userService.register(validUsername, "");
    }
}
