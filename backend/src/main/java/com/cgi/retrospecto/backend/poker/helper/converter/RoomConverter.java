package com.cgi.retrospecto.backend.poker.helper.converter;

import com.cgi.retrospecto.backend.poker.controller.dto.UserDetails;
import com.cgi.retrospecto.backend.poker.controller.dto.RoomDetails;
import com.cgi.retrospecto.backend.poker.domain.Room;
import com.cgi.retrospecto.backend.poker.domain.User;

import java.util.stream.Collectors;

public class RoomConverter {
    public static RoomDetails toDTO(Room entity) {
        RoomDetails dto = new RoomDetails();
        dto.setId(entity.getId());
        dto.setAuthor(entity.getAuthor());
        dto.setStories(entity.getStories());
        dto.setSelectedStoryId(entity.getSelectedStory() == null ? null : entity.getSelectedStory().getId());
        dto.setUsers(entity.getUsers().stream().map(u -> new UserDetails(u.getUsername())).collect(Collectors.toList()));

        return dto;
    }

    public static Room toEntity(User author) {
        Room entity = new Room();
        entity.setAuthor(author);
        entity.addUser(author);
        return entity;
    }
}
