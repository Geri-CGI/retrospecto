package com.cgi.retrospecto.backend.poker.helper.converter;

import com.cgi.retrospecto.backend.poker.controller.dto.NewRoomDetails;
import com.cgi.retrospecto.backend.poker.controller.dto.RoomDetails;
import com.cgi.retrospecto.backend.poker.domain.Room;

public class RoomConverter {
    // @formatter:off
    public static RoomDetails toDTO(Room entity) {
        RoomDetails dto = new RoomDetails();
        dto.setId(entity.getId());
        dto.setAuthor(entity.getAuthor());
        dto.setStories(entity.getStories());
        dto.setSelectedStoryId(entity.getSelectedStory() == null ? null : entity.getSelectedStory().getId());
        dto.setUsers(entity.getUsers());

        return dto;
    }

    public static Room toEntity(NewRoomDetails dto) {
        Room entity = new Room();
        entity.setAuthor(dto.getAuthor());
        entity.addUser(dto.getAuthor());
        return entity;
    }
    // @formatter:on
}
