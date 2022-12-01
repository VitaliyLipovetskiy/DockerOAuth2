package com.example.dockeroauth20.user;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "oauth_client_details")
public class OAuthClientDetails {
    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    @Column(length = 36, nullable = false, updatable = false)
    private String id;
    @Column(unique = true)
    private String clientId;
    @Column
    private String resourceIds;
    @Column
    @ToString.Exclude
    private String clientSecret;
    @Column
    private String scope;
    @Column
    private String authorizedGrantTypes;
    @Column
    private String webServerRedirectUri;
    @Column
    private String authorities;
    @Column
    private int accessTokenValidity;
    @Column
    private int refreshTokenValidity;
    @Column
    private int autoapprove;
    @Column
    private String additionalInformation;
}
