package com.cgi.retrospecto.backend.poker.helper.converter;

import com.cgi.retrospecto.backend.poker.domain.Story;

public class StoryConverter {
    public static Story toEntity(String story) {
        Story entity = new Story();
        entity.setStory(story);

        return entity;
    }
}
