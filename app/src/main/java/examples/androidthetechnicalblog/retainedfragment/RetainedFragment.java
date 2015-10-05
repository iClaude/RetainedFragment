package examples.androidthetechnicalblog.retainedfragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;

/**
 * This Fragment performs a specific background task and is not
 * destroyed on configuration changes.
 */
public class RetainedFragment extends Fragment {
    /**
     * Interface used by the retained Fragment to communicate with
     * the Activity.
     */
    interface RetainedFragmentCallbacks {
        void onPreExecute();

        void onProgressUpdate(int progress);

        void onCancelled();

        void onPostExecute();
    }

    private RetainedFragmentCallbacks retainedFragmentCallbacks;
    private MyAsyncTask myAsyncTask;

    /**
     * Hold a reference to the parent Activity so we can report the
     * task's current progress and results (the Activity must implement
     * the RetainedFragmentCallbacks interface to receive the data sent
     * by this Fragment).
     * On configuration changes the Android framework passes to this
     * method a reference to the newly created Activity. This way
     * the AsyncTask can deliver its results to the new Activity.
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        retainedFragmentCallbacks = (RetainedFragmentCallbacks) activity;
    }

    /**
     * This method gets invoked when the Fragment is created for
     * first time.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This Fragment won’t be destroyed on configuration changes.
        setRetainInstance(true);

        // Create and execute the background task.
        myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute();
    }

    /**
     * Set the callback to null so we don't accidentally leak the
     * Activity instance.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        retainedFragmentCallbacks = null;
    }

    /**
     * This AsyncTask performs a long task in the background
     * (we fake it with a sleep) and communicates progress and
     * results to the containing Activity.
     * In each method we check that retainedFragmentCallbacks is
     * not null because the Activity may have been destroyed on
     * configuration changes or because the user exits the Activity
     * pressing the back button.
     */
    private class MyAsyncTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {
            if (retainedFragmentCallbacks != null) {
                retainedFragmentCallbacks.onPreExecute();
            }
        }

        /**
         * Note that we do NOT call the callback object's methods
         * directly from the background thread, as this could result
         * in a race condition.
         */
        @Override
        protected Void doInBackground(Void... ignore) {
            for (int i = 0; !isCancelled() && i < 10; i++) {
                SystemClock.sleep(1000);
                publishProgress(i);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            if (retainedFragmentCallbacks != null) {
                retainedFragmentCallbacks.onProgressUpdate(progress[0]);
            }
        }

        @Override
        protected void onCancelled() {
            if (retainedFragmentCallbacks != null) {
                retainedFragmentCallbacks.onCancelled();
            }
        }

        @Override
        protected void onPostExecute(Void ignore) {
            if (retainedFragmentCallbacks != null) {
                retainedFragmentCallbacks.onPostExecute();
            }
        }
    }
}
