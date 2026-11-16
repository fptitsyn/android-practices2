package ru.mirea.ptitsyn.fragmentmanagerapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ShareViewModel extends ViewModel {
    private final MutableLiveData<Country> selectedItem = new MutableLiveData<>();
    private final List<Country> countries = List.of(
            new Country("Россия", "Москва", "Государство, расположенное в Европе и Азии."),
            new Country("Бразилия", "Бразилиа", "Крупнейшее по площади государство Южной Америки."),
            new Country("Китай", "Пекин", "Государство в Восточной Азии."),
            new Country("Индия", "Нью-Дели", "Государство в Южной Азии."),
            new Country("ЮАР", "Претория, Кейптаун, Блумфонтейн", "Государство на юге Африки с тремя столицами.")
    );

    public List<Country> getCountries() { return countries; }
    public LiveData<Country> getSelectedItem() { return selectedItem; }
    public void selectItem(Country country) { selectedItem.setValue(country); }
}
