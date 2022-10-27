package space.kheyrati.nanowatch;

public interface RequestCallback {
    void onAccept(RequestResponseModel model);
    void onReject(RequestResponseModel model);
}
