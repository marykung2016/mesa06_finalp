package com.guru.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//1.On LogIn button click checkLogin() function is triggered.
// Which innitiate AsyncLogin class to carry out Asynchronous task.
// 按下LogIn按鈕時，啟動"checkLogin()"登入檢查功能
// 此動作將會初始化AsyncLogin類別來進行非同步任務
//2.onPreExecute(), invoked on the UI thread before the task is executed.
// Here We are displaying loading message.
// "onPreExecute()執行前的功能：在任務執行前，調用UI前景執行續。
// 於此同時我們也會顯示下載中的訊息。
//3.doInBackground(Params…), invoked on the background thread immediately after
//  onPreExecute() finishes executing.
// "doInBackground(Params...)在背景中執行(參數...)的功能：在onPreExecute()執行前的功能
//  執行完畢後，立即調用背景執行緒。
// The sending and recieving data from and to php file using HttpURLConnection
// class has done in this function.
// 透過HttpURLConnection類別從PHP檔案送出及取得資料的功能在此完成
//4.onPostExecute(Result), invoked on the UI thread after the background
// computation finishes. Here we are checking for recieved result.
//  "onPostExecute(Result)在執行完成後(結果)的功能：在背景的計算完成後，調用UI前景執行緒。
// 我們在此檢查收到的結果。
//5.The parameters params[0] and params[1] is from AsyncLogin’s execute method.
//  參數params[0]及params[1]來自於AsyncLogin類別的執行方法。
public class MainActivity extends AppCompatActivity {

    //CONNECTION_TIMEOUT and READ_TIMEOUT are in milliseconds
    //網路連線跟讀取的限制時間，其單位為毫秒
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private EditText etEmail;
    private EditText etPassword;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Get Reference to variables
        //變數的指向
        etEmail = (EditText) findViewById(R.id.email);
        //將Email變數名稱參考/指向到EditText這個物件中其id
        // 名稱為email的元件
        etPassword = (EditText) findViewById(R.id.password);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    //Trggers when LOGIN Button clicked
    //當按下LOGIN按鈕時觸發
    public void checkLogin(View arg0) {
        //Get text from email and password field
        //從email及password的(物件)資料成員中取得內容
        final String email = etEmail.getText().toString();
        if (!isValidEmail(email)) {
            //set error message for email field
            //設定email資料成員的錯誤訊息
            etEmail.setError("Invalid Email");
        }

        final String password = etPassword.getText().toString();
        if (isValidPassword(password)) {
            //set error message for password field
            //設定password資料成員的錯誤訊息
            etPassword.setError("Password cannot be empty");
        }

        if (isValidEmail(email) && isValidPassword(password)) {
            //Validation Completed 格式驗證完成

            //Initialize AsyncLogin()class with email and password
            //初始化"AsyncLogin()"類別，其中包含了其資料成員:email跟password
            new AsyncLogin().execute(email, password);
        }
    }

    //validating email id
    //規定email的格式並驗證輸入的email是否符合要求
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    //validating password
    // 規定密碼的格式並驗證輸入是否符合要求

    private boolean isValidPassword(String pass) {
        if (pass != null && pass.length() >= 4) {
            return true;
        }
        return false;
    }

    private class AsyncLogin extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(MainActivity.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //this method will be running on UI thread
            //這個方法將會在UI前景執行緒中執行
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();
        }

        @Override
        protected String doInBackground(String... params) {
            //Enter URL address where your php file resides
            //輸入你的PHP檔案位置的URL門號
            try {
                url = new URL("http://10.2.1.132/login.inc.php");
                //10.2.1.132 iii
                //192.168.43.144 home
            } catch (MalformedURLException e) {
                Log.v("mary", e.toString());
                return "exception e";
            }

            try {
                // Setup HttpURLConnection class to send and
                // receive data from php and mysql
                //設定Http網路連線的類別來送出及接收php跟mysql的資料

                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling
                // of both send and receive
                // 定義兩個方法來描述收發資料的處理

                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Append parameters to URL
                //將參數附加到URL門牌
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("username", params[0])
                        .appendQueryParameter("password", params[1]);
                String query = builder.build().getEncodedQuery();

                // Open connection for sending data
                //將連線打開來送出資料
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                os.close();
                conn.connect();

            } catch (IOException e1) {
                Log.v("mary", e1.toString());
                return "exception e1";

            }

            try {
                int response_code = conn.getResponseCode();

                // Check if successful connection made
                //檢查是否已經成功連線
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    //讀取伺服器送來的資料
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }


                    //Pass data to onPostExecute method
                    //將資料轉到"onPostExecute"執行後的方法
                    return (result.toString());

                } else {

                    return ("unsuccessful");
                }

            } catch (IOException e2) {
                Log.v("mary", e2.toString());
                return "exception e2";

            } finally {
                conn.disconnect();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            //this method will be running on UI thread
            //這個方法將會跑在UI前景執行緒
            pdLoading.dismiss();
            if (result.equalsIgnoreCase("true")) {
/* Here launching another activity when login successful.
If you persist login state use sharedPreferences of Android.
and logout button to clear sharedPreferences.
當登入成功時，這裡會啟動另外一個activity
如果你想要記住這個登入狀態，請使用Android的分享喜好sharedPreference功能
登出時清除分享喜好 */
                Intent intent = new Intent(
                        MainActivity.this, SuccessActivity.class);
                startActivity(intent);
                MainActivity.this.finish();
            } else if (result.equalsIgnoreCase("false")) {
                // If username and password does not match display
                // a error message
                //如果username跟password不符合，則顯示錯誤訊息
                Toast.makeText(MainActivity.this, "電子郵件或密碼錯誤" +
                        "!", Toast.LENGTH_LONG).show();
            } else if (result.equalsIgnoreCase("exception")
                    || result.equalsIgnoreCase("unsuccessful")) {

                Toast.makeText(MainActivity.this, "錯誤!" +
                                "連線有問題"
                        , Toast.LENGTH_LONG).show();
            }
        }
    }
}
