package com.helfit.wodiary.domain.user.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        // 로그인 실패 시 메시지 설정
        String errorMessage = "아이디 또는 비밀번호를 잘못입력하셨습니다.";
        // 로그인 페이지로 리디렉션하면서 에러 메시지를 파라미터로 추가
        response.sendRedirect("/login?error=true&exception=" + errorMessage);
    }
}

