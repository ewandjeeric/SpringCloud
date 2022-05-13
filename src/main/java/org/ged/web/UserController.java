package org.ged.web;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ged.bean.RegistrationForm;
import org.ged.entities.AppUser;
import org.ged.security.SecurityConstants;
import org.ged.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@CrossOrigin(value = "http://localhost:4200", allowCredentials = "true")
public class UserController {
	@Autowired
	AccountService accservice;

	@PostMapping("/users")
	public AppUser signUp(@RequestBody RegistrationForm data) {

		String username = data.getUsername();
		AppUser u = accservice.findByUsername(username);

		if (u != null)
			throw new RuntimeException("Cet utilisateur existe déjà, essayez avec un autre nom d'utilisateur");

		String password = data.getPassword();
		String repassword = data.getRepassword();

		if (!password.equals(repassword))
			throw new RuntimeException("Les Mots de passe sont different");

		AppUser user = new AppUser();
		user.setPassword(data.getPassword());
		user.setUsername(data.getUsername());

		accservice.saveUser(user);
		accservice.AddRoleUser(username, "USER");
		return user;

	}

	@GetMapping("/refreshToken")
	public void refreshToken(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		String JwtToken = request.getHeader("Authorization");
		if (JwtToken != null && JwtToken.startsWith("Bearer")) {
			try {

				String jwt = JwtToken.substring(7);
				Algorithm algorithm = Algorithm.HMAC512("MySecret@1234");
				JWTVerifier jwtverifier = JWT.require(algorithm).build();
				DecodedJWT decodejwt = jwtverifier.verify(jwt);
				String username = decodejwt.getSubject();

				AppUser springUser = accservice.findByUsername(username);

				String JwtaccessToken = JWT.create().withSubject(springUser.getUsername())
						.withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
						.withIssuer(request.getRequestURL().toString())
						.withClaim("roles",
								springUser.getRoles().stream().map(r -> r.getRole()).collect(Collectors.toList()))
						.sign(Algorithm.HMAC512(SecurityConstants.SECRET));
				response.setHeader(SecurityConstants.HEADER_STRING, JwtaccessToken);

				Map<String, String> idToken = new HashMap<>();
				idToken.put("Acces-Token", SecurityConstants.TOKEN_PREFIX + JwtToken);
				idToken.put("Refresh-Token", jwt);
				response.setContentType("application/json");
				new ObjectMapper().writeValue(response.getOutputStream(), idToken);

			} catch (Exception e) {
				response.setHeader("error-message", e.getMessage());
				response.sendError(HttpServletResponse.SC_FORBIDDEN);
			}

		} else {
			chain.doFilter(request, response);
		}

	}

}
