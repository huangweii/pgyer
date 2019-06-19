package com.pgyer.simple.pinpoint.utils;

public class TransactionIdUtils {

    public static final String TRANSACTION_ID_DELIMITER = "^";

    public static String formatString(String agentId, long agentStartTime, long transactionSequence) {
        if (agentId == null) {
            throw new NullPointerException("agentId must not be null");
        }
        StringBuilder sb = new StringBuilder(64);
        sb.append(agentId);
        sb.append(TRANSACTION_ID_DELIMITER);
        sb.append(agentStartTime);
        sb.append(TRANSACTION_ID_DELIMITER);
        sb.append(transactionSequence);
        return sb.toString();
    }
}
