package tommolocznik.com.axisusage;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by tommolocznik on 10/9/14.
 */
public class UsageCameraActivity extends SingleFragmentActivity
{
    private static final String TAG = "UsageCameraActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment createFragment()
    {
        return new UsageCameraFragment();

    }
}
