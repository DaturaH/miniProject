package com.miniProject.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.filter.GenericFilterBean;

import com.miniProject.util.AuthInfo;
import com.miniProject.util.Config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;

public class JwtFilter extends GenericFilterBean {

    @Override
    public void doFilter(final ServletRequest req,
                         final ServletResponse res,
                         final FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) req;
        

        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            //throw new ServletException("Missing or invalid Authorization header.");
        	
        } else {
        	final String token = authHeader.substring(7); // The part after "Bearer "

            try {
                final Claims claims = Jwts.parser().setSigningKey(Config.SECRET_KEY)
                    .parseClaimsJws(token).getBody();
                
                request.setAttribute("AuthInfo", new AuthInfo(claims.getSubject(), (String)claims.get("uid"), (Integer)claims.get("type")));
                
            }
            catch (final SignatureException e) {
                throw new ServletException("Invalid token.");
            }
        }

        chain.doFilter(req, res);
    }

}