plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "ru.mirea.ptitsyn.lists.listviewapp"
    compileSdk {
        version = release(36) { minorApiLevel = 1 }
    }
    defaultConfig {
        applicationId = "ru.mirea.ptitsyn.lists.listviewapp"
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

}
