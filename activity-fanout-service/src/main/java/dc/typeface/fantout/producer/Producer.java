package dc.typeface.fantout.producer;

import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import dc.typeface.fantout.models.ActivityMessage;
import lombok.extern.log4j.Log4j2;

@Service
@EnableScheduling
@Log4j2
public class Producer {

    private static final Random RANDOM = new Random(System.currentTimeMillis());

    @Autowired
    private JmsTemplate jmsTemplate;

    @Value("${queue-name}")
    private String queueName;

    @Value("${user-count:3}")
    private int userCount;

    @Value("${fant-out-percentage:1}")
    private int fanOutRatio;

    @Scheduled(fixedRateString = "${activity_rate_in_millis:10000}")
    public void submitActivities() {
        log.info("Putting message inside the queue");
        int usersToFanOut = Math.max(RANDOM.nextInt(userCount) / fanOutRatio, 1);
        jmsTemplate.convertAndSend(queueName,
                new ActivityMessage(getRandomUsers(usersToFanOut),
                        "Welcome to workspace alpha",
                        Instant.now().toEpochMilli()));
    }

    private List<String> getRandomUsers(int count) {

        return IntStream.range(0, userCount)
                .map(i -> RANDOM.nextInt(count + 1))
                .boxed()
                .distinct()
                .map(i -> "user-" + i)
                .collect(Collectors.toList());
    }

}
