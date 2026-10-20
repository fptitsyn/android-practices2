package ru.mirea.ptitsyn.lists.recyclerviewapp;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public final class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View root = findViewById(R.id.mainRoot);
        int left = root.getPaddingLeft();
        int top = root.getPaddingTop();
        int right = root.getPaddingRight();
        int bottom = root.getPaddingBottom();
        ViewCompat.setOnApplyWindowInsetsListener(root, (view, windowInsets) -> {
            Insets bars = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(left + bars.left, top + bars.top, right + bars.right, bottom + bars.bottom);
            return windowInsets;
        });
        ViewCompat.requestApplyInsets(root);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new HistoricalEventAdapter(getEvents()));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, layoutManager.getOrientation()));
    }

    private List<HistoricalEvent> getEvents() {
        List<HistoricalEvent> events = new ArrayList<>();
        events.add(new HistoricalEvent("Около 2560 года до н. э.", "Строительство пирамиды Хеопса",
                "В Древнем Египте построена Великая пирамида в Гизе.", R.drawable.ic_pyramid));
        events.add(new HistoricalEvent("Около 1450 года", "Книгопечатание Гутенберга",
                "Иоганн Гутенберг развивает в Европе печать с использованием подвижных металлических литер.",
                R.drawable.ic_book));
        events.add(new HistoricalEvent("1492 год", "Экспедиция Колумба",
                "Экспедиция Христофора Колумба пересекла Атлантику и достигла островов Карибского моря.",
                R.drawable.ic_ship));
        events.add(new HistoricalEvent("1703 год", "Основание Санкт-Петербурга",
                "По указу Петра I началось строительство Петропавловской крепости и нового города.",
                R.drawable.ic_city));
        events.add(new HistoricalEvent("1769 год", "Патент Джеймса Уатта",
                "Джеймс Уатт получил патент на усовершенствование паровой машины с отдельным конденсатором.",
                R.drawable.ic_gears));
        events.add(new HistoricalEvent("1812 год", "Отечественная война",
                "Вторжение армии Наполеона в Россию завершилось её отступлением.", R.drawable.ic_shield));
        events.add(new HistoricalEvent("1945 год", "Окончание Второй мировой войны",
                "Вторая мировая война завершилась капитуляцией Японии в сентябре 1945 года.", R.drawable.ic_peace));
        events.add(new HistoricalEvent("1957 год", "Первый искусственный спутник",
                "4 октября СССР запустил первый искусственный спутник Земли — Спутник-1.", R.drawable.ic_satellite));
        events.add(new HistoricalEvent("1961 год", "Первый полёт человека в космос",
                "12 апреля Юрий Гагарин совершил орбитальный полёт на корабле Восток-1.", R.drawable.ic_rocket));
        events.add(new HistoricalEvent("1969 год", "Первая высадка людей на Луну",
                "Во время экспедиции Аполлон-11 Нил Армстронг и Базз Олдрин вышли на поверхность Луны.",
                R.drawable.ic_moon));
        events.add(new HistoricalEvent("1989 год", "Предложение Всемирной паутины",
                "Тим Бернерс-Ли предложил в CERN проект гипертекстовой системы, ставшей основой Web.",
                R.drawable.ic_computer));
        return events;
    }
}
