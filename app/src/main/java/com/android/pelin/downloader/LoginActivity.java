package com.android.pelin.downloader;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import util.OrmHelper;
import util.User;

public class LoginActivity extends OrmLiteBaseActivity<OrmHelper> {
    public static final String PASSWORD = "14562335";
    public static final String USERNAME = "admin";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        checkLastLogin();
        Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int result =  checkCredentials();
                String info = "";
                switch (result)
                {
                    case -1:
                        info = getResources().getString(R.string.emptyField);
                        break;
                    case 0 :
                        info = getResources().getString(R.string.userNotFound);
                        break;
                    case 1 :
                        info = getResources().getString(R.string.credentialsWrong);
                        break;
                    default:
                        break;
                }
                if(result < 2)  // login failed
                {
                    Context context = getApplicationContext();
                    CharSequence text = info;
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
                else  // login successed
                {
                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        });
    }

    private void checkLastLogin() {

        RuntimeExceptionDao<User, Integer> users = getHelper().getSimpleDataDao();

        QueryBuilder<User, Integer> queryBuilder = users.queryBuilder();
        Where<User, Integer> where = queryBuilder.where();

        Long now  = System.currentTimeMillis();

        try {
            where.eq("name", "admin");
            where.and();
            where.eq("password", 14562335);
            PreparedQuery<User> p = queryBuilder.prepare();
            List<User> list = users.query(p);

            if(list.size()>0)
            {
                User u = list.get(0);

                if(now - u.getDate() < 300000)
                {
                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public int checkCredentials()
    {
        RuntimeExceptionDao<User, Integer> users = getHelper().getSimpleDataDao();

        List<User> list = users.queryForAll();

        if(list.size() < 1){
            Log.i(LoginActivity.class.getName(),"new user");
            long millis = System.currentTimeMillis();
            User simple = new User(millis, "admin", 14562335);
            // store it in the database
            users.create(simple);

            Log.i(OrmHelper.class.getName(), "created new entries in onCreate: " + millis);
        }


        String user = ((EditText) findViewById(R.id.usernameET)).getText().toString();
        String pass = ((EditText) findViewById(R.id.passwordET)).getText().toString();






        if(user.equals("") || pass.equals(""))
        {
            return -1;
        }

        QueryBuilder<User, Integer> queryBuilder = users.queryBuilder();
        Where<User, Integer> where = queryBuilder.where();
        try {
            where.eq("name", user);
            where.and();
            where.eq("password", Integer.parseInt(pass));
            PreparedQuery<User> p = queryBuilder.prepare();
            list = users.query(p);

        } catch (SQLException e) {
            e.printStackTrace();
        }


        /*if( !user.equals(USERNAME))
        {
            return 0;
        }
        if( ! pass.equals(PASSWORD))
        {
            return 1;
        }*/
        if(list.size() <1 )
        {
            return 1;
        }

        else
        {
            queryBuilder = null;
            where = null;

            queryBuilder = users.queryBuilder();
            where = queryBuilder.where();

            try {
                long now = System.currentTimeMillis();

                UpdateBuilder<User, Integer> updateBuilder =  users.updateBuilder();
                updateBuilder.updateColumnValue("date", now);
                updateBuilder.where().eq("name", user);
                updateBuilder.update();


            } catch (SQLException e) {
                e.printStackTrace();
            }




            return 2;
        }
    }

    public void checkLogin(){

    }
}