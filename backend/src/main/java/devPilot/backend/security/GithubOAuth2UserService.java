package devPilot.backend.security;

import devPilot.backend.entity.User;
import devPilot.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class GithubOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        Long githubId = ((Number) attributes.get("id")).longValue();
        String login = (String) attributes.get("login");
        String name = (String) attributes.get("name");
        String avatarUrl = (String) attributes.get("avatar_url");
        String email = (String) attributes.get("email");

        User user = userRepository.findByGithubId(githubId)
                .map(existing -> {
                    existing.setGithubUsername(login);
                    existing.setDisplayName(name != null ? name : login);
                    existing.setAvatarUrl(avatarUrl);
                    if (email != null) {
                        existing.setEmail(email);
                    }
                    return userRepository.save(existing);
                })
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .githubId(githubId)
                            .githubUsername(login)
                            .displayName(name != null ? name : login)
                            .avatarUrl(avatarUrl)
                            .email(email)
                            .build();
                    return userRepository.save(newUser);
                });

        return new AppUserPrincipal(user, attributes);
    }
}
