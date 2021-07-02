package com.binarystudio.academy.springsecurity.security.oauth2;

import com.binarystudio.academy.springsecurity.domain.user.UserRepository;
import com.binarystudio.academy.springsecurity.security.jwt.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.Optional;

import static com.binarystudio.academy.springsecurity.security.oauth2.RedirectUriToCookiePersister.REDIRECT_URI_PARAM;

public class SimpleOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OAuth2Properties oAuth2Properties;

    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        var redirUrl = extractRedirectUri(request).orElseGet(() -> super.determineTargetUrl(request, response, authentication));
        if (redirUrlIsUnknown(redirUrl)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Redirect uri is unknown");
        }
        var oauthUser = (OAuth2User) authentication.getPrincipal();
        var storedUser = userRepository.findByEmail(oauthUser.getAttribute("email")).orElseThrow();

        var accessToken = jwtProvider.generateAccessToken(storedUser);
        var refreshToken = jwtProvider.generateRefreshToken(storedUser);
        return UriComponentsBuilder.fromUriString(redirUrl)
                .queryParam("accessToken", accessToken)
                .queryParam("refreshToken", refreshToken).build().toUriString();
    }

    private Optional<String> extractRedirectUri(HttpServletRequest request) {
        for (Cookie cookie : request.getCookies()) {
            if (REDIRECT_URI_PARAM.equals(cookie.getName())) {
                return Optional.of(cookie.getValue());
            }
        }
        return Optional.empty();
    }

    private boolean redirUrlIsUnknown(String url) {
        final var clientRedirectUri = URI.create(url);
        return oAuth2Properties.getRedirectUris().stream()
                .noneMatch(authorizedRedirectUri -> {
                    URI authorizedURI = URI.create(authorizedRedirectUri);
                    return authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                            && authorizedURI.getPort() == clientRedirectUri.getPort();
                });
    }
}
