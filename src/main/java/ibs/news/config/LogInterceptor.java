package ibs.news.config;

import ibs.news.constrants.ValidationConstants;
import ibs.news.entity.LogEntity;
import ibs.news.repository.LogRepository;
import ibs.news.security.UserEntityDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class LogInterceptor implements HandlerInterceptor {

    private final LogRepository logRepo;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {

        String exception;
        if (!response.getHeaders(ValidationConstants.ERROR_MESSAGE).isEmpty()) {
            exception = response.getHeaders(ValidationConstants.ERROR_MESSAGE).toString();
        } else {
            exception = "no errors";
        }

        createLog(response.getStatus(), request.getMethod(), request.getRequestURI(), exception);
    }

    public void createLog(Integer status, String method, String uri, String exception) {

        LogEntity log = new LogEntity();

        log.setStatus(status);
        log.setMethod(method);
        log.setUri(uri);
        log.setException(exception);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() &&
                !(authentication.getPrincipal() instanceof String)) {

            var userEntityDetails = (UserEntityDetails) authentication.getPrincipal();
            log.setUseId(userEntityDetails.getId().toString());
        } else {
            log.setUseId("Anonymous");
        }

        logRepo.save(log);
    }
}
