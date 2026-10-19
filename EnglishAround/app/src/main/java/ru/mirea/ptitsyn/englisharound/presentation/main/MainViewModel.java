package ru.mirea.ptitsyn.englisharound.presentation.main;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

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

public final class MainViewModel extends ViewModel {

    private static final String TAG = "MainViewModel";

    private final ExecutorService worker = Executors.newSingleThreadExecutor();

    private final Executor mainExecutor;

    private final UserRepository userRepository;

    private final GetCurrentUserUseCase getCurrentUser;
    private final LogoutUserUseCase logoutUser;
    private final GetWordCatalogUseCase getCatalog;
    private final GetPersonalDictionaryUseCase getDictionary;
    private final AddWordToDictionaryUseCase addWord;
    private final RemoveWordFromDictionaryUseCase removeWord;

    // Источник № 1: результат получения каталога из NetworkApi.
    private final MutableLiveData<List<Word>> catalogSource =
            new MutableLiveData<>(Collections.emptyList());

    // Источник № 2: результат чтения личного словаря из Room.
    private final MutableLiveData<List<Word>> dictionarySource =
            new MutableLiveData<>(Collections.emptyList());

    // Общее состояние экрана, зависящее от обоих источников.
    private final MediatorLiveData<MainUiState> state = new MediatorLiveData<>();

    private User currentUser;
    private boolean loading;
    private boolean signedOut;
    private String error = "";

    private volatile boolean cleared;

    public MainViewModel(
            AuthRepository authRepository,
            UserRepository userRepository,
            WordRepository wordRepository,
            UserDictionaryRepository dictionaryRepository,
            Executor mainExecutor
    ) {
        this.mainExecutor = mainExecutor;
        this.userRepository = userRepository;

        getCurrentUser = new GetCurrentUserUseCase(authRepository);
        logoutUser = new LogoutUserUseCase(authRepository);

        getCatalog = new GetWordCatalogUseCase(wordRepository);

        getDictionary = new GetPersonalDictionaryUseCase(dictionaryRepository);

        addWord = new AddWordToDictionaryUseCase(dictionaryRepository);

        removeWord = new RemoveWordFromDictionaryUseCase(dictionaryRepository);

        state.addSource(catalogSource, words -> publishState());
        state.addSource(dictionarySource, words -> publishState());

        Log.d(TAG, "MainViewModel created");

        refresh();
    }

    public LiveData<MainUiState> getState() {
        return state;
    }

    public void refresh() {
        if (loading || signedOut || cleared) {
            return;
        }

        submit(() -> {
                    User authenticatedUser = getCurrentUser.execute();

                    if (authenticatedUser == null) {
                        userRepository.clearUser();

                        return new LoadedData(
                                null,
                                Collections.emptyList(),
                                Collections.emptyList()
                        );
                    }

                    // SharedPreferences: сохраняем и читаем профиль.
                    userRepository.saveUser(authenticatedUser);
                    User localUser = userRepository.getUser();

                    if (localUser == null) {
                        throw new IllegalStateException(
                                "Локальный профиль отсутствует"
                        );
                    }

                    // Сетевая заглушка.
                    List<Word> catalog = getCatalog.execute();

                    // Room.
                    List<Word> dictionary = getDictionary.execute(
                            localUser.getId()
                    );

                    return new LoadedData(
                            localUser,
                            catalog,
                            dictionary
                    );
                },
                result -> {
                    currentUser = result.user;
                    signedOut = result.user == null;

                    catalogSource.setValue(result.catalog);
                    dictionarySource.setValue(result.dictionary);
                },
                "Не удалось загрузить данные"
        );
    }

    public void addToDictionary(Word word) {
        if (word == null || !canChangeDictionary()) {
            return;
        }

        String userId = currentUser.getId();

        submit(
                () -> {
                    addWord.execute(userId, word);
                    return getDictionary.execute(userId);
                },
                words -> dictionarySource.setValue(words),
                "Не удалось добавить слово"
        );
    }

    public void removeFromDictionary(Word word) {
        if (word == null || !canChangeDictionary()) {
            return;
        }

        String userId = currentUser.getId();

        submit(
                () -> {
                    removeWord.execute(userId, word.getId());
                    return getDictionary.execute(userId);
                },
                words -> dictionarySource.setValue(words),
                "Не удалось удалить слово"
        );
    }

    public void logout() {
        if (loading || signedOut || cleared) {
            return;
        }

        submit(
                () -> {
                    logoutUser.execute();
                    userRepository.clearUser();
                    return Boolean.TRUE;
                },
                result -> {
                    currentUser = null;
                    signedOut = true;

                    catalogSource.setValue(Collections.emptyList());
                    dictionarySource.setValue(Collections.emptyList());
                },
                "Не удалось выйти из аккаунта"
        );
    }

    private boolean canChangeDictionary() {
        return !loading
                && !signedOut
                && !cleared
                && currentUser != null;
    }

    private <T> void submit(
            Callable<T> task,
            Consumer<T> onSuccess,
            String errorMessage
    ) {
        loading = true;
        error = "";
        publishState();

        worker.execute(() -> {
            try {
                T result = task.call();

                mainExecutor.execute(() -> {
                    if (cleared) {
                        return;
                    }

                    onSuccess.accept(result);

                    loading = false;
                    error = "";
                    publishState();
                });

            } catch (Exception exception) {
                Log.e(TAG, errorMessage, exception);

                mainExecutor.execute(() -> {
                    if (cleared) {
                        return;
                    }

                    loading = false;
                    error = errorMessage;
                    publishState();
                });
            }
        });
    }

    private void publishState() {
        if (cleared) {
            return;
        }

        List<Word> catalog = catalogSource.getValue();
        List<Word> dictionary = dictionarySource.getValue();

        state.setValue(
                new MainUiState(
                        currentUser,
                        catalog == null
                                ? Collections.emptyList()
                                : catalog,
                        dictionary == null
                                ? Collections.emptyList()
                                : dictionary,
                        loading,
                        signedOut,
                        error
                )
        );
    }

    @Override
    protected void onCleared() {
        cleared = true;
        worker.shutdown();

        Log.d(TAG, "MainViewModel cleared");

        super.onCleared();
    }

    private static final class LoadedData {
        final User user;
        final List<Word> catalog;
        final List<Word> dictionary;

        LoadedData(
                User user,
                List<Word> catalog,
                List<Word> dictionary
        ) {
            this.user = user;
            this.catalog = catalog;
            this.dictionary = dictionary;
        }
    }
}