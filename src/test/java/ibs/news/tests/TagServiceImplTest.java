package ibs.news.tests;

import ibs.news.constrants.Constants;
import ibs.news.entity.TagEntity;
import ibs.news.repository.TagRepository;
import ibs.news.service.impl.TagServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TagServiceImplTest implements Constants {

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private TagServiceImpl tagService;

    Set<String> tagsTitles;

    @BeforeEach
    void SetUp() {
        MockitoAnnotations.openMocks(this);
        tagsTitles = createTagsTitles();
    }

    @Test
    void createTagsShouldCreateNewTagsWhenTagsDoNotExist() {
        when(tagRepository.findTags(tagsTitles)).thenReturn(new HashSet<>());

        Set<TagEntity> createdTags = tagService.createTags(tagsTitles);

        assertEquals(2, createdTags.size());
        verify(tagRepository, times(1)).saveAll(anySet());
    }

    @Test
    void createTagsShouldReturnExistingTagsWhenTagsExist() {
        Set<TagEntity> existingTags = new HashSet<>();
        existingTags.add(new TagEntity(TAG));

        when(tagRepository.findTags(tagsTitles)).thenReturn(existingTags);

        Set<TagEntity> resultTags = tagService.createTags(tagsTitles);

        assertEquals(2, resultTags.size());
        verify(tagRepository, times(1)).saveAll(anySet());
    }

    @Test
    void createTags_ShouldNotSaveAnyTags_WhenAllTagsExist() {
        Set<TagEntity> existingTags = new HashSet<>();
        existingTags.add(new TagEntity(TAG));
        existingTags.add(new TagEntity(ANOTHER_TAG));

        when(tagRepository.findTags(tagsTitles)).thenReturn(existingTags);

        Set<TagEntity> resultTags = tagService.createTags(tagsTitles);

        assertEquals(2, resultTags.size());
        verify(tagRepository, times(0)).saveAll(anySet());
    }
}
