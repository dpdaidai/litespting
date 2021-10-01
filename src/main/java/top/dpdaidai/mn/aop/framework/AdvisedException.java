package top.dpdaidai.mn.aop.framework;




@SuppressWarnings("serial")
public class AdvisedException extends RuntimeException {

	/**
	 * Constructor for AdvisedException.
	 * @param msg the detail message
	 */
	public AdvisedException(String msg) {
		super(msg);
	}

	/**
	 * Constructor for AdvisedException.
	 * @param msg the detail message
	 * @param cause the root cause
	 */
	public AdvisedException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
