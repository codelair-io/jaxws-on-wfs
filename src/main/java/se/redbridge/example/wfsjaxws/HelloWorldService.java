package se.redbridge.example.wfsjaxws;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.cache.annotation.CacheResult;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebService;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Stateless
@WebService
@DenyAll
@HandlerChain(file = "/META-INF/jaxws/handlers.xml")
public class HelloWorldService {
  @Inject
  private JsonWebToken jwt;

  @WebMethod
  @RolesAllowed("test")
  public String helloWorld() {
    return "Hello, World!";
  }

  @WebMethod
  @PermitAll
  @CacheResult
  public String superSlowHelloWorld() {
    try {
      Thread.sleep(10000L);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    return "Slow Hello World (at least the first time).";
  }
}
