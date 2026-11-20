package ru.mirea.ptitsyn.englisharound.presentation;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import ru.mirea.ptitsyn.englisharound.R;
import ru.mirea.ptitsyn.englisharound.databinding.ActivityMainBinding;
import ru.mirea.ptitsyn.englisharound.presentation.main.MainViewModel;
import ru.mirea.ptitsyn.englisharound.presentation.main.MainViewModelFactory;

public final class MainActivity extends AppCompatActivity {

    public static final String EXTRA_GUEST_MODE = "ru.mirea.ptitsyn.englisharound.GUEST_MODE";

    private ActivityMainBinding binding;
    private NavController navController;
    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (view, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return WindowInsetsCompat.CONSUMED;
        });

        ViewCompat.requestApplyInsets(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        NavHostFragment host = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host);

        if (host == null) {
            throw new IllegalStateException("NavHostFragment не найден");
        }

        navController = host.getNavController();

        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_catalog, R.id.nav_recognition, R.id.nav_dictionary, R.id.nav_profile).build();

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        boolean guestMode = getIntent().getBooleanExtra(EXTRA_GUEST_MODE, false);
        binding.bottomNavigation.getMenu().findItem(R.id.nav_dictionary).setVisible(!guestMode);
        NavigationUI.setupWithNavController(binding.bottomNavigation, navController);

        MainViewModel model = new ViewModelProvider(this, new MainViewModelFactory(this)).get(MainViewModel.class);

        model.getState().observe(this, state -> {
            if (state.isSignedOut()) {
                openLogin();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
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
}