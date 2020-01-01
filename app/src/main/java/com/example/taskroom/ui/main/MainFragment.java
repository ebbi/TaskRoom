package com.example.taskroom.ui.main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.taskroom.R;
import com.example.taskroom.database.Task;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {

    private List<Task> mTasks;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    private static final String LOG_TAG = MainFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        Log.d( LOG_TAG, "onCreateView");

        MainViewModel mMainViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        final View view = inflater.inflate(R.layout.main_fragment, container, false);

        mMainViewModel.getTasks().observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                mTasks = tasks;
                updateUI(view);
            }
        });

        return view;
    }

    private void updateUI(View view) {

        Task mTask = mTasks.get(0);

        final TextView  mTextViewMessage = view.findViewById(R.id.textViewMessage);

        final TextView mTextViewTitle = view.findViewById(R.id.textViewTitle);
        final TextView mTextViewDescription = view.findViewById(R.id.textViewDescription);
        final TextView mTextViewStatus = view.findViewById(R.id.textViewStatus);

        mTextViewTitle.setText(mTask.getTitle());
        mTextViewDescription.setText(mTask.getDescription());
        mTextViewStatus.setText(mTask.getStatus());

/*
        Button mButtonNewTask = view.findViewById(R.id.buttonNewTask);
        mButtonNewTask.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                mMainViewModel.setTask(mTask);

                TaskAddFragment taskAddFragment = TaskAddFragment.newInstance();
                assert getFragmentManager() != null;
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, taskAddFragment);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });

        Button mButtonNext = view.findViewById(R.id.buttonNext);
        mButtonNext.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                if ( mMainViewModel.isLast(mTask)) {

                    Toast toast = Toast.makeText(
                            getActivity(),
                            "Hurray! end of tasks, time to plant trees and read poetry!",
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();

                } else {

                    mTask = mMainViewModel.getNextTask(mTask);
                    mMainViewModel.setTask(mTask);
                    mTextViewTitle.setText(mTask.getTitle());
                    mTextViewDescription.setText(mTask.getDescription());
                    mTextViewStatus.setText(mTask.getStatus());

                }
            }

        });

        Button mButtonPrev = view.findViewById(R.id.buttonPrev);
        mButtonPrev.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                if ( mMainViewModel.isFirst(mTask)) {

                    Toast toast = Toast.makeText(
                            getActivity(),
                            "First task!",
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();

                } else {

                    mTask = mMainViewModel.getPrevTask(mTask);
                    mMainViewModel.setTask(mTask);
                    mTextViewTitle.setText(mTask.getTitle());
                    mTextViewDescription.setText(mTask.getDescription());
                    mTextViewStatus.setText(mTask.getStatus());

                }
            }

        });
*/

    }
}
