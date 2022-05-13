package org.ged.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

public class JWTAuthorizationFilter extends OncePerRequestFilter {

	/**
	 * La methode permet de verifier les permissions d'un utilisateur En cas
	 * d'envoie du Token de rafrechissemnet on effectue aucune action
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		/**
		 * On verifie si l'utilisateur souhaite rafraichire le token
		 */
		if (request.getServletPath().equals("/refreshToken")) {
			filterChain.doFilter(request, response);
		}
		/**
		 * On verifie les permissions de l'utilisateur si il souhaite avoir acces a une
		 * ressource
		 */
		else {
			// On recupere le Header de la requete
			String JwtToken = request.getHeader("Authorization");

			// On verifie si le Header n'est pas vide et qu'il commence par Bearer
			if (JwtToken != null && JwtToken.startsWith("Bearer ")) {
				try {

					// On supprime les 7 premiers caracteres de token
					String jwt = JwtToken.substring(7);

					// On decode a partir de l'algorithme de chiffrage initiale
					Algorithm algorithm = Algorithm.HMAC512("MySecret@1234");

					// On cree le verifier
					JWTVerifier jwtverifier = JWT.require(algorithm).build();

					// On verifie le jeton
					DecodedJWT decodejwt = jwtverifier.verify(jwt);

					// On recupere le username
					String username = decodejwt.getSubject();

					// On recupere les permissions contenus dans le token
					String[] permissions = decodejwt.getClaim("permissions").asArray(String.class);

					// On met tous les permissions dans un objet GrantedAuthority
					Collection<GrantedAuthority> authorities = new ArrayList<>();
					for (String r : permissions) {
						authorities.add(new SimpleGrantedAuthority(r));
					}

					// On creer un objet UsernamePasswordAuthenticationToken pour authentifier
					UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
							username, null, authorities);

					// On authenfier
					SecurityContextHolder.getContext().setAuthentication(authenticationToken);

					filterChain.doFilter(request, response);

				} catch (Exception e) {
					response.setHeader("error-message", e.getMessage());
					response.sendError(HttpServletResponse.SC_FORBIDDEN);
				}

			} else {
				// On passe
				filterChain.doFilter(request, response);
			}

		}

	}

}
