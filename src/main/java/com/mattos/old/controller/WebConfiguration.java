package com.mattos.old.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.WebFilter;

@Configuration
public class WebConfiguration {

   @Bean
   public WebFilter indexHtmlFilter() {
      return (exchange, chain) -> {
         if (exchange.getRequest().getURI().getPath().equals("/")) {
            return chain.filter(exchange
               .mutate()
               .request(exchange
                  .getRequest()
                  .mutate()
                  .path("/orders.html")
                  .build())
               .build());
         }
         return chain.filter(exchange);
      };
   }

}
