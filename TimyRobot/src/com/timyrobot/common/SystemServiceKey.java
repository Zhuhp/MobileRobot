package com.timyrobot.common;

/**
 * Created by zhangtingting on 15/8/8.
 */
public class SystemServiceKey {
    public final static class SystemKey{
        public final static String OPERATOR = "operator";
        public final static String SERVICE = "service";
    }

    public final static class Phone{
        public final static String OPERATOR_CALL = "call";
        public final static String SERVICE = "telephone";
        public final static String CONTACT_NAME = "name";
    }
    public final static class Sms{
        public final static String OPERATOR_SEND = "send";
        public final static String SERVICE = "message";
        public final static String CONTACT_NAME = "name";
        public final static String CONTENT = "content";
    }
    public final static class Music{
        public final static String OPERATOR_PLAY = "play";
        public final static String SERVICE = "music";
        public final static String SONG = "song";
        public final static String ARTIST = "artist";
    }
    public final static class Video{
        public final static String OPERATOR_PLAY = "play";
        public final static String SERVICE = "video";
        public final static String KEYWORDS = "keywords";
        public final static String QUERYFIELD = "queryField";
    }
    public final static class WebSearch{
        public final static String OPERATOR_PLAY = "query";
        public final static String SERVICE = "websearch";
        public final static String KEYWORDS = "keywords";
        public final static String CHANNEL = "channel";
    }

}
