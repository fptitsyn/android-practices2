package ru.mirea.ptitsyn.englisharound.presentation;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import ru.mirea.ptitsyn.englisharound.R;
import ru.mirea.ptitsyn.englisharound.presentation.auth.AuthUi;
import ru.mirea.ptitsyn.englisharound.presentation.fragments.CatalogFragment;
import ru.mirea.ptitsyn.englisharound.presentation.fragments.PersonalDictionaryFragment;
import ru.mirea.ptitsyn.englisharound.presentation.fragments.ProfileFragment;
import ru.mirea.ptitsyn.englisharound.presentation.fragments.WordDetailsFragment;
import ru.mirea.ptitsyn.englisharound.presentation.main.MainViewModel;
import ru.mirea.ptitsyn.englisharound.presentation.main.MainViewModelFactory;

public final class MainActivity extends AppCompatActivity {

    private static final String ROOT_KEY = "current_root";
    private int currentRoot = R.id.nav_catalog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AuthUi.applyInsets(this, findViewById(R.id.mainRoot));

        MainViewModel model = new ViewModelProvider(this, new MainViewModelFactory(this)).get(MainViewModel.class);

        model.getState().observe(this, state -> {
            if (state.isSignedOut()) {
                openLogin();
            }
        });

        getSupportFragmentManager().setFragmentResultListener("open_word", this, (requestKey, bundle) -> {
            openWordDetails(bundle);
        });

        BottomNavigationView navigation = findViewById(R.id.bottomNavigation);

        if (savedInstanceState == null) {
            showRoot(R.id.nav_catalog);
        } else {
            currentRoot = savedInstanceState.getInt(ROOT_KEY, R.id.nav_catalog);
        }

        navigation.getMenu().findItem(currentRoot).setChecked(true);

        navigation.setOnItemSelectedListener(item -> {
            if (item.getItemId() != currentRoot) {
                showRoot(item.getItemId());
            }
            return true;
        });

        navigation.setOnItemReselectedListener(item -> {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                showRoot(item.getItemId());
            }
        });
    }

    private void showRoot(int itemId) {
        FragmentManager manager = getSupportFragmentManager();

        if (manager.isStateSaved()) {
            return;
        }

        Class<? extends Fragment> fragmentClass;

        if (itemId == R.id.nav_dictionary) {
            fragmentClass = PersonalDictionaryFragment.class;
        } else if (itemId == R.id.nav_profile) {
            fragmentClass = ProfileFragment.class;
        } else {
            fragmentClass = CatalogFragment.class;
        }

        manager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        manager.beginTransaction().setReorderingAllowed(true)
                .replace(R.id.fragmentContainer, fragmentClass, null).commit();

        currentRoot = itemId;
    }

    private void openWordDetails(Bundle arguments) {
        FragmentManager manager = getSupportFragmentManager();

        if (manager.isStateSaved()) {
            return;
        }

        manager.beginTransaction().setReorderingAllowed(true)
                .replace(R.id.fragmentContainer, WordDetailsFragment.class, arguments)
                .addToBackStack("word_details").commit();
    }

    private void openLogin() {
        if (isFinishing()) {
            return;
        }

        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(ROOT_KEY, currentRoot);
        super.onSaveInstanceState(outState);
    }
}