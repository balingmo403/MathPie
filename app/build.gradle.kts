import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
}

val deepSeekProperties = Properties().apply {
    val configFile = rootProject.file("deepseek.properties")
    if (configFile.exists()) {
        configFile.inputStream().use(::load)
    }
}

android {
    namespace = "com.mathhelp.app"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.mathhelp.app"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "0.1.0"

        buildConfigField(
            "String",
            "DEFAULT_DEEPSEEK_BASE_URL",
            "\"${deepSeekProperties.getProperty("baseUrl", "https://api.deepseek.com/")}\""
        )
        buildConfigField(
            "String",
            "DEFAULT_DEEPSEEK_MODEL",
            "\"${deepSeekProperties.getProperty("model", "deepseek-chat")}\""
        )
        buildConfigField(
            "String",
            "DEFAULT_DEEPSEEK_API_KEY",
            "\"${deepSeekProperties.getProperty("apiKey", "")}\""
        )
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    debugImplementation("androidx.compose.ui:ui-tooling")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)

    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.moshi)
    implementation(libs.okhttp.logging)
    implementation(libs.moshi.kotlin)
    implementation(libs.onnxruntime.android)
    implementation(libs.mlkit.text.recognition.chinese)
    implementation(libs.kotlinx.coroutines.play.services)
}
