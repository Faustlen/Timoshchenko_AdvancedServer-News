package ibs.news.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ibs.news.config.LogInterceptor;
import ibs.news.constrants.ValidationConstants;
import ibs.news.dto.response.common.CustomSuccessResponse;
import ibs.news.error.ErrorCodes;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    private final LogInterceptor logInterceptor;

    @Override
    public void commence(
            HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException {
        List<Integer> errorCodes = new ArrayList<Integer>();
        Integer errorCode = ErrorCodes.getErrorCodeByMessage(ValidationConstants.UNAUTHORISED);
        errorCodes.add(errorCode);

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.getWriter().write(convertObjectToJson(new CustomSuccessResponse<>(errorCode, errorCodes)));

        logInterceptor.createLog(HttpStatus.UNAUTHORIZED.value(), request.getMethod(), request.getRequestURI(),
                ValidationConstants.UNAUTHORISED);

    }

    public String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }
}
