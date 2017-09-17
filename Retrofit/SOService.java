
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Anup Dey on 16-Sep-17.
 */

public interface SOService {

    @GET("/answers?order=desc&sort=activity&site=stackoverflow")
    Call<SOAnswersResponse> getAnswers();

    @GET("/answers?order=desc&sort=activity&site=stackoverflow")
    Call<SOAnswersResponse> getAnswer(@Query("tagged") String tags);

    @FormUrlEncoded
    @POST("photos.php")
    Call<ImageModel> uploadImage(@Field("encoded_string") String image, @Field("image_name") String title);



}
