package com.cgi.retrospecto.backend.controller;

import com.cgi.retrospecto.backend.domain.RetroBoard;
import com.cgi.retrospecto.backend.domain.RetroBoardMessage;
import com.cgi.retrospecto.backend.service.RetroBoardHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Set;


@Controller
public class BoardController {

    @Autowired
    private RetroBoardHandler retroBoardHandler;

    @MessageMapping("/board/{boardId}/card.add")
    @SendTo("/topic/board/{boardId}/add")
    public RetroBoardMessage addRetroBoardCard(@DestinationVariable int boardId, @Payload RetroBoardMessage retroBoardMessage) {
        return retroBoardHandler.addRetroBoardCard(boardId, retroBoardMessage);
    }

    @MessageMapping("/board/{boardId}/card.delete")
    @SendTo("/topic/board/{boardId}/delete")
    public RetroBoardMessage deleteRetroBoardCard(@DestinationVariable int boardId, @Payload RetroBoardMessage retroBoardMessage) {
        return retroBoardHandler.deleteRetroBoardCard(boardId, retroBoardMessage);
    }

    @MessageMapping("/board/{boardId}/card.edit")
    @SendTo("/topic/board/{boardId}/edit")
    public RetroBoardMessage editRetroBoardCard(@DestinationVariable int boardId, @Payload RetroBoardMessage retroBoardMessage) {
        return retroBoardHandler.editRetroBoardCard(boardId, retroBoardMessage);
    }

    @MessageMapping("/board/{boardId}/{username}/card.like")
    @SendTo("/topic/board/{boardId}/like")
    public RetroBoardMessage likeRetroBoardCard(@DestinationVariable int boardId, @DestinationVariable String username, @Payload RetroBoardMessage retroBoardMessage) {
        return retroBoardHandler.likeRetroBoardCard(boardId, username, retroBoardMessage);
    }

    @MessageMapping("/board/{boardId}/{username}/card.dislike")
    @SendTo("/topic/board/{boardId}/like")
    public RetroBoardMessage dislikeRetroBoardCard(@DestinationVariable int boardId, @DestinationVariable String username, @Payload RetroBoardMessage retroBoardMessage) {
        return retroBoardHandler.dislikeRetroBoardCard(boardId, username, retroBoardMessage);
    }

    @MessageMapping("/board/{boardId}/{username}/card.removeLike")
    @SendTo("/topic/board/{boardId}/like")
    public RetroBoardMessage removeLikeRetroBoardCard(@DestinationVariable int boardId, @DestinationVariable String username, @Payload RetroBoardMessage retroBoardMessage) {
        return retroBoardHandler.removeLikeRetroBoardCard(boardId, username, retroBoardMessage);
    }

    @MessageMapping("/board/{boardId}/{username}/user.add")
    @SendTo("/topic/board/{boardId}/user")
    public Set<String> addUserToRetroBoard(@DestinationVariable int boardId, @DestinationVariable String username) {
        return retroBoardHandler.addUserToRetroBoard(boardId, username);
    }

    @MessageMapping("/board/{boardId}/{username}/user.remove")
    @SendTo("/topic/board/{boardId}/user")
    public Set<String> removeUserFromRetroBoard(@DestinationVariable int boardId, @DestinationVariable String username) {
        return retroBoardHandler.removeUserFromRetroBoard(boardId, username);
    }

    @MessageMapping("/board/{boardId}/{username}/lock")
    @SendTo("/topic/board/{boardId}/locking")
    public ResponseEntity<RetroBoard> lockRetroBoard(@DestinationVariable int boardId, @DestinationVariable String username) {
        return retroBoardHandler.lockRetroBoard(boardId, username);
    }

    @MessageMapping("/board/{boardId}/{username}/unlock")
    @SendTo("/topic/board/{boardId}/locking")
    public ResponseEntity<RetroBoard> unlockRetroBoard(@DestinationVariable int boardId, @DestinationVariable String username) {
        return retroBoardHandler.unlockRetroBoard(boardId, username);
    }

    @MessageMapping("/board/{boardId}/order.like")
    @SendTo("/topic/board/{boardId}/reorder")
    public RetroBoard getRetroBoardReorganizedByLikes(@DestinationVariable int boardId) {
        return retroBoardHandler.getRetroBoardReorganizedByLikes(boardId);
    }


    @MessageMapping("/board/{boardId}/order.dislike")
    @SendTo("/topic/board/{boardId}/reorder")
    public RetroBoard getRetroBoardReorganizedByDislikes(@DestinationVariable int boardId) {
        return retroBoardHandler.getRetroBoardReorganizedByDislikes(boardId);
    }

    @CrossOrigin
    @ResponseBody
    @RequestMapping(path = "/board/{id}/{username}", method = RequestMethod.GET)
    public ResponseEntity<RetroBoard> getRetroBoard(@PathVariable int id, @PathVariable String username) {
        return retroBoardHandler.getRetroBoard(id, username);
    }


    @CrossOrigin
    @ResponseBody
    @RequestMapping(path = "/board/number", method = RequestMethod.GET)
    public int getNumberOfActiveRetroBoards() {
        return retroBoardHandler.getNumberOfActiveRetroBoards();
    }

    @CrossOrigin
    @ResponseBody
    @RequestMapping(path = "/board/create/{author}", method = RequestMethod.POST)
    public RetroBoard createBoard(@PathVariable String author) {
        return retroBoardHandler.createBoard(author);
    }
}
