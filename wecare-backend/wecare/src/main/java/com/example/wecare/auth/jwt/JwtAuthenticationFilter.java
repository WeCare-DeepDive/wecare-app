package com.example.wecare.auth.jwt;

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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // JWT 파싱
        String authHeader = request.getHeader("Authorization");

        // 헤더 포함 안된 요청의 경우 Request를 다음 필터로 넘김
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7); // "Bearer " 제거

        if (!jwtUtil.validateToken(token)) {
            throw new BadCredentialsException("유효하지 않은 토큰입니다."); //401
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
}
