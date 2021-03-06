package pt.oposec.vulnapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class UserActivity extends AppCompatActivity {

    User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //fab.setOnClickListener(new View.OnClickListener() {
        //    @Override
         //   public void onClick(View view) {
          //      Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
          //              .setAction("Action", null).show();
          //  }
        //});

        String username;
        String points;
        String role;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            username = bundle.getString("username");
            points = bundle.getString("points");
            role = bundle.getString("role");
            user = new User(username, points, role);
        }

        TextView userTxt = findViewById(R.id.textView6);
        userTxt.setText(user.username);
        TextView coinTxt = findViewById(R.id.textView5);
        coinTxt.setText(user.points);

        Button checkMoney = findViewById(R.id.checkMoneyButton);
        checkMoney.setOnClickListener(new checkMoneyButtonClick());

        Button adminStuff = findViewById(R.id.adminStuffMoney);
        if (user.role.compareToIgnoreCase(getString(R.string.adminRole)) == 0) {
            adminStuff.setEnabled(true);
            adminStuff.setOnClickListener(new adminStuffButtonClick());
        }

    }

    private class adminStuffButtonClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String text = getString(R.string.AdminMessage);
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(getApplicationContext(), text, duration);
            toast.show();
        }
    }

    private class checkMoneyButtonClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int duration = Toast.LENGTH_SHORT;
            String text;
            if (user.points.equalsIgnoreCase(getString(R.string.one_three_three_seven))) {
                text = getString(R.string.Leet);
            } else {
                text = user.points;
            }
            Toast toast = Toast.makeText(getApplicationContext(), text, duration);
            toast.show();
        }
    }

}
