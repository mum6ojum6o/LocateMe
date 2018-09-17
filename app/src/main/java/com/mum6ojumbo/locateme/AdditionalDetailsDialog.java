package com.mum6ojumbo.locateme;

import android.app.AlertDialog;
import android.app.Dialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

public class AdditionalDetailsDialog extends DialogFragment implements View.OnClickListener,
SeekBar.OnSeekBarChangeListener{
    private static final String TAG = "AdditionalDetailsDialog";
    private Button mShareWithButton;
    private SeekBar mSeekBar;
    private TextView mShareUntil;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.dialog_additional_sync_details,null);
        mSeekBar =(SeekBar)view.findViewById(R.id.seekBar);
        mSeekBar.setOnSeekBarChangeListener(this);
        mShareWithButton = (Button)view.findViewById(R.id.button_share_with);
        mShareUntil=(TextView) view.findViewById(R.id.label_share_until);
        mShareWithButton.setOnClickListener(this);
        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.button_share_with:
                Toast.makeText(getContext(), "you clicked sher with button", Toast.LENGTH_SHORT).show();
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
}
