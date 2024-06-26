package listeners;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticListener;
import javax.tools.JavaFileObject;

public class SimpleListener implements DiagnosticListener<JavaFileObject> {
    @Override
    public void report(Diagnostic<? extends JavaFileObject> diagnostic) {
        System.out.println();
        System.out.println("SIMPLE DIAGNOSTIC ==============================================================");
        System.out.println(diagnostic);
        System.out.println("================================================================================");
        System.out.println();
    }
}
