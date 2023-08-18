package pt.oposec.vulnapp.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import pt.oposec.vulnapp.R;
import pt.oposec.vulnapp.helpers.Login;
import pt.oposec.vulnapp.helpers.SaveData;
import pt.oposec.vulnapp.models.User;

public class MainActivity extends AppCompatActivity {
    boolean DEBUG = false;
    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }
    String APP_TAG=this.getClass().toString();
    private Context context;
    private Button ButtonLogin;
    private EditText UserTxt;
    private EditText PassTxt;
    private TextView MessageLogin;
    private SaveData saveData;
    private Login login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_login);
        View view = findViewById(R.id.fragment_login);
        ButtonLogin = view.findViewById(R.id.LoginBtn);
        UserTxt = view.findViewById(R.id.UsernameTxt);
        PassTxt = view.findViewById(R.id.PasswordTxt);
        UserTxt = view.findViewById(R.id.UsernameTxt);
        MessageLogin = view.findViewById(R.id.loginMsgTxt);
        ButtonLogin.setOnClickListener(new LoginButtonClick());
        ButtonLogin.setOnLongClickListener(new LoginButtonLongClick());

        stringFromJNI();

        this.context = this.getBaseContext();
        this.saveData = new SaveData(this.context,APP_TAG);
        this.login= new Login(context,APP_TAG,saveData);

        //set Default Values
        User user = saveData.loadUser();
        UserTxt.setText(user.username);
        PassTxt.setText(user.password);
    }
    private class LoginButtonClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (Integer.valueOf(saveData.readAttempts()) <= 0) {
                MessageLogin.setText("Locked");
                return;
            }
            User userCreds = new User(UserTxt.getText().toString(),PassTxt.getText().toString());
            login.CheckCredentialsOnInternet(userCreds,MessageLogin);
        }
    }

    private class LoginButtonLongClick implements View.OnLongClickListener {
        @Override
        public boolean onLongClick(View v) {
            String aaa = "I'm a lazy dev, I leave debug features on my app";
            if (DEBUG) {
                saveData.resetAttempts();
                MessageLogin.setText("Attempts Left " + saveData.readAttempts());
                Toast.makeText(context, "Attempts reset!", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}
