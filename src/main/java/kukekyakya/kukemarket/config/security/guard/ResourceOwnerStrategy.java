package kukekyakya.kukemarket.config.security.guard;

@FunctionalInterface
public interface ResourceOwnerStrategy<T> {
    boolean isResourceOwner(T id);
}
