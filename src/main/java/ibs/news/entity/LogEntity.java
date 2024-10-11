package ibs.news.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "logs", schema = "news_feed")
public class LogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private Integer status;

    private String exception;

    private String method;

    private String uri;

    private String useId;

    public LogEntity(Integer status, String exception, String method, String uri, String useId) {
        this.status = status;
        this.exception = exception;
        this.method = method;
        this.uri = uri;
        this.useId = useId;
    }
}
