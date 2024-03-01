package org.afob.execution;

public final class ExecutionClient {

    public void buy(String productId, int amount) throws ExecutionException {
        throw new ExecutionException("failed to buy: environment error");
    }

    public void sell(String productId, int amount) throws ExecutionException {
        throw new ExecutionException("failed to sell: environment error");
    }


    public static class ExecutionException extends Exception {
        public ExecutionException(String message) {
            super(message);
        }

        public ExecutionException(String message, Throwable cause) {
            super(message, cause);
        }
    }

}
