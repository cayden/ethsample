package com.cayden.ethereum.client;

import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.parity.Parity;

/**
 * Created by cuiran on 18/7/6.
 */
public class ParityClient {
    private static String ip = "http://127.0.0.1:8545/";
    private ParityClient(){}
    private static class ClientHolder{
        private static final Parity parity = Parity.build(new HttpService(ip));
    }
    public static final Parity getParity(){
        return ClientHolder.parity;
    }
}
