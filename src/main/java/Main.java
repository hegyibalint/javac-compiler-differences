import com.sun.source.util.JavacTask;
import com.sun.tools.javac.api.JavacTool;
import com.sun.tools.javac.util.Context;
import listeners.ContextEnrichedListener;
import listeners.SimpleListener;
import writers.LoggingWriter;

import javax.tools.DiagnosticListener;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import java.io.File;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Main {

    private final static List<String> COMPILER_ARGS = List.of(
            "-Werror",
            "-Xlint:all"
    );

    public static void main(String[] args) {
        System.out.printf("JVM version: %s%n%n", System.getProperty("java.version"));
        if (args.length == 0) {
            System.out.println("No files to compile.");
            return;
        }

        List<File> inputFiles = new ArrayList<>();
        for (String arg : args) {
            inputFiles.add(new File(arg));
        }

        compileWithStandardCompiler(inputFiles);
        compileWithJavacTool(inputFiles);
    }

    private static void compileWithStandardCompiler(List<File> inputFiles) {
        JavaCompiler compiler = javax.tools.ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, Locale.getDefault(), null);
        Writer loggingWriter = new LoggingWriter();
        DiagnosticListener<JavaFileObject> diagnosticListener = new SimpleListener();

        Iterable<? extends JavaFileObject> javaFileObjects = fileManager.getJavaFileObjectsFromFiles(inputFiles);
        // We pass `loggingWriter` to capture the output
        // The following is expected:
        //  1. if `diagnosticListener` is passed, no messages will be written to it
        //  2. if `null` is passed, messages will be written to the standard output
        // Option 1.
        compiler.getTask(loggingWriter, fileManager, diagnosticListener, COMPILER_ARGS, null, javaFileObjects).call();
        // Option 2. (uncomment to see)
        //compiler.getTask(loggingWriter, fileManager, null, COMPILER_ARGS, null, javaFileObjects).call();
    }

    private static void compileWithJavacTool(List<File> inputFiles) {
        // Difference: we will hold the context
        Context context = new Context();

        JavacTool tool = JavacTool.create();
        Writer loggingWriter = new LoggingWriter();
        SimpleListener listener = new SimpleListener();
        // Difference: we will use the context to enrich the listener
        DiagnosticListener<JavaFileObject> diagnosticListener = new ContextEnrichedListener(context);

        StandardJavaFileManager fileManager = tool.getStandardFileManager(listener, null, null);
        Iterable<? extends JavaFileObject> javaFileObjects = fileManager.getJavaFileObjectsFromFiles(inputFiles);

        // Difference: we will pass the context shared by the listener
        // We pass `loggingWriter` to capture the output
        // The following is expected:
        //  1. if `diagnosticListener` is passed, no messages will be written to it
        //  2. if `null` is passed, messages will be written to the standard output
        // Option 1.
        tool.getTask(loggingWriter, fileManager, diagnosticListener, COMPILER_ARGS, null, javaFileObjects, context).call();
        // Option 2. (uncomment to see)
        //tool.getTask(loggingWriter, fileManager, null, COMPILER_ARGS, null, javaFileObjects, context).call();
    }

}
