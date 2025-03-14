/**
 * 
 */
package com.dorado.push.enhander;

import java.util.List;

import org.springframework.util.CollectionUtils;

import com.dorado.push.event.IPushMessage;

/**
 * @author Lookis (lucas@diandian.com)
 * 
 */
public class MessageEnhancerChain implements MessageEnhancer {

    private List<MessageEnhancer> enhancerChain;

    public MessageEnhancerChain() {
    }

    @Override
    public void enhance(IPushMessage message) {
        if (CollectionUtils.isEmpty(enhancerChain)) {
            return;
        }
        for (MessageEnhancer enhancer : enhancerChain) {
            enhancer.enhance(message);
        }
    }

    public void setEnhancers(List<MessageEnhancer> enhancers) {
        this.enhancerChain = enhancers;
    }
}
