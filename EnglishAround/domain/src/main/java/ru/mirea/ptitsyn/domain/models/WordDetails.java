package ru.mirea.ptitsyn.domain.models;

public class WordDetails {
    private final String word;
    private final String phonetic;
    private final String partOfSpeech;
    private final String definition;
    private final String example;

    public WordDetails(String word, String phonetic, String partOfSpeech, String definition, String example) {
        this.word = word;
        this.phonetic = phonetic;
        this.partOfSpeech = partOfSpeech;
        this.definition = definition;
        this.example = example;
    }

    public String getWord() {
        return word;
    }

    public String getPhonetic() {
        return phonetic;
    }

    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    public String getDefinition() {
        return definition;
    }

    public String getExample() {
        return example;
    }
}
