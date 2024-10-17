package com.f4.fqs.queue_manage.presentation.controller;

import com.f4.fqs.commons.domain.exception.BusinessException;
import com.f4.fqs.commons.domain.response.ResponseBody;
import com.f4.fqs.queue_manage.application.response.CloseQueueResponse;
import com.f4.fqs.queue_manage.application.response.CreateQueueResponse;
import com.f4.fqs.queue_manage.application.response.UpdateQueueExpirationTimeResponse;
import com.f4.fqs.queue_manage.application.response.QueueInfo;
import com.f4.fqs.queue_manage.application.service.QueueService;
import com.f4.fqs.queue_manage.infrastructure.aop.AuthorizationRequired;
import com.f4.fqs.queue_manage.presentation.exception.QueueErrorCode;
import com.f4.fqs.queue_manage.presentation.request.CreateQueueRequest;
import com.f4.fqs.queue_manage.presentation.request.UpdateExpirationTimeRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.f4.fqs.commons.domain.response.ResponseUtil.createSuccessResponse;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/queue")
public class QueueController {

    private final QueueService queueService;

    @AuthorizationRequired({"ROLE_ROOT"}) // TODO. ROOT 권한의 이름으로 대체
    @PostMapping
    public ResponseEntity<ResponseBody<CreateQueueResponse>> createQueue(
            @RequestBody @Valid CreateQueueRequest request,
            @RequestHeader("X-User-Id") Long userId,
            HttpServletRequest httpRequest) {

        return ResponseEntity.ok(createSuccessResponse(queueService.createQueue(request, userId)));
    }

    @AuthorizationRequired({"ROLE_ROOT"})
    @GetMapping
    public ResponseEntity<ResponseBody<List<QueueInfo>>> getQueueInfoByUserId(
            @RequestHeader("X-User-Id") Long userId,
            HttpServletRequest httpRequest) {

        return ResponseEntity.ok(createSuccessResponse(queueService.getQueueInfoByUserId(userId)));
    }

    @AuthorizationRequired({"ROLE_ROOT"})
    @PatchMapping("/{queueId}/expiration-time")
    public ResponseEntity<ResponseBody<UpdateQueueExpirationTimeResponse>> updateExpirationTime(
            @PathVariable Long queueId,
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody @Valid UpdateExpirationTimeRequest request,
            HttpServletRequest httpRequest) {

        return ResponseEntity.ok(createSuccessResponse(queueService.updateExpirationTime(queueId, userId, request)));
    }

    @AuthorizationRequired({"ROLE_ROOT"})
    @PatchMapping("/{queueId}/close")
    public ResponseEntity<ResponseBody<CloseQueueResponse>> closeQueue(
            @PathVariable Long queueId,
            @RequestHeader("X-User-Id") Long userId) {

        return ResponseEntity.ok(createSuccessResponse(queueService.closeQueue(queueId, userId)));
    }

    @GetMapping("/validate")
    public ResponseEntity<Boolean> validateSecretKeyAndQueueName(
            @RequestHeader("X-Secret-Key") String secretKey,
            @RequestParam String queueName) {
        return ResponseEntity.ok(queueService.validateSecretKeyAndQueueName(secretKey, queueName));
    }
}
