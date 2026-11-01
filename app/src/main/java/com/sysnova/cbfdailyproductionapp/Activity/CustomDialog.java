package com.sysnova.cbfdailyproductionapp.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.sysnova.cbfdailyproductionapp.R;

public class CustomDialog extends AppCompatDialogFragment {

    private EditText qtyedit;
    private CustomeDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_dialogue, null);

        builder.setView(view)
                .setTitle("OTP Authentication")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String qtyval = qtyedit.getText().toString();
                        listener.applytexts(qtyval);

//                        if(qtyval.isEmpty() || qtyval.equals("") || qtyval.equals(null) || qtyval.length()!=6){
//                            qtyedit.setError("Please enter 6 digits OTP");
//                        }else {
//                            listener.applytexts(qtyval);
//                        }
                    }
                });

        qtyedit = view.findViewById(R.id.qtyedittext);
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (CustomeDialogListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()+"must implement CustomeDialogListener");
        }
    }

    public interface CustomeDialogListener{
        void applytexts(String qty);
    }
}
