package ibs.news.service;

import ibs.news.entity.TagEntity;

import java.util.Set;

public interface TagService {

    Set<TagEntity> createTags(Set<String> tagsTitles);
}
