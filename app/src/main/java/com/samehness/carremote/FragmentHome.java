package com.samehness.carremote;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;

import static com.samehness.carremote.BluetoothConnection.btSocket;

public class FragmentHome extends Fragment {
    View view;
    public FragmentHome(){

    }

    String EngineStandBy = "";
    String EngineStart = "";
    String DoorsLock = "";
    String DoorsUnlock = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_fragment,container,false);

        ImageButton startEngine = view.findViewById(R.id.startEngine);
        startEngine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(), "Engine Ready!", Toast.LENGTH_SHORT).show();
                msg("Engine Ready!",0);
                sendSignal(EngineStandBy,"Engine Ready!");
            }
        });
        startEngine.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                msg("ENGINE STARTED!!!",0);
                msg("ENGINE STARTED!!!",0);
                sendSignal(EngineStart,"ENGINE STARTED!!!");
                return true;
            }
        });

        ImageButton lock = view.findViewById(R.id.lock);
        lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msg("lOCKED",0);
                sendSignal(DoorsLock,"lOCKED");

            }
        });

        ImageButton unlock = view.findViewById(R.id.unlock);
        unlock.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                msg("UNLOCKED",0);
                sendSignal(DoorsUnlock,"UNLOCKED");
                return true;
            }
        });


        return view;
    }

    public void sendSignal (String signal, String message ){
        if (btSocket != null)
        {
            try{
                btSocket.getOutputStream().write(signal.toString().getBytes());
                msg(message,0);
            } catch (IOException e) {
                msg("Error", 0);
            }
        }

    }

    private void msg (String s, int t){
        if(t == 0){
            int toastDurationInMilliSeconds = 500;
            final Toast toast = Toast.makeText(getContext(), s, Toast.LENGTH_SHORT);
            CountDownTimer toastCountDown;
            toastCountDown = new CountDownTimer(toastDurationInMilliSeconds, 500 /*Tick duration*/) {
                public void onTick(long millisUntilFinished) {
                    toast.show();
                }
                public void onFinish() {
                    toast.cancel();
                }
            };

            // Show the toast and starts the countdown
            toast.show();
            toastCountDown.start();

        } else if (t == 1 ){
            Toast toast = Toast.makeText(getContext(), s, Toast.LENGTH_LONG);
            toast.show();
        }


    }

}
