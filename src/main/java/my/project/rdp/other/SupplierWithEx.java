package my.project.rdp.other;

@FunctionalInterface
 public interface SupplierWithEx<T> {

    /**
     * Gets a result.
     *
     * @return a result
     */
    T get() throws Exception;
}