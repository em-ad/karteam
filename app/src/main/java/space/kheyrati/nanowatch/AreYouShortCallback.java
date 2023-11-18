package space.kheyrati.nanowatch;

interface AreYouShortCallback {
    void accept();

    void reject();

    default void sendMessage(String text){}

    void dismiss();
}
