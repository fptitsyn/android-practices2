package ru.mirea.ptitsyn.lists.listviewapp;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.widget.ListView;
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

        ListView bookList = findViewById(R.id.bookList);
        bookList.setAdapter(new BookAdapter(this, getBooks()));
    }

    private List<Book> getBooks() {
        List<Book> books = new ArrayList<>();
        books.add(new Book("Фёдор Достоевский", "Бесы"));
        books.add(new Book("Лев Толстой", "Война и мир"));
        books.add(new Book("Эдуард Успенский", "Гарантийные человечки"));
        books.add(new Book("Михаил Булгаков", "Мастер и Маргарита"));
        books.add(new Book("Александр Пушкин", "Евгений Онегин"));
        books.add(new Book("Михаил Лермонтов", "Герой нашего времени"));
        books.add(new Book("Иван Гончаров", "Обрыв"));
        books.add(new Book("Антон Чехов", "Палата № 6"));
        books.add(new Book("Александр Грибоедов", "Горе от ума"));
        books.add(new Book("Николай Лесков", "Очарованный странник"));
        books.add(new Book("Иван Бунин", "Тёмные аллеи"));
        books.add(new Book("Михаил Шолохов", "Тихий Дон"));
        books.add(new Book("Евгений Замятин", "Мы"));
        books.add(new Book("Андрей Платонов", "Котлован"));
        books.add(new Book("Борис Пастернак", "Доктор Живаго"));
        books.add(new Book("Братья Стругацкие", "Пикник на обочине"));
        books.add(new Book("Даниил Гранин", "Иду на грозу"));
        books.add(new Book("Джордж Оруэлл", "1984"));
        books.add(new Book("Олдос Хаксли", "О дивный новый мир"));
        books.add(new Book("Рэй Брэдбери", "451 градус по Фаренгейту"));
        books.add(new Book("Дэниел Киз", "Цветы для Элджернона"));
        books.add(new Book("Эрих Мария Ремарк", "Три товарища"));
        books.add(new Book("Эрнест Хемингуэй", "Старик и море"));
        books.add(new Book("Джек Лондон", "Предсмертные записки Пиквинксого клуба"));
        books.add(new Book("Габриэль Гарсиа Маркес", "Сто лет одиночества"));
        books.add(new Book("Франц Кафка", "Процесс"));
        books.add(new Book("Альбер Камю", "Посторонний"));
        books.add(new Book("Герман Гессе", "Степной волк"));
        books.add(new Book("Оскар Уайльд", "Портрет Дориана Грея"));
        books.add(new Book("Джейн Остин", "Гордость и предубеждение"));
        books.add(new Book("Шарлотта Бронте", "Джейн Эйр"));
        books.add(new Book("Мэри Шелли", "Франкенштейн"));
        books.add(new Book("Уильям Голдинг", "Повелитель мух"));
        books.add(new Book("Джон Стейнбек", "Гроздья гнева"));
        books.add(new Book("Умберто Эко", "Имя розы"));
        return books;
    }
}
