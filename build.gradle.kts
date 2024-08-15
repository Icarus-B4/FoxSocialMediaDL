// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
}

fun implementation(s: String) {

}
dependencies {
    implementation(libs.androidx.viewpager2.get().toString())
    implementation(libs.material.v150.get().toString())
    implementation(libs.androidx.drawerlayout.get().toString())
    implementation(libs.androidx.core.ktx.v170.get().toString())
    implementation(libs.androidx.appcompat.get().toString())
    implementation(libs.androidx.constraintlayout.v212.get().toString())


}