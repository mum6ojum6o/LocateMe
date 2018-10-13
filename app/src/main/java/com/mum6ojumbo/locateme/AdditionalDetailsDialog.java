package com.mum6ojumbo.locateme;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mum6ojumbo.locateme.model.CurrentUserLocationSync;
import com.mum6ojumbo.locateme.room.AppExecutor;
import com.mum6ojumbo.locateme.services.SyncService;

import java.io.Serializable;
import java.util.Date;

public class AdditionalDetailsDialog extends DialogFragment implements View.OnClickListener,
SeekBar.OnSeekBarChangeListener{
    private static final String TAG = "AdditionalDetailsDialog";
    private Button mShareWithButton;
    private SeekBar mSeekBar;
    private TextView mShareUntil;
    private EditText mEditText;
    private Date mDate;
    private UpdateContainer updateContainer;
    @Override
    public void onAttach(Context activity){
        super.onAttach(activity);
        updateContainer = (UpdateContainer)activity;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.dialog_additional_sync_details,null);
        mSeekBar =(SeekBar)view.findViewById(R.id.seekBar);
        mSeekBar.setOnSeekBarChangeListener(this);
        mShareWithButton = (Button)view.findViewById(R.id.button_share_with);
        mShareUntil=(TextView) view.findViewById(R.id.label_share_until);
        mEditText = (EditText)view.findViewById(R.id.editText_share_with);
        mShareWithButton.setOnClickListener(this);
        builder.setView(view);
        mDate =new Date();
        return builder.create();

    }



    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.button_share_with:
                //Toast.makeText(getContext(), "you clicked sher with button", Toast.LENGTH_SHORT).show();
                Toast.makeText(getContext(),"shareWith:"+mEditText.getText(),Toast.LENGTH_SHORT).show();
                Log.i(TAG,"share until:"+mDate);
                CurrentUserLocationSync aRecord =
                        new CurrentUserLocationSync(MainActivity.shareId++,0.0,
                                0.0,mDate,
                                mEditText.getText().toString());
                Bundle bundle = new Bundle();
                bundle.putSerializable("CurrentCustomerDetails", aRecord);
                Intent intent = new Intent(getContext(), SyncService.class);
                intent.putExtras(bundle);
                AppExecutor.getInstance().getNetworkIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        getContext().startService(intent);
                    }
                });
                updateContainer.updateMembers();
                updateContainer.storeServiceStartingIntent(intent);
                getFragmentManager().beginTransaction().remove(AdditionalDetailsDialog.this).addToBackStack(null).commit();

        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        if(b){
            Date date = new Date();
            Log.i(TAG,"current date:"+date.toString());
            Log.i(TAG,"value selected:"+i);
            long addedtime = date.getTime()+(i*3600000);
            Date syncUntil = new Date(addedtime);
            mDate= new Date(addedtime);
            int indexStart=syncUntil.toString().indexOf(" ");
            int indexEnd=syncUntil.toString().indexOf(":",-1);
            Log.i(TAG, "indexStart:"+indexStart+" "+" indexEnd:"+indexEnd);
            mShareUntil.setText("Until "+syncUntil.toString().substring(indexStart,indexEnd+3));
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        //do nothing
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        //do nothing
    }
    int minutesMultiplier(int seekbarProgress){
        return seekbarProgress*60;
    }
    interface UpdateContainer{
        public void updateMembers();
        public void storeServiceStartingIntent(Intent serviceStartingIntent);
    }
}
