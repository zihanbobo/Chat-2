package cn.roy.demo.util;

import androidx.collection.LruCache;

import java.util.ArrayList;
import java.util.List;

import cn.kk20.chat.base.message.ChatMessage;
import cn.kk20.chat.base.message.chat.ChatMessageType;
import cn.roy.demo.model.User;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * @Description:
 * @Author: Roy
 * @Date: 2019/2/13 13:53
 * @Version: v1.0
 */
public class CacheManager {
    private static CacheManager instance = null;

    private CacheManager() {
        // 最多缓存50个聊天记录
        messageCache = new LruCache<>(50);
        groupMessageCache = new LruCache<>(50);

        observableEmitterList = new ArrayList<>();
        observable = Observable.create(new ObservableOnSubscribe<ChatMessage>() {
            @Override
            public void subscribe(ObservableEmitter<ChatMessage> emitter) throws Exception {
                observableEmitterList.add(emitter);
            }
        });
    }

    public static CacheManager getInstance() {
        if (instance == null) {
            synchronized (CacheManager.class) {
                if (instance == null) {
                    instance = new CacheManager();
                }
            }
        }
        return instance;
    }

    /**********功能：缓存用户信息**********/
    private User currentUser;
    private Long currentUserId;
    private String currentUserName;

    public void cacheCurrentUser(User currentUser) {
        this.currentUser = currentUser;
        this.currentUserId = currentUser.getId();
        this.currentUserName = currentUser.getName();
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public Long getCurrentUserId() {
        return currentUserId;
    }

    public String getCurrentUserName() {
        return currentUserName;
    }

    /**********功能：缓存聊天信息**********/
    private LruCache<Long, List<ChatMessage>> messageCache;
    private LruCache<Long, List<ChatMessage>> groupMessageCache;

    public void cacheMessage(ChatMessage message) {
        Long fromUserId = message.getFromUserId();
        Long toUserId = message.getToUserId();
        Long cacheKey;
        List<ChatMessage> chatMessages;
        if (message.getChatMessageType() == ChatMessageType.GROUP) {
            // 群消息
            cacheKey = toUserId;
            chatMessages = groupMessageCache.get(cacheKey);
        } else {
            cacheKey = currentUserId == fromUserId ? toUserId : fromUserId;
            chatMessages = messageCache.get(cacheKey);
        }
        if (chatMessages == null) {
            chatMessages = new ArrayList<>();
            if (message.getChatMessageType() == ChatMessageType.GROUP) {
                groupMessageCache.put(cacheKey, chatMessages);
            } else {
                messageCache.put(cacheKey, chatMessages);
            }
        }
        chatMessages.add(message);
        LogUtil.d(CacheManager.this, "消息条数：" + chatMessages.size());
        if (observableEmitterList.size() == 0) {
            return;
        }
        List<ObservableEmitter> disposedList = null;
        for (ObservableEmitter emitter : observableEmitterList) {
            if (emitter.isDisposed()) {
                if (disposedList == null) {
                    disposedList = new ArrayList<>();
                }
                disposedList.add(emitter);
                continue;
            }
            emitter.onNext(message);
        }
        if (!disposedList.isEmpty()) {
            observableEmitterList.removeAll(disposedList);
        }
    }

    public List<ChatMessage> getCacheMessageList(ChatMessageType type, Long chatUserId) {
        List<ChatMessage> chatMessages;
        if (type == ChatMessageType.GROUP) {
            chatMessages = groupMessageCache.get(chatUserId);
        } else {
            chatMessages = messageCache.get(chatUserId);
        }
        if (chatMessages == null) {
            chatMessages = new ArrayList<>();
            if (type == ChatMessageType.GROUP) {
                groupMessageCache.put(chatUserId, chatMessages);
            } else {
                messageCache.put(chatUserId, chatMessages);
            }
        }
        return chatMessages;
    }

    /**********功能：观察者**********/
    private Observable<ChatMessage> observable;
    private List<ObservableEmitter<ChatMessage>> observableEmitterList;

    public Observable<ChatMessage> getObservable() {
        return observable;
    }

}
