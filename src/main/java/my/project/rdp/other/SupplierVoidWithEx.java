package my.project.rdp.other;

@FunctionalInterface
public interface SupplierVoidWithEx<T> {

    void doSmth() throws Exception;
}
