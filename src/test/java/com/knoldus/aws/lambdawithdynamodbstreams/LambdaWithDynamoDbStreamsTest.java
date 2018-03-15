package com.knoldus.aws.lambdawithdynamodbstreams;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.StreamRecord;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import com.knoldus.aws.utils.mailhelper.MailerHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(MailerHelper.class)
@PowerMockIgnore("javax.management.*")
public class LambdaWithDynamoDbStreamsTest {

    private LambdaWithDynamoDbStreams lambdaWithDynamoDbStreams = new LambdaWithDynamoDbStreams();

    private DynamodbEvent mockedDynamoDbEvent = mock(DynamodbEvent.class);
    private StreamRecord mockedStreamRecord = mock(StreamRecord.class);
    private DynamodbEvent.DynamodbStreamRecord mockedDynamoDbStreamRecord =
            mock(DynamodbEvent.DynamodbStreamRecord.class);

    private Context context = null;

    @Test
    public void handleRequestTest() throws Exception {
        List<DynamodbEvent.DynamodbStreamRecord> records = new ArrayList<>();
        records.add(mockedDynamoDbStreamRecord);

        Map<String, AttributeValue> newImage = new HashMap<>();

        when(mockedDynamoDbEvent.getRecords()).thenReturn(records);
        when(mockedDynamoDbStreamRecord.getDynamodb()).thenReturn(mockedStreamRecord);
        when(mockedStreamRecord.getNewImage()).thenReturn(newImage);

        PowerMockito.mockStatic(MailerHelper.class);
        when(MailerHelper.sendMail(anyString(), anyString(), anyString())).thenReturn(true);

        assertNull(lambdaWithDynamoDbStreams.handleRequest(mockedDynamoDbEvent, context));
    }

}
