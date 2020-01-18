package my.project.rdp.other;

@FunctionalInterface
public interface FunctionWithEx<T, R> {

    R apply(T t) throws Exception;

}
