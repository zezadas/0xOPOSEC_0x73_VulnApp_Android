package pt.oposec.vulnapp.helpers;

import pt.oposec.vulnapp.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class CustomCallback<T> implements Callback {
        String app_tag=this.getClass().getName().toString();
        public CustomCallback() {
        }

        @Override
        public void onResponse(Call call, Response response) {
            if(response.isSuccessful()){
                onLoginSuccess((User) response.body());
            }else{
                if (response.code()==401){
                    onLoginFailure("Login Failed");
                }else{
                    onApiError(response.code());
                }
            }
        }

        @Override
        public void onFailure(Call call, Throwable t) {
            call.cancel();
            onNetworkFailure(t);
        }

        public abstract void onLoginSuccess(User response);
        public abstract void onLoginFailure(String throwMsg);
        public abstract void onApiError(int errorCode);
        public abstract void onNetworkFailure(Throwable throwMsg);
    }
