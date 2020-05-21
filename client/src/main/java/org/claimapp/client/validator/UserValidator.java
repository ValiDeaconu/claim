package org.claimapp.client.validator;

import org.claimapp.client.dto.LoginUserDTO;
import org.claimapp.client.dto.RegisterUserDTO;
import org.claimapp.client.entity.User;
import org.claimapp.client.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

@Component
public class UserValidator {

    private static final String USERNAME_REGEX = "[a-zA-Z0-9_.]+";
    private static final String PASSWORD_REGEX = "[a-zA-Z0-9!@#$%^&*()_+=\\-]+";

    private UserRepository userRepository;

    @Autowired
    public UserValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void validateUser(User user, BindingResult bindingResult) {
        validateUsernameAndPassword(user.getUsername(), user.getPassword(), bindingResult);

        if (user.getWins() < 0) {
            bindingResult.rejectValue("wins", "NegativeWins");
        }

        if (user.getLoss() < 0) {
            bindingResult.rejectValue("loss", "NegativeLoss");
        }
    }

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
