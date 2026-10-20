plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "ru.mirea.ptitsyn.lists.recyclerviewapp"
    compileSdk {
        version = release(36) { minorApiLevel = 1 }
    }
    defaultConfig {
        applicationId = "ru.mirea.ptitsyn.lists.recyclerviewapp"
        minSdk = 27
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation("androidx.recyclerview:recyclerview:1.4.0")
}
