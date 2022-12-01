package com.example.dockeroauth20.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OAuthClientDetailsService {
    private final OAuthClientDetailsRepository repository;

    public List<OAuthClientDetails> findAll() {
        return repository.findAll();
    }

    public boolean isEmpty() {
        return repository.count() == 0;
    }

    public OAuthClientDetails save(OAuthClientDetails client) {
        return repository.save(client);
    }
}
