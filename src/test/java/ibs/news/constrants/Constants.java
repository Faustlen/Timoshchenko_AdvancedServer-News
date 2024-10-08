package ibs.news.constrants;

import ibs.news.dto.request.AuthUserRequest;
import ibs.news.dto.request.CreateNewsRequest;
import ibs.news.dto.request.RegisterUserRequest;
import ibs.news.dto.request.UserNewDataRequest;
import ibs.news.dto.response.LoginUserResponse;
import ibs.news.dto.response.PublicUserView;
import ibs.news.entity.NewsEntity;
import ibs.news.entity.TagEntity;
import ibs.news.entity.UserEntity;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.apache.commons.lang3.RandomStringUtils.*;

public interface Constants {

    UUID USER_UUID = UUID.randomUUID();
    Long NEWS_ID = Long.valueOf(randomNumeric(3));
    String TITLE = randomAlphanumeric(3, 25);
    String DESCRIPTION = randomAlphanumeric(3, 25);
    String IMAGE = randomAlphanumeric(3, 25);
    String TAG = randomAlphanumeric(3, 25);
    String ANOTHER_TAG = randomAlphanumeric(3, 25);
    String NAME = randomAlphanumeric(3, 25);
    String EMAIL = randomAlphanumeric(3, 25) + '@' + randomAlphanumeric(3, 25) + ".com";
    String PASSWORD = randomAlphanumeric(3, 25);
    String ROLE = randomAlphanumeric(3, 25);
    String AVATAR = randomAlphanumeric(3, 25);
    String TOKEN = randomAlphanumeric(30);
    String FILE = randomAlphanumeric(3,25) + ".txt";

    default UserEntity createUserEntity() {
        UserEntity userEntity = new UserEntity();
        userEntity.setName(NAME);
        userEntity.setEmail(EMAIL);
        userEntity.setPassword(PASSWORD);
        userEntity.setRole(ROLE);
        userEntity.setAvatar(AVATAR);

        return userEntity;
    }

    default RegisterUserRequest createRegisterUserRequest() {
        return new RegisterUserRequest(NAME, EMAIL, PASSWORD, ROLE, AVATAR);
    }

    default LoginUserResponse creteLoginUserResponse() {
        return new LoginUserResponse(null, NAME, EMAIL, ROLE, AVATAR, null);
    }

    default AuthUserRequest createAuthUserRequest() {
        return new AuthUserRequest(EMAIL, PASSWORD);
    }

    default PublicUserView createPublicUserView() {
        return new PublicUserView(USER_UUID, NAME, EMAIL, ROLE, AVATAR);
    }

    default UserNewDataRequest createUserNewDataRequest() {
        return new UserNewDataRequest(NAME, EMAIL, ROLE, AVATAR);
    }

    default CreateNewsRequest createCreateNewsRequest() {
        return new CreateNewsRequest(TITLE, DESCRIPTION, IMAGE, Set.of(TAG));
    }

    default TagEntity createTagEntity() {
        return new TagEntity(TAG);
    }

    default NewsEntity createNewsEntity() {
        NewsEntity newsEntity = new NewsEntity();
        newsEntity.setId(NEWS_ID);
        newsEntity.setUserId(createUserEntity());
        newsEntity.setTitle(TITLE);
        newsEntity.setDescription(DESCRIPTION);
        newsEntity.setImage(IMAGE);
        newsEntity.setTags(Set.of(createTagEntity()));

        return newsEntity;
    }

    default Set<String> createTagsTitles() {
        Set<String> tags = new HashSet<>();
        tags.add(TAG);
        tags.add(ANOTHER_TAG);

        return tags;
    }
}