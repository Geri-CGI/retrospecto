package com.cgi.retrospecto.backend.poker.controller;

import com.cgi.retrospecto.backend.poker.api.PokerConstants;
import com.cgi.retrospecto.backend.poker.controller.dto.*;
import com.cgi.retrospecto.backend.poker.domain.Room;
import com.cgi.retrospecto.backend.poker.domain.Story;
import com.cgi.retrospecto.backend.poker.domain.Vote;
import com.cgi.retrospecto.backend.poker.exception.RoomNotFoundException;
import com.cgi.retrospecto.backend.poker.exception.StoryNotFoundException;
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

@RestController
@RequestMapping(POKER)
@CrossOrigin
public class PokerController {
    @Autowired
    private RoomService roomService;

    // @formatter:off
    @ResponseBody
    @RequestMapping(path = ROOM, method = RequestMethod.POST)
    public ResponseEntity<RoomDTO> createRoom(@RequestBody CreateRoomDTO dto) {
        return new ResponseEntity<>(
                RoomConverter.toDTO(
                    roomService.createRoom(
                            RoomConverter.toEntity(dto)
                    )
                ), HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(path = GET_ROOM, method = RequestMethod.GET)
    public ResponseEntity<RoomDTO> getRoom(
            @PathVariable(PokerConstants.ID) int id,
            @PathVariable(PokerConstants.USERNAME) String username)
            throws RoomNotFoundException {
        Room room = roomService.getPokerRoom(id);
        room.addUser(username);
        return new ResponseEntity<>(RoomConverter.toDTO(room), HttpStatus.OK);
    }

    @MessageMapping(POKER + "/{roomId}/story/add")
    @SendTo("/topic" + POKER + "/{roomId}/story/add")
    public ResponseEntity<Story> createStory(
            @DestinationVariable int roomId,
            @Payload CreateStoryDTO dto) throws RoomNotFoundException {
        return new ResponseEntity<>(
                        roomService.createStory(roomId,
                                StoryConverter.toEntity(dto, new Story())
                ), HttpStatus.OK);
    }

    @MessageMapping(POKER + "/{roomId}/story/{storyId}/vote")
    @SendTo("/topic" + POKER + "/{roomId}/vote")
    public ResponseEntity<RoomDTO> vote(
            @DestinationVariable int roomId,
            @DestinationVariable int storyId,
            @Payload Vote dto) throws RoomNotFoundException, StoryNotFoundException {
        roomService.vote(roomId, storyId, dto);
        return new ResponseEntity<>(
                RoomConverter.toDTO(
                        roomService.getPokerRoom(roomId)
                ), HttpStatus.OK);
    }

    @MessageMapping(POKER + "/{roomId}/story/{storyId}/selected")
    @SendTo("/topic" + POKER + "/{roomId}/selectedStory")
    public ResponseEntity<SelectedStoryDTO> selectStory(
            @DestinationVariable int roomId,
            @DestinationVariable int storyId)
            throws RoomNotFoundException, StoryNotFoundException {
        roomService.setSelectedStory(roomId, storyId);
        return new ResponseEntity<>(new SelectedStoryDTO(roomId, storyId), HttpStatus.OK);
    }

    @MessageMapping(POKER + ROOM + "/{roomId}/story/{storyId}/vote/open")
    @SendTo("/topic" + POKER + "/{roomId}/vote/open-close")
    public ResponseEntity<VoteIsOpenDTO> openVoting(
            @DestinationVariable int roomId,
            @DestinationVariable int storyId) {
        return new ResponseEntity<>(new VoteIsOpenDTO(true), HttpStatus.OK);
    }

    @MessageMapping(POKER + ROOM + "/{roomId}/story/{storyId}/vote/close")
    @SendTo("/topic" + POKER + "/{roomId}/vote/open-close")
    public ResponseEntity<VoteIsOpenDTO> closeVoting(
            @DestinationVariable int roomId,
            @DestinationVariable int storyId)
            throws RoomNotFoundException, StoryNotFoundException {
        roomService.closeVoting(roomId, storyId);
        return new ResponseEntity<>(new VoteIsOpenDTO(false), HttpStatus.OK);
    }
    // @formatter:on
}
