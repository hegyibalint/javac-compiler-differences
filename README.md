# Public vs. Internal Java Compiler Demo

This repo presents a micro-demo of how differences in publicly accessible (i.e. `javax.tools.JavaCompiler`) and private (i.e. `com.sun.tools.javac.api.JavacTool`) are supposedly not equal in features.

The demo shows:
 - The difference between the diagnostic output of the two approaches
 - The fact that if a diagnostic listener is used, no output will be printed to the console

## How to run:

Execute `./gradlew run`