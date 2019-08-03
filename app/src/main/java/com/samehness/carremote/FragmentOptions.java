package com.samehness.carremote;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.io.IOException;

import static com.samehness.carremote.BluetoothConnection.btSocket;

public class FragmentOptions extends Fragment {
    View view;
    public FragmentOptions(){

    }

    //Change the values to suit your needs
    String TrunkOn="on";
    String TrunkOff="off";

    String WarningOn="warning_on";
    String WarningOff="warning_off";

    String LightsOn="lights_on";
    String LightsOff="lights_off";

    String SideMirrorOn="side_mirror_on";
    String SideMirrorOff="side_mirror_off";

    String AirConditionerOn="air_conditioner_on";
    String AirConditionerOff="air_conditioner_off";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.options_fragment,container,false);




        LinearLayout trunk = view.findViewById(R.id.trunk);
        trunk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                View popupView = inflater.inflate(R.layout.trunk_choice, null);
                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                boolean focusable = true;
                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
                popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
                ImageButton trunk_on = popupView.findViewById(R.id.trunk_on);
                trunk_on.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendSignal(TrunkOn,"Opening Trunk...");
                    }
                });
                ImageButton trunk_off = popupView.findViewById(R.id.trunk_off);
                trunk_off.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendSignal(TrunkOff,"Closing Trunk...");
                    }
                });
            }
        });

        LinearLayout warning = view.findViewById(R.id.warning);
        warning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                View popupView = inflater.inflate(R.layout.warning_choice, null);
                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                boolean focusable = true;
                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
                popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

                ImageButton warning_on = popupView.findViewById(R.id.warning_on);
                warning_on.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendSignal(WarningOn,"Turning On Warning...");
                    }
                });
                ImageButton warning_off = popupView.findViewById(R.id.warning_off);
                warning_off.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendSignal(WarningOff,"Turning Off Warning...");
                    }
                });

            }
        });

        LinearLayout lights = view.findViewById(R.id.lights);
        lights.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                View popupView = inflater.inflate(R.layout.lights_choice, null);
                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                boolean focusable = true;
                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
                popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
                ImageButton lights_on = popupView.findViewById(R.id.lights_on);
                lights_on.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendSignal(LightsOn,"Turning On Lights...");
                    }
                });
                ImageButton lights_off = popupView.findViewById(R.id.lights_off);
                lights_off.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendSignal(LightsOff,"Turning Off Lights...");
                    }
                });
            }
        });

        LinearLayout side_mirrors = view.findViewById(R.id.side_mirrors);
        side_mirrors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                View popupView = inflater.inflate(R.layout.side_mirror_choice, null);
                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                boolean focusable = true;
                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
                popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

                ImageButton side_mirror_on = popupView.findViewById(R.id.side_mirror_off);
                side_mirror_on.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendSignal(SideMirrorOn,"Turning On Side Mirrors...");
                    }
                });

                ImageButton side_mirror_off = popupView.findViewById(R.id.side_mirror_off);
                side_mirror_off.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendSignal(SideMirrorOff,"Turning Off Side Mirrors...");
                    }
                });

            }
        });
        LinearLayout air_conditioner = view.findViewById(R.id.air_conditioner);
        air_conditioner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                View popupView = inflater.inflate(R.layout.air_conditioner_choice, null);
                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                boolean focusable = true;
                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
                popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

                ImageButton air_conditioner_on = popupView.findViewById(R.id.air_conditioner_on);
                air_conditioner_on.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendSignal(AirConditionerOn,"Turning On Air Conditioner...");
                    }
                });

                ImageButton air_conditioner_off = popupView.findViewById(R.id.air_conditioner_off);
                air_conditioner_off.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendSignal(AirConditionerOff,"Turning Off Air Conditioner...");
                    }
                });
            }
        });

        return view;
    }


    // Main Class for communicating with Arduino Board
    public void sendSignal (String signal, String message ){
        if (btSocket != null)
        {
            try{
                btSocket.getOutputStream().write(signal.getBytes());
                msg(message,0);
            } catch (IOException e) {
                msg("Error", 0);
            }
        }

    }

    // Make Toast Class
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
