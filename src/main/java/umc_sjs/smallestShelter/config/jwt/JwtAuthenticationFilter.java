
package umc_sjs.smallestShelter.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import umc_sjs.smallestShelter.config.auth.PrincipalDetails;
import umc_sjs.smallestShelter.user.userDto.LoginDto;
import umc_sjs.smallestShelter.user.userDto.LoginRes;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        ObjectMapper om = new ObjectMapper();
        LoginDto loginDto = null;
        try{
            loginDto = om.readValue(request.getInputStream(), LoginDto.class);

        } catch(IOException e){
            e.printStackTrace();
        }

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

        Authentication authentication =
                authenticationManager.authenticate(authenticationToken);

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        System.out.println("로그인 완료 : " + principalDetails.getUser().getUsername());

        return authentication;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        String jwtToken = JWT.create()
                .withSubject(principalDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis()+JwtProperties.EXPIRATION_TIME))
                .withClaim("id", principalDetails.getUser().getIdx())
                .withClaim("username", principalDetails.getUser().getUsername())
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));

        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX+jwtToken);

        //body에 추가 정보 응답
        LoginRes loginRes = new LoginRes();
        loginRes.setUserIdx(principalDetails.getUser().getIdx());
        loginRes.setName(principalDetails.getUser().getName());
        loginRes.setRole(principalDetails.getUser().getRole().toString());
        loginRes.setOrganizationName(principalDetails.getUser().getOrganizationName());
        loginRes.setProfileImgUrl(principalDetails.getUser().getProfileImgUrl());
        loginRes.setPhoneNumber(principalDetails.getUser().getPhoneNumber());
        loginRes.setEmail(principalDetails.getUser().getEmail());
        loginRes.setAddress(principalDetails.getUser().getAddress());

        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        MediaType jsonMimeType = MediaType.APPLICATION_JSON;

        System.out.println("jsonResponse" + loginRes);

        jsonConverter.write(loginRes, jsonMimeType, new ServletServerHttpResponse(response));
    }
}
