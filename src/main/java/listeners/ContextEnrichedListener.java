package listeners;

import com.sun.tools.javac.api.ClientCodeWrapper;
import com.sun.tools.javac.api.DiagnosticFormatter;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.JCDiagnostic;
import com.sun.tools.javac.util.Log;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticListener;
import javax.tools.JavaFileObject;

@ClientCodeWrapper.Trusted
public class ContextEnrichedListener implements DiagnosticListener<JavaFileObject> {

    private final Context context;

    public ContextEnrichedListener(Context context) {
        this.context = context;
    }

    @Override
    public void report(Diagnostic<? extends JavaFileObject> diagnostic) {
        DiagnosticFormatter<JCDiagnostic> diagnosticFormatter = Log.instance(context).getDiagnosticFormatter();
        System.out.println(diagnosticFormatter.format((JCDiagnostic) diagnostic, null));
    }
}
