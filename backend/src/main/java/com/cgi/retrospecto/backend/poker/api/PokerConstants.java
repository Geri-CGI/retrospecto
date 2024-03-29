package com.cgi.retrospecto.backend.poker.api;

import static com.cgi.retrospecto.backend.poker.api.PokerConstants.ApiConstants.POKER;
import static com.cgi.retrospecto.backend.poker.api.PokerConstants.ApiConstants.ROOM;
import static com.cgi.retrospecto.backend.poker.api.PokerConstants.PathVarConstants.*;

public class PokerConstants {
    public static final String STORY = "story";
    public static final String ROOM_ID = "roomId";
    public static final String STORY_ID = "storyId";

    private PokerConstants() {
    }

    public class ApiConstants {
        public static final String POKER = "/poker";
        public static final String ROOM = "/room";
        public static final String GET_ROOM = ROOM + PATH_VAR_ROOM_ID;
        public static final String USER = "/user";

        private ApiConstants() {
        }
    }

    public class PathVarConstants {
        public static final String PATH_VAR_ROOM_ID = "/{" + ROOM_ID +"}";
        public static final String PATH_VAR_STORY_ID = "/{" + STORY_ID +"}";

        private PathVarConstants() {
        }
    }

    public class WebSocketConstants {
        private static final String TOPIC = "/topic";
        private static final String PATH_VOTE = "/vote";
        private static final String PATH_STORY = "/" + STORY;
        public static final String ADD_STORY = POKER + ROOM + PATH_VAR_ROOM_ID + PATH_STORY + "/add";
        public static final String ADD_STORY_TOPIC = TOPIC + ADD_STORY;
        public static final String EDIT_STORY = POKER + ROOM + PATH_VAR_ROOM_ID + PATH_STORY + PATH_VAR_STORY_ID + "/edit";
        public static final String EDIT_STORY_TOPIC = TOPIC + POKER + ROOM + PATH_VAR_ROOM_ID + PATH_STORY + "/edit";
        public static final String REMOVE_STORY = POKER + ROOM + PATH_VAR_ROOM_ID + PATH_STORY + PATH_VAR_STORY_ID + "/remove";
        public static final String REMOVE_STORY_TOPIC = TOPIC + POKER + ROOM + PATH_VAR_ROOM_ID + PATH_STORY + "/remove";
        public static final String VOTE = POKER + ROOM + PATH_VAR_ROOM_ID + PATH_STORY + PATH_VAR_STORY_ID + PATH_VOTE;
        public static final String VOTE_TOPIC = TOPIC + POKER + ROOM + PATH_VAR_ROOM_ID + PATH_VOTE;
        public static final String SELECT_STORY = POKER + ROOM + PATH_VAR_ROOM_ID + PATH_STORY + PATH_VAR_STORY_ID + "/select";
        public static final String SELECT_STORY_TOPIC = TOPIC + POKER + ROOM + PATH_VAR_ROOM_ID + PATH_STORY + "/select";
        public static final String OPEN_VOTING = POKER + ROOM + PATH_VAR_ROOM_ID + PATH_STORY + PATH_VAR_STORY_ID + PATH_VOTE + "/open";
        public static final String OPEN_VOTING_TOPIC = TOPIC + POKER + ROOM + PATH_VAR_ROOM_ID + PATH_VOTE + "/open-close";
        public static final String CLOSE_VOTING = POKER + ROOM + PATH_VAR_ROOM_ID + PATH_STORY + PATH_VAR_STORY_ID + PATH_VOTE + "/close";
        public static final String CLOSE_VOTING_TOPIC = OPEN_VOTING_TOPIC;
        public static final String ADD_USER = POKER + ROOM + PATH_VAR_ROOM_ID + ApiConstants.USER + "/add";
        public static final String ADD_USER_TOPIC = TOPIC + POKER + ROOM + PATH_VAR_ROOM_ID + ApiConstants.USER + "/joined";
        public static final String REMOVE_USER = POKER + ROOM + PATH_VAR_ROOM_ID + ApiConstants.USER + "/remove";
        public static final String REMOVE_USER_TOPIC = TOPIC + POKER + ROOM + PATH_VAR_ROOM_ID + ApiConstants.USER + "/remove";

        private WebSocketConstants() {
        }
    }
}
