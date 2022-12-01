package com.example.dockeroauth20.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import static com.example.dockeroauth20.config.ResourceServerConfiguration.RESOURCE_ID;
import static com.example.dockeroauth20.config.SecurityConstants.ADMIN_ROLE_NAME;
import static com.example.dockeroauth20.config.SecurityConstants.USER_ROLE_NAME;

@Component
@RequiredArgsConstructor
public class InitialLoader implements CommandLineRunner {
    @Value("${oauth2.front.client_id:kotopes-front-app-1}")
    private String clientId;
    @Value("${oauth2.front.client_secret:password}")
    private String clientSecret;
    @Value("${oauth2.access_token_validity:86400}")
    private int accessTokenValidity;
    @Value("${oauth2.refresh_token_validity:604800}")
    private int refreshTokenValidity;

    private final RoleService roleService;
    private final OAuthClientDetailsService clientService;
    private final PasswordEncoder oauthClientPasswordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (roleService.findAll().isEmpty()) {
            roleService.save(Role.builder().name(ADMIN_ROLE_NAME).description("Админ").build());
            roleService.save(Role.builder().name(USER_ROLE_NAME).description("Пользователь").build());
//            roleService.save(Role.builder().name(CARRIER_ROLE_NAME).description("Поставщик услуг").build());
        }
        if (clientService.isEmpty()) {
            clientService.save(OAuthClientDetails.builder()
                    .clientId(clientId)
                    .clientSecret(oauthClientPasswordEncoder.encode(clientSecret))
                    .resourceIds(RESOURCE_ID)
                    .scope("read,write")
                    .accessTokenValidity(accessTokenValidity)
                    .refreshTokenValidity(refreshTokenValidity)
                    .authorizedGrantTypes("password,authorization_code,refresh_token")
                    .autoapprove(1)
                    .build()
            );
        }

    }
}
