package com.market.backend.signup.basicSignUp.Controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.market.backend.signup.basicSignUp.Model.BasicClient;
import com.market.backend.signup.basicSignUp.Service.ClientService;

@RestController
@CrossOrigin
@RequestMapping("/SignUp")
public class ClientController {
    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping("/ClientBasicSignUp")
    public String clientBasicSignUp(@RequestBody BasicClient client) {
        String resultMsg;
        System.out.println("Received User: " + client.getUsername() + ", " + client.getPassword());
        if (client.getUsername() == null)
            return "The username can't be empty";
        if (client.getUsername().length() > 80)
            return "The username can't be more than 80 character";
        if (client.getPassword() == null)
            return "The password can't be empty";
        if (client.getPassword().length() > 80)
            return "The password can't be more than 80 character";
        resultMsg = clientService.insertBasicClient(client);
        System.out.println(resultMsg);
        return resultMsg;
    }
}
