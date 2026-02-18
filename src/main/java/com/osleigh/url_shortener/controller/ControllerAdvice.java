package com.osleigh.url_shortener.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

  private static final Logger logger = LoggerFactory.getLogger(ControllerAdvice.class);

  @ExceptionHandler(IllegalArgumentException.class)
  public String handleException(IllegalArgumentException e) {
    logger.error(e.getMessage(), e);
    ProblemDetail problemDetail = ProblemDetail.forStatus(400);

    problemDetail.setTitle("Bad Request");
    problemDetail.setDetail(e.getMessage());

    return problemDetail.toString();
  }

  @ExceptionHandler(Exception.class)
  public String handleException(Exception e) {
    logger.error(e.getMessage(), e);
    ProblemDetail problemDetail = ProblemDetail.forStatus(500);

    problemDetail.setTitle("Internal Server Error");
    problemDetail.setDetail(e.getMessage());

    return problemDetail.toString();
  }
}
