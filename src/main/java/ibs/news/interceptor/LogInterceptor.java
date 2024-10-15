package ibs.news.interceptor;

import ibs.news.constrants.MessageConstants;
import ibs.news.entity.LogEntity;
import ibs.news.entity.UserEntity;
import ibs.news.repository.LogRepository;
import ibs.news.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import java.util.Optional;

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
        if (!response.getHeaders(MessageConstants.ERROR_MESSAGE).isEmpty()) {
            exception = response.getHeaders(MessageConstants.ERROR_MESSAGE).toString();
        } else {
            exception = MessageConstants.NO_ERRORS;
        }

        Optional<UserEntity> optionalUserEntity = Optional.ofNullable(userService.getAuthorizedUser());

        String userId = optionalUserEntity
                .map(user -> user.getId().toString())
                .orElse(ANON);

        LogEntity log = new LogEntity(
                null,
                response.getStatus(),
                exception,
                request.getMethod(),
                request.getRequestURI(),
                userId
        );

        logRepo.save(log);
    }
}
