package com.example.milan.dz10_21;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Chat extends Thread {
    private final Socket socket;
    private final InputStream inputStream;
    private final OutputStream outputStream;

    public Chat(Socket sock) {
        socket = sock;
        InputStream tmpIn = null;
        OutputStream tmpOut = null; 
        try {
            //---kreira inputstream i outputstream objekte
            // za čitanje i pisanje preko socket-a---
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) {
            Log.d("SocketChat", e.getLocalizedMessage());
        } 
        inputStream = tmpIn;
        outputStream = tmpOut;
    }
    
    public void run() {
        //---bafer skladište za konkretan tok---
        byte[] buffer = new byte[1024];
        //---vraćeni bajtovi iz read()---
        int bytes;  
        //---nastavak osluškivanja InputStream do 
        // pojave izuzetka---
        while (true) {
            try {
                //---čitanje iz inputStream---
                bytes = inputStream.read(buffer);

                //---ažuriranje glavne aktivnosti - UI---
                Fragment3.UIupdater.obtainMessage(
                    0,bytes, -1, buffer).sendToTarget();
            } catch (IOException e) {
                break;
            }
        }
    } 
    //---poziv iz glavne aktivnosti 
    // slanje podataka na udaljeni uređaj---
    public void write(byte[] bytes) {
        try {
            outputStream.write(bytes);
        } catch (IOException e) { }
    }
 
    //---pozivanje iz glavne aktivnosti 
    // za zatvaranje konekcije--- 
    public void cancel() {
        try {
            socket.close();
        } catch (IOException e) { }
    }
}
