package com.cgi.retrospecto.backend.poker.helper.converter;

import com.cgi.retrospecto.backend.poker.controller.dto.CreateStoryDTO;
import com.cgi.retrospecto.backend.poker.controller.dto.StoryDTO;
import com.cgi.retrospecto.backend.poker.domain.Story;

import java.util.stream.Collectors;

public class StoryConverter {
    // @formatter:off
    public static StoryDTO toDTO(Story entity) {
        StoryDTO dto = new StoryDTO();
        dto.setStory(entity.getStory());
        dto.setId(entity.getId());
        dto.setVoteResults(
                entity.getVoteResults()
                .stream()
                .map(VoteResultConverter::toDTO)
                .collect(Collectors.toList()));

        return dto;
    }

    public static Story toEntity(CreateStoryDTO dto, Story entity) {
        entity.setStory(dto.getStory());

        return entity;
    }
    // @formatter:on
}
