package hcmut.thesis.gpduserver.config.authentication;

import hcmut.thesis.gpduserver.models.entity.User;
import hcmut.thesis.gpduserver.models.entity.UserSecure;
import hcmut.thesis.gpduserver.service.UserService;
import hcmut.thesis.gpduserver.utils.JwttUtil;
import io.jsonwebtoken.ExpiredJwtException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.thymeleaf.expression.Lists;

@Component
@Slf4j
public class  JwtRequestFilter extends OncePerRequestFilter {

  @Autowired
  private UserDetailsService userService;
  @Autowired
  private JwttUtil jwtUtil;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain)
      throws ServletException, IOException {
    final String authorizationHeader = request.getHeader("Authorization");
    String username = null;
    String jwt = null;
    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
      jwt = authorizationHeader.substring(7);
      try {
        username = jwtUtil.extractSubject(jwt);
      } catch (ExpiredJwtException e) {
        log.error("Error when doFilterInternal: {}", e);
      }
    }
    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      UserSecure user = (UserSecure) userService.loadUserByUsername(username);
      UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
          user,
          null,
          user.getAuthorities()
      );
      usernamePasswordAuthenticationToken.setDetails(
          new WebAuthenticationDetailsSource().buildDetails(request));
      SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }
    filterChain.doFilter(request, response);
  }
}
