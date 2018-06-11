package de.itemis.bonn.microservices.api.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping(path = "/info")
public class InfoResource {

  @RequestMapping(method = GET)
  public String info() {
    return "Hello, world";
  }
}
