package se.redbridge.example.wfsjaxws;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.inject.spi.CDI;
import javax.servlet.http.HttpServletRequest;
import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import org.eclipse.microprofile.jwt.JsonWebToken;

public class JWTSubjectPrinter implements SOAPHandler<SOAPMessageContext> {
  private final Logger logger = Logger.getLogger(JWTSubjectPrinter.class.getName());

  @Override
  public Set<QName> getHeaders() {
    return null;
  }

  @Override
  public boolean handleMessage(final SOAPMessageContext context) {
    if ((Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY)) {
      return true; // we just wanna log inbound
    }

    try {
      final JsonWebToken jwt = CDI.current().select(JsonWebToken.class).get();
      logger.info(jwt.getSubject());
    } catch (final Exception e) {
      final HttpServletRequest servletRequest = (HttpServletRequest) context.get(MessageContext.SERVLET_REQUEST);
      logger.log(Level.WARNING, "Didn't parse JWT, but we got this principal " + servletRequest.getUserPrincipal().getName(), e);
    }

    return true;
  }

  @Override
  public boolean handleFault(final SOAPMessageContext context) {
    return false;
  }

  @Override
  public void close(final MessageContext context) {
    // intentionally left empty
  }
}
