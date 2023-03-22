package com.cgi.retrospecto.backend.poker.helper.converter;

import com.cgi.retrospecto.backend.poker.controller.dto.VoteResultDTO;
import com.cgi.retrospecto.backend.poker.domain.Vote;
import com.cgi.retrospecto.backend.poker.domain.VoteResult;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class VoteResultConverter {
    // @formatter:off
    public static VoteResultDTO toDTO(VoteResult entity) {
        VoteResultDTO dto = new VoteResultDTO();
        dto.setId(entity.getId());
        dto.setVotes(
                entity.getVotes()
                 .stream()
                 .map(VoteConverter::toDTO)
                 .collect(Collectors.toList()));
        dto.setLocked(entity.isLocked());

        return dto;
    }

    public static VoteResult toEntity(VoteResultDTO dto, VoteResult entity) {
        entity.setId(dto.getId());
        entity.setLocked(entity.isLocked());
        entity.setVotes(
                dto.getVotes()
                .stream()
                .map(e -> VoteConverter.toEntity(e, new Vote()))
                .collect(Collectors.toList()));

        return entity;
    }
    // @formatter:on
}
