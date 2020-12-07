package org.codejudge.android.apicall;

import android.content.Context;

import org.codejudge.android.ApicallInterface;
import org.codejudge.android.apicall.restbeandata.BaseRestoResponseModel;
import org.codejudge.android.utils.RetrofitClient;
import org.codejudge.android.utils.ServiceConstant;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ApiCallRestoList {

    private RestoInterface mRestoInterface;
    //    private Call<HoroscopeSummaryResponse> call;
    private Observable<BaseRestoResponseModel> call;
    Context context;
    String languageid;

    public ApiCallRestoList( RestoInterface mRestoInterface, Context context) {
        this.mRestoInterface = mRestoInterface;
        this.context = context;
    }

    public void getRestListNames(String location,Integer radius,String type,String key) {

        ApicallInterface apicallInterface = RetrofitClient.getClient().create(ApicallInterface.class);
        call = apicallInterface.getrestolist( location,radius,type,key);
        call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseRestoResponseModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(BaseRestoResponseModel response) {
                        if (response!=null) {
                            if (response.getResults() != null) {
                                mRestoInterface.onRestolistresponsedata(response);
                            }else {
                                mRestoInterface.onRestolistresponseError("Something went wrong \nplease try after some time.");
                            }
                        }
                        else{
                            mRestoInterface.onRestolistresponseError("Something went wrong \nplease try after some time.");
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof SocketTimeoutException) {
                            mRestoInterface.onRestolistresponseError("Internet connection is slow please try again.");
                        } else if (e instanceof UnknownHostException) {
                            mRestoInterface.onRestolistresponseError("Internet connection is slow please try again.");
                        } else if (e instanceof SocketException) {
                            mRestoInterface.onRestolistresponseError("Internet connection is slow please try again.");
                        } else {
                            mRestoInterface.onRestolistresponseError("Internet connection is slow please try again.");
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
