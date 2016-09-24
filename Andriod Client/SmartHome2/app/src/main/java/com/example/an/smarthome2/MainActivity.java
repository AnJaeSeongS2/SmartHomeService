package com.example.an.smarthome2;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.util.Log;
import android.app.Activity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.ECFieldFp;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.EllipticCurve;

import javax.crypto.KeyAgreement;
import javax.crypto.spec.DHParameterSpec;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;


public class MainActivity extends AppCompatActivity {


    private static SeekBar seekBar;
    private static TextView textViewHigh;
    private static  TextView textViewLow;
    private static TextView textViewMiddle;
    private static TextView textViewIcon;

    private int portNum = 5678;
    private String return_msg;
    private EditText msgEditText;

    private static Button button_sbm_mypage;
    private static Button button_sbm_home;
    private static Button button_sbm_write;
    private static Button button_onoff;
    private static ViewPager mViewPager;

    private AES256Cipher aes256 = AES256Cipher.getInstance();

    int onoffCountBL = 0;  //bedroom light
    int onoffCountLL = 0; //livingroom window
    int onoffCountLT = 0;  //livingroom light
    // int onoffCountTW = 0;  //Toilet window

    MyClientTask myClientTask;
    ImageView imageViewLivingTemp;
    ImageView imageViewLivingLight;
    ImageView imageViewBedroomLight;
    // ImageView imageViewToiletWindow;;

/*
        private Socket inetSocket = null;
        private class TCPclient implements Runnable{
            private static final String serverIP = "192.168.0.22";
            private static final int serverPort = 5678;
            //private Socket inetSocket = null;
            private String msg;




            public TCPclient(String inputMsg){
                this.msg = inputMsg;
                try {
                    System.out.println("TCP connecting///");
                    Log.d("TCP", "C : conneting.............");
                    inetSocket = new Socket(serverIP, serverPort);
                }catch(Exception e){
                    Log.e("TCP", "C: Error on connecting : ", e);
                }
            }
            public TCPclient(){
                this.msg = "00";
                try {
                    System.out.println("TCP connection....");

                    Log.d("TCP", "C : conneting.............");
                    inetSocket = new Socket(serverIP, serverPort);
                }catch(Exception e){
                        Log.e("TCP", "C: Error on connecting : ", e);

                }
            }
            public void run(){//receive action

                try{
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(inetSocket.getInputStream()));
                    return_msg = in.readLine();
                    System.out.println("받은 메시지 :");
                    System.out.println(return_msg);
                    Log.d("TCP", "C: receive msg : " + return_msg);

                    switch(Integer.getInteger(return_msg)/10){
                        case 0:
                            if(Integer.getInteger(return_msg)%10 ==0){
                                imageViewBedroomLight.setImageResource(R.drawable.icon_light_off);
                                imageViewBedroomLight.invalidate();
                                onoffCountBL =0;
                            }
                            else{
                                imageViewBedroomLight.setImageResource(R.drawable.icon_light_on);
                                imageViewBedroomLight.invalidate();
                                onoffCountBL =1;
                            }
                            break;
                        case 1:
                            if(Integer.getInteger(return_msg)%10 ==0){
                                imageViewLivingLight.setImageResource(R.drawable.icon_light_off);
                                imageViewLivingLight.invalidate();
                                onoffCountLT =0;
                            }
                            else{
                                imageViewLivingLight.setImageResource(R.drawable.icon_light_on);
                                imageViewLivingLight.invalidate();
                                onoffCountLT = 1;
                            }
                            break;
                        case 2:
                            if(Integer.getInteger(return_msg)%10 ==0){
                                imageViewLivingWindow.setImageResource(R.drawable.icon_window_close);
                                imageViewLivingWindow.invalidate();
                                onoffCountLW =0;
                            }
                            else{
                                imageViewLivingWindow.setImageResource(R.drawable.icon_window_open);
                                imageViewLivingWindow.invalidate();
                                onoffCountLW =1;
                            }
                            break;

                       /* case 3:
                            if(Integer.getInteger(return_msg)%10 ==0){
                                imageViewToiletWindow.setImageResource(R.drawable.icon_window_close);
                                imageViewToiletWindow.invalidate();
                                onoffCountTW =0;
                            }
                            else{
                                imageViewToiletWindow.setImageResource(R.drawable.icon_window_open);
                                imageViewToiletWindow.invalidate();
                                onoffCountTW =1 ;
                            }
                            break;
                        */

    /*
                    }

                }catch(Exception e){
                    Log.e("TCP", "C: Error on send/receive : ", e);
                }
                finally{
                    //inetSocket.close();
                }

            }
        }
*/

    public void OnClickButtonListener(){

        button_onoff = (Button)findViewById(R.id.button_onoff);
        button_onoff.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v) {
                        byte[] msg;
                        //MyClientTask myClientTask;
                      //  MyClientTaskReceive myClientTaskReceive;
                        switch(mViewPager.getCurrentItem()) {
                            case 0:
                                msg = new byte[2];
                                msg[0] = (byte)0;
                                msg[1] = (byte)0;

                                for( int i = 0; i < msg.length; i++){

                                    System.out.print((int)msg[i]);
                                    System.out.print(" ");
                                }
                                System.out.println(" ");
                                System.out.println(msg.toString());

                                myClientTask = new MyClientTask("192.168.0.22",portNum);
                                myClientTask.setData(msg);
                                myClientTask.execute();


                                portNum++;
                             //   myClientTaskReceive = new MyClientTaskReceive("192.168.0.22",5678);
                              //  myClientTaskReceive.execute();
                                break;
                            case 1:

                                msg = new byte[2];
                                msg[0] = (byte)2;
                                msg[1] = (byte)0;

                                for( int i = 0; i < msg.length; i++){

                                    System.out.print((int)msg[i]);
                                    System.out.print(" ");
                                }
                                System.out.println(" ");
                                System.out.println(msg.toString());

                                myClientTask = new MyClientTask("192.168.0.22",portNum);
                                myClientTask.setData(msg);
                                myClientTask.execute();

                               // myClientTaskReceive = new MyClientTaskReceive("192.168.0.22",5678);
                                //myClientTaskReceive.execute();
                                portNum++;
                                break;

                            case 2:
                                button_onoff.setVisibility(View.INVISIBLE);
                                seekBar.setVisibility(View.VISIBLE);
                                textViewMiddle.setVisibility(View.VISIBLE);
                                textViewHigh.setVisibility(View.VISIBLE);
                                textViewLow.setVisibility(View.VISIBLE);

                                break;
                          /*  case 3:
                                if(onoffCountTW ==0){
                                    imageViewToiletWindow.setImageResource(R.drawable.icon_window_close);
                                    imageViewToiletWindow.invalidate();
                                    onoffCountTW++;
                                }
                                else{
                                    imageViewToiletWindow.setImageResource(R.drawable.icon_window_open);
                                    imageViewToiletWindow.invalidate();
                                    onoffCountTW--;
                                }
                                break;
                                */
                        }

                    }
                }
        );
        button_sbm_mypage = (Button)findViewById(R.id.buttonMypage);
        button_sbm_mypage.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent("com.example.an.smarthome2.secondActivity");
                        startActivity(intent);
                    }
                }
        );
        button_sbm_write = (Button)findViewById(R.id.buttonWrite);
        button_sbm_write.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent("com.example.an.smarthome2.secondActivity");
                        startActivity(intent);
                    }
                }
        );
        button_sbm_home = (Button)findViewById(R.id.buttonHome);
        button_sbm_home.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent("com.example.an.smarthome2.secondActivity");
                        startActivity(intent);
                    }
                }
        );
    }
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    //  private ViewPager mViewPager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());


        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        //이미지뷰
        imageViewLivingTemp = (ImageView)findViewById(R.id.imageViewLivingTemp);
        imageViewLivingLight = (ImageView)findViewById(R.id.imageViewLivingLight);
        imageViewBedroomLight = (ImageView)findViewById(R.id.imageViewBedroomLight);
        // imageViewToiletWindow = (ImageView)findViewById(R.id.imageViewToiletWindow);

        textViewHigh = (TextView)findViewById(R.id.textViewHigh);
        textViewLow = (TextView)findViewById(R.id.textViewLow);
        textViewMiddle = (TextView)findViewById(R.id.textViewMiddle);
        textViewIcon = (TextView)findViewById(R.id.textViewIcon);

        seekBar = (SeekBar)findViewById(R.id.seekBar);

        /*MyClientTaskReceive myClientTaskReceive = new MyClientTaskReceive("192.168.0.22",5678);
        myClientTaskReceive.execute();
*/

        seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                progress = progresValue;
                //  Toast.makeText(getApplicationContext(), "Changing seekbar's progress", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

                //Toast.makeText(getApplicationContext(), "Started tracking seekbar", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                textViewMiddle.setText(progress + "도");

                byte[] msg;
                MyClientTask myClientTask;
                msg = new byte[2];
                msg[0] = (byte)1;
                msg[1] = (byte)progress;

                for( int i = 0; i < msg.length; i++){

                    System.out.print((int)msg[i]);
                    System.out.print(" ");
                }
                System.out.println(" ");
                System.out.println(msg.toString());
                myClientTask = new MyClientTask("192.168.0.22",portNum);
                myClientTask.setData(msg);
                myClientTask.execute();
                button_onoff.setVisibility(View.VISIBLE);
                seekBar.setVisibility(View.INVISIBLE);
                textViewMiddle.setVisibility(View.INVISIBLE);
                textViewHigh.setVisibility(View.INVISIBLE);
                textViewLow.setVisibility(View.INVISIBLE);

                Toast.makeText(getApplicationContext(), progress +"도 로 설정합니다.", Toast.LENGTH_SHORT).show();portNum++;
            }
        });


        //MyClientTaskReceive myClientTaskReceive = new MyClientTaskReceive("192.168.0.22",5678);
        //myClientTaskReceive.execute();myClientTaskReceive.execute();myClientTaskReceive.execute();

        OnClickButtonListener();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        //오른쪽상단 setting버튼
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */


    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */

        private static final String ARG_SECTION_NUMBER = "section_number";


        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);

            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));


            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 3;
            //return 4; //욕실 창문 구현 실패.
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "침실 주조명";
                case 1:
                    return "거실 주조명";
                case 2:
                    return "거실 난방";
                /*
                case 3:
                    return "욕실 창문";
                    */
            }
            return null;
        }
    }

    public class MyClientTask extends AsyncTask<Void, Void, Void> {

        String dstAddress ;
        int dstPort ;
        String response;
        byte[] myData;

        MyClientTask(String addr, int port ){
            dstAddress = addr;
            dstPort = port;
        }

        public void setData(byte[] bytes){
                myData = bytes.clone();

        }
        @Override
        protected Void doInBackground(Void... arg0) {

            try {
                Socket socket = new Socket(dstAddress, dstPort);

                OutputStream outputStream = socket.getOutputStream();
                //ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(myData);
                outputStream.write(myData);
                System.out.println("length :"+myData.length);
//                System.out.println(myData[0]);
                //              System.out.println(myData[1]);
                socket.close();
                //  response = byteArrayOutputStream.toString("UTF-8");

            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //textResponse.setText(response);
            super.onPostExecute(result);
        }

    }
    public class MyClientTaskReceive extends AsyncTask<Void, Void, Void> {

        String dstAddress ;
        int dstPort ;
        String response;
        byte[] buffer;
        MyClientTaskReceive(String addr, int port ){
            dstAddress = addr;
            dstPort = port;
            buffer = new byte[2];
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try {
                Socket socket = new Socket(dstAddress, dstPort);

                InputStream inputStream = socket.getInputStream();
                ByteArrayOutputStream byteArrayOutputStream =
                        new ByteArrayOutputStream(2);

                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1){
                    byteArrayOutputStream.write(buffer, 0, bytesRead);
                }

                System.out.println((int)buffer[0]);
                System.out.println((int)buffer[1]);
                socket.close();
                response = byteArrayOutputStream.toString("UTF-8");

            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println("이상함1.");
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            System.out.println((int)buffer[0]);
            System.out.println((int)buffer[1]);
            switch(buffer[0]){
                case 0:
                    if( buffer[1]==0){
                        imageViewBedroomLight.setImageResource(R.drawable.icon_light_off);
                        imageViewBedroomLight.invalidate();
                    }
                    else{
                        imageViewBedroomLight.setImageResource(R.drawable.icon_light_on);
                        imageViewBedroomLight.invalidate();
                    }
                    break;
                case 1:
                    if(buffer[1]==0){
                        textViewMiddle.setText("off");
                        textViewIcon.setText("off");
                    }
                    else{
                        textViewMiddle.setText((int)buffer[1]+ "도");
                        textViewIcon.setText((int)buffer[1]+ "도");
                    }
                    break;
                case 2:
                    if( buffer[1]==0){
                        imageViewLivingLight.setImageResource(R.drawable.icon_light_off);
                        imageViewLivingLight.invalidate();
                    }
                    else {
                        imageViewLivingLight.setImageResource(R.drawable.icon_light_on);
                        imageViewLivingLight.invalidate();
                    }
                    break;
                default:
                    System.out.println("이상한게 도착했다.");

            }
            System.out.println("이상함2.");
            super.onPostExecute(result);
        }

    }
}


