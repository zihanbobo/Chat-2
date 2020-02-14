package cn.kk20.chat.model;

import java.util.Date;

public class MessageModel {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column message.id
     *
     * @mbg.generated Wed Jan 30 10:48:00 CST 2019
     */
    private String id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column message.from_user_id
     *
     * @mbg.generated Wed Jan 30 10:48:00 CST 2019
     */
    private String fromUserId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column message.to_user_id
     *
     * @mbg.generated Wed Jan 30 10:48:00 CST 2019
     */
    private String toUserId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column message.content
     *
     * @mbg.generated Wed Jan 30 10:48:00 CST 2019
     */
    private String content;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column message.send_time
     *
     * @mbg.generated Wed Jan 30 10:48:00 CST 2019
     */
    private Date sendTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column message.status
     *
     * @mbg.generated Wed Jan 30 10:48:00 CST 2019
     */
    private Byte status;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column message.from_user_delete
     *
     * @mbg.generated Wed Jan 30 10:48:00 CST 2019
     */
    private Boolean fromUserDelete;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column message.to_user_delete
     *
     * @mbg.generated Wed Jan 30 10:48:00 CST 2019
     */
    private Boolean toUserDelete;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column message.id
     *
     * @return the value of message.id
     *
     * @mbg.generated Wed Jan 30 10:48:00 CST 2019
     */
    public String getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column message.id
     *
     * @param id the value for message.id
     *
     * @mbg.generated Wed Jan 30 10:48:00 CST 2019
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column message.from_user_id
     *
     * @return the value of message.from_user_id
     *
     * @mbg.generated Wed Jan 30 10:48:00 CST 2019
     */
    public String getFromUserId() {
        return fromUserId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column message.from_user_id
     *
     * @param fromUserId the value for message.from_user_id
     *
     * @mbg.generated Wed Jan 30 10:48:00 CST 2019
     */
    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId == null ? null : fromUserId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column message.to_user_id
     *
     * @return the value of message.to_user_id
     *
     * @mbg.generated Wed Jan 30 10:48:00 CST 2019
     */
    public String getToUserId() {
        return toUserId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column message.to_user_id
     *
     * @param toUserId the value for message.to_user_id
     *
     * @mbg.generated Wed Jan 30 10:48:00 CST 2019
     */
    public void setToUserId(String toUserId) {
        this.toUserId = toUserId == null ? null : toUserId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column message.content
     *
     * @return the value of message.content
     *
     * @mbg.generated Wed Jan 30 10:48:00 CST 2019
     */
    public String getContent() {
        return content;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column message.content
     *
     * @param content the value for message.content
     *
     * @mbg.generated Wed Jan 30 10:48:00 CST 2019
     */
    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column message.send_time
     *
     * @return the value of message.send_time
     *
     * @mbg.generated Wed Jan 30 10:48:00 CST 2019
     */
    public Date getSendTime() {
        return sendTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column message.send_time
     *
     * @param sendTime the value for message.send_time
     *
     * @mbg.generated Wed Jan 30 10:48:00 CST 2019
     */
    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column message.status
     *
     * @return the value of message.status
     *
     * @mbg.generated Wed Jan 30 10:48:00 CST 2019
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column message.status
     *
     * @param status the value for message.status
     *
     * @mbg.generated Wed Jan 30 10:48:00 CST 2019
     */
    public void setStatus(Byte status) {
        this.status = status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column message.from_user_delete
     *
     * @return the value of message.from_user_delete
     *
     * @mbg.generated Wed Jan 30 10:48:00 CST 2019
     */
    public Boolean getFromUserDelete() {
        return fromUserDelete;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column message.from_user_delete
     *
     * @param fromUserDelete the value for message.from_user_delete
     *
     * @mbg.generated Wed Jan 30 10:48:00 CST 2019
     */
    public void setFromUserDelete(Boolean fromUserDelete) {
        this.fromUserDelete = fromUserDelete;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column message.to_user_delete
     *
     * @return the value of message.to_user_delete
     *
     * @mbg.generated Wed Jan 30 10:48:00 CST 2019
     */
    public Boolean getToUserDelete() {
        return toUserDelete;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column message.to_user_delete
     *
     * @param toUserDelete the value for message.to_user_delete
     *
     * @mbg.generated Wed Jan 30 10:48:00 CST 2019
     */
    public void setToUserDelete(Boolean toUserDelete) {
        this.toUserDelete = toUserDelete;
    }
}