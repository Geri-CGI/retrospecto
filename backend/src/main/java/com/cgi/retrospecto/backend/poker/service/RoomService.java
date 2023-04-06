package com.cgi.retrospecto.backend.poker.service;

import com.cgi.retrospecto.backend.poker.domain.*;
import com.cgi.retrospecto.backend.poker.exception.RoomNotFoundException;
import com.cgi.retrospecto.backend.poker.exception.StoryNotFoundException;
import com.cgi.retrospecto.backend.poker.exception.UsernameAlreadyInUseException;
import com.cgi.retrospecto.backend.poker.repository.RoomRepository;
import com.cgi.retrospecto.backend.util.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
public class RoomService {

    @Autowired
    private RoomRepository repo;

    public Room createRoom(Room room) {
        room.setId(Helper.generateId());
        room.setLastActionSubmittedTime(LocalDateTime.now());
        repo.addRoom(room);
        return room;
    }

    public Story createStory(int roomId, Story story) throws RoomNotFoundException {
        final Room room = repo.getPokerRoom(roomId);

        story.setId(Helper.generateId());

        if (room.getStories().size() == 0) {
            story.setDisabled(false);
        } else {
            story.setDisabled(true);
        }

        room.addStory(story);
        room.setLastActionSubmittedTime(LocalDateTime.now());

        return story;
    }

    public Room getPokerRoom(int id) throws RoomNotFoundException {
        return repo.getPokerRoom(id);
    }

    public void addUser(int id, User user) throws RoomNotFoundException, UsernameAlreadyInUseException {
        Room room = getPokerRoom(id);
        if (room.getUsers().contains(user)) {
            throw new UsernameAlreadyInUseException("Username already in use!");
        }
        room.addUser(user);
    }

    public VoteResult vote(int roomId, int storyId, Vote vote) throws RoomNotFoundException, StoryNotFoundException {
        Story story = repo.getPokerStory(roomId, storyId);
        VoteResult voteResult = getVotingOrCreateNew(story);
        voteResult.addVote(vote);

        return voteResult;
    }

    public VoteResult closeVoting(int roomId, int storyId) throws RoomNotFoundException, StoryNotFoundException {
        Story story = repo.getPokerStory(roomId, storyId);
        VoteResult voteResult = story.getOpenPokerVoteResult();
        if (voteResult != null) {
            voteResult.setLocked(true);
        }

        return voteResult;
    }

    public boolean setSelectedStory(int roomId, int storyId) throws RoomNotFoundException, StoryNotFoundException {
        Story story = repo.getPokerStory(roomId, storyId);
        story.setDisabled(false);
        Room room = getPokerRoom(roomId);
        room.setSelectedStory(story);

        return story.isDisabled();
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

    public VoteResult openVoting(int roomId, int storyId) throws RoomNotFoundException, StoryNotFoundException {
        return getVotingOrCreateNew(repo.getPokerStory(roomId, storyId));
    }

    public String removeUser(int roomId, User user) throws RoomNotFoundException {
        getPokerRoom(roomId)
                .getUsers()
                  .removeIf(u -> u.getUsername().equals(user.getUsername()));

        return user.getUsername();
    }
}
