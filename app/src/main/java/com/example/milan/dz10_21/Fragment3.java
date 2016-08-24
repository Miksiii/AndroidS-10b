package com.example.milan.dz10_21;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class Fragment3 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        txtMessage = (EditText)getView().findViewById(R.id.txtMessage);
        txtMessagesReceived = (TextView)getView().
                findViewById(R.id.showMessage);


        return inflater.inflate(R.layout.fragment_fragment3, container, false);
    }

    static final String NICKNAME = "NIkola";
    //---socket---
    InetAddress serverAddress;
    Socket socket;
    //---svi pogledi---
    static TextView txtMessagesReceived;
    EditText txtMessage;
    //---nit za komuniciranje sa socket-om---
    Chat commsThread;
    //---ažuriranje UI glavne aktivnosti---
    static Handler UIupdater = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int numOfBytesReceived = msg.arg1;
            byte[] buffer = (byte[]) msg.obj;

            //---prevođenje byte niza u string---
            String strReceived = new String(buffer);

            //---pruzimanje aktuelnog stringa---
            strReceived = strReceived.substring(
                    0, numOfBytesReceived);

            //---prikazivanje primljenog teksta u TextView---
            txtMessagesReceived.setText(
                    txtMessagesReceived.getText().toString() +
                            strReceived);
        }
    };

    private class CreateCommThreadTask extends AsyncTask
            <Void, Integer, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                //---kreiranje soketa---
                serverAddress =
                        InetAddress.getByName("192.168.1.37");
                socket = new Socket(serverAddress, 500);
                commsThread = new Chat(socket);
                commsThread.start();
                //---prijavljivanje korisnika; šalje nadimak---
                sendToServer(NICKNAME);
            } catch (UnknownHostException e) {
                Log.d("Sockets", e.getLocalizedMessage());
            } catch (IOException e) {
                Log.d("Sockets", e.getLocalizedMessage());
            }
            return null;
        }
    }

    private class WriteToServerTask extends AsyncTask
            <byte[], Void, Void> {
        protected Void doInBackground(byte[]...data) {
            commsThread.write(data[0]);
            return null;
        }
    }
    private class CloseSocketTask extends AsyncTask
            <Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                socket.close();
            } catch (IOException e) {
                Log.d("Sockets", e.getLocalizedMessage());
            }
            return null;
        }
    }
    public void onClickSend(View view) {
        //---prosleđivanje poruke na server---
        sendToServer(txtMessage.getText().toString());
    }

    private void sendToServer(String message) {
        byte[] theByteArray =
                message.getBytes();
        new WriteToServerTask().execute(theByteArray);
    }

    @Override
    public void onResume() {
        super.onResume();
        new CreateCommThreadTask().execute();
    }

    @Override
    public void onPause() {
        super.onPause();
        new CloseSocketTask().execute();
    }
}
