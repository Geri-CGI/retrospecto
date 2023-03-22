package com.cgi.retrospecto.backend.poker.service;

import com.cgi.retrospecto.backend.poker.domain.Room;
import com.cgi.retrospecto.backend.poker.domain.Story;
import com.cgi.retrospecto.backend.poker.domain.Vote;
import com.cgi.retrospecto.backend.poker.exception.RoomNotFoundException;
import com.cgi.retrospecto.backend.poker.exception.StoryNotFoundException;

public interface RoomService {
    Room createRoom(Room room);

    Story createStory(int roomId, Story story) throws RoomNotFoundException;

    Room getPokerRoom(int id) throws RoomNotFoundException;

    Vote vote(int roomId, int storyId, Vote vote) throws RoomNotFoundException, StoryNotFoundException;

    void closeVoting(int roomId, int storyId) throws RoomNotFoundException, StoryNotFoundException;

    void setSelectedStory(int roomId, int storyId) throws RoomNotFoundException, StoryNotFoundException;


}
