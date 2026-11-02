package ru.mirea.ptitsyn.data.network;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;

import retrofit2.Response;

import ru.mirea.ptitsyn.domain.repository.RepositoryException;

public final class NetworkApi {
    private final DictionaryApiService service;

    public NetworkApi() {
        service = ApiClient.createDictionaryService();
    }

    public List<DictionaryEntryDto> getWord(String word) {
        try {
            Response<List<DictionaryEntryDto>> response = service.getWord(word).execute();

            if (!response.isSuccessful()) {
                if (response.errorBody() != null) {
                    response.errorBody().close();
                }

                throw new RepositoryException(getHttpError(response.code()));
            }

            List<DictionaryEntryDto> entries = response.body();

            if (entries == null || entries.isEmpty()) {
                throw new RepositoryException("Словарь вернул пустой ответ");
            }

            return entries;

        } catch (SocketTimeoutException exception) {
            throw new RepositoryException("Словарь долго не отвечает. Попробуйте ещё раз.", exception);

        } catch (UnknownHostException exception) {
            throw new RepositoryException("Не удалось найти сервер словаря. Проверьте интернет.", exception);

        } catch (IOException exception) {
            throw new RepositoryException("Не удалось загрузить статью. Проверьте подключение к интернету.", exception);

        } catch (RepositoryException exception) {
            throw exception;

        } catch (RuntimeException exception) {
            throw new RepositoryException("Словарь вернул данные в неожиданном формате", exception);
        }
    }

    private String getHttpError(int code) {
        if (code == 404) {
            return "Это слово не найдено в словаре";
        }

        if (code == 429) {
            return "Слишком много запросов. Попробуйте позже.";
        }

        if (code == 401 || code == 403) {
            return "Сервер словаря запретил доступ";
        }

        if (code >= 500) {
            return "Сервер словаря временно недоступен";
        }

        return "Не удалось получить словарную статью. HTTP " + code;
    }
}