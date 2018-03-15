package com.knoldus.aws.lambdawithkinesis;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.KinesisEvent;
import com.knoldus.aws.utils.mailhelper.MailerHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(MailerHelper.class)
@PowerMockIgnore("javax.management.*")
public class LambdaWithKinesisTest {

    private LambdaWithKinesis lambdaWithKinesis = new LambdaWithKinesis();
    private Context context = null;
    private KinesisEvent mockedKinesisEvent = mock(KinesisEvent.class);
    private KinesisEvent.KinesisEventRecord mockedKinesisEventRecord = mock(KinesisEvent.KinesisEventRecord.class);
    private KinesisEvent.Record mockedKinesisRecord = mock(KinesisEvent.Record.class);

    @Test
    public void handleRequestTest() throws Exception {
        byte[] byteArray = "This is a Kinesis test string".getBytes();
        ByteBuffer byteBuffer = ByteBuffer.wrap(byteArray);

        List<KinesisEvent.KinesisEventRecord> records = new ArrayList<>();
        records.add(mockedKinesisEventRecord);

        when(mockedKinesisEvent.getRecords()).thenReturn(records);
        when(mockedKinesisEventRecord.getKinesis()).thenReturn(mockedKinesisRecord);
        when(mockedKinesisRecord.getData()).thenReturn(byteBuffer);

        PowerMockito.mockStatic(MailerHelper.class);
        when(MailerHelper.sendMail(anyString(), anyString(), anyString())).thenReturn(true);

        assertNull(lambdaWithKinesis.handleRequest(mockedKinesisEvent, context));
    }

}
