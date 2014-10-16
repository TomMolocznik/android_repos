package tommolocznik.com.axisusage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import android.content.Context;
/**
 * Created by tommolocznik on 10/10/14.
 */
public class UsageIntentJSONSerializer {

    private Context mContext;
    private String mFilename;

    public UsageIntentJSONSerializer(Context c, String f) {
        mContext = c;
        mFilename = f;
    }

    public ArrayList<Usage> loadUsages() throws IOException, JSONException {
        ArrayList<Usage> usages = new ArrayList<Usage>();
        BufferedReader reader = null;
        try {
            // open and read the file into a StringBuilder
            InputStream in = mContext.openFileInput(mFilename);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                // line breaks are omitted and irrelevant
                jsonString.append(line);
            }
            // parse the JSON using JSONTokener
            JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();
            // build the array of crimes from JSONObjects
            for (int i = 0; i < array.length(); i++) {
               usages.add(new Usage(array.getJSONObject(i)));
            }
        } catch (FileNotFoundException e) {
            // we will ignore this one, since it happens when we start fresh
        } finally {
            if (reader != null)
                reader.close();
        }
        return usages;
    }

    public void saveUsages(ArrayList<Usage> usages) throws JSONException, IOException {
        // build an array in JSON
        JSONArray array = new JSONArray();
        for (Usage c : usages)
            array.put(c.toJSON());

        // write the file to disk
        Writer writer = null;
        try {
            OutputStream out = mContext.openFileOutput(mFilename, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(array.toString());
        } finally {
            if (writer != null)
                writer.close();
        }
    }
}

