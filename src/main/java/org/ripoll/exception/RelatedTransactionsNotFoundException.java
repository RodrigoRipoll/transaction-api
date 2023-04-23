package org.ripoll.exception;

import java.text.MessageFormat;

public class RelatedTransactionsNotFoundException extends RuntimeException {

    public RelatedTransactionsNotFoundException(long transactionId) {
        super(MessageFormat.format("could not get any transaction related to {0}.", transactionId));
    }
}
