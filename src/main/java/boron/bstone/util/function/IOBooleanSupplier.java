package boron.bstone.util.function;

import java.io.IOException;
import java.util.function.Supplier;

/**
 * Represents a supplier of {@code boolean}-valued results.  This is the
 * {@code boolean}-producing primitive specialization of {@link Supplier} and
 * may throw {@link IOException}.
 * <p>
 * <p>There is no requirement that a new or distinct result be returned each
 * time the supplier is invoked.
 * <p>
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #getAsBoolean()}.
 *
 * @see java.util.function.BooleanSupplier
 */
@FunctionalInterface
public interface IOBooleanSupplier
{

	/**
	 * Gets a result.
	 *
	 * @return a result
	 *
	 * @throws IOException Includes any I/O exceptions that may occur
	 */
	boolean getAsBoolean() throws IOException;
}