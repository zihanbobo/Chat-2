package cn.kk20.chat.mapper;

import cn.kk20.chat.model.MessageModel;
import java.util.List;

public interface MessageModelMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table message
     *
     * @mbg.generated Wed Jan 30 10:48:00 CST 2019
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table message
     *
     * @mbg.generated Wed Jan 30 10:48:00 CST 2019
     */
    int insert(MessageModel record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table message
     *
     * @mbg.generated Wed Jan 30 10:48:00 CST 2019
     */
    MessageModel selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table message
     *
     * @mbg.generated Wed Jan 30 10:48:00 CST 2019
     */
    List<MessageModel> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table message
     *
     * @mbg.generated Wed Jan 30 10:48:00 CST 2019
     */
    int updateByPrimaryKey(MessageModel record);
}