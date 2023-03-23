package com.cgi.retrospecto.backend.poker.helper.converter;

import com.cgi.retrospecto.backend.poker.controller.dto.CreateRoomDTO;
import com.cgi.retrospecto.backend.poker.controller.dto.RoomDTO;
import com.cgi.retrospecto.backend.poker.domain.Room;

import java.util.stream.Collectors;

public class RoomConverter {
    // @formatter:off
    public static RoomDTO toDTO(Room entity) {
        RoomDTO dto = new RoomDTO();
        dto.setId(entity.getId());
        dto.setAuthor(entity.getAuthor());
        dto.setStories(entity.getStories());
        dto.setSelectedStoryId(entity.getSelectedStory() == null ? null : entity.getSelectedStory().getId());
        dto.setUsers(entity.getUsers());

        return dto;
    }

    public static Room toEntity(CreateRoomDTO dto) {
        Room entity = new Room();
        entity.setAuthor(dto.getAuthor());
        entity.addUser(dto.getAuthor());
        return entity;
    }
    // @formatter:on
}
