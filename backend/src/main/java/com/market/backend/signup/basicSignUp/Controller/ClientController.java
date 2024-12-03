//package com.market.backend.signup.basicSignUp.Controller;
//
//import com.market.backend.signup.basicSignUp.Model.BasicClient;
//import com.market.backend.signup.basicSignUp.Service.ClientService;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@CrossOrigin
//@RequestMapping("/SignUp")
//public class ClientController {
//    private final ClientService clientService;
//
//    public ClientController(ClientService clientService) {
//        this.clientService = clientService;
//    }
//
//    @PostMapping("/ClientBasicSignUp")
//    public String clientBasicSignUp(@RequestBody BasicClient client) {
//        String resultMsg;
//        System.out.println("Received User: " + client.getUsername() + ", " + client.getPassword());
//        resultMsg = clientService.insertBasicClient(client);
//        System.out.println(resultMsg);
//        return resultMsg;
//    }
//}
