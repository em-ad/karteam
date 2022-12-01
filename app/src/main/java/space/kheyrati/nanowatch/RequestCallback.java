package space.kheyrati.nanowatch;

import space.kheyrati.nanowatch.model.RequestResponseModel;

public interface RequestCallback {
    void onAccept(RequestResponseModel model);

    void onReject(RequestResponseModel model);
}
