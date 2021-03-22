/**
 * 
 */
package io.pismo.testback.exceptions;

/**
 * @author victormartins
 *
 */
public class TransactionAmountGreaterThanLimitException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6312459862957533613L;

	public TransactionAmountGreaterThanLimitException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TransactionAmountGreaterThanLimitException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public TransactionAmountGreaterThanLimitException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public TransactionAmountGreaterThanLimitException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
	
}