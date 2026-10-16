package ru.mirea.ptitsyn.data.network;

public final class NetworkApi {

    public String getWordsJson() {
        // Имитация JSON-ответа внешнего сервиса.
        return "["
                + "{\"id\":1,\"english\":\"apple\",\"translation\":\"яблоко\"},"
                + "{\"id\":2,\"english\":\"book\",\"translation\":\"книга\"},"
                + "{\"id\":3,\"english\":\"cup\",\"translation\":\"чашка\"},"
                + "{\"id\":4,\"english\":\"chair\",\"translation\":\"стул\"},"
                + "{\"id\":5,\"english\":\"window\",\"translation\":\"окно\"}"
                + "]";
    }
}