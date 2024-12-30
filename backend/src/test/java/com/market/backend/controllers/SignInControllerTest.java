//package com.market.backend.controllers;
//
//import com.market.backend.configurations.JWTFilter;
//import com.market.backend.repositories.AccountRepository;
//import com.market.backend.services.JWTService;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.MockedStatic;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.security.authentication.TestingAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//
//import java.util.List;
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@WebMvcTest(SignInController.class)
//@AutoConfigureMockMvc(addFilters = false)
//@Import({JWTService.class, JWTFilter.class})
//class SignInControllerTest {
//
//    @Mock
//    private JWTService jwtService;
//
//    @Mock
//    private AccountRepository accountRepository;
//
//    @InjectMocks
//    private SignInController signInController;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testSignIn_Success() {
//        try (MockedStatic<SecurityContextHolder> mockedStatic = mockStatic(SecurityContextHolder.class)) {
//            // Mock authentication context
//            Authentication authentication = new TestingAuthenticationToken(
//                    new User("testuser", "password", List.of(new SimpleGrantedAuthority("ROLE_USER"))),
//                    null,
//                    "ROLE_USER"
//            );
//            SecurityContext securityContext = mock(SecurityContext.class);
//            when(securityContext.getAuthentication()).thenReturn(authentication);
//            mockedStatic.when(SecurityContextHolder::getContext).thenReturn(securityContext);
//
//            // Mock repository and JWT service
//            when(accountRepository.findIdByUsername("testuser")).thenReturn(1L);
//            when(jwtService.generateToken("testuser", "[ROLE_USER]", 1L)).thenReturn("mocked-jwt-token");
//
//            // Call the method and assert the result
//            String jwt = signInController.signIn();
//            assertEquals("mocked-jwt-token", jwt);
//
//            // Verify interactions
//            verify(accountRepository).findIdByUsername("testuser");
//            verify(jwtService).generateToken("testuser", "[ROLE_USER]", 1L);
//        }
//    }
//
//    @Test
//    void testSignIn_Failure_NoAuthentication() {
//        try (MockedStatic<SecurityContextHolder> mockedStatic = mockStatic(SecurityContextHolder.class)) {
//            SecurityContext securityContext = mock(SecurityContext.class);
//            when(securityContext.getAuthentication()).thenReturn(null);
//            mockedStatic.when(SecurityContextHolder::getContext).thenReturn(securityContext);
//
//            // Call the method and assert exception
//            IllegalStateException exception = assertThrows(IllegalStateException.class, () -> signInController.signIn());
//            assertEquals("User is not authenticated", exception.getMessage());
//        }
//    }
//
//    @Test
//    void testGoogleSignIn_Success() {
//        // Mock principal
//        OAuth2User principal = new DefaultOAuth2User(
//                List.of(new SimpleGrantedAuthority("ROLE_USER")),
//                Map.of("email", "testuser@gmail.com"),
//                "email"
//        );
//
//        // Mock repository and JWT service
//        when(accountRepository.findIdByUsername("testuser@gmail.com")).thenReturn(1L);
//        when(accountRepository.getTypeByUserName("testuser@gmail.com")).thenReturn("ROLE_USER");
//        when(jwtService.generateToken("testuser@gmail.com", "ROLE_USER", 1L)).thenReturn("mocked-jwt-token");
//
//        // Call the method and assert the result
//        String jwt = signInController.googleSignIn(principal);
//        assertEquals("mocked-jwt-token", jwt);
//
//        // Verify interactions
//        verify(accountRepository).findIdByUsername("testuser@gmail.com");
//        verify(accountRepository).getTypeByUserName("testuser@gmail.com");
//        verify(jwtService).generateToken("testuser@gmail.com", "ROLE_USER", 1L);
//    }
//
//    @Test
//    void testGoogleSignIn_Failure_NullPrincipal() {
//        // Call the method with null principal and assert the result
//        String response = signInController.googleSignIn(null);
//        assertEquals("Google Sign-In failed", response);
//    }
//
//    @Test
//    void testGoogleSignIn_Failure_NullAttributes() {
//        // Mock principal with null attributes
//        OAuth2User principal = new DefaultOAuth2User(List.of(), null, "email");
//
//        // Call the method and assert the result
//        String response = signInController.googleSignIn(principal);
//        assertEquals("Google Sign-In failed", response);
//    }
//}
