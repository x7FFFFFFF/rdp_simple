package my.project.rdp.other;

@FunctionalInterface
public interface ConsumerWithEx<T> {
    void accept(T t) throws Exception;
}