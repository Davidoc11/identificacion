package com.example.demo.model;

import java.awt.TrayIcon.MessageType;

/**
 * @author David
 */
public class ErrorMessage {
private String message;
  private MessageType type;
  
  public ErrorMessage() {
    super();
  }
  
  public ErrorMessage(MessageType type, String message) {
    super();
    this.message = message;
    this.type = type;
  }

  public String getMessage() {
    return message;
  }
  
  public void setMessage(String message) {
    this.message = message;
  }
  
  public MessageType getType() {
    return type;
  }
  
  public void setType(MessageType type) {
    this.type = type;
  }
}
