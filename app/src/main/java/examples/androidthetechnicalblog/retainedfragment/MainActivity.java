package examples.androidthetechnicalblog.retainedfragment;

import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

/**
 * This Activity creates a RetainedFragment to perform a background
 * task, and receives progress updates and results from the
 * Fragment when they occur.
 */
public class MainActivity extends AppCompatActivity implements RetainedFragment. RetainedFragmentCallbacks {

    private static final String TAG_RETAINED_FRAGMENT = "RETAINED FRAGMENT";
    private RetainedFragment retainedFragment;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);

        FragmentManager fm = getFragmentManager();
        retainedFragment = (RetainedFragment) fm.findFragmentByTag(TAG_RETAINED_FRAGMENT);

        // If the Fragment is not null, then it is currently being
        // retained across a configuration change.
        if (retainedFragment == null) {
            retainedFragment = new RetainedFragment();
            fm.beginTransaction().add(retainedFragment, TAG_RETAINED_FRAGMENT).commit();
            Log.d(TAG_RETAINED_FRAGMENT, "Retained Fragment created anew");
        }
        else {
            Log.d(TAG_RETAINED_FRAGMENT, "Retained Fragment retained on configuration change");
        }
    }

    // The four methods below are called by the RetainedFragment when new
    // progress updates or results are available. The MainActivity
    // should respond by updating its UI to indicate the change.

    @Override
    public void onPreExecute() {
        Log.d(TAG_RETAINED_FRAGMENT, "onPreExecute() received...");
        textView.setText("onPreExecute() received...");
    }

    @Override
    public void onProgressUpdate(int progress) {
        Log.d(TAG_RETAINED_FRAGMENT, "onProgressUpdate received with progress: " + progress);
        textView.setText("onProgressUpdate received with progress: " + progress);
    }

    @Override
    public void onCancelled() {
        Log.d(TAG_RETAINED_FRAGMENT, "onCancelled() received...");
        textView.setText("onCancelled() received...");
    }

    @Override
    public void onPostExecute() {
        Log.d(TAG_RETAINED_FRAGMENT, "onPostExecute() received...");
        textView.setText("onPostExecute() received...");
    }


}
