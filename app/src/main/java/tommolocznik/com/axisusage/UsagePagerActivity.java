package tommolocznik.com.axisusage;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by tommolocznik on 10/8/14.
 */
public class UsagePagerActivity extends FragmentActivity

{
    private Usage mUsage;
    private ViewPager mViewPager;
    private ArrayList<Usage> mUsages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.viewPager);
        setContentView(mViewPager);
        mUsages = UsageCreate.get(this).getUsage();
        FragmentManager fm = getSupportFragmentManager();

        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public int getCount()
            {
                return mUsages.size();
            }

            @Override
            public Fragment getItem(int pos)
            {
                Usage usage = mUsages.get(pos);

                return UsageFragment.newInstance(usage.getId());
            }
        });

        UUID usageId = (UUID) getIntent().getSerializableExtra(UsageFragment.EXTRA_USAGE_ID);

        for (int i = 0; i < mUsages.size(); i ++)
        {
            if (mUsages.get(i).getId().equals(usageId))
            {
                mViewPager.setCurrentItem(i);
                break;
            }
        }

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            public void onPageScrollStateChanged(int state) { }
            public void onPageScrolled(int pos,float posOffset,int posOffsetPixels) { }
            public void onPageSelected( int pos)
            {
                Usage usage = mUsages.get( pos);
                if (usage.getItem() != null)
                { setTitle( usage.getItem());


                }
            }
        });

    }
}
