package org.codejudge.android;



import org.codejudge.android.apicall.restbeandata.BaseRestoResponseModel;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface ApicallInterface {

    @GET("api/place/nearbysearch/json")
    Observable<BaseRestoResponseModel> getrestolist(@Query("location") String location,
                                                    @Query("radius") Integer radius,
                                                    @Query("type") String type,@Query("key") String key);
}
