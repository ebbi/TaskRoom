package com.example.taskroom.ui.main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.example.taskroom.R;
import com.example.taskroom.database.Task;

import java.util.List;

public class TaskAddFragment extends Fragment {

    private static final String LOG_TAG = TaskAddFragment.class.getSimpleName();

    private MainViewModel mMainViewModel;

    public static TaskAddFragment newInstance() {

        return new TaskAddFragment();
    }

    public TaskAddFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mMainViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);

        final View view = inflater.inflate(R.layout.fragment_add_task, container, false);
        updateUI(view);
        return view;
    }

    private void updateUI(View view) {

        final EditText mEditTextTitle = view.findViewById(R.id.editTextTitle);
        final EditText mEditTextDescription = view.findViewById(R.id.editTextDescription);
        mEditTextTitle.requestFocus();

        Button mButtonAddTask = view.findViewById(R.id.buttonAddTask);
        mButtonAddTask.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Task newTask = new Task();

                newTask.setTitle(mEditTextTitle.getText().toString());
                newTask.setDescription(mEditTextDescription.getText().toString());

                Log.d( LOG_TAG, "updateUI insert " + newTask.getTaskId());
                mMainViewModel.insert(newTask);

                MainFragment mainFragment = MainFragment.newInstance();
                assert getFragmentManager() != null;
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, mainFragment);
                transaction.commitNow();

            }
        });
    }

}
