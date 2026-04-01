plugins {
    id("java")
    id("application")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // MySQL JDBC Driver
    implementation("com.mysql:mysql-connector-j:8.2.0")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

// Cau hinh UTF-8 cho Java compiler
tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

// Cau hinh UTF-8 cho console khi chay
application {
    mainClass.set("org.example.Main")
    applicationDefaultJvmArgs = listOf(
        "-Dfile.encoding=UTF-8",
        "-Dconsole.encoding=UTF-8",
        "-Dstdout.encoding=UTF-8",
        "-Dstderr.encoding=UTF-8"
    )
}

// Cau hinh UTF-8 cho JavaExec tasks (bao gom ca Run trong IntelliJ)
tasks.withType<JavaExec> {
    jvmArgs(
        "-Dfile.encoding=UTF-8",
        "-Dconsole.encoding=UTF-8",
        "-Dstdout.encoding=UTF-8",
        "-Dstderr.encoding=UTF-8"
    )
}

tasks.test {
    useJUnitPlatform()
}