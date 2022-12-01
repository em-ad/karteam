package space.kheyrati.nanowatch;

public interface ApiCallback {
    void apiFailed(Object o);

    void apiSucceeded(Object o);
}