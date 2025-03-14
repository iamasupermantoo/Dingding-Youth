package com.dorado.push.enhander;

import com.dorado.push.event.IPushMessage;


public interface MessageEnhancer {
    public void enhance(IPushMessage message);
}
