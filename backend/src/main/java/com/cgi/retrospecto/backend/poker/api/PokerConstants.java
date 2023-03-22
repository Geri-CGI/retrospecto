package com.cgi.retrospecto.backend.poker.api;

public class PokerConstants {
    public static final String USERNAME = "username";
    public static final String ID = "id";

    private PokerConstants() {
    }

    public class ApiConstants {
        public static final String POKER = "/poker";
        public static final String ROOM = "/room";
        public static final String USERNAME = "/username";
        public static final String GET_ROOM = ApiConstants.ROOM + PathVarConstants.PATH_VAR_ID + ApiConstants.USERNAME + PathVarConstants.PATH_VAR_USERNAME;

        private ApiConstants() {
        }
    }

    public class PathVarConstants {
        public static final String PATH_VAR_USERNAME = "/{" + PokerConstants.USERNAME + "}";
        public static final String PATH_VAR_ID = "/{" + ID + "}";

        private PathVarConstants() {
        }
    }

    public class WebSocketConstants {

        private WebSocketConstants() {
        }
    }
}
