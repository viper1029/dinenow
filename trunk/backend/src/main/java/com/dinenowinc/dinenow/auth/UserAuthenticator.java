package com.dinenowinc.dinenow.auth;

import com.dinenowinc.dinenow.model.User;
import com.dinenowinc.dinenow.model.UserRole;
import com.github.toastshaman.dropwizard.auth.jwt.JsonWebTokenValidator;
import com.github.toastshaman.dropwizard.auth.jwt.model.JsonWebToken;
import com.github.toastshaman.dropwizard.auth.jwt.validator.ExpiryValidator;
import com.google.common.base.Optional;
import io.dropwizard.auth.Authenticator;

import java.security.Principal;
import java.util.UUID;

public class UserAuthenticator implements Authenticator<JsonWebToken, Principal> {
  @Override
  public Optional<Principal> authenticate(JsonWebToken token) {
    final JsonWebTokenValidator expiryValidator = new ExpiryValidator();
    expiryValidator.validate(token);

    Object userId = token.claim().getParameter("id");
    Object name = token.claim().getParameter("name").toString();
    Object role = token.claim().getParameter("role");
    Object email = token.claim().getParameter("email");

    if (userId != null && role != null) {
      final User user = new User();
      user.setId(UUID.fromString(userId.toString()));
      user.setName(name.toString());
      user.setRole(UserRole.valueOf(role.toString()));
      user.setEmail(email.toString());
      return Optional.of((Principal) user);
    }

    return Optional.absent();
  }
}