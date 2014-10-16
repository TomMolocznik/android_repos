package tommolocznik.com.axisusage;

/**
 * Created by tommolocznik on 10/8/14.
 */
public class UsageListActivity extends SingleFragmentActivity {

    private static final String TAG = "UsageListActivity";
    @Override
    protected android.support.v4.app.Fragment createFragment()
    {
        return new UsageListFragment();

    }

}
