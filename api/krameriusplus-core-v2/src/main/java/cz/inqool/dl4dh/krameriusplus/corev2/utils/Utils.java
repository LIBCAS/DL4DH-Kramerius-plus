package cz.inqool.dl4dh.krameriusplus.corev2.utils;

import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

public class Utils {

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
     * Check whether the given object is a JDK dynamic proxy or a CGLIB proxy.
     *
     * @see AopUtils#isAopProxy(Object)
     */
    public static boolean isProxy(Object a) {
        return (AopUtils.isAopProxy(a) && a instanceof Advised);
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

}
