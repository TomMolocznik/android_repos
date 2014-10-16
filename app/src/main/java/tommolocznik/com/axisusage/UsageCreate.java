package tommolocznik.com.axisusage;

import android.content.Context;
import android.util.Log;

import java.net.FileNameMap;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by tommolocznik on 10/8/14.
 */
public class UsageCreate {

        private static final String TAG = "UsageCreate";
        private static final String FILENAME = "usages.json";

        private ArrayList<Usage> mUsage;
        private  UsageIntentJSONSerializer mSerializer;

        private static UsageCreate sUsageCreate;
        private Context mAppContext;

        private UsageCreate(Context appContext)
        {
            mAppContext = appContext;
            mSerializer = new UsageIntentJSONSerializer(mAppContext,FILENAME);
            try {
                mUsage = mSerializer.loadUsages();
            }   catch(Exception e){
                mUsage = new ArrayList<Usage>();
                Log.e(TAG, "Error loading usage: ",e);
            }
        }
        public boolean saveUsages() {
            try{
                mSerializer.saveUsages(mUsage);
                return true;
            }catch(Exception e){
                Log.e(TAG,"Error saving usage: ",e);
                return false;
            }
        }


    public static UsageCreate get(Context c)
    {
        if (sUsageCreate == null)

        {
            sUsageCreate = new UsageCreate(c.getApplicationContext());
        }
        return sUsageCreate;
    }
    public void addUsage(Usage c)

    {
        mUsage.add(c);
    }

    public void deleteUsage(Usage c) {mUsage.remove(c);}



    public Usage getUsage(UUID id)
    {
        for (Usage usage : mUsage){
            if (usage.getId().equals(id))
                return  usage;
        }
        return null;
    }
    public ArrayList<Usage> getUsage()
    {
        return mUsage;
    }
}
