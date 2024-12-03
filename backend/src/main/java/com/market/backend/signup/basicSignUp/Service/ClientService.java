//package com.market.backend.signup.basicSignUp.Service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.market.backend.signup.basicSignUp.Model.BasicClient;
//import com.market.backend.signup.basicSignUp.Repository.BasicClientRepo;
//
//@Service
//public class ClientService {
//    @Autowired
//    private BasicClientRepo basicClientRepo;
//
//    public String insertBasicClient(BasicClient client) {
//        if (client.getUsername() == null)
//            return "The username can't be empty";
//        if (client.getUsername().length() > 80)
//            return "The username can't be more than 80 character";
//        if (client.getPassword() == null)
//            return "The password can't be empty";
//        if (client.getPassword().length() > 80)
//            return "The password can't be more than 80 character";
//        if (basicClientRepo.existsByUsername(client.getUsername())) {
//            return "The username is already exist";
//        }
//        basicClientRepo.save(client);
//        return "Successfully registered";
//    }
//}
