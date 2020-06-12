plugins {
    java
    application

}

group = "org.example"
version = "1.0-SNAPSHOT"

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

