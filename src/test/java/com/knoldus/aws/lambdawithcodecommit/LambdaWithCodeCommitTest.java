package com.knoldus.aws.lambdawithcodecommit;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.CodeCommitEvent;
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
public class LambdaWithCodeCommitTest {

    private LambdaWithCodeCommit lambdaWithCodeCommit = new LambdaWithCodeCommit();
    private CodeCommitEvent mockedCodeCommitEvent = mock(CodeCommitEvent.class);
    private CodeCommitEvent.Record mockedRecord = mock(CodeCommitEvent.Record.class);

    private Context context = null;

    @Test
    public void handleRequestTest() throws Exception {
        List<CodeCommitEvent.Record> records = new ArrayList<>();
        records.add(mockedRecord);

        when(mockedCodeCommitEvent.getRecords()).thenReturn(records);

        PowerMockito.mockStatic(MailerHelper.class);
        when(MailerHelper.sendMail(anyString(), anyString(), anyString())).thenReturn(true);

        assertNull(lambdaWithCodeCommit.handleRequest(mockedCodeCommitEvent, context));
    }

}
