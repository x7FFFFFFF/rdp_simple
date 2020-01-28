package my.project.rdp.other;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

//DataOutputStream dos = new OutStream(ByteArrayOutputStream::new).buffered(255).data().get();
public class OutStream {
    private final OutStream ref;
    private final Function<OutputStream, OutputStream> out;

    private OutStream(OutStream ref, Function<OutputStream, OutputStream> out) {
        this.ref = ref;
        this.out = out;
    }

    public OutStream decorate(Function<OutputStream, OutputStream> out) {
        return new OutStream(this, out);
    }

    public OutStream buffered() {
        return decorate(BufferedOutputStream::new);
    }

    public OutStream buffered(int size) {
        return decorate(out -> new BufferedOutputStream(out, size));
    }

    public OutStream data() {
        return decorate(DataOutputStream::new);
    }

    public <T extends OutputStream> T get() {
        OutStream refLocal;
        final List<OutStream> list = new ArrayList<>();
        list.add(this);
        for (refLocal = ref; refLocal != null; refLocal = refLocal.ref) {
            list.add(refLocal);
        }
        final int size = list.size();
        if (size == 0) {
            throw new IllegalStateException();
        }
        OutputStream val = null;
        for (int i = list.size() - 1; i >= 0; i--) {
            val = list.get(i).out.apply(val);

        }
        return (T) val;

    }

    public static OutStream byteArray() {
        return new OutStream(null, o -> new ByteArrayOutputStream());
    }

    public static OutStream of(Socket socket) {
        return new OutStream(null, o -> Utils.rethrow(socket::getOutputStream));
    }

    public static void main(String[] args) {
        final OutputStream outputStream = OutStream.byteArray().buffered().data().get();
        System.out.println("outputStream = " + outputStream);
    }
}
