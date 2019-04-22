package pt.oposec.vulnapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class LoginFragment extends Fragment {
    boolean DEBUG = false;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
   // private static final String ARG_PARAM1 = "param1";
    //private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    //private String mParam1;
    //private String mParam2;

    public LoginFragment() {
        // Required empty public constructor
    }

    //public static LoginFragment newInstance(String param1, String param2) {
     //   LoginFragment fragment = new LoginFragment();
     //   Bundle args = new Bundle();
    //    args.putString(ARG_PARAM1, param1);
     //   args.putString(ARG_PARAM2, param2);
     //   fragment.setArguments(args);
     //   return fragment;
    //}

    ApiService apiService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  if (getArguments() != null) {
       //     mParam1 = getArguments().getString(ARG_PARAM1);
        //    mParam2 = getArguments().getString(ARG_PARAM2);
        //}

        final String BASE_URL = getString(R.string.API_ENDPOINT);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService =
                retrofit.create(ApiService.class);

    }


    Button ButtonLogin;
    EditText UserTxt;
    EditText PassTxt;
    TextView MessageLogin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButtonLogin = view.findViewById(R.id.LoginButton);
        UserTxt = view.findViewById(R.id.UsernameTxt);
        PassTxt = view.findViewById(R.id.PasswordTxt);
        UserTxt = view.findViewById(R.id.UsernameTxt);
        MessageLogin = view.findViewById(R.id.MessageLogin);

        ButtonLogin.setOnClickListener(new LoginButtonClick());
        ButtonLogin.setOnLongClickListener(new LoginButtonLongClick());
        return view;
    }

    User userDetails = null;

    private void CheckCredentialsOnInternet(String usertxt, String passtxt) {

        User user = new User(usertxt, passtxt);
        Call<User> call = apiService.getUser(user.username, user);
        call.enqueue(new Callback<User>() {

            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                int statusCode = response.code();
                userDetails = response.body();
                if (userDetails != null) {
                    Log.d("loggedIn:", userDetails.username);
                    resetAttempts();
                    Intent i = new Intent(getContext(), UserActivity.class);
                    i.putExtra("username", userDetails.username);
                    i.putExtra("points", userDetails.points);
                    i.putExtra("role", userDetails.role);
                    startActivity(i);
                } else {
                    decreaseAttemptsLeft();
                    MessageLogin.setText("Wrong Password " + getAttemptsLeft() + " attempts left.");
                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("help", "No internet... maybe");
            }
        });


    }

    private void doLogin() {
        Log.d("Debug:LoginFragment", UserTxt.getText() + ":" + PassTxt.getText());

        if (Integer.valueOf(getAttemptsLeft()) <= 0) {
            MessageLogin.setText("Locked");
            return;
        }

        CheckCredentialsOnInternet(UserTxt.getText().toString(), PassTxt.getText().toString());
    }


    private String getAttemptsLeft() {
        if (!fileConfigExists()) {
            writeToFile("5", this.getContext());
        }
        return readFromFile(this.getContext());

    }

    private void decreaseAttemptsLeft() {
        if (!fileConfigExists()) {
            writeToFile("4", this.getContext());
        }
        String numStr = readFromFile(this.getContext());
        int num = Integer.parseInt(numStr);
        num--;
        writeToFile(String.valueOf(num), this.getContext());
    }

    private void resetAttempts() {
        writeToFile("5", this.getContext());
    }

    private boolean fileConfigExists() {
        return (new File(this.getContext().getApplicationInfo().dataDir + "/files/config.txt")).exists();
    }

    private void writeToFile(String data, Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("config.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private String readFromFile(Context context) {
        String ret = "";
        try {
            InputStream inputStream = context.openFileInput("config.txt");

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
        return ret;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private class LoginButtonClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            doLogin();
        }
    }

    private class LoginButtonLongClick implements View.OnLongClickListener {
        @Override
        public boolean onLongClick(View v) {
            String aaa = "I'm a lazy dev, I leave debug features on my app";
            if (DEBUG) {
                resetAttempts();
                String text = "Attempts reset!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(getContext(), text, duration);
                toast.show();
            }
            return true;
        }
    }
}
