package com.cgi.retrospecto.backend.poker.controller;

import com.cgi.retrospecto.backend.poker.api.PokerConstants;
import com.cgi.retrospecto.backend.poker.controller.dto.*;
import com.cgi.retrospecto.backend.poker.domain.*;
import com.cgi.retrospecto.backend.poker.exception.RoomNotFoundException;
import com.cgi.retrospecto.backend.poker.exception.StoryNotFoundException;
import com.cgi.retrospecto.backend.poker.exception.UsernameAlreadyInUseException;
import com.cgi.retrospecto.backend.poker.helper.converter.RoomConverter;
import com.cgi.retrospecto.backend.poker.helper.converter.StoryConverter;
import com.cgi.retrospecto.backend.poker.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import static com.cgi.retrospecto.backend.poker.api.PokerConstants.ApiConstants.*;
import static com.cgi.retrospecto.backend.poker.api.PokerConstants.ROOM_ID;
import static com.cgi.retrospecto.backend.poker.api.PokerConstants.STORY_ID;
import static com.cgi.retrospecto.backend.poker.api.PokerConstants.WebSocketConstants.*;

@RestController
@RequestMapping(POKER)
@CrossOrigin
public class PokerController {
    @Autowired
    private RoomService roomService;

    @ResponseBody
    @RequestMapping(path = ROOM, method = RequestMethod.POST)
    public ResponseEntity<RoomDetails> createRoom(@RequestBody User author) {
        return new ResponseEntity<>(
                RoomConverter.toDTO(
                        roomService.createRoom(
                                RoomConverter.toEntity(author)
                        )
                ), HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(path = GET_ROOM, method = RequestMethod.GET)
    public ResponseEntity<RoomDetails> getRoom(
            @PathVariable(PokerConstants.ID) int id)
            throws RoomNotFoundException {
        return new ResponseEntity<>(
                RoomConverter.toDTO(
                        roomService.getPokerRoom(id)
                ), HttpStatus.OK);
    }

    @MessageMapping(ADD_USER)
    @SendTo(ADD_USER_TOPIC)
    public ResponseEntity<UserDetails> addUser(
            @DestinationVariable(ROOM_ID) int roomId,
            @Payload User user)
            throws RoomNotFoundException {
        try {
            roomService.addUser(roomId, user);
            return new ResponseEntity<>(new UserDetails(user.getUsername()), HttpStatus.OK);
        } catch (UsernameAlreadyInUseException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @MessageMapping(REMOVE_USER)
    @SendTo(REMOVE_USER_TOPIC)
    public ResponseEntity<UserDetails> removeUser(
            @DestinationVariable(ROOM_ID) int roomId,
            @Payload User user)
            throws RoomNotFoundException {
        return new ResponseEntity<>(
                new UserDetails(
                        roomService.removeUser(
                                roomId,
                                user)
                ), HttpStatus.OK);
    }

    @MessageMapping(ADD_STORY)
    @SendTo(ADD_STORY_TOPIC)
    public ResponseEntity<Story> createStory(
            @DestinationVariable(ROOM_ID) int roomId,
            @Payload String story)
            throws RoomNotFoundException {
        return new ResponseEntity<>(
                roomService.createStory(
                        roomId,
                        StoryConverter.toEntity(story)
                ), HttpStatus.OK);
    }

    @MessageMapping(VOTE)
    @SendTo(VOTE_TOPIC)
    public ResponseEntity<VoteResult> vote(
            @DestinationVariable(ROOM_ID) int roomId,
            @DestinationVariable(STORY_ID) int storyId,
            @Payload Vote dto)
            throws RoomNotFoundException, StoryNotFoundException {
        return new ResponseEntity<>(
                roomService.vote(
                        roomId,
                        storyId,
                        dto
                ), HttpStatus.OK);
    }

    // TODO: refactor
    @MessageMapping(SELECT_STORY)
    @SendTo(SELECT_STORY_TOPIC)
    public ResponseEntity<SelectedStoryDetails> selectStory(
            @DestinationVariable(ROOM_ID) int roomId,
            @DestinationVariable(STORY_ID) int storyId)
            throws RoomNotFoundException, StoryNotFoundException {
        return new ResponseEntity<>(
                new SelectedStoryDetails(
                        roomId,
                        storyId,
                        roomService.setSelectedStory(roomId, storyId)
                ), HttpStatus.OK);
    }

    @MessageMapping(OPEN_VOTING)
    @SendTo(OPEN_VOTING_TOPIC)
    public ResponseEntity<VoteResultDetails> openVoting(
            @DestinationVariable(ROOM_ID) int roomId,
            @DestinationVariable(STORY_ID) int storyId)
            throws RoomNotFoundException, StoryNotFoundException {
        return new ResponseEntity<>(
                new VoteResultDetails(
                        true,
                        storyId,
                        roomService.openVoting(roomId, storyId)
                ), HttpStatus.OK);
    }

    @MessageMapping(CLOSE_VOTING)
    @SendTo(CLOSE_VOTING_TOPIC)
    public ResponseEntity<VoteResultDetails> closeVoting(
            @DestinationVariable(ROOM_ID) int roomId,
            @DestinationVariable(STORY_ID) int storyId)
            throws RoomNotFoundException, StoryNotFoundException {
        return new ResponseEntity<>(
                new VoteResultDetails(
                        false,
                        storyId,
                        roomService.closeVoting(roomId, storyId)
                ), HttpStatus.OK);
    }
}
