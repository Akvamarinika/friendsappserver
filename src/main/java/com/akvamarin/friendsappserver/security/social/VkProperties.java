package com.akvamarin.friendsappserver.security.social;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@ConfigurationProperties("vk.properties")
@ConstructorBinding
@Validated
@Getter
@RequiredArgsConstructor
public class VkProperties {

    @NotNull
    private final String version;

    @NotNull
    private final String urlSecure;

    @NotNull
    private final String accessToken;

    @NotNull
    private final String secretKey;

}

