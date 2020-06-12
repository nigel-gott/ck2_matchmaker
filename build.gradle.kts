plugins {
    java
    application
    id("edu.sc.seis.launch4j") version "2.4.6"
    id("com.github.johnrengelman.shadow") version "5.2.0"
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

launch4j {
    mainClassName = "fam.badger_ken.matchmaker.SwingGui"
    icon = "${projectDir}/etc/matchmaker.ico"
    initialHeapSize = 812
    maxHeapSize = 1536
    stayAlive = false
    headerType = "gui"
    jreMinVersion = "1.8.0"
    copyConfigurable = project.tasks.shadowJar.get().outputs
    jar = "${buildDir}/libs/${project.tasks.shadowJar.get().archiveFileName.get()}"
}

