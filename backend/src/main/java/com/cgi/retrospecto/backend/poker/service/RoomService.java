package com.cgi.retrospecto.backend.poker.service;

import com.cgi.retrospecto.backend.poker.domain.Room;
import com.cgi.retrospecto.backend.poker.domain.Story;
import com.cgi.retrospecto.backend.poker.domain.Vote;
import com.cgi.retrospecto.backend.poker.domain.VoteResult;
import com.cgi.retrospecto.backend.poker.exception.RoomNotFoundException;
import com.cgi.retrospecto.backend.poker.exception.StoryNotFoundException;
import com.cgi.retrospecto.backend.poker.repository.RoomRepository;
import com.cgi.retrospecto.backend.util.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RoomService {

    @Autowired
    private RoomRepository repo;

    public Room createRoom(Room room) {
        room.setId(Helper.generateId(repo.getPokerRoomIds()));
        room.setLastActionSubmittedTime(LocalDateTime.now());
        repo.addRoom(room);
        return room;
    }

    public Story createStory(int roomId, Story story) throws RoomNotFoundException {
        final Room room = repo.getPokerRoom(roomId);

        story.setId(Helper.generateId(room.getStoriesIdsList()));
        room.addStory(story);
        room.setLastActionSubmittedTime(LocalDateTime.now());

        return story;
    }

    public Room getPokerRoom(int id) throws RoomNotFoundException {
        return repo.getPokerRoom(id);
    }

    public Vote vote(int roomId, int storyId, Vote vote) throws RoomNotFoundException, StoryNotFoundException {
        Story story = repo.getPokerStory(roomId, storyId);
        VoteResult pvr = getVotingOrCreateNew(story);
        pvr.addVote(vote);

        return vote;
    }

    public void closeVoting(int roomId, int storyId) throws RoomNotFoundException, StoryNotFoundException {
        Story story = repo.getPokerStory(roomId, storyId);
        VoteResult voteResult = story.getOpenPokerVoteResult();
        if (voteResult != null) {
            voteResult.setLocked(true);
        }
    }

    public void setSelectedStory(int roomId, int storyId) throws RoomNotFoundException, StoryNotFoundException {
        Story story = repo.getPokerStory(roomId, storyId);
        Room room = getPokerRoom(roomId);
        room.setSelectedStory(story);
    }

    private VoteResult getVotingOrCreateNew(Story story) {
        VoteResult voteResult = story.getOpenPokerVoteResult();

        if (voteResult == null) {
            voteResult = new VoteResult();
            voteResult.setId(story.generateNextPokerResultId());
            voteResult.setLocked(false);

            story.getVoteResults().add(voteResult);
        }

        return voteResult;
    }
}
