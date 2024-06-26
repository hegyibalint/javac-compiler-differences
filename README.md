# Public vs. Internal Java Compiler Demo

This repo presents a micro-demo of how differences in publicly accessible (i.e. `javax.tools.JavaCompiler`) and private (i.e. `com.sun.tools.javac.api.JavacTool`) are supposedly not equal in features.

The demo's target is to replicate, and hold the same message what's printed in the terminal.

## How to run:

Execute `./gradlew run`