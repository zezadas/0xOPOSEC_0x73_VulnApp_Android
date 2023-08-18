package pt.oposec.vulnapp.helpers;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import pt.oposec.vulnapp.R;
import pt.oposec.vulnapp.activities.UserActivity;
import pt.oposec.vulnapp.api.Endpoints;
import pt.oposec.vulnapp.models.User;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login {
    private final Context context;
    private final Endpoints endpoints;
    private final String APP_TAG;
    private final SaveData saveData;
    public Login(Context context, String app_tag, SaveData saveData){
        this.context=context;
        this.APP_TAG = app_tag;
        this.saveData=saveData;
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(600, TimeUnit.SECONDS)
                .connectTimeout(600, TimeUnit.SECONDS)
                .build();

        final String BASE_URL = this.context.getString(R.string.API_ENDPOINT);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        endpoints = retrofit.create(Endpoints.class);
    }
    public boolean CheckCredentialsOnInternet(User userCreds, TextView messageLogin) {
        Log.d(APP_TAG, "Validating creds: "+userCreds.username + ":" + userCreds.password);
        Call<User> call = endpoints.getUser(userCreds.username, userCreds);
        call.enqueue(new CustomCallback(){
            @Override
            public void onLoginSuccess(User user) {
                saveData.resetAttempts();
                saveData.saveUser(userCreds.username, userCreds.password);
                messageLogin.setText("Last User Login: " + user.username);
                Log.d(APP_TAG, "LoggedIn user: " + user.username);
                Log.d(APP_TAG, "Stating User Activity");
                startUserAtivity(user);
            }
            @Override
            public void onLoginFailure(String throwMsg){
                saveData.decreaseAttemptsLeft();
                messageLogin.setText("Wrong Password " + saveData.readAttempts() + " attempts left.");
            }
            @Override
            public void onApiError(int errorCode){
                String errorMsg="Error communicating with api, response code: " + errorCode;
                Log.d(app_tag,errorMsg);
                Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNetworkFailure(Throwable throwMsg){
                Log.d(app_tag, throwMsg.getStackTrace().toString());
                Toast.makeText(context, "No internet... maybe", Toast.LENGTH_SHORT).show();
            }
        });
        return false;
    }

    private void startUserAtivity(User user) {
        Intent i = new Intent(context, UserActivity.class);
        i.putExtra("username", user.username);
        i.putExtra("points", user.points);
        i.putExtra("role", user.role);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}
