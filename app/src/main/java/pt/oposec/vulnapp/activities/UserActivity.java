package pt.oposec.vulnapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import pt.oposec.vulnapp.R;
import pt.oposec.vulnapp.models.User;

public class UserActivity extends AppCompatActivity {
    User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String username = bundle.getString("username");
            String points = bundle.getString("points");
            String role = bundle.getString("role");
            user = new User(username,points,role);
        }

        setContentView(R.layout.activity_user);
        ((TextView)findViewById(R.id.userTxt)).setText(user.username);
        ((TextView)findViewById(R.id.coinTxt)).setText(user.points);
        findViewById(R.id.checkMoneyBtn).setOnClickListener(new checkMoneyButtonClick());
        findViewById(R.id.logoutBtn).setOnClickListener(new logout());
        findViewById(R.id.adminStuffBtn).setOnClickListener(new adminStuffButtonClick());
        findViewById(R.id.editProfile).setOnClickListener(new editProfileButtonClick());
        findViewById(R.id.doOperations).setOnClickListener(new doOperationsButtonClick());
        findViewById(R.id.adminStuffBtn).setEnabled(user.isAdmin());

    }

    private class logout implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            finish();
        }
    }

    private class adminStuffButtonClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String text = getString(R.string.AdminMessage);
            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
        }
    }

    private class checkMoneyButtonClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            String points = user.points;
            if (user.isLeet()){
                points=getString(R.string.Leet);
            }
            Toast.makeText(getApplicationContext(), points, Toast.LENGTH_SHORT).show();
        }
    }

    private class editProfileButtonClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Toast.makeText(getApplicationContext(), "Sorry. Not implemented", Toast.LENGTH_SHORT).show();
        }
    }

    private class doOperationsButtonClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Toast.makeText(getApplicationContext(), "Sorry. Not implemented", Toast.LENGTH_SHORT).show();
        }
    }

}
