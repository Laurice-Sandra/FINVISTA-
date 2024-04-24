package esprit.tn.flexifin.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import esprit.tn.flexifin.entities.FlexiFin;
import esprit.tn.flexifin.entities.MessageResponse;
import esprit.tn.flexifin.entities.UserLoginRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper mapper;
    private final RestTemplate restTemplate;

    @Value("${chatbot.url}")
    private String chatbotUrl;

    public ChatWebSocketHandler(ObjectMapper mapper, RestTemplate restTemplate) {
        this.mapper = mapper;
        this.restTemplate = restTemplate;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
        log.info("Payload: " + message.getPayload());
        var messageDto = mapper.readValue(message.getPayload(), UserLoginRequest.class);

        if (StringUtils.isBlank(messageDto.getMessage())) return;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserLoginRequest> request = new HttpEntity<>(messageDto, headers);

        ResponseEntity<MessageResponse> responseEntity;
        try {
            responseEntity = restTemplate.postForEntity(chatbotUrl, request, MessageResponse.class);
        } catch (Exception e) {
            log.info("Session ID: " + session.getId());
            log.error("Happened error", e);
            session.sendMessage(new TextMessage(mapper.writeValueAsString(UserLoginRequest.builder().message("Sorry, the bot is not available at this time.").build())));
            return;
        }

        var messageResponse = responseEntity.getBody();

        if (Objects.requireNonNull(messageResponse).getType().equalsIgnoreCase("riddles")) {
            var dto = mapper.convertValue(messageResponse.getData(), FlexiFin.class);
            log.info("RiddlesDto:: " + dto);
            var question = new TextMessage(mapper.writeValueAsString(UserLoginRequest.builder().message(dto.getQuestion()).build()));
            var answer = new TextMessage(mapper.writeValueAsString(UserLoginRequest.builder().message(dto.getAnswer()).build()));
            session.sendMessage(question);
            TimeUnit.SECONDS.sleep(1);
            session.sendMessage(answer);
        } else {
            var response = new TextMessage(mapper.writeValueAsString(messageResponse.getData()));
            log.info("Response: " + response.getPayload());
            session.sendMessage(response);
        }
    }
}
