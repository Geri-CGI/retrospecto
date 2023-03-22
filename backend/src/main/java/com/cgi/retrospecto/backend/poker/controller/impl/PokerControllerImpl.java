package com.cgi.retrospecto.backend.poker.controller.impl;

import com.cgi.retrospecto.backend.poker.api.PokerConstants;
import com.cgi.retrospecto.backend.poker.controller.PokerController;
import com.cgi.retrospecto.backend.poker.controller.dto.*;
import com.cgi.retrospecto.backend.poker.domain.Room;
import com.cgi.retrospecto.backend.poker.domain.Story;
import com.cgi.retrospecto.backend.poker.domain.Vote;
import com.cgi.retrospecto.backend.poker.exception.RoomNotFoundException;
import com.cgi.retrospecto.backend.poker.exception.StoryNotFoundException;
import com.cgi.retrospecto.backend.poker.helper.converter.RoomConverter;
import com.cgi.retrospecto.backend.poker.helper.converter.StoryConverter;
import com.cgi.retrospecto.backend.poker.helper.converter.VoteConverter;
import com.cgi.retrospecto.backend.poker.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.*;

import static com.cgi.retrospecto.backend.poker.api.PokerConstants.ApiConstants.POKER;

@RestController
@RequestMapping(POKER)
@CrossOrigin
public class PokerControllerImpl implements PokerController {
    @Autowired
    private RoomService roomService;

    // @formatter:off
    @Override
    public ResponseEntity<RoomDTO> createRoom(@RequestBody CreateRoomDTO dto) {
        return new ResponseEntity<>(
                RoomConverter.toDTO(
                    roomService.createRoom(
                            RoomConverter.toEntity(dto, new Room())
                    )
                ), HttpStatus.OK);
    }
    // @formatter:on

    @Override
    public ResponseEntity<RoomDTO> getRoom(@PathVariable(PokerConstants.ID) int id, @PathVariable(PokerConstants.USERNAME) String username) throws RoomNotFoundException {
        Room room = roomService.getPokerRoom(id);
        room.addUser(username);
        return new ResponseEntity<>(RoomConverter.toDTO(room), HttpStatus.OK);
    }

    // @formatter:off
    @Override
    public ResponseEntity<StoryDTO> createStory(@DestinationVariable int roomId, @Payload CreateStoryDTO dto) throws RoomNotFoundException {
        return new ResponseEntity<>(
                StoryConverter.toDTO(
                        roomService.createStory(roomId,
                                StoryConverter.toEntity(dto, new Story())
                        )
                ), HttpStatus.OK);
    }
    // @formatter:on

    // @formatter:off
    @Override
    public ResponseEntity<RoomDTO> vote(@DestinationVariable int roomId, @DestinationVariable int storyId, @Payload VoteDTO dto) throws RoomNotFoundException, StoryNotFoundException {
        roomService.vote(roomId, storyId, VoteConverter.toEntity(dto, new Vote()));
        return new ResponseEntity<>(
                RoomConverter.toDTO(
                        roomService.getPokerRoom(roomId)
                ), HttpStatus.OK);
    }
    // @formatter:on

    @Override
    public ResponseEntity<SelectedStoryDTO> selectStory(@DestinationVariable int roomId, @DestinationVariable int storyId) throws RoomNotFoundException, StoryNotFoundException {
        roomService.setSelectedStory(roomId, storyId);
        return new ResponseEntity<>(new SelectedStoryDTO(roomId, storyId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<VoteIsOpenDTO> openVoting(@DestinationVariable int roomId, @DestinationVariable int storyId) {
        return new ResponseEntity<>(new VoteIsOpenDTO(true), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<VoteIsOpenDTO> closeVoting(@DestinationVariable int roomId, @DestinationVariable int storyId) throws RoomNotFoundException, StoryNotFoundException {
        roomService.closeVoting(roomId, storyId);
        return new ResponseEntity<>(new VoteIsOpenDTO(false), HttpStatus.OK);
    }

}
