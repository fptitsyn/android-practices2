package ru.mirea.ptitsyn.data.network;

import java.util.List;

public final class DictionaryEntryDto {
    private String word;
    private String phonetic;
    private List<PhoneticDto> phonetics;
    private List<MeaningDto> meanings;

    public String getWord() {
        return word;
    }

    public String getPhonetic() {
        return phonetic;
    }

    public List<PhoneticDto> getPhonetics() {
        return phonetics;
    }

    public List<MeaningDto> getMeanings() {
        return meanings;
    }

    public static final class PhoneticDto {
        private String text;

        public String getText() {
            return text;
        }
    }

    public static final class MeaningDto {
        private String partOfSpeech;
        private List<DefinitionDto> definitions;

        public String getPartOfSpeech() {
            return partOfSpeech;
        }

        public List<DefinitionDto> getDefinitions() {
            return definitions;
        }
    }

    public static final class DefinitionDto {
        private String definition;
        private String example;

        public String getDefinition() {
            return definition;
        }

        public String getExample() {
            return example;
        }
    }
}