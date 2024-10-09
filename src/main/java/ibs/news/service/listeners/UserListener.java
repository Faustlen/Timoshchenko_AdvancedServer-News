package ibs.news.service.listeners;

import ibs.news.entity.UserEntity;
import ibs.news.repository.NewsRepository;
import jakarta.persistence.PreRemove;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;

import java.io.File;

@Setter
public class UserListener {

    private final NewsRepository newsRepo;

    @Value("${files.shelter.path}")
    private String shelter;

    public UserListener(@Lazy NewsRepository newsRepo) {
        this.newsRepo = newsRepo;
    }

    @PreRemove
    public void preRemove(UserEntity user) {
        String avatarPath = System.getProperty("user.dir") + shelter +
                user.getAvatar().substring(user.getAvatar().lastIndexOf("/") + 1);

        File file = new File(avatarPath);
        if (file.exists()) {
            file.delete();
        }

        newsRepo.deleteByUserIdId(user.getId());
    }
}
