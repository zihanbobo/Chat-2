package cn.kk20.chat.core.main.client.handler.heartbeat;

import cn.kk20.chat.base.message.HeartbeatMessage;
import cn.kk20.chat.base.message.NotifyMessage;
import cn.kk20.chat.core.common.ConstantValue;
import cn.kk20.chat.core.main.ClientComponent;
import cn.kk20.chat.core.main.client.MessageSender;
import cn.kk20.chat.core.main.client.UserChannelManager;
import cn.kk20.chat.core.util.RedisUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Description: 读心跳处理器
 * @Author: Roy
 * @Date: 2019/1/21 15:31
 * @Version: v1.0
 */
@ClientComponent
@ChannelHandler.Sharable
public class ReadHeartbeatMessageHandler extends SimpleChannelInboundHandler<HeartbeatMessage> {
    private Logger logger = LoggerFactory.getLogger(ReadHeartbeatMessageHandler.class);
    private final int heartFailMax = 5;
    private volatile int heartFailCount = 0;

    @Autowired
    UserChannelManager userChannelManager;
    @Autowired
    MessageSender messageSender;
    @Autowired
    RedisUtil redisUtil;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HeartbeatMessage heartbeatMessage) throws Exception {
        // 收到任何消息，均把心跳失败数置零
        heartFailCount = 0;
        // 原样返回心跳信息
        messageSender.sendMessage(ctx.channel(), heartbeatMessage);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        boolean hasDeal = false;
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                heartFailCount++;
                logger.debug("客户端：{}，心跳消息读失败：{}次", ctx.channel().remoteAddress(), heartFailCount);
                hasDeal = true;
                if (heartFailCount > heartFailMax) {
                    heartFailCount = 0;
                    heartbeatFail(ctx);
                }
            }
        }

        if (!hasDeal) {
            super.userEventTriggered(ctx, evt);
        }
    }

    private void heartbeatFail(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        logger.debug("客户端：{}，读超时，关闭通道", channel.remoteAddress());
        Long userId = userChannelManager.getUserId(channel.remoteAddress());
        userChannelManager.removeClient(userId);
        // 通知好友，该用户下线了
        notifyFriend(userId);
        // 关闭通道
        ctx.close();
    }

    private void notifyFriend(Long userId) {
        if (null == userId) {
            return;
        }
        // 查询好友列表
        Set<Long> userIdSet = redisUtil.getLongSetValue(ConstantValue.FRIEND_OF_USER + userId);
        if (CollectionUtils.isEmpty(userIdSet)) {
            return;
        }
        Map<Long, String> onlineFriendMap = new HashMap<>(userIdSet.size());
        // 查询在线好友
        for (Long id : userIdSet) {
            String host = redisUtil.getStringValue(ConstantValue.HOST_OF_USER + id);
            if (!StringUtils.isEmpty(host)) {
                onlineFriendMap.put(id, host);
            }
        }
        // 通知在线好友，用户掉线了
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", userId);
        map.put("login", false);
        NotifyMessage notifyMessage = new NotifyMessage();
        for (Long friendId : onlineFriendMap.keySet()) {
            messageSender.sendMessage(friendId, notifyMessage);
        }
    }

}