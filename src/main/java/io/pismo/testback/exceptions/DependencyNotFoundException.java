/**
 * 
 */
package io.pismo.testback.exceptions;

/**
 * @author victormartins
 *
 */
public class DependencyNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5341393469584127578L;

	public DependencyNotFoundException() {
		super();
	}

	public DependencyNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public DependencyNotFoundException(String message) {
		super(message);
	}

	public DependencyNotFoundException(Throwable cause) {
		super(cause);
	}
}