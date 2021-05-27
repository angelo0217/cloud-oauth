package com.auth.authorization.service;

import com.auth.authorization.model.ClientDetailVO;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;

@Service
public class UserClientService implements ClientDetailsService {


    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        return new ClientDetailVO("client", "123", 60 * 60, 3 * 60 * 60);
    }
}
