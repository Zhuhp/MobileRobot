package com.timyrobot.common;

/**
 * Created by zhangtingting on 15/8/5.
 */
public class ConstDefine {
    private ConstDefine(){
    }

    public final static class TriggerDataType{
        public final static String Voice = "voice";
        public final static String Vision = "vision";
        public final static String Sensor = "sensor";
        public final static String Touch = "touch";
    }

    public final static class TriggerDataKey{
        public final static String TYPE = "type";
        public final static String CONTENT = "content";

        public final static String FACE_TGR_TYPE = "face_tgr_type";
        public final static String NUMBER = "number";

    }

    public final static class VisionCMD{
        public final static String DETECT_FACE = "detect_face";
        public final static String DETECT_MANY_FACES = "detect_many_faces";
        public final static String DETECT_FACE_LOCATION = "detect_face_location";
    }

    public final static class TouchCMD{
        public final static String DETECT_TOUCH = "detect_touch";
    }

    public final static class IntentFilterString{
        public final static String BROADCAST_START_CONVERSATION = "broadcast_start_conversation";
        public final static String BROADCAST_IDLE_CONVERSATION = "broadcast_idle_conversation";
    }

    public final static class EmotionNames{
        public final static String EMOTION_NAME_LAGTH = "laugh";
    }


}
