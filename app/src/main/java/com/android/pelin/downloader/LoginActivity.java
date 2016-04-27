package com.android.pelin.downloader;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
public class LoginActivity extends AppCompatActivity {
    public static final String PASSWORD = "1";
    public static final String USERNAME = "Name";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
                }

            }
        });
    }
    public int checkCredentials()
    {
        String user = ((EditText) findViewById(R.id.usernameET)).getText().toString();
        String pass = ((EditText) findViewById(R.id.passwordET)).getText().toString();
        if(user.equals("") || pass.equals(""))
        {
            return -1;
        }
        if( !user.equals(USERNAME))
        {
            return 0;
        }
        if( ! pass.equals(PASSWORD))
        {
            return 1;
        }
        else
        {
            return 2;
        }
    }
}