package com.cgi.retrospecto.backend.poker.service.impl;

import com.cgi.retrospecto.backend.poker.domain.Room;
import com.cgi.retrospecto.backend.poker.domain.Story;
import com.cgi.retrospecto.backend.poker.domain.Vote;
import com.cgi.retrospecto.backend.poker.domain.VoteResult;
import com.cgi.retrospecto.backend.poker.exception.RoomNotFoundException;
import com.cgi.retrospecto.backend.poker.exception.StoryNotFoundException;
import com.cgi.retrospecto.backend.poker.repository.RoomRepository;
import com.cgi.retrospecto.backend.poker.service.RoomService;
import com.cgi.retrospecto.backend.util.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomRepository repo;

    @Override
    public Room createRoom(Room room) {
        room.setId(Helper.generateId(repo.getPokerRoomIds()));
        room.setLastActionSubmittedTime(LocalDateTime.now());
        repo.addRoom(room);
        return room;
    }

    @Override
    public Story createStory(int roomId, Story story) throws RoomNotFoundException {
        final Room room = repo.getPokerRoom(roomId);
        if (room == null) {
            throw new RoomNotFoundException(String.format("PokerRoom with given id does not exists. [id=%s]", roomId));
        }

        story.setId(Helper.generateId(room.getStoriesIdsList()));
        room.addStory(story);
        room.setLastActionSubmittedTime(LocalDateTime.now());

        return story;
    }

    @Override
    public Room getPokerRoom(int id) throws RoomNotFoundException {
        return repo.getPokerRoom(id);
    }

    @Override
    public Vote vote(int roomId, int storyId, Vote vote) throws RoomNotFoundException, StoryNotFoundException {
        Story story = repo.getPokerStory(roomId, storyId);
        VoteResult pvr = getVotingOrCreateNew(story);
        pvr.addVote(vote);

        return vote;
    }

    @Override
    public void closeVoting(int roomId, int storyId) throws RoomNotFoundException, StoryNotFoundException {
        VoteResult pvr = getVotingOrCreateNew(repo.getPokerStory(roomId, storyId));
        pvr.setLocked(true);
    }

    @Override
    public void setSelectedStory(int roomId, int storyId) throws RoomNotFoundException, StoryNotFoundException {
        Story story = repo.getPokerStory(roomId, storyId);
        Room room = getPokerRoom(roomId);
        room.setSelectedStory(story);
    }

    private VoteResult getVotingOrCreateNew(Story story) {
        VoteResult vr = story.getOpenPokerVoteResult();

        if (vr == null) {
            vr = new VoteResult();
            vr.setId(story.generateNextPokerResultId());
            vr.setLocked(false);

            story.getVoteResults().add(vr);
        }

        return vr;
    }
}
