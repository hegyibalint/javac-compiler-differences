plugins {
    `java-library`
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val compilableSources = sourceSets.create("compilable") {
    java {
        srcDir("src/compilable/java")
    }
}

val compiler = javaToolchains.compilerFor {
    languageVersion.set(JavaLanguageVersion.of(21))
}

dependencies {
    // Put the tools.jar on the classpath for Java 8
    if (JavaVersion.current().isJava8Compatible) {
        implementation(files(compiler.get().metadata.installationPath.file("lib/tools.jar")))
    }
}

tasks {
    compileJava {
        options.compilerArgs = listOf(
            "--add-exports", "jdk.compiler/com.sun.tools.javac.api=ALL-UNNAMED",
            "--add-exports", "jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED",
        )
    }

    test {
        useJUnitPlatform()
    }

    val run by creating(JavaExec::class) {
        group = "application"
        description = "Runs the application"

        mainClass = "Main"
        classpath(
            sourceSets.main.get().output
        )

        jvmArguments.addAll(listOf(
            "--add-exports=jdk.compiler/com.sun.tools.javac.api=ALL-UNNAMED",
            "--add-exports=jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED",
        ))

        compilableSources.allJava.files.map {
            it.absolutePath
        }.forEach {
            args(it)
        }
    }
}