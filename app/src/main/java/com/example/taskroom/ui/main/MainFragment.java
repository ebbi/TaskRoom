package com.example.taskroom.ui.main;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.taskroom.R;
import com.example.taskroom.database.Task;

import java.util.List;

public class MainFragment extends Fragment {

    private MainViewModel mMainViewModel;
    private Task mTask;
    private List<Task> mTasks;
    private View view;

    private Button mButtonNewTask;
    private Button mButtonDelete;
    private Button mButtonNext;
    private Button mButtonPrev;
    private Button mButtonEdit;

    private TextView mTextViewTitle;
    private TextView mTextViewDescription;
    private TextView mTextViewStatus;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    private static final String LOG_TAG = MainFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d( LOG_TAG, "onCreateView");

        view = inflater.inflate(R.layout.main_fragment, container, false);
        mButtonNewTask = view.findViewById(R.id.buttonNewTask);
        mButtonDelete = view.findViewById(R.id.buttonDelete);
        mButtonNext = view.findViewById(R.id.buttonNext);
        mButtonPrev = view.findViewById(R.id.buttonPrev);
        mButtonEdit = view.findViewById(R.id.buttonEdit);
        mTextViewTitle = view.findViewById(R.id.textViewTitle);
        mTextViewDescription = view.findViewById(R.id.textViewDescription);
        mTextViewStatus = view.findViewById(R.id.textViewStatus);

        setupViewModel();
        return view;
    }

    private void setupViewModel(){

        mMainViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);

        mMainViewModel.getLiveDataTasks().observe(getActivity(), new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> observedTasks) {
                Log.d( LOG_TAG, "tasks changed, getLiveDataTasks().observer in ViewModel");

                mMainViewModel.setTasks(observedTasks);
                mTasks = mMainViewModel.getTasks();
                Log.d( LOG_TAG, "setupViewModel observedTasks mTasks.size " + mTasks.size());

                if (mTasks.isEmpty()){

                    Log.d( LOG_TAG, "setupViewModel Database tasks is empty");
                    mTextViewTitle.setText("Database tasks is empty");
                    mTask = null;
                    mMainViewModel.setTask(mTask);

                } else {

                    mTask = mMainViewModel.getTask();
                    if (mTask == null) {
                        mTask = mTasks.get(0);
                    }
                    updateUI();
                }
            }
        });
    }

    private void updateUI() {

        Log.d( LOG_TAG, "updateUI mTask " + mTask.getTaskId());

        mTextViewTitle.setText(mTask.getTitle());
        mTextViewDescription.setText(mTask.getDescription());
        mTextViewStatus.setText(mTask.getStatus());

        mButtonNewTask.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                TaskAddFragment taskAddFragment = TaskAddFragment.newInstance();
                assert getFragmentManager() != null;
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, taskAddFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        mButtonNext.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (isLast(mTask)) {

                    Toast toast = Toast.makeText(getActivity(),
                            "Hurray! end of tasks, time to plant trees and read poetry!",
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();

                } else {

                    mTask = mTasks.get(mTasks.indexOf(mTask) + 1);
                    mMainViewModel.setTask(mTask);

                    mTextViewTitle.setText(mTask.getTitle());
                    mTextViewDescription.setText(mTask.getDescription());
                    mTextViewStatus.setText(mTask.getStatus());
                }
            }

        });

        mButtonPrev.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                if ( isFirst(mTask) ) {

                    Toast toast = Toast.makeText( getActivity(),
                            "No previous tasks",
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();

                } else if (mTasks.contains(mTask)) {

                    mTask = getPrev(mTask);
                    mMainViewModel.setTask(mTask);

                    mTextViewTitle.setText(mTask.getTitle());
                    mTextViewDescription.setText(mTask.getDescription());
                    mTextViewStatus.setText(mTask.getStatus());

                } else { // new inserted a task or deleted a task hence change in index

                    mTask = mTasks.get(0);
                    mMainViewModel.setTask(mTask);
                    mTextViewTitle.setText(mTask.getTitle());
                    Log.d( LOG_TAG,"updateUI Oh Look! out of Task exception");
                }
            }

        });

        mButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mTask != null){

                    Log.d( LOG_TAG,"mButtonDelete " + mTask.getTaskId());
                    mMainViewModel.delete(mTask);  // repository delete db

                    if (isLast(mTask)) {

                        Toast toast = Toast.makeText(getActivity(),
                                "Hurray! end of tasks, time to plant trees and read poetry!",
                                Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        mTasks.remove(mTask);

                    } else {

                        mTask = mTasks.get(mTasks.indexOf(mTask) + 1);
                        mMainViewModel.setTask(mTask);

                        mTextViewTitle.setText(mTask.getTitle());
                        mTextViewDescription.setText(mTask.getDescription());
                        mTextViewStatus.setText(mTask.getStatus());
                    }

                } else {
                    Toast toast = Toast.makeText(getActivity(),
                            "Task list is empty, time to plant trees!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });

    }

    private boolean isFirst(Task task){

        if ( ! mTasks.isEmpty()){
            return (mTasks.get(0).getTaskId().equals(task.getTaskId()));
        }
        return true;
    }

    private boolean isLast(Task task){

        if (mTasks.isEmpty()){

            return true;

        } else {

            return (mTasks.get(mTasks.size() - 1).getTaskId().equals(task.getTaskId()));
        }
    }

    private boolean hasPrev(Task task){
        return (mTasks.indexOf(task) > 0);
    }

    private Task getPrev(Task task){
        return mTasks.get( mTasks.indexOf(task) - 1 );
    }

    private Task getNext(Task task){
        if (isLast(task)){
            return task;
        } else {
            return mTasks.get( mTasks.indexOf(task) + 1 );
        }
    }

}
