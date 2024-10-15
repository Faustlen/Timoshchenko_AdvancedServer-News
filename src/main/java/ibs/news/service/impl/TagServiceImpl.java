package ibs.news.service.impl;

import ibs.news.entity.TagEntity;
import ibs.news.repository.TagRepository;
import ibs.news.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepo;

    @Override
    @Transactional
    public synchronized Set<TagEntity> createTags(Set<String> tagsTitles) {
        Set<TagEntity> existingTags = tagRepo.findTags(tagsTitles);

        Set<TagEntity> newTags = new HashSet<>();
        for (String title : tagsTitles) {
            if (existingTags.stream().noneMatch(tag -> tag.getTitle().equals(title))) {
                newTags.add(new TagEntity(title));
            }
        }

        if (!newTags.isEmpty()) {
            tagRepo.saveAll(newTags);
            existingTags.addAll(newTags);
        }

        return existingTags;
    }
}
