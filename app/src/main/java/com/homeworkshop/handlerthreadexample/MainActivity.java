package com.homeworkshop.handlerthreadexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;

import static com.homeworkshop.handlerthreadexample.ExampleHandlerThread.EXAMPLE_TASK;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    //Wątek tła z własnym handlerem
    private ExampleHandlerThread handlerThread = new ExampleHandlerThread();

    private ExampleRunnable1 runnable1 = new ExampleRunnable1();
    //Obiekt token do indentyfikacji Runnable przekazywanego do handlera
    private Object token = new Object();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handlerThread.start();
    }

    public void doWork(View view) {

        Message msg = Message.obtain(); //tworzenie wiadomości
        msg.what = EXAMPLE_TASK; //identyfikator wiadomości
        msg.arg1 = 23; // możemy wysłać jakiś argument razem w wiadomości
        msg.obj = "Obj String"; //możemy wysłać jakis obiekt np String razem w wiadomości
        //msg.setData( bundle ); // mozemy wysłać jeszcze więcej danych w wiadomości w postaci Bundle

        handlerThread.getHandler().sendMessage(msg); // wysyłamy tym sposobem bo dodaliśmy wiecej rzeczy do zasobu widomości
        //handlerThread.getHandler().sendEmptyMessage(1); // tak możemy w jednej lini wysłać wiadomoć z indentyfikatorem 1 bez tworzenia osobno tej wiadomości

        //uruchomiamy różne zadania dodając je do wątku tła
        handlerThread.getHandler().postAtTime(runnable1, token, SystemClock.uptimeMillis());
        handlerThread.getHandler().post(runnable1);
//        handlerThread.getHandler().post(new ExampleRunnable1());
//        handlerThread.getHandler().post(new ExampleRunnable1());
//        handlerThread.getHandler().postAtFrontOfQueue(new ExampleRunnable2());
    }

    public void removeMessages(View view) {
//        handlerThread.getHandler().removeCallbacksAndMessages(null); // z nullem usuwa wszystkie messages i callbacks
//        handlerThread.getHandler().removeCallbacks(runnable1);
        handlerThread.getHandler().removeCallbacks(runnable1,token);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handlerThread.quit();
    }

    //zadanie do wykonania w wątku niegłownym
    static class ExampleRunnable1 implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 4; i++) {
                Log.d(TAG, "Runnable1: " + i);
                SystemClock.sleep(1000);
            }
        }
    }

    static class ExampleRunnable2 implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 4; i++) {
                Log.d(TAG, "Runnable2: " + i);
                SystemClock.sleep(1000);
            }
        }
    }
}