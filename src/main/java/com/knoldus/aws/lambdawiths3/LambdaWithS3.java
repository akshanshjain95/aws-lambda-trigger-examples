package com.knoldus.aws.lambdawiths3;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.event.S3EventNotification;
import com.knoldus.aws.lambdawithkinesis.LambdaWithKinesis;
import com.knoldus.aws.utils.confighelper.ConfigReader;
import com.knoldus.aws.utils.loggerhelper.LoggerFactory;
import com.knoldus.aws.utils.loggerhelper.LoggerService;
import com.knoldus.aws.utils.mailhelper.MailerHelper;

import java.util.List;
import java.util.stream.Collectors;

public class LambdaWithS3 implements RequestHandler<S3Event, Void> {

    private static final LoggerService LOGGER = LoggerFactory.getLogService(LambdaWithKinesis.class.getName());
    private static final ConfigReader configReader = ConfigReader.getConfigReader("mail");

	/**
	 * Handler function for handling S3 Events generated by an action on S3 bucket linked
	 * with this lambda function. This lambda function needs CloudWatch permissions as it
	 * takes the records through <code>{@link S3Event}</code> triggering this lambda,
	 * constructs a {@code String} and logs it on CloudWatch.
	 *
	 * @param s3Event Event that triggers this lambda function.
	 * @param context Used to access lambda environment information.
	 * @return Void
	 */
	@Override
	public Void handleRequest(S3Event s3Event, Context context) {
		List<S3EventNotification.S3EventNotificationRecord> records = s3Event.getRecords();

		String resultString =
				records.stream()
						.map(record -> "Filename: " + record.getS3().getObject().getKey())
						.collect(Collectors.joining("\n"));

        String to = configReader.getProperty("to");
		String subject = "Lambda triggered due to S3";

        String body = String.join(
                System.getProperty("line.separator"),
                "<h2>Your lambda was triggered due to S3</h2>",
                "<p>The records in S3 -",
                "<p>" + resultString + "</p>"
        );

		try {
			MailerHelper.sendMail(to, subject, body);
            LOGGER.info("Records in S3 --> " + resultString);
		} catch (Exception ex) {
			LOGGER.info(ex.getMessage());
		}

		return null;
	}
}
