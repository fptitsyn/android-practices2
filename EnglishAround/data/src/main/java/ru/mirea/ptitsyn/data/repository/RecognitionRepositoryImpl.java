package ru.mirea.ptitsyn.data.repository;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;

import androidx.exifinterface.media.ExifInterface;

import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.label.Category;
import org.tensorflow.lite.task.vision.classifier.Classifications;
import org.tensorflow.lite.task.vision.classifier.ImageClassifier;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import ru.mirea.ptitsyn.domain.models.RecognitionResult;
import ru.mirea.ptitsyn.domain.repository.RecognitionRepository;
import ru.mirea.ptitsyn.domain.repository.RepositoryException;

public final class RecognitionRepositoryImpl implements RecognitionRepository {
    private final Context context;
    private ImageClassifier classifier;

    public RecognitionRepositoryImpl(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    public List<RecognitionResult> recognize(String imageUri) {
        Bitmap bitmap = null;

        try {
            if (classifier == null) {
                ImageClassifier.ImageClassifierOptions options =
                        ImageClassifier.ImageClassifierOptions.builder().setMaxResults(5).build();

                classifier = ImageClassifier.createFromFileAndOptions(
                        context, "efficientnet-lite0.tflite", options);
            }

            bitmap = readBitmap(Uri.parse(imageUri));
            List<Classifications> classifications = classifier.classify(TensorImage.fromBitmap(bitmap));

            List<RecognitionResult> candidates = new ArrayList<>();

            for (Classifications classification : classifications) {
                for (Category category : classification.getCategories()) {
                    String label = category.getLabel();

                    if (label == null || label.trim().isEmpty()) {
                        continue;
                    }

                    candidates.add(new RecognitionResult(label, normalizeLabel(label), category.getScore()));
                }
            }

            candidates.sort(Comparator.comparingDouble(RecognitionResult::getScore).reversed());

            List<RecognitionResult> results = new ArrayList<>();
            Set<String> addedWords = new HashSet<>();

            for (RecognitionResult candidate : candidates) {
                if (addedWords.add(candidate.getWord())) {
                    results.add(candidate);
                }
            }

            if (results.isEmpty()) {
                throw new RepositoryException("Модель не вернула доступных вариантов");
            }

            return results;

        } catch (RepositoryException exception) {
            throw exception;

        } catch (Exception exception) {
            throw new RepositoryException("Не удалось распознать фотографию: " + exception.getMessage(), exception);

        } finally {
            if (bitmap != null) {
                bitmap.recycle();
            }
        }
    }

    private Bitmap readBitmap(Uri uri) throws Exception {
        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;

        try (InputStream stream = context.getContentResolver().openInputStream(uri)) {
            BitmapFactory.decodeStream(stream, null, bounds);
        }

        if (bounds.outWidth <= 0 || bounds.outHeight <= 0) {
            throw new RepositoryException("Не удалось прочитать изображение");
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inSampleSize = 1;

        while (Math.max(bounds.outWidth, bounds.outHeight) / options.inSampleSize > 1024) {
            options.inSampleSize *= 2;
        }

        Bitmap bitmap;

        try (InputStream stream = context.getContentResolver().openInputStream(uri)) {
            bitmap = BitmapFactory.decodeStream(stream, null, options);
        }

        if (bitmap == null) {
            throw new RepositoryException("Неподдерживаемый формат изображения");
        }

        Matrix matrix = new Matrix();
        int orientation = ExifInterface.ORIENTATION_NORMAL;

        try (InputStream stream = context.getContentResolver().openInputStream(uri)) {
            if (stream != null) {
                orientation = new ExifInterface(stream).getAttributeInt(
                        ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            }
        } catch (Exception ignored) {
            // У изображения может не быть EXIF.
        }

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(270);
                break;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setScale(1, -1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
        }

        Bitmap rotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        if (rotated != bitmap) {
            bitmap.recycle();
        }

        return rotated;
    }

    private String normalizeLabel(String label) {
        String word = label.split(",")[0].replace('_', ' ').trim().toLowerCase(Locale.ROOT);
        return word;
    }

    @Override
    public void close() {
        if (classifier != null) {
            classifier.close();
            classifier = null;
        }
    }
}