package ru.mirea.ptitsyn.englisharound.presentation.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ru.mirea.ptitsyn.data.database.AppDatabase;
import ru.mirea.ptitsyn.data.network.NetworkApi;
import ru.mirea.ptitsyn.data.repository.AuthRepositoryImpl;
import ru.mirea.ptitsyn.data.repository.UserDictionaryRepositoryImpl;
import ru.mirea.ptitsyn.data.repository.UserRepositoryImpl;
import ru.mirea.ptitsyn.data.repository.WordRepositoryImpl;
import ru.mirea.ptitsyn.data.storage.UserPreferences;

import ru.mirea.ptitsyn.domain.models.User;
import ru.mirea.ptitsyn.domain.models.Word;
import ru.mirea.ptitsyn.domain.repository.AuthRepository;
import ru.mirea.ptitsyn.domain.repository.UserDictionaryRepository;
import ru.mirea.ptitsyn.domain.repository.UserRepository;
import ru.mirea.ptitsyn.domain.repository.WordRepository;

import ru.mirea.ptitsyn.domain.usecases.auth.GetCurrentUserUseCase;
import ru.mirea.ptitsyn.domain.usecases.auth.LogoutUserUseCase;
import ru.mirea.ptitsyn.domain.usecases.catalog.GetWordCatalogUseCase;
import ru.mirea.ptitsyn.domain.usecases.dict.AddWordToDictionaryUseCase;
import ru.mirea.ptitsyn.domain.usecases.dict.GetPersonalDictionaryUseCase;
import ru.mirea.ptitsyn.domain.usecases.dict.RemoveWordFromDictionaryUseCase;

public final class MainViewModel extends AndroidViewModel {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private final MutableLiveData<MainUiState> state =
            new MutableLiveData<>(
                    new MainUiState(
                            null,
                            Collections.emptyList(),
                            Collections.emptyList(),
                            true,
                            false,
                            ""
                    )
            );

    private final UserRepository userRepository;

    private final GetCurrentUserUseCase getCurrentUser;
    private final LogoutUserUseCase logoutUser;

    private final GetWordCatalogUseCase getCatalog;
    private final GetPersonalDictionaryUseCase getDictionary;
    private final AddWordToDictionaryUseCase addWord;
    private final RemoveWordFromDictionaryUseCase removeWord;

    private User currentUser;
    private List<Word> catalog = new ArrayList<>();
    private List<Word> dictionary = new ArrayList<>();
    private boolean signedOut;

    public MainViewModel(@NonNull Application application) {
        super(application);

        AuthRepository authRepository = new AuthRepositoryImpl();

        userRepository = new UserRepositoryImpl(
                new UserPreferences(application)
        );

        WordRepository wordRepository = new WordRepositoryImpl(
                new NetworkApi()
        );

        UserDictionaryRepository dictionaryRepository =
                new UserDictionaryRepositoryImpl(
                        AppDatabase.getInstance(application)
                                .dictionaryWordDao()
                );

        getCurrentUser = new GetCurrentUserUseCase(authRepository);
        logoutUser = new LogoutUserUseCase(authRepository);

        getCatalog = new GetWordCatalogUseCase(wordRepository);

        getDictionary = new GetPersonalDictionaryUseCase(
                dictionaryRepository
        );

        addWord = new AddWordToDictionaryUseCase(
                dictionaryRepository
        );

        removeWord = new RemoveWordFromDictionaryUseCase(
                dictionaryRepository
        );

        refresh();
    }

    public LiveData<MainUiState> getState() {
        return state;
    }

    public void refresh() {
        executor.execute(() -> {
            if (signedOut) {
                return;
            }

            publish(true, "");

            try {
                // Проверяем настоящую сессию Firebase.
                User authenticatedUser = getCurrentUser.execute();

                if (authenticatedUser == null) {
                    userRepository.clearUser();

                    currentUser = null;
                    catalog = new ArrayList<>();
                    dictionary = new ArrayList<>();
                    signedOut = true;

                    publish(false, "");
                    return;
                }

                // Записываем профиль в SharedPreferences.
                userRepository.saveUser(authenticatedUser);

                // Читаем профиль из SharedPreferences.
                currentUser = userRepository.getUser();

                if (currentUser == null) {
                    throw new IllegalStateException(
                            "Не удалось получить локальный профиль"
                    );
                }

                // Каталог приходит из NetworkApi.
                catalog = getCatalog.execute();

                // Личный словарь читается из Room.
                dictionary = getDictionary.execute(
                        currentUser.getId()
                );

                publish(false, "");

            } catch (Exception exception) {
                publish(false, "Не удалось загрузить данные");
            }
        });
    }

    public void addToDictionary(Word word) {
        if (word == null) {
            return;
        }

        executor.execute(() -> {
            if (signedOut || currentUser == null) {
                return;
            }

            publish(true, "");

            try {
                addWord.execute(currentUser.getId(), word);

                dictionary = getDictionary.execute(
                        currentUser.getId()
                );

                publish(false, "");

            } catch (Exception exception) {
                publish(false, "Не удалось добавить слово");
            }
        });
    }

    public void removeFromDictionary(Word word) {
        if (word == null) {
            return;
        }

        executor.execute(() -> {
            if (signedOut || currentUser == null) {
                return;
            }

            publish(true, "");

            try {
                removeWord.execute(
                        currentUser.getId(),
                        word.getId()
                );

                dictionary = getDictionary.execute(
                        currentUser.getId()
                );

                publish(false, "");

            } catch (Exception exception) {
                publish(false, "Не удалось удалить слово");
            }
        });
    }

    public void logout() {
        executor.execute(() -> {
            if (signedOut) {
                return;
            }

            publish(true, "");

            try {
                logoutUser.execute();
                userRepository.clearUser();

                currentUser = null;
                catalog = new ArrayList<>();
                dictionary = new ArrayList<>();
                signedOut = true;

                publish(false, "");

            } catch (Exception exception) {
                publish(false, "Не удалось выйти из аккаунта");
            }
        });
    }

    private void publish(boolean loading, String error) {
        state.postValue(
                new MainUiState(
                        currentUser,
                        catalog,
                        dictionary,
                        loading,
                        signedOut,
                        error
                )
        );
    }

    @Override
    protected void onCleared() {
        executor.shutdown();
        super.onCleared();
    }
}