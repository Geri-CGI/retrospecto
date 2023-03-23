package com.cgi.retrospecto.backend.poker.repository;

import com.cgi.retrospecto.backend.poker.domain.Room;
import com.cgi.retrospecto.backend.poker.domain.Story;
import com.cgi.retrospecto.backend.poker.exception.RoomNotFoundException;
import com.cgi.retrospecto.backend.poker.exception.StoryNotFoundException;
import com.cgi.retrospecto.backend.util.Helper;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@EnableScheduling
public class RoomRepository {
    private static final Map<Integer, Room> pokerRoomsMap = new HashMap<>();

    public Room getPokerRoom(int roomId) throws RoomNotFoundException {
        Room room = pokerRoomsMap.get(roomId);

        if (room == null) {
            throw new RoomNotFoundException(String.format("PokerRoom with given id does not exists. [id=%s]", roomId));
        }

        return room;
    }

    // @formatter:off
    public Story getPokerStory(int roomId, int storyId) throws RoomNotFoundException, StoryNotFoundException {
        Room room = getPokerRoom(roomId);

        return room.getStories()
                .stream()
                .filter(s -> s.getId() == storyId)
                .findFirst()
                .orElseThrow(() -> new StoryNotFoundException(String.format("PokerStory with given id in the given room does not exists. [roomId=%s, storyId=%s]", roomId, storyId)));
    }
    // @formatter:on

    public void addRoom(Room room) {
        pokerRoomsMap.put(room.getId(), room);
    }

    // @formatter:off
    public List<Integer> getPokerRoomIds() {
        return pokerRoomsMap.values()
                .stream()
                .map(Room::getId)
                .collect(Collectors.toList());
    }
    // @formatter:on
    // @formatter:off
    @Scheduled(fixedRate = 60 * 60 * 1000)
    private void purgeInactivePokerRooms() {
        pokerRoomsMap.entrySet()
                .removeIf(e ->
                        Helper.isOlderThanXHour(
                                e.getValue().getLastActionSubmittedTime(),
                                1)
                );
    }
    // @formatter:on
}
