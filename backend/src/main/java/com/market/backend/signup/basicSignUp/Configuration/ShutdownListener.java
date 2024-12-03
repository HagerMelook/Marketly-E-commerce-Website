//package com.market.backend.signup.basicSignUp.Configuration;
//
//import org.springframework.context.ApplicationListener;
//import org.springframework.context.event.ContextClosedEvent;
//import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
//
//public class ShutdownListener implements ApplicationListener<ContextClosedEvent> {
//
//    private final SecurityContextLogoutHandler logoutHandler;
//
//    public ShutdownListener() {
//        // Using the default SecurityContextLogoutHandler to perform the logout
//        this.logoutHandler = new SecurityContextLogoutHandler();
//    }
//
//    @Override
//    public void onApplicationEvent(ContextClosedEvent event) {
//        // Logout when the application is shutting down or restarting
//        logoutHandler.logout(null, null, null);
//    }
//}