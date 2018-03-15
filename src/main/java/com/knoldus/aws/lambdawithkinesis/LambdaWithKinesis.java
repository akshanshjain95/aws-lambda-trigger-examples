package com.knoldus.aws.lambdawithkinesis;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.KinesisEvent;
import com.knoldus.aws.utils.confighelper.ConfigReader;
import com.knoldus.aws.utils.loggerhelper.LoggerFactory;
import com.knoldus.aws.utils.loggerhelper.LoggerService;
import com.knoldus.aws.utils.mailhelper.MailerHelper;

import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;

public class LambdaWithKinesis implements RequestHandler<KinesisEvent, Void> {

	private static final LoggerService LOGGER = LoggerFactory.getLogService(LambdaWithKinesis.class.getName());
	private static final ConfigReader configReader = ConfigReader.getConfigReader("mail");

	/**
	 * Handler function for handling Kinesis Events generated by an action on kinesis data streams
	 * linked with this lambda function. This lambda function needs CloudWatch permissions as it
	 * takes the records through <code>{@link KinesisEvent}</code> triggering this lambda,
	 * constructs a {@code String} and logs it on CloudWatch.
	 *
	 * @param kinesisEvent Event that triggers this lambda function.
	 * @param context Used to access lambda environment information.
	 * @return Void
	 */
	@Override
	public Void handleRequest(KinesisEvent kinesisEvent, Context context) {
		List<KinesisEvent.KinesisEventRecord> kinesisEventRecords = kinesisEvent.getRecords();

		String finalString =
				kinesisEventRecords.stream()
						.map(record -> new String(record.getKinesis().getData().array(), Charset.forName("UTF-8")))
						.collect(Collectors.joining("\n"));

		String to = configReader.getProperty("to");
		String subject = "Lambda triggered due to Kinesis";

		String body = String.join(
				System.getProperty("line.separator"),
				"<h2>Your lambda was triggered due to Kinesis</h2>",
				"<p>Kinesis Data -",
				"<p>" + finalString + "</p>"
		);

		try {
			MailerHelper.sendMail(to, subject, body);
			LOGGER.info("Kinesis Data --> " + finalString);
		} catch (Exception ex) {
			LOGGER.info(ex.getMessage());
		}

		return null;
	}
}