package com.three.recipingrecipeservicebe.recommendation.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class ChatGptResponseDto {
    private List<Choice> choices;

    @Getter
    public static class Choice {
        private Message message;

        @Getter
        public static class Message {
            private String content;
        }
    }
}
