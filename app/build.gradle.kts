import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    kotlin("plugin.serialization") version "2.0.0"
    id("jacoco")
}

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(localPropertiesFile.inputStream())
}

android {
    namespace = "com.maulana.weathermobile"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.maulana.weathermobile"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        buildConfigField("String", "BASE_URL", "\"https://api.openweathermap.org/\"")
        buildConfigField("String", "API_KEY", "\"${localProperties.getProperty("apiKey")}\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            enableUnitTestCoverage = true  // Enable test coverage for the debug build
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
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
    ksp {
        arg("dagger.hilt.disableModulesHaveInstallInCheck", "true")
    }


}

dependencies {

    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)

    ksp(libs.hilt.compiler)

    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.constraintlayout.compose)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.runtime.android)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material)
    implementation(libs.gson)
    implementation(libs.retrofit)

    implementation(libs.okhttp)
    debugImplementation(libs.okhttp.logging)

    implementation(libs.gson.converter.retrofit)

    implementation(libs.accompanist.permissions)

    implementation(libs.androidx.room.runtime)

    ksp(libs.androidx.room.compiler)

    implementation(libs.androidx.room.ktx)

    implementation(libs.coil.compose)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.play.services.location)

    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.junit.jupiter)

    //androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    //androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    debugImplementation(libs.chucker)
    releaseImplementation(libs.chucker.no.op)
}

tasks.getByName("clean") {
    delete(layout.buildDirectory)
}

extensions.getByType<JacocoPluginExtension>().run {
    toolVersion = "0.8.12"
}


tasks.withType<Test> {
    useJUnitPlatform()

    finalizedBy("jacocoTestReport")

    extensions.configure<JacocoTaskExtension> {
        isIncludeNoLocationClasses = true
        excludes = listOf("jdk.internal.*")
    }
}

tasks.register<JacocoReport>("jacocoTestReport") {
    group = "Reporting"
    description = "Generate Jacoco coverage reports for the debug build."

    dependsOn("testDebugUnitTest")  // Ensure tests are run first

    reports {
        html.required.set(true)
        xml.required.set(true)
        csv.required.set(false)
    }

    val fileFilter = listOf(
        "**/R.class",
        "**/R\$*.class",
        "**/BuildConfig.*",
        "**/Manifest*.*",
        "**/*\$ViewInjector*.*",
        "**/*\$ViewBinder*.*",
        "**/databinding/*",
        "**/android/databinding/*",
        "**/androidx/databinding/*",
        "**/*_MembersInjector.*",
        "**/Dagger*.*",
        "**/*_Factory.*",
        "**/*_Provide*Factory.*",
        "**/*Extensions*.*"
    )

    val debugTree = fileTree("${buildDir}/intermediates/javac/debug/classes") {
        exclude(fileFilter)
    }

    val kotlinDebugTree = fileTree("${buildDir}/tmp/kotlin-classes/debug") {
        exclude(fileFilter)
    }

    sourceDirectories.setFrom(files(listOf("src/main/java", "src/main/kotlin")))
    classDirectories.setFrom(files(listOf(debugTree, kotlinDebugTree)))
    executionData.setFrom(files("${buildDir}/outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec"))

}