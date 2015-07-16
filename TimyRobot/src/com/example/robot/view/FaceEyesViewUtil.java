package com.example.robot.view;

import java.util.ArrayList;

import android.R.integer;

public class FaceEyesViewUtil {
	public static final int NORMAL = 0;
//	public static final int HAPPY = 1;
//	public static final int SURPRISE = 2;
//	public static final int SUSPECT = 3;
	
	//public static final int ANGRY = 1;
	public static final int HUMS = 1;
	//public static final int DESPISE = 3;
	//public static final int SQUINT = 4;
	//public static final int UPSET = 5;
	
	public static ArrayList faceNameViewsIdList = new ArrayList<Integer>();
	
	
	public static void addFaceView(int FaceNameId, int ViewId){
		faceNameViewsIdList.add(FaceNameId, ViewId);
	}
	
	public static int getFaceView(int FaceNameId){
		return ((Integer) faceNameViewsIdList.get(FaceNameId)).intValue();
	}
}
