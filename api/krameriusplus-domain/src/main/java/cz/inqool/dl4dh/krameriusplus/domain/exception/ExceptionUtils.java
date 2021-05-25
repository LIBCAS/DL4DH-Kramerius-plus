package cz.inqool.dl4dh.krameriusplus.domain.exception;

import lombok.NonNull;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.lang.Nullable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Methods from Utils class from UAS
 *
 * @author Norbert Bodnar
 */
public class ExceptionUtils {

    public static <T extends RuntimeException> void gt(Integer x, Integer y, Supplier<T> supplier) {
        if (x <= y) {
            throw supplier.get();
        }
    }

    public static <T extends RuntimeException> void gt(Long x, Long y, Supplier<T> supplier) {
        if (x <= y) {
            throw supplier.get();
        }
    }

    public static <T extends RuntimeException> void gt(BigDecimal x, BigDecimal y, Supplier<T> supplier) {
        if (x.compareTo(y) <= 0) {
            throw supplier.get();
        }
    }

    public static <T extends RuntimeException> void gte(Integer x, Integer y, Supplier<T> supplier) {
        if (x < y) {
            throw supplier.get();
        }
    }

    public static <T extends RuntimeException> void gte(Long x, Long y, Supplier<T> supplier) {
        if (x < y) {
            throw supplier.get();
        }
    }

    public static <T extends RuntimeException> void gte(BigDecimal x, BigDecimal y, Supplier<T> supplier) {
        if (x.compareTo(y) < 0) {
            throw supplier.get();
        }
    }

    public static <T extends RuntimeException> void gte(LocalDate x, LocalDate y, Supplier<T> supplier) {
        if (x.compareTo(y) < 0) {
            throw supplier.get();
        }
    }

    public static <T extends RuntimeException> void lt(Integer x, Integer y, Supplier<T> supplier) {
        if (x >= y) {
            throw supplier.get();
        }
    }

    public static <T extends RuntimeException> void lt(Long x, Long y, Supplier<T> supplier) {
        if (x >= y) {
            throw supplier.get();
        }
    }

    public static <T extends RuntimeException> void lt(BigDecimal x, BigDecimal y, Supplier<T> supplier) {
        if (x.compareTo(y) >= 0) {
            throw supplier.get();
        }
    }

    public static <T extends RuntimeException> void lte(Integer x, Integer y, Supplier<T> supplier) {
        if (x > y) {
            throw supplier.get();
        }
    }

    public static <T extends RuntimeException> void lte(Long x, Long y, Supplier<T> supplier) {
        if (x > y) {
            throw supplier.get();
        }
    }

    public static <T extends RuntimeException> void lte(BigDecimal x, BigDecimal y, Supplier<T> supplier) {
        if (x.compareTo(y) > 0) {
            throw supplier.get();
        }
    }

    public static <T extends RuntimeException> void lte(LocalDate x, LocalDate y, Supplier<T> supplier) {
        if (x.compareTo(y) > 0) {
            throw supplier.get();
        }
    }

    public static <U, T extends RuntimeException> void eq(U o1, U o2, Supplier<T> supplier) {
        if (!Objects.equals(o1, o2)) {
            throw supplier.get();
        }
    }

    public static <U, T extends RuntimeException> void ne(U o1, U o2, Supplier<T> supplier) {
        if (Objects.equals(o1, o2)) {
            throw supplier.get();
        }
    }

    public static <T extends RuntimeException> void isTrue(Boolean o, Supplier<T> supplier) {
        if (o != null && !o) {
            throw supplier.get();
        }
    }

    public static <T extends RuntimeException> void isFalse(Boolean o, Supplier<T> supplier) {
        if (o != null && o) {
            throw supplier.get();
        }
    }

    public static <T extends RuntimeException> void notNull(Object o, Supplier<T> supplier) {
        if (o == null) {
            throw supplier.get();
        } else if (o instanceof Optional) {
            if (((Optional<?>) o).isEmpty()) {
                throw supplier.get();
            }
        } else if (isProxy(o)) {
            if (unwrap(o) == null) {
                throw supplier.get();
            }
        }
    }

    public static <T extends RuntimeException> void isNull(Object o, Supplier<T> supplier) {
        if (o instanceof Optional) {
            if (((Optional<?>) o).isPresent()) {
                throw supplier.get();
            }
        } else if (isProxy(o)) {
            if (unwrap(o) != null) {
                throw supplier.get();
            }
        } else if (o != null) {
            throw supplier.get();
        }
    }

    /**
     * Checks whether object is an instance of providen class. Throws an exception if not.
     *
     * @param o        object to be checked
     * @param clazz    class that the object should be instance of
     * @param supplier provides an exception instance
     * @param <O>      type of object to be checked
     * @param <C>      type of class that the object should be instance of
     * @param <T>      type of exception to be thrown
     * @throws T if the object is not an instance of clazz
     * @see Class#isInstance(Object)
     */
    public static <O, C extends O, T extends RuntimeException> void isInstance(@Nullable O o, @NonNull Class<C> clazz, @NonNull Supplier<T> supplier) {
        if (!clazz.isInstance(o)) {
            throw supplier.get();
        }
    }

    /**
     * Cast object to providen type.
     *
     * @param o        object to be casted
     * @param clazz    type that the object will be casted to
     * @param supplier provides an exception instance
     * @param <O>      type of object to be casted
     * @param <C>      type of class that the object will be casted to
     * @param <T>      type of exception to be thrown
     * @return casted object
     * @throws T if the object is not an instance of clazz
     * @see Class#cast(Object)
     */
    public static <O, C extends O, T extends RuntimeException> C cast(@Nullable O o, @NonNull Class<C> clazz, @NonNull Supplier<T> supplier) {
        isInstance(o, clazz, supplier);
        return clazz.cast(o);
    }

    /**
     * Extracts exposed instance from a given proxy object or the object in case of already exposed instance.
     */
    public static <T> T unwrap(T a) {
        if (isProxy(a)) {
            try {
                Object target = ((Advised) a).getTargetSource().getTarget();

                if (!Objects.equals(target, null)) {
                    return (T) target;
                } else {
                    return null;
                }
            } catch (Exception ignored) {
                return null; // return null if not in scope
            }
        } else {
            return a;
        }
    }

    /**
     * Check whether the given object is a JDK dynamic proxy or a CGLIB proxy.
     *
     * @see AopUtils#isAopProxy(Object)
     */
    public static boolean isProxy(Object a) {
        return (AopUtils.isAopProxy(a) && a instanceof Advised);
    }
}
