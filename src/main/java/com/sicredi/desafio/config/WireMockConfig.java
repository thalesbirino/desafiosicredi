package com.sicredi.desafio.config;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@Configuration
public class WireMockConfig {

    private WireMockServer wireMockServer;

    @PostConstruct
    public void setupWireMock() {
        wireMockServer = new WireMockServer(8081);
        wireMockServer.start();
        configureCpfValidation();
    }

    private void configureCpfValidation() {
        wireMockServer.stubFor(get(urlPathEqualTo("/cpf-validator"))
                .withQueryParam("cpf", matching("[0-9]{11}"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"status\": \"ABLE_TO_VOTE\"}")));

        wireMockServer.stubFor(get(urlPathEqualTo("/cpf-validator"))
                .withQueryParam("cpf", matching("[^0-9]|.{12,}"))
                .willReturn(aResponse()
                        .withStatus(404)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"status\": \"UNABLE_TO_VOTE\"}")));
    }


}
