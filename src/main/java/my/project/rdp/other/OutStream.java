package my.project.rdp.other;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.Socket;
import java.util.function.Function;

//DataOutputStream dos = new OutStream(ByteArrayOutputStream::new).buffered(255).data().get();
public class OutStream {
    private final OutStream ref;
    private final Function<? super OutputStream, ? extends OutputStream> func;
    private WeakReference<OutputStream> out;

    private OutStream(OutStream ref, Function<? super OutputStream, ? extends OutputStream> out) {
        this.ref = ref;
        this.func = out;
    }

    private OutStream decorate(Function<? super OutputStream, ? extends OutputStream> out) {
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
        final OutputStream outputStream = func.apply(ref == null ? null : ref.get());
        out = new WeakReference<>(outputStream);
        return (T) outputStream;

    }

    public static OutStream byteArray() {
        return new OutStream(null, o -> new ByteArrayOutputStream());
    }

    public static OutStream of(Socket socket) {
        return new OutStream(null, o -> Utils.rethrow(socket::getOutputStream));
    }

    public static void main(String[] args) {
        final OutputStream outputStream = new Out1(null, i -> new ByteArrayOutputStream()).buffered().data().get();
    }

}

abstract class Pipe<T, S extends Pipe<T, S>> {
    private final Pipe<T, S> ref;
    private final Function<T, T> func;
    //private WeakReference<T> out;

    Pipe(Pipe<T, S> ref, Function<T,T> out) {
        this.ref = ref;
        this.func = out;
    }

    abstract S decorate(Function< T, T> out);

    public T get() {
        //out = new WeakReference<>(value);
        return func.apply(ref == null ? null : ref.get());

    }

}

class Out1 extends Pipe<OutputStream, Out1> {
    Out1(Pipe<OutputStream, Out1> ref, Function<OutputStream,OutputStream> out) {
        super(ref, out);
    }

    @Override
    Out1 decorate(Function<OutputStream,OutputStream> fu) {
        return new Out1(this, fu);
    }

    Out1 buffered() {
        return decorate(BufferedOutputStream::new);

    }

    Out1 data() {
        return decorate(DataOutputStream::new);
    }

    public static Out1 of(Socket socket) {
        return new Out1(null, o -> Utils.rethrow(socket::getOutputStream));
    }

}
