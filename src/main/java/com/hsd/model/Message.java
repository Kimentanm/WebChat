package com.hsd.model;

import java.time.LocalDateTime;
import java.util.Date;
import javax.persistence.*;

@Table(name = "hsd_message")
public class Message {
    /**
     * 自增长的Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 信息的发送方
     */
    private Long sender;

    @Transient
    private String senderName;

    /**
     * 信息的接收方
     */
    private Long receiver;

    @Transient
    private String receiverName;

    /**
     * 消息发送的时间
     */
    private LocalDateTime time;

    /**
     * 三种状态UNREAD、READ、DELETE
     */
    private String status;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "last_modified_by")
    private Long lastModifiedBy;

    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;

    private Integer version;

    private String handleResult;

    /**
     * 消息的内容
     */
    private String content;

    /**
     * 获取自增长的Id
     *
     * @return id - 自增长的Id
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置自增长的Id
     *
     * @param id 自增长的Id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取信息的发送方
     *
     * @return sender - 信息的发送方
     */
    public Long getSender() {
        return sender;
    }

    /**
     * 设置信息的发送方
     *
     * @param sender 信息的发送方
     */
    public void setSender(Long sender) {
        this.sender = sender;
    }

    /**
     * 获取信息的接收方
     *
     * @return receiver - 信息的接收方
     */
    public Long getReceiver() {
        return receiver;
    }

    /**
     * 设置信息的接收方
     *
     * @param receiver 信息的接收方
     */
    public void setReceiver(Long receiver) {
        this.receiver = receiver;
    }

    /**
     * 获取消息发送的时间
     *
     * @return time - 消息发送的时间
     */
    public LocalDateTime getTime() {
        return time;
    }

    /**
     * 设置消息发送的时间
     *
     * @param time 消息发送的时间
     */
    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    /**
     * 获取三种状态UNREAD、READ、DELETE
     *
     * @return status - 三种状态UNREAD、READ、DELETE
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置三种状态UNREAD、READ、DELETE
     *
     * @param status 三种状态UNREAD、READ、DELETE
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return created_by
     */
    public Long getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy
     */
    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * @return created_date
     */
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    /**
     * @param createdDate
     */
    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * @return last_modified_by
     */
    public Long getLastModifiedBy() {
        return lastModifiedBy;
    }

    /**
     * @param lastModifiedBy
     */
    public void setLastModifiedBy(Long lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    /**
     * @return last_modified_date
     */
    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    /**
     * @param lastModifiedDate
     */
    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    /**
     * @return version
     */
    public Integer getVersion() {
        return version;
    }

    /**
     * @param version
     */
    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     * 获取消息的内容
     *
     * @return content - 消息的内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置消息的内容
     *
     * @param content 消息的内容
     */
    public void setContent(String content) {
        this.content = content;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getHandleResult() {
        return handleResult;
    }

    public void setHandleResult(String handleResult) {
        this.handleResult = handleResult;
    }
}