package cn.kk20.chat.api.service;

import cn.kk20.chat.api.entity.vo.UserVo;
import cn.kk20.chat.dao.model.UserModel;

import java.util.List;

/**
 * @Description:
 * @Author: Roy
 * @Date: 2019-01-29 16:53
 * @Version: v1.0
 */
public interface UserService {
    int delete(Long id);

    int update(UserModel model);

    UserModel find(Long id);

    List<UserModel> selectAll();

    UserModel register(UserModel model) throws Exception;

    UserModel login(String name, String password) throws Exception;

    List<UserVo> search(String key);

    List<UserVo> getFriendList(Long userId);

}
