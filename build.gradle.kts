plugins {
    java
    application

}

group = "fam.badger_ken.matchmaker"
version = "2.0"

repositories {
    mavenCentral()
}


dependencies {
    implementation(files("lib/swingx-core-1.6.2-AutoCompletition.jar"))
}


configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}



application {
    mainClassName = "fam.badger_ken.matchmaker.SwingGui"
}

