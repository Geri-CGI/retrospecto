package com.cgi.retrospecto.backend.poker.controller;

import com.cgi.retrospecto.backend.poker.api.PokerConstants;
import com.cgi.retrospecto.backend.poker.controller.dto.*;
import com.cgi.retrospecto.backend.poker.exception.RoomNotFoundException;
import com.cgi.retrospecto.backend.poker.exception.StoryNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import static com.cgi.retrospecto.backend.poker.api.PokerConstants.ApiConstants.*;

public interface PokerController {
    // @formatter:off
    @ResponseBody
    @RequestMapping(path = ROOM, method = RequestMethod.POST)
    ResponseEntity<RoomDTO> createRoom(@RequestBody CreateRoomDTO dto);

    @ResponseBody
    @RequestMapping(path = GET_ROOM, method = RequestMethod.GET)
    ResponseEntity<RoomDTO> getRoom(
            @PathVariable(PokerConstants.ID) int id,
            @PathVariable(PokerConstants.USERNAME) String username)
            throws RoomNotFoundException;

    @MessageMapping(POKER + "/{roomId}/story/add")
    @SendTo("/topic" + POKER + "/{roomId}/story/add")
    ResponseEntity<StoryDTO> createStory(
            @DestinationVariable int roomId,
            @Payload CreateStoryDTO dto)
            throws RoomNotFoundException;

    @MessageMapping(POKER + "/{roomId}/story/{storyId}/vote")
    @SendTo("/topic" + POKER + "/{roomId}/vote")
    ResponseEntity<RoomDTO> vote(
            @DestinationVariable int roomId,
            @DestinationVariable int storyId, @Payload VoteDTO dto)
            throws RoomNotFoundException, StoryNotFoundException;

    @MessageMapping(POKER + "/{roomId}/story/{storyId}/selected")
    @SendTo("/topic" + POKER + "/{roomId}/selectedStory")
    ResponseEntity<SelectedStoryDTO> selectStory(
            @DestinationVariable int roomId,
            @DestinationVariable int storyId)
            throws RoomNotFoundException, StoryNotFoundException;

    @MessageMapping(POKER + ROOM + "/{roomId}/story/{storyId}/vote/open")
    @SendTo("/topic" + POKER + "/{roomId}/vote/open-close")
    ResponseEntity<VoteIsOpenDTO> openVoting(
            @DestinationVariable int roomId,
            @DestinationVariable int storyId);

    @MessageMapping(POKER + ROOM + "/{roomId}/story/{storyId}/vote/close")
    @SendTo("/topic" + POKER + "/{roomId}/vote/open-close")
    ResponseEntity<VoteIsOpenDTO> closeVoting(
            @DestinationVariable int roomId,
            @DestinationVariable int storyId)
            throws RoomNotFoundException, StoryNotFoundException;
    // @formatter:on
}
