package com.timyrobot.system.triggersystem;

import com.timyrobot.system.bean.BaseCommand;
import com.timyrobot.system.bean.HttpCommand;
import com.timyrobot.system.triggersystem.listener.DataReceiver;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * Created by zhangtingting on 15/9/16.
 */
public enum HttpTrigger {
    INSTANCE;
    private BufferedReader mReader;
    private BufferedWriter mWriter;
    private Socket mSocket;

    private int mCode;
    private boolean isReceiver;

    private DataReceiver mDataReceiver;
    public void init(DataReceiver dataReceiver, int code, boolean isReceiver){
        mDataReceiver = dataReceiver;
        this.isReceiver = isReceiver;
        mCode = code;
        initSocket();
    }

    public void initSocket() {
        //新建一个线程，用于初始化socket和检测是否有接收到新的消息
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                String ip = "121.43.226.152";//IP
                int port = 8888;//Socket

                try {
                    mSocket = new Socket(ip, port);
                    mReader = new BufferedReader(new InputStreamReader(mSocket.getInputStream(), "GBK"));
                    mWriter = new BufferedWriter(new OutputStreamWriter(mSocket.getOutputStream(), "utf-8"));
                    if(isReceiver) {
                        sendReceiverMsg(mCode);
                    }
                    while(true) {
                        if(mReader.ready()) {
                            /*读取一行字符串，读取的内容来自于客户机
                            reader.readLine()方法是一个阻塞方法，
                            从调用这个方法开始，该线程会一直处于阻塞状态，
                            直到接收到新的消息，代码才会往下走*/
                            String data = mReader.readLine();
                            BaseCommand command = new HttpCommand();
                            command.setVoiceReconContent(data);
                            mDataReceiver.onReceive(command);
                            //handler发送消息，在handleMessage()方法中接收
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    try {
                        mWriter.close();
                        mReader.close();
                        mSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        thread.start();
    }

    public void sendMsg(String msgSend) {
        try {
            JSONObject json = new JSONObject();
            json.put("id", mCode);
            json.put("msg", msgSend+"\n");
            mWriter.write(json.toString());
            mWriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendReceiverMsg(int code){
        try {
            JSONObject json = new JSONObject();
            json.put("login", code);
            mWriter.write(json.toString());
            mWriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}