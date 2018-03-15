package com.knoldus.aws.lambdawithsns;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.knoldus.aws.utils.mailhelper.MailerHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(MailerHelper.class)
@PowerMockIgnore("javax.management.*")
public class LambdaWithSNSTest {

    private LambdaWithSNS lambdaWithSNS = new LambdaWithSNS();

    private SNSEvent mockedSNSEvent = mock(SNSEvent.class);
    private SNSEvent.SNSRecord mockedSNSRecord = mock(SNSEvent.SNSRecord.class);

    private Context context = null;

    @Test
    public void handleRequestTest() throws Exception {
        List<SNSEvent.SNSRecord> records = new ArrayList<>();
        records.add(mockedSNSRecord);

        when(mockedSNSEvent.getRecords()).thenReturn(records);

        PowerMockito.mockStatic(MailerHelper.class);
        when(MailerHelper.sendMail(anyString(), anyString(), anyString())).thenReturn(true);

        assertNull(lambdaWithSNS.handleRequest(mockedSNSEvent, context));
    }

}