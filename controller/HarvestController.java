package com.coopagri.controller;

import com.coopagri.model.Harvest;
import com.coopagri.model.dto.HarvestRequest;
import com.coopagri.service.HarvestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
