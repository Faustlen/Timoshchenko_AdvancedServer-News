package ibs.news.service.listeners;

import ibs.news.entity.NewsEntity;
import jakarta.persistence.PreRemove;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;

@Setter
public class NewsListener {

    @Value("${files.shelter.path}")
    private String shelter;

    @PreRemove
    public void preRemove(NewsEntity news) {
        String avatarPath = System.getProperty("user.dir") + shelter +
                news.getImage().substring(news.getImage().lastIndexOf("/") + 1);

        File file = new File(avatarPath);
        if (file.exists()) {
            file.delete();
        }
    }
}
