package com.project.lovable.dto.chat;

import com.project.lovable.entity.ChatEvent;
import com.project.lovable.entity.ChatSession;
import com.project.lovable.enums.MessageRole;

import java.time.Instant;
import java.util.List;

public record ChatResponse(
        Long id,
        MessageRole role,
        List<ChatEventResponse> events,
        String content,
        Integer tokensUsed,
        Instant createdAt
) {
}
