package com.omworldgame.genaiandroidgameexam.common;

public class Constant {
    public static final int EVENT_CG_MAX = 100;
    public static final int CHARACTER_COUNT = 4;
    public static final int LOVE_COUNT = 3; // 공략 가능 캐릭터
    public static final int EVENT_COUNT = 100; // 캐릭터당 이벤트 개수
    public static final int VARIABLE_COUNT = 100;
    public static final int SAVE_FILE_COUNT = 10;
    public static final String GAME_VERSION = "HeartbeatsIdol v1.0";
    public static final String DB_NAME = "heartbeats_idol.db";

    public static final int GM_INTRO			= 1;
    public static final int GM_ENDING			= 4;
    public static final int GM_PLAY				= 5;
    public static final int GM_MOVE				= 6;
    public static final int GM_LOAD				= 7;
    public static final int GM_SAVE				= 8;
    public static final int GM_CONFIG			= 15;

    public static class EventData {
        private static final String[] EVT = {
                // 유아라 11
                "001", "002", "003", "004", "005", "006", "007", "008", "009", "010", "011",
                // 박지원 17
                "101", "102", "103", "104", "105", "106", "107", "108", "109", "110",
                "111", "112", "113", "114", "115", "116", "117",
                // 김유진 14
                "201", "202", "203", "204", "205", "206", "207", "208", "209", "210",
                "211", "212", "213", "214",
                // 공통 6
                "301", "302", "303", "304", "305", "306",
        };

        public static final int[] START_POINT = {
                -1,
                11 - 1,
                11 + 17 - 1,
                11 + 17 + 14 - 1,
        };

        public static final int[] END_POINT = {
                11,
                11 + 17,
                11 + 17 + 14,
                11 + 17 + 14 + 6,
        };

        public static String getEventData(int num) {
            return EVT[num];
        }

        public static int getEventDataSize() {
            return EVT.length;
        }
    }
}
