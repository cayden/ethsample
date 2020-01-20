package com.cayden.ethereum.client;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

/**
 * http://127.0.0.1:8545/
 * Created by cuiran on 18/7/6.
 */
public class Web3JClient {

    private static String ip = "http://127.0.0.1:8545/";
    private Web3JClient(){}
    private volatile static Web3j web3j;
    public static Web3j getClient(){
        if(web3j==null){
            synchronized (Web3JClient.class){
                if(web3j==null){
                    web3j = Web3j.build(new HttpService(ip));
                }
            }
        }
        return web3j;
    }
}
