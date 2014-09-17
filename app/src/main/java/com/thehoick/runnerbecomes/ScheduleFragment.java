package com.thehoick.runnerbecomes;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.sql.SQLException;

import db.ScheduleDataSource;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ScheduleFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ScheduleFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class ScheduleFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Activity mActivity = this.getActivity();
    public Boolean mScheduled = null;
    SharedPreferences mPrefs;
    public static final String PREFS = "preferences.xml";

    private OnFragmentInteractionListener mListener;
    protected ScheduleDataSource mDataSource;

    public static final String TAG = ScheduleFragment.class.getSimpleName();

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ScheduleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScheduleFragment newInstance(String param1, String param2) {
        ScheduleFragment fragment = new ScheduleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public ScheduleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        // Check settings for Calendar events.
        //SettingsFragment settings = new SettingsFragment();
        //Preference pref = settings.findPreference("scheduled");
        //Log.d("RunnerBecomes", pref.getKey());

        //PreferenceManager.setDefaultValues(this.getActivity(), R.xml.preferences, true);

            //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            //mScheduled = prefs.getBoolean("scheduled", true);

        //mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        //mScheduled = mPrefs.getBoolean("scheduled", false);

        // Restore preference
        //mScheduled = preferences.getBoolean("scheduled", mScheduled.getBoolean(""));



        //SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        //Boolean scheduled = sharedPref.getBoolean(RunnerBecomesActivity.SCH);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_schedule, container, false);
        final RelativeLayout mRelativeLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_schedule,
                container, false);

        SharedPreferences preferences = this.getActivity().getSharedPreferences("pref_general", Context.MODE_PRIVATE);
        //SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        //mScheduled = preferences.getBoolean("scheduled", false);
        mDataSource = new ScheduleDataSource(getActivity().getBaseContext());
        try {
            mDataSource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Save Time to SQLite3.
        mScheduled = mDataSource.checkScheduled();

        mDataSource.close();

        // Change button text if there are no RunnerBecomes events.
        Button button = (Button) mRelativeLayout.findViewById(R.id.editSchedule);
        Button removeScheduleButton = (Button) mRelativeLayout.findViewById(R.id.removeSchedule);
        removeScheduleButton.setVisibility(View.INVISIBLE);
        if (mScheduled) {
            button.setText("Edit Schedule");
            removeScheduleButton.setVisibility(View.VISIBLE);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // here you set what you want to do when user clicks your button,
                // e.g. launch a new activity
                Log.d("RunnerBecomes", "scheduled: " + mScheduled);

                getActivity().setProgressBarIndeterminate(true);

                if (mScheduled) {
                    Intent calIntent = new Intent(Intent.ACTION_MAIN);
                    calIntent.addCategory(Intent.CATEGORY_APP_CALENDAR);
                    startActivity(calIntent);
                } else {
                    // Launch the ScheduleActivity.
                    Intent intent = new Intent(getActivity(), ScheduleActivity.class);
                    startActivity(intent);
                }
            }
        });

        removeScheduleButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Find and remove all events.
                // Might just get the event IDs from the SQLite database.

                mDataSource = new ScheduleDataSource(getActivity().getBaseContext());
                try {
                    mDataSource.open();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                // Save Time to SQLite3.
                Cursor cursor = mDataSource.selectAllSchedules();

                cursor.moveToFirst();
                while( !cursor.isAfterLast() ) {
                    // do stuff
                    int i = cursor.getColumnIndex("event_id");
                    //Log.i("RunnerBecomes", "value index: " + i);
                    int eventID = cursor.getInt(i);

                    ContentResolver cr = getActivity().getContentResolver();
                    ContentValues values = new ContentValues();
                    Uri deleteUri = null;
                    deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventID);
                    int rows = getActivity().getContentResolver().delete(deleteUri, null, null);
                    Log.i(TAG, "Rows deleted: " + rows);

                    cursor.moveToNext();
                }

                Toast.makeText(getActivity().getBaseContext(), "Deleted " + cursor.getCount() +
                        "Events", Toast.LENGTH_LONG).show();

            }
        });

        // after you've done all your manipulation, return your layout to be shown
        return mRelativeLayout;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("RunnerBecomes", "Resume ScheduleFragment");
        // Change button text if there are no RunnerBecomes events.
        Log.i("RunnerBecomes", "mScheduled: " + mScheduled);
        if (mScheduled) {
            Button button = (Button) getView().findViewById(R.id.editSchedule);
            button.setText("Edit Schedule");
        }

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void OnClickListener(Uri uri) {
        Log.d("RunnerBecomes", "button pressed...");


        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
