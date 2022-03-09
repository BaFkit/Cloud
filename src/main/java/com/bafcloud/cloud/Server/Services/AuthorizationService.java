package com.bafcloud.cloud.Server.Services;


public interface AuthorizationService {
    void start();
    void stop();
    long checkUserVerification(String login, String password);
    String getRootClient(String login, String password);
}
