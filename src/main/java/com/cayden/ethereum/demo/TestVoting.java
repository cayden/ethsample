package com.cayden.ethereum.demo;

import com.cayden.ethereum.client.Web3JClient;
import com.cayden.ethereum.contract.Voting;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.tx.Contract;
import org.web3j.tx.gas.ContractGasProvider;

import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cuiran on 20/1/19.
 */
public class TestVoting {
    private static final int  GAS_PRICE=10;

    private static final int  GAS_LIMIT=10;



    public static void main(String [] args){
        try{
            Web3j web3j= Web3JClient.getClient();
            Web3ClientVersion web3ClientVersion = web3j.web3ClientVersion().send();
            String clientVersion = web3ClientVersion.getWeb3ClientVersion();

            System.out.println("clientVersion:"+ clientVersion);
            //为用户创建凭证credentials
            String filePath = "/Users/cuiran/ethereum/web3j/wallet";
            File file=new File(filePath);
            System.out.println("file:"+ file.isDirectory());
            String fileName = WalletUtils.generateLightNewWalletFile("a12345678",new File(filePath));

            System.out.println("file:"+filePath + "/" + fileName);
            Credentials credentials = WalletUtils.loadCredentials("a12345678", filePath + "/" + fileName);

            List<byte[]> list=new ArrayList<>();
            list.add("zhangsan".getBytes());
            list.add("lisi".getBytes());

            //这样就可以创建一个凭证
            Voting  voting=Voting.deploy(web3j,credentials,Contract.GAS_PRICE, Contract.GAS_LIMIT,list).send();
            System.out.println(voting.getContractAddress());
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
