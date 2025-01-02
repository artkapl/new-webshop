package com.artkapl.new_webshop.controller;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import org.springframework.http.ResponseEntity;

import com.artkapl.response.ApiResponse;

public class ControllerTools {

    public static ResponseEntity<ApiResponse> getInternalErrorResponse(Exception e) {
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), INTERNAL_SERVER_ERROR));
    }

}
