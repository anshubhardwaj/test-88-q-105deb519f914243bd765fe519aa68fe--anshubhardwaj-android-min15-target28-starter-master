package org.codejudge.android.apicall;

import org.codejudge.android.apicall.restbeandata.BaseRestoResponseModel;

public interface RestoInterface {
    void onRestolistresponsedata(BaseRestoResponseModel getrestolist);
    void onRestolistresponseError(String error);
}
