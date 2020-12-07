package org.codejudge.android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.codejudge.android.apicall.ApiCallRestoList;
import org.codejudge.android.apicall.RestoInterface;
import org.codejudge.android.apicall.restbeandata.BaseRestoResponseModel;
import org.codejudge.android.apicall.restbeandata.Result;
import org.codejudge.android.helper.ConfigHelper;
import org.codejudge.android.utils.CommenUtil;
import org.codejudge.android.utils.ServiceConstant;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, RestoInterface {

  @BindView(R.id.recycler_view)
  RecyclerView recycler_view;
  @BindView(R.id.evSearchAstro)
  EditText mSearchEdittext;
  private ApiCallRestoList mApiCallRestoList;
  private RestListAdapter mAdapter;
  private LinearLayoutManager mLayoutManager;
  private ArrayList<Result> NewData;
  private int lastVisibleItem, totalItemCount;
  int pageNumber = 1;
  private boolean onLoadMore = false;
  private int visibleThreshold = 10;
  private int Total = 0;
  private final long DELAY = 1500;
  int pageSize = 10;
  private Timer timer = new Timer();

  public MainActivity() {
    super();
  }

  @Override
  public void onDestroy() {
    //Log.e("THe Distroy", "BLog Fragment");
    //Log.e("THE BLog","Destoy call");
    super.onDestroy();
    NewData = null;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    mApiCallRestoList = new ApiCallRestoList(this, this);
    mApiCallRestoList.getRestListNames("47.6204-122.3491", 2500, "restaurant", ServiceConstant.key);
    NewData = new ArrayList<>();
    mSearchEdittext.setOnKeyListener(new View.OnKeyListener() {
      public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
          switch (keyCode) {
            case KeyEvent.KEYCODE_ENTER: {
              if (!mSearchEdittext.getText().toString().isEmpty() && mSearchEdittext.toString().length() >= 3) {
                //
//stuff that updates ui

                if (timer != null) {
                  timer.cancel();
                }

                if (NewData.size()!=0) {
                  runOnUiThread(() ->
                          mApiCallRestoList.getRestListNames("47.6204-122.3491",2500,"restaurant", ServiceConstant.key));

                }else{
                  getAutocomplete(mSearchEdittext.getText());
                }
              }
              break;
            }
            case EditorInfo.IME_ACTION_DONE: {
              if (!mSearchEdittext.getText().toString().isEmpty()) {
                //
//stuff that updates ui
                if (timer != null) {
                  timer.cancel();
                }

                if (NewData.size()!=0) {
                  runOnUiThread(() ->
                          mApiCallRestoList.getRestListNames("47.6204-122.3491",2500,"restaurant", ServiceConstant.key));

                }else{
                  getAutocomplete(mSearchEdittext.getText());
                }
              }
              break;
            }
            default:
              break;
          }
        }
        return false;
      }
    });

    mSearchEdittext.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (timer != null)
          timer.cancel();


      }

      @Override
      public void afterTextChanged(Editable s) {
        if (!s.toString().isEmpty() && s.toString().length() >= 3) {

//                                        Log.e(" onTextChanged", "onTextChanged Size=" + s.toString().length());
          if (CommenUtil.networkConnectionCheck) {

            timer = new Timer();
            timer.schedule(new TimerTask() {
              @Override
              public void run() {
                if (NewData.size() != 0) {
                  runOnUiThread(() ->
                          mApiCallRestoList.getRestListNames("47.6204-122.3491", 2500, "restaurant", ServiceConstant.key)
                  );

                } else {
                  getAutocomplete(mSearchEdittext.getText());
                }
              }

            }, DELAY);

          }
        }

      }
    });
  }

  private ArrayList<Result> getAutocomplete(CharSequence constraint) {
    NewData.clear();
    if (CommenUtil.networkConnectionCheck) {
      NewData.clear();
      mApiCallRestoList.getRestListNames("47.6204-122.3491", 2500, "restaurant", ServiceConstant.key);

    } else {
      Toast.makeText(this, "NoInternetConnection", Toast.LENGTH_SHORT).show();
    }
    return NewData;
  }

  @Override
  public void onClick(View v) {
//    Access api_url from config.properties

  }

  @Override
  public void onRestolistresponsedata(BaseRestoResponseModel getrestolist) {
    if (getrestolist.getResults() != null && getrestolist.getResults().size() != 0) {
      NewData.addAll(getrestolist.getResults());
      mAdapter = new RestListAdapter(this, NewData, this); // purchaseItemAdapter
      mLayoutManager = new LinearLayoutManager(this);
      recycler_view.setLayoutManager(mLayoutManager);
      recycler_view.setItemAnimator(new DefaultItemAnimator());
      recycler_view.setAdapter(mAdapter);


      recycler_view
              .addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView,
                                       int dx, int dy) {
                  // Log.e("THe Scroll start","The Scroll End");
                  super.onScrolled(recyclerView, dx, dy);
                  totalItemCount = mLayoutManager.getItemCount();
                  lastVisibleItem = mLayoutManager
                          .findLastVisibleItemPosition();
                  //Log.e("totalItemCount", " " + totalItemCount);
                  //Log.e("lastVisibleItem", " " + lastVisibleItem);
                  //Log.e("visibleThreshold", " " + visibleThreshold);
                  if ((pageNumber != 1 && !onLoadMore) && (totalItemCount <= (lastVisibleItem + visibleThreshold) && lastVisibleItem == totalItemCount - 1)) {


                    //  Log.e("THe Scroll end","The Scroll End");
                    onLoadMore = true;
                    if (Total > 0) {
                      loaddata();


                    }
                                /*if (onLoadMoreListener != null) {
                                    onLoadMoreListener.onLoadMore();
                                }*/
                    // loading = true;
                  }
                }
              });

    }

  }

  public void loaddata() {

    mApiCallRestoList = new ApiCallRestoList(this, this);
    mApiCallRestoList.getRestListNames("47.6204-122.3491", 2500, "restaurant", ServiceConstant.key);

  }

  @Override
  public void onRestolistresponseError(String error) {

  }
}

