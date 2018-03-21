package com.sample1.sample1;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MainMenuActivity extends AppCompatActivity {
    Connection con;
    String un,pass,db,ip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Declaring Server ip, username, database name and password
        ip = "champdbinstance.c15rihsciryr.ap-south-1.rds.amazonaws.com";//"106.51.22.49";
        db = "champdbinstance";
        un = "Admin";
        pass = "Vishnu123";
       /* Ip  :106.51.22.49 (not sure)
        DB : champdbinstance
        User: Admin
        Pass: Vishnu123*/
    }

    public void onclick(View view){

        switch (view.getId()){
            case R.id.btn_login:
                CheckLogin checkLogin = new CheckLogin();// this is the Asynctask, which is used to process in background to reduce load on app process
                checkLogin.execute("");
                break;
        }
    }

    public class CheckLogin extends AsyncTask<String,String,String>
    {
        String z = "";
        Boolean isSuccess = false;

        @Override
        protected void onPreExecute()
        {

        }

        @Override
        protected void onPostExecute(String r)
        {

            Toast.makeText(MainMenuActivity.this, r, Toast.LENGTH_SHORT).show();
            if(isSuccess)
            {
                Toast.makeText(MainMenuActivity.this , "Login Successfull" , Toast.LENGTH_LONG).show();
                //finish();
            }
        }
        @Override
        protected String doInBackground(String... params)
        {
            String usernam = "hello";
            String passwordd = "world";
            if(usernam.trim().equals("")|| passwordd.trim().equals(""))
                z = "Please enter Username and Password";
            else
            {
                try
                {

                    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                    String ConnectionURL = "jdbc:jtds:sqlserver://" + ip + ":1433/databaseName=" + db ;
                    Log.d("error here 1 : ", "thdhn ");
//                    con = DriverManager.getConnection(ConnectionURL,un,pass);
                    con = connectionclass(un, pass, db, ip);        // Connect to database
                    Log.d("error here 1 : ", "sdhdfn ");
                    if (con == null)
                    {
                        z = "Check Your Internet Access!";
                    }
                    else
                    {
                        Log.d("error here 1 : ", "ffff");
                        // Change below query according to your own database.
                        String query = "select * from login where user_name= '" + usernam.toString() + "' and pass_word = '"+ passwordd.toString() +"'  ";
                        Statement stmt = con.createStatement();
                        ResultSet rs = stmt.executeQuery(query);
                        if(rs.next())
                        {
                            z = "Login successful";
                            isSuccess=true;
                            con.close();
                        }
                        else
                        {
                            z = "Invalid Credentials!";
                            isSuccess = false;
                        }
                    }
                }
                catch (Exception ex)
                {
                    Log.d("error here 1 : ", "eyerf");
                    isSuccess = false;
                    z = ex.getMessage();
                }
            }
            return z;
        }
    }


    @SuppressLint("NewApi")
    public Connection connectionclass(String user, String password, String database, String server)
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String ConnectionURL = null;
        try
        {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            ConnectionURL = "jdbc:jtds:sqlserver://" + server +  ";databaseName=" + database + ";user=" + user+ ";password=" + password + ";";
            connection = DriverManager.getConnection(ConnectionURL);
        }
        catch (SQLException se)
        {
            Log.e("error here 1 : ", se.getMessage());
        }
        catch (ClassNotFoundException e)
        {
            Log.e("error here 2 : ", e.getMessage());
        }
        catch (Exception e)
        {
            Log.e("error here 3 : ", e.getMessage());
        }
        return connection;
    }

}
