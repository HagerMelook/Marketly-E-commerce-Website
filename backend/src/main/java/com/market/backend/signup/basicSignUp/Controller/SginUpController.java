package com.market.backend.signup.basicSignUp.Controller;

//import com.market.backend.signup.basicSignUp.Model.BasicClient;
//import com.market.backend.signup.basicSignUp.Model.BasicVendor;
import com.market.backend.signup.basicSignUp.Model.Account;
import com.market.backend.signup.basicSignUp.Model.Client;
import com.market.backend.signup.basicSignUp.Model.VendorRequests;
import com.market.backend.signup.basicSignUp.Service.SignUpService;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/SignUp")
public class SginUpController {

    @Autowired
    private SignUpService service;
    @Autowired
    private Client client;

    @GetMapping("/Google/Client")
    public String googleOauthCleint(@AuthenticationPrincipal OAuth2User principal){
        System.out.println("Trying to register a client");
//        System.out.println(principal.toString());
        return service.insertClientByGoogleAccount(principal.getAttributes());
    }

    @GetMapping("/Google/Vendor/{org}/{tax}")
    public String googleOauthVendor(@AuthenticationPrincipal OAuth2User request, @PathVariable String org, @PathVariable long tax){
        System.out.println("Defining a Vendor Request");
        System.out.println(org+" "+tax);
        return service.registerVendorRequest(request.getAttributes(), org, tax);
    }

    @GetMapping("/")
    public String testHome(){
        return "Welcome";
    }

    @GetMapping("/about")
    public String testAbout(){
        return "I'm Ahmed";
    }

    @PostMapping("/ClientBasicSignUp")
    public String clientBasicSignUp(@RequestBody Account client) {
        String resultMsg;
        System.out.println("Received User: " + client.getUsername() + ", " + client.getPassword());
        resultMsg = service.insertBasicClient(client);
        System.out.println(resultMsg);
        return resultMsg;
    }

    @PostMapping("/VendorBasicSignUp")
    public String vendorBasicSignUp(@RequestBody VendorRequests vendor) {
        System.out.println("Received User: " + vendor.getOrganizationName() + ", " + vendor.getPassword() + ", "
                + vendor.getTaxNumber());
        String resultMsg;
        resultMsg = service.insertBasicVendor(vendor);
        System.out.println(resultMsg);
        return resultMsg;
    }


}
