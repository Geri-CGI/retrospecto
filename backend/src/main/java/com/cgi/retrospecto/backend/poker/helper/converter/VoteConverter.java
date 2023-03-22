package com.cgi.retrospecto.backend.poker.helper.converter;

import com.cgi.retrospecto.backend.poker.controller.dto.VoteDTO;
import com.cgi.retrospecto.backend.poker.domain.Vote;
import org.springframework.stereotype.Component;

@Component
public class VoteConverter {
    public static VoteDTO toDTO(Vote entity) {
        VoteDTO dto = new VoteDTO();
        dto.setUsername(entity.getUsername());
        dto.setValue(entity.getValue());

        return dto;
    }

    public static Vote toEntity(VoteDTO dto, Vote entity) {
        entity.setValue(dto.getValue());
        entity.setUsername(dto.getUsername());

        return entity;
    }
}
