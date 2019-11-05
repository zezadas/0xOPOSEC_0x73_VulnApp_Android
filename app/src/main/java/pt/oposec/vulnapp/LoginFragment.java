package pt.oposec.vulnapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Pair;
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
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class LoginFragment extends Fragment {
    boolean DEBUG = false;
    private Context context;
    private final static String SECRET = "pwned";

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

        this.context = this.getContext();

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(600, TimeUnit.SECONDS)
                .connectTimeout(600, TimeUnit.SECONDS)
                .build();

        final String BASE_URL = getString(R.string.API_ENDPOINT);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
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

        Pair<String, String> user = getDefaultUser();
        UserTxt.setText(user.first);
        PassTxt.setText(user.second);
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
                    Intent i = new Intent(context, UserActivity.class);
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

    private static String xor(String word, String secret_str) {
        byte[] secret = secret_str.getBytes();
        byte[] input = word.getBytes();
        final byte[] output = new byte[input.length];
        if (secret.length == 0) {
            throw new IllegalArgumentException("empty security key");
        }
        int spos = 0;
        for (int pos = 0; pos < input.length; ++pos) {
            output[pos] = (byte) (input[pos] ^ secret[spos]);
            ++spos;
            if (spos >= secret.length) {
                spos = 0;
            }
        }
        return new String(output);
    }

    private Pair<String, String> getDefaultUser() {
        String filePath = "user.txt";
        if (!fileExists(filePath)) {
            String username = "john";
            //String password = "123456";
            //String password_encoded = xor(password, SECRET);
            String password_encoded ="AE]QQF";
            writeToFile(username + ":" + password_encoded, filePath);
        }
        String aux = readFromFile(filePath);
        String[] a = aux.split(":");
        String username = a[0];
        String password = a[1];
        String password_encoded = xor(password, SECRET);

        return new Pair(username, password_encoded);
    }

    private void saveUser(String user, String password) {
        String filePath = "user.txt";
        String password_encoded = xor(password, SECRET);
        writeToFile(user + ":" + password_encoded, filePath);
    }

    private String getAttemptsLeft() {
        String filePath = "config.txt";
        if (!fileExists(filePath)) {
            writeToFile("5", filePath);
        }
        return readFromFile(filePath);
    }

    private void decreaseAttemptsLeft() {
        String filePath = "config.txt";
        if (!fileExists(filePath)) {
            writeToFile("5", filePath);
        }
        String numStr = readFromFile(filePath);
        int num = Integer.parseInt(numStr);
        num--;
        writeToFile(String.valueOf(num), filePath);
    }

    private void resetAttempts() {
        String filePath = "config.txt";
        writeToFile("5", filePath);
    }

    private boolean fileExists(String filePath) {
        return (new File(context.getApplicationInfo().dataDir + "/files/" + filePath)).exists();
    }

    private void writeToFile(String data, String filePath) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(filePath, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private String readFromFile(String filePath) {
        String ret = "";
        try {
            InputStream inputStream = context.openFileInput(filePath);

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }
                bufferedReader.close();
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
            String username = UserTxt.getText().toString();
            String password = PassTxt.getText().toString();
            saveUser(username, password);
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

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
            return true;
        }
    }
}
