package com.knoldus.aws.lambdawiths3;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.event.S3EventNotification;
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
public class LambdaWithS3Test {

    private LambdaWithS3 lambdaWithS3 = new LambdaWithS3();

    private S3EventNotification.S3Entity mockedS3Entity = mock(S3EventNotification.S3Entity.class);
    private S3Event mockedS3Event = mock(S3Event.class);

    private S3EventNotification.S3EventNotificationRecord mockedS3EventNotificationRecord =
            mock(S3EventNotification.S3EventNotificationRecord.class);
    private S3EventNotification.S3ObjectEntity mockedS3ObjectEntity =
            mock(S3EventNotification.S3ObjectEntity.class);

    private Context context = null;

    @Test
    public void handleRequestTest() throws Exception {
        List<S3EventNotification.S3EventNotificationRecord> records = new ArrayList<>();
        records.add(mockedS3EventNotificationRecord);

        when(mockedS3Event.getRecords()).thenReturn(records);
        when(mockedS3EventNotificationRecord.getS3()).thenReturn(mockedS3Entity);

        when(mockedS3Entity.getObject()).thenReturn(mockedS3ObjectEntity);
        when(mockedS3ObjectEntity.getKey()).thenReturn("key-name");

        PowerMockito.mockStatic(MailerHelper.class);
        when(MailerHelper.sendMail(anyString(), anyString(), anyString())).thenReturn(true);

        assertNull(lambdaWithS3.handleRequest(mockedS3Event, context));
    }

}
