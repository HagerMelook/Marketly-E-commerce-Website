package com.market.backend;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.market.backend.dtos.UpdateStringInfoDTO;
import com.market.backend.models.Account;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EditProfileControllerTests {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldReturnAdminInfoWhenValidIdProvided() {
        ResponseEntity<String> response = restTemplate
                .getForEntity("/account/admininfo/1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        Number id = documentContext.read("accountId");

        assertThat(id).isEqualTo(1);
    }

    @Test
    void shouldNotReturnAdminInfoForNonExistentId() {
        ResponseEntity<String> response = restTemplate
                .getForEntity("/account/admininfo/9999", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isBlank();
    }

    @Test
    void shouldReturnClientInfoWhenValidIdProvided() {
        ResponseEntity<String> response = restTemplate
                .getForEntity("/account/clientinfo/4", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        Number id = documentContext.read("accountId");

        assertThat(id).isEqualTo(4);
    }

    @Test
    void shouldNotReturnClientInfoForNonExistentId() {
        ResponseEntity<String> response = restTemplate
                .getForEntity("/account/clientinfo/9999", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isBlank();
    }

    @Test
    void shouldReturnVendorInfoWhenValidIdProvided() {
        ResponseEntity<String> response = restTemplate
                .getForEntity("/account/vendorinfo/5", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        Number id = documentContext.read("accountId");

        assertThat(id).isEqualTo(5);
    }

    @Test
    void shouldNotReturnVendorInfoForNonExistentId() {
        ResponseEntity<String> response = restTemplate
                .getForEntity("/account/vendorinfo/9999", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isBlank();
    }

    @Test
    void shouldUpdateAdminFirstName() {
        UpdateStringInfoDTO newFirstName = new UpdateStringInfoDTO("UpdatedFirstName");
        HttpEntity<UpdateStringInfoDTO> request = new HttpEntity<>(newFirstName);
        ResponseEntity<Void> response = restTemplate
                .exchange("/account/admininfo/firstname/1", HttpMethod.PUT, request, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<String> getResponse = restTemplate.getForEntity("/account/admininfo/1", String.class);
        DocumentContext documentContext = JsonPath.parse(getResponse.getBody());
        String firstName = documentContext.read("$.firstName");

        assertThat(firstName).isEqualTo("UpdatedFirstName");
    }
}
