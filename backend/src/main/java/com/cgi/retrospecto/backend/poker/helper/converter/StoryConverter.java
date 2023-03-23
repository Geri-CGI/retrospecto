package com.cgi.retrospecto.backend.poker.helper.converter;

import com.cgi.retrospecto.backend.poker.controller.dto.CreateStoryDetails;
import com.cgi.retrospecto.backend.poker.domain.Story;

public class StoryConverter {
    // @formatter:off
    public static Story toEntity(CreateStoryDetails dto, Story entity) {
        entity.setStory(dto.getStory());

        return entity;
    }
    // @formatter:on
}
