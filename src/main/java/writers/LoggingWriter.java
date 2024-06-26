package writers;

import java.io.IOException;
import java.io.Writer;

public class LoggingWriter extends Writer {
    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        String message = new String(cbuf, off, len);
        System.out.printf("Message: %s%n", message);
    }

    @Override
    public void flush() throws IOException {
        System.out.println("Flushing...");
    }

    @Override
    public void close() throws IOException {
        System.out.println("Closing...");
    }
}
