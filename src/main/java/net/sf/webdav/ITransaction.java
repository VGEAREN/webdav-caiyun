package net.sf.webdav;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

public interface ITransaction {

    Principal getPrincipal();

    HttpServletRequest getRequest();

    HttpServletResponse getResponse();
}
