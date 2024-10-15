package ibs.news.constrants;

import ibs.news.dto.request.AuthUserRequest;
import ibs.news.dto.request.CreateNewsRequest;
import ibs.news.dto.request.RegisterUserRequest;
import ibs.news.dto.request.UserNewDataRequest;
import ibs.news.dto.response.LoginUserResponse;
import ibs.news.dto.response.PublicUserResponse;
import ibs.news.entity.NewsEntity;
import ibs.news.entity.TagEntity;
import ibs.news.entity.UserEntity;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;

public class Constants {
    public static final UUID USER_UUID = UUID.randomUUID();
    public static final Long NEWS_ID = Long.valueOf(randomNumeric(3));
    public static final String TITLE = randomAlphanumeric(3, 25);
    public static final String DESCRIPTION = randomAlphanumeric(3, 25);
    public static final String IMAGE = randomAlphanumeric(3, 25);
    public static final String TAG = randomAlphanumeric(3, 25);
    public static final String ANOTHER_TAG = randomAlphanumeric(3, 25);
    public static final String NAME = randomAlphanumeric(3, 25);
    public static final String EMAIL = randomAlphanumeric(3, 25) + '@' + randomAlphanumeric(3, 25) + ".com";
    public static final String ANOTHER_EMAIL = randomAlphanumeric(3, 25) + '@' + randomAlphanumeric(3, 25) + ".com";
    public static final String PASSWORD = randomAlphanumeric(3, 25);
    public static final String ROLE = randomAlphanumeric(3, 25);
    public static final String AVATAR = randomAlphanumeric(3, 25);
    public static final String TOKEN = randomAlphanumeric(30);
    public static final String FILE = randomAlphanumeric(3,25) + ".txt";
    public static final String TEMP_SHELTER = "/src/test/java/ibs/news/files/";
    public static final String DOWNLOAD_URL = "http://localhost:8080/api/v1/file/";

    public static UserEntity createUserEntity() {
        UserEntity userEntity = new UserEntity();
        userEntity.setName(NAME);
        userEntity.setEmail(EMAIL);
        userEntity.setPassword(PASSWORD);
        userEntity.setRole(ROLE);
        userEntity.setAvatar(AVATAR);

        return userEntity;
    }

    public static UserEntity createAnotherUserEntity() {
        UserEntity userEntity = new UserEntity();
        userEntity.setName(NAME);
        userEntity.setEmail(ANOTHER_EMAIL);
        userEntity.setPassword(PASSWORD);
        userEntity.setRole(ROLE);
        userEntity.setAvatar(AVATAR);

        return userEntity;
    }

    public static RegisterUserRequest createRegisterUserRequest() {
        return new RegisterUserRequest(NAME, EMAIL, PASSWORD, ROLE, AVATAR);
    }

    public static LoginUserResponse creteLoginUserResponse() {
        return new LoginUserResponse(null, NAME, EMAIL, ROLE, AVATAR, null);
    }

    public static AuthUserRequest createAuthUserRequest() {
        return new AuthUserRequest(EMAIL, PASSWORD);
    }

    public static PublicUserResponse createPublicUserView() {
        return new PublicUserResponse(USER_UUID, NAME, EMAIL, ROLE, AVATAR);
    }

    public static UserNewDataRequest createUserNewDataRequest() {
        return new UserNewDataRequest(NAME, EMAIL, ROLE, AVATAR);
    }

    public static CreateNewsRequest createCreateNewsRequest() {
        return new CreateNewsRequest(TITLE, DESCRIPTION, IMAGE, Set.of(TAG));
    }

    public static TagEntity createTagEntity() {
        return new TagEntity(TAG);
    }

    public static NewsEntity createNewsEntity() {
        NewsEntity newsEntity = new NewsEntity();
        newsEntity.setId(NEWS_ID);
        newsEntity.setAuthor(createUserEntity());
        newsEntity.setTitle(TITLE);
        newsEntity.setDescription(DESCRIPTION);
        newsEntity.setImage(IMAGE);
        newsEntity.setTags(Set.of(createTagEntity()));

        return newsEntity;
    }

    public static Set<String> createTagsTitles() {
        Set<String> tags = new HashSet<>();
        tags.add(TAG);
        tags.add(ANOTHER_TAG);

        return tags;
    }
}