package ibs.news.controller;

import ibs.news.constrants.ValidationConstants;
import ibs.news.dto.response.PublicUserView;
import ibs.news.dto.response.common.CustomSuccessResponse;
import ibs.news.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.UUID;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/user")
@Validated
public class UserController {

    private final UserServiceImpl userService;

    @GetMapping
    public CustomSuccessResponse<?> getAllUsersController() {

        return null;
    }
    @GetMapping("/info")
    public CustomSuccessResponse<PublicUserView> getUserInfoController() {

        return userService.getUserInfoService();
    }

    @GetMapping("/{id}")
    public CustomSuccessResponse<?> getUserInfoByIdController(@PathVariable @UUID(message = ValidationConstants.MAX_UPLOAD_SIZE_EXCEEDED) String id) {

        return null;
    }

    @PutMapping
    public CustomSuccessResponse<?> replaceUserController() {

        return null;
    }

    @DeleteMapping
    public CustomSuccessResponse<?> deleteUserController() {

        return null;
    }
}
