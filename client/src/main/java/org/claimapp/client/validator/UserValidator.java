package org.claimapp.client.validator;

import org.claimapp.common.dto.LoginUserDTO;
import org.claimapp.common.dto.RegisterUserDTO;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

@Component
public class UserValidator {

    private static final String USERNAME_REGEX = "[a-zA-Z0-9_.]+";
    private static final String PASSWORD_REGEX = "[a-zA-Z0-9!@#$%^&*()_+=\\-]+";

    public void validateLoginUser(LoginUserDTO loginUserDTO, BindingResult bindingResult) {
        validateUsernameAndPassword(loginUserDTO.getUsername(), loginUserDTO.getPassword(), bindingResult);
    }

    public void validateRegisterUser(RegisterUserDTO registerUserDTO, BindingResult bindingResult) {
        validateUsernameAndPassword(registerUserDTO.getUsername(), registerUserDTO.getPassword(), bindingResult);

        if (registerUserDTO.getRe_password() == null ||
                !registerUserDTO.getRe_password().equals(registerUserDTO.getPassword())) {
            bindingResult.rejectValue("re_password", "PasswordsNotMatching");
        }
    }

    private void validateUsernameAndPassword(String username, String password, BindingResult bindingResult) {
        if (username == null || !username.matches(USERNAME_REGEX)) {
            bindingResult.rejectValue("username", "UsernameNotAllowed");
        } else if (username.length() < 4) {
            bindingResult.rejectValue("username", "UsernameLengthSmallerThan4");
        }

        if (password == null || !password.matches(PASSWORD_REGEX)) {
            bindingResult.rejectValue("password", "PasswordNotAllowed");
        } else if (password.length() < 6) {
            bindingResult.rejectValue("password", "PasswordLengthSmallerThan6");
        }
    }
}
