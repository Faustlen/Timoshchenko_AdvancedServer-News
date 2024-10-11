package ibs.news.interceptor;

import ibs.news.constrants.ValidationConstants;
import ibs.news.entity.LogEntity;
import ibs.news.entity.UserEntity;
import ibs.news.repository.LogRepository;
import ibs.news.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class LogInterceptor implements HandlerInterceptor {
    private static final String ANON = "Anonymous";

    private final LogRepository logRepo;

    private final UserService userService;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        String exception;
        if (!response.getHeaders(ValidationConstants.ERROR_MESSAGE).isEmpty()) {
            exception = response.getHeaders(ValidationConstants.ERROR_MESSAGE).toString();
        } else {
            exception = ValidationConstants.NO_ERRORS;
        }

        LogEntity log;

        UserEntity userEntity = userService.getAuthorizedUser();
        if (userEntity != null) {
            log = new LogEntity(response.getStatus(), exception, request.getMethod(),
                    request.getRequestURI(), userEntity.getId().toString());
        } else {
            log = new LogEntity(response.getStatus(), exception, request.getMethod(),
                    request.getRequestURI(), ANON);
        }

        logRepo.save(log);
    }
}
