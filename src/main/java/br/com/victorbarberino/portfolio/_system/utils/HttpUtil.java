package br.com.victorbarberino.portfolio._system.utils;

import br.com.victorbarberino.portfolio._system.HttpContext;
import jakarta.servlet.http.HttpServletRequest;

public class HttpUtil {
    public static String getDynamicAppDomain() {
        HttpServletRequest request = HttpContext.getCurrentRequest();
        return  request.getScheme() + "://" +  // http ou https
                request.getServerName() +      // dominio ou IP
                (request.getServerPort() == 80 || request.getServerPort() == 443 ? "" : ":" + request.getServerPort());
    }
}
