package com.websecurity.service;

import com.websecurity.pojo.User;

import java.util.List;

public interface QueueService {

    public <T> List<T> queueUser(Class<T> clazz, String sql, Object... args);
}
