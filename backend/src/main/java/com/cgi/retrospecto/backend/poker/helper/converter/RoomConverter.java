package com.cgi.retrospecto.backend.poker.helper.converter;

import com.cgi.retrospecto.backend.poker.controller.dto.JoinedUserDetails;
import com.cgi.retrospecto.backend.poker.controller.dto.RoomDetails;
import com.cgi.retrospecto.backend.poker.domain.Room;

import java.util.stream.Collectors;

public class RoomConverter {
    public static RoomDetails toDTO(Room entity) {
        RoomDetails dto = new RoomDetails();
        dto.setId(entity.getId());
        dto.setAuthor(entity.getAuthor());
        dto.setStories(entity.getStories());
        dto.setSelectedStoryId(entity.getSelectedStory() == null ? null : entity.getSelectedStory().getId());
        dto.setUsers(entity.getUsers().stream().map(JoinedUserDetails::new).collect(Collectors.toList()));

        return dto;
    }

    public static Room toEntity(String author) {
        Room entity = new Room();
        entity.setAuthor(author);
        entity.addUser(author);
        return entity;
    }
}
