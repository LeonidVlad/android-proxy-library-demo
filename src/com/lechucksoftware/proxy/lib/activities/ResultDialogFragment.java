package com.lechucksoftware.proxy.lib.activities;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ResultDialogFragment extends DialogFragment
{
	String msg;

	public ResultDialogFragment(String message)
	{
		msg = message;
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
        View view = inflater.inflate(R.layout.dialog, container);
        getDialog().setTitle("Test: Get URI - Obtained result:");
        
        TextView t = (TextView) view.findViewById(R.id.lbl_your_name);
        t.setText(msg);

        return view;
    }

}
