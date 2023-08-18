package pt.oposec.vulnapp.api;

import pt.oposec.vulnapp.models.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface Endpoints {
    @POST("{username}")
    Call<User> getUser(@Path("username") String username, @Body User user);
}
