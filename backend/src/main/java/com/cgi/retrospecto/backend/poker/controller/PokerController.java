package com.cgi.retrospecto.backend.poker.controller;

import com.cgi.retrospecto.backend.poker.api.PokerConstants;
import com.cgi.retrospecto.backend.poker.controller.dto.*;
import com.cgi.retrospecto.backend.poker.domain.Room;
import com.cgi.retrospecto.backend.poker.domain.Story;
import com.cgi.retrospecto.backend.poker.domain.Vote;
import com.cgi.retrospecto.backend.poker.domain.VoteResult;
import com.cgi.retrospecto.backend.poker.exception.RoomNotFoundException;
import com.cgi.retrospecto.backend.poker.exception.StoryNotFoundException;
import com.cgi.retrospecto.backend.poker.exception.UsernameAlreadyInUseException;
import com.cgi.retrospecto.backend.poker.helper.converter.RoomConverter;
import com.cgi.retrospecto.backend.poker.helper.converter.StoryConverter;
import com.cgi.retrospecto.backend.poker.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    @RequestMapping(path = ROOM, method = RequestMethod.POST, consumes = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<RoomDetails> createRoom(@RequestBody String author) {
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
            @PathVariable(PokerConstants.ID) int id,
            @PathVariable(PokerConstants.USERNAME) String username)
            throws RoomNotFoundException {
        try {
            Room room = roomService.addUser(id, username);
            return new ResponseEntity<>(RoomConverter.toDTO(room), HttpStatus.OK);
        } catch (UsernameAlreadyInUseException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @MessageMapping(ADD_USER)
    @SendTo(ADD_USER_TOPIC)
    public ResponseEntity<JoinedUserDetails> addUser(
            @DestinationVariable(ROOM_ID) int roomId,
            @Payload String username) {
        return new ResponseEntity<>(new JoinedUserDetails(username), HttpStatus.OK);
    }

    @MessageMapping(REMOVE_USER)
    @SendTo(REMOVE_USER_TOPIC)
    public ResponseEntity<JoinedUserDetails> removeUser(
            @DestinationVariable(ROOM_ID) int roomId,
            @Payload String username) throws RoomNotFoundException {
        return new ResponseEntity<>(
                new JoinedUserDetails(
                        roomService.removeUser(
                                roomId,
                                username)
                ), HttpStatus.OK);
    }

    @MessageMapping(ADD_STORY)
    @SendTo(ADD_STORY_TOPIC)
    public ResponseEntity<Story> createStory(
            @DestinationVariable(ROOM_ID) int roomId,
            @Payload String story) throws RoomNotFoundException {
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
            @Payload Vote dto) throws RoomNotFoundException, StoryNotFoundException {
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
    public ResponseEntity<OpenVote> openVoting(
            @DestinationVariable(ROOM_ID) int roomId,
            @DestinationVariable(STORY_ID) int storyId)
            throws RoomNotFoundException, StoryNotFoundException {
        return new ResponseEntity<>(
                new OpenVote(
                        true,
                        storyId,
                        roomService.openVoting(roomId, storyId)
                ), HttpStatus.OK);
    }

    @MessageMapping(CLOSE_VOTING)
    @SendTo(CLOSE_VOTING_TOPIC)
    public ResponseEntity<OpenVote> closeVoting(
            @DestinationVariable(ROOM_ID) int roomId,
            @DestinationVariable(STORY_ID) int storyId)
            throws RoomNotFoundException, StoryNotFoundException {
        return new ResponseEntity<>(
                new OpenVote(
                        false,
                        storyId,
                        roomService.closeVoting(roomId, storyId)
                ), HttpStatus.OK);
    }
}
