package ibs.news.controller;

import ibs.news.constrants.ValidationConstants;
import ibs.news.dto.request.UserNewDataRequest;
import ibs.news.dto.response.PublicUserView;
import ibs.news.dto.response.common.BaseSuccessResponse;
import ibs.news.dto.response.common.CustomSuccessResponse;
import ibs.news.service.impl.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.UUID;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/user")
@Validated
public class UserController {

    private final UserServiceImpl userService;

    @GetMapping
    public CustomSuccessResponse<List<PublicUserView>> getAllUsersController() {

        return userService.getAllUsersService();
    }

    @GetMapping("/info")
    public CustomSuccessResponse<PublicUserView> getUserInfoController() {

        return userService.getUserInfoService();
    }

    @GetMapping("/{id}")
    public CustomSuccessResponse<PublicUserView> getUserInfoByIdController(
            @PathVariable @UUID(message = ValidationConstants.MAX_UPLOAD_SIZE_EXCEEDED) String id) {

        return userService.getUserInfoByIdService(id);
    }

    @PutMapping
    public CustomSuccessResponse<PublicUserView> replaceUserController(@RequestBody @Valid UserNewDataRequest dto) {

        return userService.replaceUserService(dto);
    }

    @DeleteMapping
    public BaseSuccessResponse deleteUserController() {

        userService.deleteUserService();

        return new BaseSuccessResponse();
    }
}
