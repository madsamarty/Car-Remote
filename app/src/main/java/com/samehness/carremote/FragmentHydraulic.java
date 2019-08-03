package com.samehness.carremote;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;

import static com.samehness.carremote.BluetoothConnection.btSocket;

public class FragmentHydraulic extends Fragment {
    View view;
    public FragmentHydraulic(){

    }

    boolean front_left= false;
    boolean front_right = false;
    boolean rear_left = false;
    boolean rear_right = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.hydraulic_fragment,container,false);




        final ImageButton window1 = view.findViewById(R.id.button1);
        window1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (front_left == false){
                    window1.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ic_car_door_red));
                    front_left = true;
                    //front_left = true;
                }else if (front_left == true){
                    window1.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ic_car_door_white));
                    front_left = false;
                    //front_left = false;
                }
            }
        });
        final ImageButton window2 = view.findViewById(R.id.button2);
        window2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rear_left == false){
                    window2.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ic_car_door_red));
                    rear_left = true;
                    //rear_left = true;
                }else if (rear_left == true){
                    window2.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ic_car_door_white));
                    rear_left = false;
                    //rear_left = false;
                }
            }
        });
        final ImageButton window3 = view.findViewById(R.id.button3);
        window3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (front_right == false){
                    window3.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ic_car_door_m_red));
                    front_right = true;
                    //front_right = true;
                }else if (front_right == true){
                    window3.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ic_car_door_m_white));
                    front_right = false;
                    //front_right = false;
                }
            }
        });
        final ImageButton window4 = view.findViewById(R.id.button4);
        window4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rear_right == false){
                    window4.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ic_car_door_m_red));
                    rear_right = true;
                    //rear_right = true;
                }else if (rear_right == true){
                    window4.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ic_car_door_m_white));
                    rear_right = false;
                    //rear_right = false;
                }
            }
        });

        ImageButton windowsUp = view.findViewById(R.id.windowsUp);
        windowsUp.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                msg("Windows Up...",0);
                if(front_left)sendSignal("1");
                if(front_right)sendSignal("3");
                if(rear_left)sendSignal("5");
                if(rear_right)sendSignal("7");
                return true;
            }
        });

        ImageButton windowsDown = view.findViewById(R.id.windowsDown);
        windowsDown.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                msg("Windows Down...",0);
                if(front_left)sendSignal("2");
                if(front_right)sendSignal("4");
                if(rear_left)sendSignal("6");
                if(rear_right)sendSignal("8");
                return true;
            }
        });


        return view;
    }


    public void sendSignal (String number){
        if (btSocket != null)
        {
            try{
                btSocket.getOutputStream().write(number.toString().getBytes());
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
