
import android.app.Application;
import android.view.WindowManager;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

/**
 * Created by Administrator on 2015/6/20.
 */
public class IRApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		SpeechUtility.createUtility(this, SpeechConstant.APPID + "=5584c37d");
	}

}
