import com.sun.source.util.JavacTask;
import com.sun.tools.javac.api.JavacTool;
import com.sun.tools.javac.util.Context;
import listeners.ContextEnrichedListener;
import listeners.SimpleListener;

import javax.tools.DiagnosticListener;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import java.io.File;
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
        DiagnosticListener<JavaFileObject> diagnosticListener = new SimpleListener();

        Iterable<? extends JavaFileObject> javaFileObjects = fileManager.getJavaFileObjectsFromFiles(inputFiles);
        compiler.getTask(null, fileManager, diagnosticListener, COMPILER_ARGS, null, javaFileObjects).call();
    }

    private static void compileWithJavacTool(List<File> inputFiles) {
        JavacTool tool = JavacTool.create();
        Context context = new Context();
        SimpleListener listener = new SimpleListener();
        DiagnosticListener<JavaFileObject> diagnosticListener = new ContextEnrichedListener(context);

        StandardJavaFileManager fileManager = tool.getStandardFileManager(listener, null, null);
        Iterable<? extends JavaFileObject> javaFileObjects = fileManager.getJavaFileObjectsFromFiles(inputFiles);

        JavacTask task = tool.getTask(null, fileManager, diagnosticListener, COMPILER_ARGS, null, javaFileObjects, context);
        task.call();
    }

}
