package bean.spider;

/**
 * Created by scott on 2016/12/20.
 */
public class RuleException extends RuntimeException {
    public RuleException(){
        super();
    }

    public RuleException (String message, Throwable cause){
        super(message, cause);
    }

    public RuleException (String message){
        super(message);
    }

    public RuleException (Throwable cause){
        super(cause);
    }
}
