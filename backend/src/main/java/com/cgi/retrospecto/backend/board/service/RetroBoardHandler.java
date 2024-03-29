package com.cgi.retrospecto.backend.board.service;

import com.cgi.retrospecto.backend.board.domain.RetroBoard;
import com.cgi.retrospecto.backend.board.domain.RetroBoardMessage;
import com.cgi.retrospecto.backend.board.dto.RetroBoardInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class RetroBoardHandler {

    @Autowired
    private RetroBoardKeeper retroBoardKeeper;

    public RetroBoardMessage addRetroBoardCard(int boardId, RetroBoardMessage retroBoardMessage) {
        final RetroBoard retroBoard = retroBoardKeeper.getRetroBoard(boardId);
        retroBoard.setLastActionSubmittedTime(LocalDateTime.now());
        switch (retroBoardMessage.getColumnType()) {
            case TRY -> retroBoard.getWantToTryColumn().add(retroBoardMessage);
            case WELL -> retroBoard.getWentWellColumn().add(retroBoardMessage);
            case EXPECT -> retroBoard.getExpectColumn().add(retroBoardMessage);
            case NOT_WELL -> retroBoard.getDidNotGoWellColumn().add(retroBoardMessage);
        }
        return retroBoardMessage;
    }

    public ResponseEntity<RetroBoard> getRetroBoard(int id, String username) {
        RetroBoard retroBoard = retroBoardKeeper.getRetroBoard(id);
        if (retroBoard == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } else {
            if (retroBoard.getUsers().contains(username)) {
                return new ResponseEntity<>(retroBoard, HttpStatus.FOUND);
            } else {
                return new ResponseEntity<>(retroBoard, HttpStatus.OK);
            }
        }
    }

    public RetroBoard createBoard(RetroBoardInput retroBoardInput) {
        RetroBoard retroBoard = retroBoardKeeper.getActiveBoardForAuthorIfExists(retroBoardInput.getAuthor());
        if (retroBoard == null) {
            synchronized (this) {
                int id = new Random().nextInt(900000) + 100000;
                retroBoard = new RetroBoard(id, retroBoardInput.getAuthor(), retroBoardInput.getFirstColumnName(),
                        retroBoardInput.getSecondColumnName(), retroBoardInput.getThirdColumnName(),
                        retroBoardInput.getFourthColumnName());
                retroBoard.getUsers().add(retroBoard.getAuthor());
                retroBoardKeeper.addBoard(retroBoard);
            }
        }
        return retroBoard;
    }

    public int getNumberOfActiveRetroBoards() {
        return retroBoardKeeper.getNumberOfActiveRetroBoards();
    }

    public RetroBoardMessage deleteRetroBoardCard(int boardId, RetroBoardMessage retroBoardMessage) {
        final RetroBoard retroBoard = retroBoardKeeper.getRetroBoard(boardId);
        int index = retroBoardMessage.getIndex();
        retroBoard.setLastActionSubmittedTime(LocalDateTime.now());
        switch (retroBoardMessage.getColumnType()) {
            case TRY -> retroBoard.getWantToTryColumn().remove(index);
            case WELL -> retroBoard.getWentWellColumn().remove(index);
            case EXPECT -> retroBoard.getExpectColumn().remove(index);
            case NOT_WELL -> retroBoard.getDidNotGoWellColumn().remove(index);
        }
        return retroBoardMessage;
    }

    public RetroBoardMessage editRetroBoardCard(int boardId, RetroBoardMessage retroBoardMessage) {
        final RetroBoard retroBoard = retroBoardKeeper.getRetroBoard(boardId);
        int index = retroBoardMessage.getIndex();
        retroBoard.setLastActionSubmittedTime(LocalDateTime.now());
        switch (retroBoardMessage.getColumnType()) {
            case TRY -> retroBoard.getWantToTryColumn().get(index).setCardMessage(retroBoardMessage.getCardMessage());
            case WELL -> retroBoard.getWentWellColumn().get(index).setCardMessage(retroBoardMessage.getCardMessage());
            case EXPECT -> retroBoard.getExpectColumn().get(index).setCardMessage(retroBoardMessage.getCardMessage());
            case NOT_WELL ->
                    retroBoard.getDidNotGoWellColumn().get(index).setCardMessage(retroBoardMessage.getCardMessage());
        }
        return retroBoardMessage;
    }

    public RetroBoardMessage likeRetroBoardCard(int boardId, String username, RetroBoardMessage retroBoardMessage) {
        final RetroBoard retroBoard = retroBoardKeeper.getRetroBoard(boardId);
        int index = retroBoardMessage.getIndex();
        retroBoard.setLastActionSubmittedTime(LocalDateTime.now());
        addToLikedRecords(retroBoard, username, retroBoardMessage);
        switch (retroBoardMessage.getColumnType()) {
            case TRY -> retroBoard.getWantToTryColumn().get(index).increaseLikes(username);
            case WELL -> retroBoard.getWentWellColumn().get(index).increaseLikes(username);
            case EXPECT -> retroBoard.getExpectColumn().get(index).increaseLikes(username);
            case NOT_WELL -> retroBoard.getDidNotGoWellColumn().get(index).increaseLikes(username);
        }
        retroBoardMessage.increaseLikes(username);
        return retroBoardMessage;
    }

    public RetroBoardMessage dislikeRetroBoardCard(int boardId, String username, RetroBoardMessage retroBoardMessage) {
        final RetroBoard retroBoard = retroBoardKeeper.getRetroBoard(boardId);
        int index = retroBoardMessage.getIndex();
        retroBoard.setLastActionSubmittedTime(LocalDateTime.now());
        addToLikedRecords(retroBoard, username, retroBoardMessage);
        switch (retroBoardMessage.getColumnType()) {
            case TRY -> retroBoard.getWantToTryColumn().get(index).increaseDislikes(username);
            case WELL -> retroBoard.getWentWellColumn().get(index).increaseDislikes(username);
            case EXPECT -> retroBoard.getExpectColumn().get(index).increaseDislikes(username);
            case NOT_WELL -> retroBoard.getDidNotGoWellColumn().get(index).increaseDislikes(username);
        }
        retroBoardMessage.increaseDislikes(username);
        return retroBoardMessage;
    }

    public RetroBoardMessage removeLikeRetroBoardCard(int boardId, String username, RetroBoardMessage retroBoardMessage) {
        final RetroBoard retroBoard = retroBoardKeeper.getRetroBoard(boardId);
        int index = retroBoardMessage.getIndex();
        retroBoard.setLastActionSubmittedTime(LocalDateTime.now());
        removeFromLikedRecords(retroBoard, username, retroBoardMessage);
        RetroBoardMessage retroBoardMessageFromMemory = null;
        switch (retroBoardMessage.getColumnType()) {
            case TRY -> retroBoardMessageFromMemory = retroBoard.getWantToTryColumn().get(index);
            case WELL -> retroBoardMessageFromMemory = retroBoard.getWentWellColumn().get(index);
            case EXPECT -> retroBoardMessageFromMemory = retroBoard.getExpectColumn().get(index);
            case NOT_WELL -> retroBoardMessageFromMemory = retroBoard.getDidNotGoWellColumn().get(index);
        }
        retroBoardMessageFromMemory.decreaseReaction(username);
        retroBoardMessageFromMemory.setIndex(index);
        return retroBoardMessageFromMemory;
    }

    public void addToLikedRecords(RetroBoard retroBoard, String username, RetroBoardMessage retroBoardMessage) {
        Map<String, List<Integer>> likedRecords = retroBoard.getLikedRecords();
        if (!likedRecords.containsKey(username)) {
            likedRecords.put(username, new ArrayList<>());
        }
        likedRecords.get(username).add(retroBoardMessage.getUniqueId());
    }

    public void removeFromLikedRecords(RetroBoard retroBoard, String username, RetroBoardMessage retroBoardMessage) {
        Map<String, List<Integer>> likedRecords = retroBoard.getLikedRecords();
        List<Integer> ids = likedRecords.get(username);
        for (int i = 0; i < ids.size(); i++) {
            if (ids.get(i) == retroBoardMessage.getUniqueId()) {
                ids.remove(i);
                return;
            }
        }
    }

    public RetroBoard getRetroBoardReorganizedByLikes(int boardId) {
        return getRetroBoardOrganized(boardId, Comparator.comparing(RetroBoardMessage::getLikes).thenComparing(RetroBoardMessage::getDislikes).reversed());
    }

    public RetroBoard getRetroBoardReorganizedByDislikes(int boardId) {
        return getRetroBoardOrganized(boardId, Comparator.comparing(RetroBoardMessage::getDislikes).thenComparing(RetroBoardMessage::getLikes).reversed());
    }

    private RetroBoard getRetroBoardOrganized(int boardId, Comparator<RetroBoardMessage> comparing) {
        RetroBoard retroBoard = retroBoardKeeper.getRetroBoard(boardId);
        retroBoard.getExpectColumn().sort(comparing);
        retroBoard.getWentWellColumn().sort(comparing);
        retroBoard.getDidNotGoWellColumn().sort(comparing);
        retroBoard.getWantToTryColumn().sort(comparing);
        return retroBoard;
    }

    public Set<String> addUserToRetroBoard(int boardId, String username) {
        RetroBoard retroBoard = retroBoardKeeper.getRetroBoard(boardId);
        retroBoard.getUsers().add(username);
        return retroBoard.getUsers();
    }

    public Set<String> removeUserFromRetroBoard(int boardId, String username) {
        RetroBoard retroBoard = retroBoardKeeper.getRetroBoard(boardId);
        retroBoard.getUsers().remove(username);
        return retroBoard.getUsers();
    }

    public ResponseEntity<RetroBoard> lockRetroBoard(int boardId, String username) {
        return handleRetroBoardLocking(boardId, username, true);
    }

    public ResponseEntity<RetroBoard> unlockRetroBoard(int boardId, String username) {
        return handleRetroBoardLocking(boardId, username, false);
    }

    private ResponseEntity<RetroBoard> handleRetroBoardLocking(int boardId, String username, boolean locked) {
        RetroBoard retroBoard = retroBoardKeeper.getRetroBoard(boardId);
        if (retroBoard.getAuthor().equals(username)) {
            retroBoard.setLocked(locked);
            return new ResponseEntity<>(retroBoard, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }

    public ResponseEntity<RetroBoard> blurRetroBoard(int boardId, String username) {
        return handleRetroBoardBlurring(boardId, username, true, 5);
    }

    public ResponseEntity<RetroBoard> unblurRetroBoard(int boardId, String username) {
        return handleRetroBoardBlurring(boardId, username, false, 0);
    }

    private ResponseEntity<RetroBoard> handleRetroBoardBlurring(int boardId, String username, boolean blurActive, int blurNumber) {
        RetroBoard retroBoard = retroBoardKeeper.getRetroBoard(boardId);
        if (retroBoard.getAuthor().equals(username)) {
            retroBoard.setBlurActive(blurActive);
            retroBoard.setBlurNumber(blurNumber);
            return new ResponseEntity<>(retroBoard, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }

    public RetroBoardMessage addActionToRetroBoardCard(int boardId, RetroBoardMessage retroBoardMessage) {
        final RetroBoard retroBoard = retroBoardKeeper.getRetroBoard(boardId);
        int index = retroBoardMessage.getIndex();
        retroBoard.setLastActionSubmittedTime(LocalDateTime.now());
        RetroBoardMessage retroBoardMessageFromDB = null;
        switch (retroBoardMessage.getColumnType()) {
            case TRY -> retroBoardMessageFromDB = retroBoard.getWantToTryColumn().get(index);
            case WELL -> retroBoardMessageFromDB = retroBoard.getWentWellColumn().get(index);
            case EXPECT -> retroBoardMessageFromDB = retroBoard.getExpectColumn().get(index);
            case NOT_WELL -> retroBoardMessageFromDB = retroBoard.getDidNotGoWellColumn().get(index);
        }
        retroBoardMessageFromDB.setActionMessage(retroBoardMessage.getActionMessage());
        retroBoardMessageFromDB.setHasAction(true);
        return retroBoardMessage;
    }
}
