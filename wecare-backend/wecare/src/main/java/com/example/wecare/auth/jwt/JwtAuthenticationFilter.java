package com.example.wecare.auth.jwt;

import com.example.wecare.auth.exception.AuthAuthenticationEntryPoint;
import com.example.wecare.member.domain.Member;
import com.example.wecare.member.repository.MemberRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final JwtRedisService jwtRedisService;
    private final MemberRepository memberRepository;
    private final AuthAuthenticationEntryPoint authAuthenticationEntryPoint;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // JWT 파싱
        String token = resolveToken(request);

        // 헤더 포함 안된 요청의 경우 Request를 다음 필터로 넘김
        if (token != null && !jwtUtil.validateToken(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        try{
            if (!jwtUtil.validateToken(token) ||
                    jwtRedisService.isTokenLogout(token) ||
                    jwtRedisService.isTokenWithdrawn(token)) {
                throw new BadCredentialsException("유효하지 않은 토큰입니다."); //401
            }
        } catch (AuthenticationException e){
            SecurityContextHolder.clearContext();
            authAuthenticationEntryPoint.commence(request, response, e);
            return;
        }

        // 토큰으로부터 사용자 ID 파싱
        Long id = jwtUtil.getIdFromToken(token);

        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new BadCredentialsException("유효하지 않은 토큰입니다.")); //401

        //Security Context에 사용자 UUID 저장
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(member, null, member.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
    
    private String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }
}
