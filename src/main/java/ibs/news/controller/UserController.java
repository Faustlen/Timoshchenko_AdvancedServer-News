package ibs.news.controller;

import ibs.news.constrants.ValidationConstants;
import ibs.news.dto.request.UserNewDataRequest;
import ibs.news.dto.response.PublicUserResponse;
import ibs.news.dto.response.common.BaseSuccessResponse;
import ibs.news.dto.response.common.CustomSuccessResponse;
import ibs.news.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.UUID;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("user")
@Validated
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<CustomSuccessResponse<List<PublicUserResponse>>> getAllUsersController() {
        return ResponseEntity.ok(userService.getAllUsersService());
    }

    @GetMapping("/info")
    public ResponseEntity<CustomSuccessResponse<PublicUserResponse>> getUserInfoController() {
        return ResponseEntity.ok(userService.getUserInfoService());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomSuccessResponse<PublicUserResponse>> getUserInfoByIdController(
            @PathVariable @UUID(message = ValidationConstants.MAX_UPLOAD_SIZE_EXCEEDED) String id) {
        return ResponseEntity.ok(userService.getUserInfoByIdService(id));
    }

    @PutMapping
    public ResponseEntity<CustomSuccessResponse<PublicUserResponse>> replaceUserController(
            @RequestBody @Valid UserNewDataRequest dto) {
        return ResponseEntity.ok(userService.replaceUserService(dto));
    }

    @DeleteMapping
    public ResponseEntity<BaseSuccessResponse> deleteUserController() {
        userService.deleteUserService();
        return ResponseEntity.ok(new BaseSuccessResponse());
    }
}
