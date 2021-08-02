package it.unipd.dei.webapp.auth;

import it.unipd.dei.webapp.resource.Costumer;
import org.apache.commons.collections4.map.PassiveExpiringMap;

import java.util.HashMap;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Token cache, a cache for user tokens
 *
 * @author eTrolley group
 * @version 1.00
 * @since 1.00
 */

public class TokenCache {
    private PassiveExpiringMap<String, Costumer> map;

    /**
     * Constructor
     * @param minutes how long to maintain tokens in the cache in minutes
     */
    public TokenCache(long minutes){
        map = new PassiveExpiringMap<>(minutes, TimeUnit.MINUTES);
    }

    /**
     * Returns a Costumer object associated to the given token.
     * if the user does not exists return null
     * @param token the token string
     * @return the Costumer
     */
    public synchronized Costumer getCostumer(String token){
        return map.get(token);
    }

    /**
     * Adds a costumer to the token cache
     * @param token
     * @param costumer
     */
    public synchronized void addCostumer(String token, Costumer costumer){
        map.put(token, costumer);
    }

    /**
     * Removes the Costumer associated to the given token and returns it
     * if the user does not exists returns null
     * @param token
     * @return
     */
    public synchronized Costumer removeCostumer(String token){
        return map.remove(token);
    }
}
