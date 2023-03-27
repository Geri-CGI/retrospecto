package com.cgi.retrospecto.backend.poker.api;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;

import static com.cgi.retrospecto.backend.poker.api.PokerConstants.ApiConstants.POKER;
import static com.cgi.retrospecto.backend.poker.api.PokerConstants.ApiConstants.ROOM;
import static com.cgi.retrospecto.backend.poker.api.PokerConstants.PathVarConstants.PATH_VAR_ROOM_ID;
import static com.cgi.retrospecto.backend.poker.api.PokerConstants.PathVarConstants.PATH_VAR_STORY_ID;

public class PokerConstants {
    public static final String USERNAME = "username";
    public static final String ID = "id";
    public static final String STORY = "story";
    public static final String ROOM_ID = "roomId";
    public static final String STORY_ID = "storyId";


    private PokerConstants() {
    }

    public class ApiConstants {
        public static final String POKER = "/poker";
        public static final String ROOM = "/room";
        public static final String USERNAME = "/username";
        public static final String GET_ROOM = ApiConstants.ROOM + PathVarConstants.PATH_VAR_ID + ApiConstants.USERNAME + PathVarConstants.PATH_VAR_USERNAME;
        public static final String STORY = "/story";

        private ApiConstants() {
        }
    }

    public class PathVarConstants {
        public static final String PATH_VAR_USERNAME = "/{" + PokerConstants.USERNAME + "}";
        public static final String PATH_VAR_ID = "/{" + ID + "}";
        public static final String PATH_VAR_ROOM_ID = "/{" + ROOM_ID +"}";
        public static final String PATH_VAR_STORY_ID = "/{" + STORY_ID +"}";

        private PathVarConstants() {
        }
    }

    public class WebSocketConstants {

        private static final String TOPIC = "/topic";
        private static final String PATH_VOTE = "/vote";
        private static final String PATH_STORY = "/" + STORY;
        public static final String ADD_STORY = POKER + PATH_VAR_ROOM_ID + PATH_STORY + "/add";
        public static final String ADD_STORY_TOPIC = TOPIC + ADD_STORY;
        public static final String VOTE = POKER + PATH_VAR_ROOM_ID + PATH_STORY + PATH_VAR_STORY_ID + PATH_VOTE;
        public static final String VOTE_TOPIC = TOPIC + POKER + PATH_VAR_ROOM_ID + PATH_VOTE;
        public static final String SELECT_STORY = POKER + PATH_VAR_ROOM_ID + PATH_STORY + PATH_VAR_STORY_ID + "/selected";
        public static final String SELECT_STORY_TOPIC = TOPIC + POKER + PATH_VAR_ROOM_ID + "/selectedStory";
        public static final String OPEN_VOTING = POKER + ROOM + PATH_VAR_ROOM_ID + PATH_STORY + PATH_VAR_STORY_ID + PATH_VOTE + "/open";
        public static final String OPEN_VOTING_TOPIC = TOPIC + POKER + PATH_VAR_ROOM_ID + PATH_VOTE + "/open-close";
        public static final String CLOSE_VOTING = POKER + ROOM + PATH_VAR_ROOM_ID + PATH_STORY + PATH_VAR_STORY_ID + PATH_VOTE + "/close";
        public static final String CLOSE_VOTING_TOPIC = OPEN_VOTING_TOPIC;

        private WebSocketConstants() {
        }
    }
}
