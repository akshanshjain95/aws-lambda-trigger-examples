package com.knoldus.aws.lambdawithapigateway;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import org.junit.Test;

public class LambdaWithAPIGatewayTest {

    private LambdaWithAPIGateway lambdaWithAPIGateway = new LambdaWithAPIGateway();
    private String requestBody = "This is some test request event body";
    private Context context = null;

    @Test
    public void handleRequestTest() throws Exception {
        APIGatewayProxyRequestEvent requestEvent = new APIGatewayProxyRequestEvent();
        requestEvent.setBody(requestBody);

        APIGatewayProxyResponseEvent responseEvent = lambdaWithAPIGateway.handleRequest(requestEvent, context);

        assert (responseEvent.getStatusCode() == 200);
    }

}
