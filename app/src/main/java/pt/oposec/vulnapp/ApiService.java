package pt.oposec.vulnapp;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
  //  @POST("oposec/{username}")
//    Call<User> getUser(@Path("username") String username, Callback<User> cb);

    @POST("{username}")
    Call<User> getUser(@Path("username") String username, @Body User user);
}
