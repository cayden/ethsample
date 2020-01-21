package com.cayden.ethereum.demo;

import com.cayden.ethereum.client.Web3JClient;
import com.cayden.ethereum.contract.Voting;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Contract;
import org.web3j.tx.Transfer;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by cuiran on 20/1/19.
 */
public class TestVoting {
    private static final BigInteger  GAS_PRICE=BigInteger.valueOf(30000000L);

    private static final BigInteger  GAS_LIMIT=BigInteger.valueOf(1000L);

    public static String hexToASCII(String hexValue)
    {
        StringBuilder output = new StringBuilder("");
        for (int i = 0; i < hexValue.length(); i += 2)
        {
            String str = hexValue.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }
        return output.toString();
    }

    public static Bytes32 stringToBytes32(String string) {
        byte[] byteValue = string.getBytes();
        byte[] byteValueLen32 = new byte[32];
        System.arraycopy(byteValue, 0, byteValueLen32, 0, byteValue.length);
        return new Bytes32(byteValueLen32);
    }
    //字符串到64位长的HexString
    // String to 64 length HexString (equivalent to 32 Hex lenght)
    public static String asciiToHex(String asciiValue)
    {
        char[] chars = asciiValue.toCharArray();
        StringBuffer hex = new StringBuffer();
        for (int i = 0; i < chars.length; i++)
        {
            hex.append(Integer.toHexString((int) chars[i]));
        }

        return hex.toString() + "".join("", Collections.nCopies(32 - (hex.length()/2), "00"));
    }
    //64位长的HexString到32位长的byte[]:
    public static byte[] stringToBytes(String string){
        return  Numeric.hexStringToByteArray(asciiToHex(string));
    }

    /**
     * deploy
     */
    private static void deploy(){
        try{
            Admin admin = Admin.build(new HttpService());
            Web3j web3j= Web3JClient.getClient();
            Web3ClientVersion web3ClientVersion = web3j.web3ClientVersion().send();
            String clientVersion = web3ClientVersion.getWeb3ClientVersion();
            BigInteger gp = BigInteger.valueOf(180l);
            BigInteger gl = BigInteger.valueOf(3000000l);
            System.out.println("clientVersion:"+ clientVersion);
            BigInteger gasPrice = web3j.ethGasPrice().sendAsync().get().getGasPrice();

            System.out.println("gasPrice:"+ gasPrice);
            EthCoinbase ethCoinbase= web3j.ethCoinbase().sendAsync().get();

            String firstAccount=ethCoinbase.getAddress();

            System.out.println("ethCoinbase:"+   firstAccount);

            //为用户创建凭证credentials
            String filePath = "E:\\eth\\data\\keystore";
            File file=new File(filePath);
            System.out.println("file:"+ file.isDirectory());
//            String fileName = WalletUtils.generateLightNewWalletFile("a12345678",new File(filePath));
            String fileName="UTC--2020-01-20T03-00-33.351009800Z--bc7fad24b6ec57730a4341a7bca4210a6871b6a3";
            System.out.println("file:"+filePath + "/" + fileName);
            Credentials credentials = WalletUtils.loadCredentials("123456", filePath + "/" + fileName);
            // get current balance for first account
//            EthGetBalance balance=admin.ethGetBalance(firstAccount, DefaultBlockParameterName.LATEST).send();
            EthGetBalance ethGetBalance = web3j.ethGetBalance(firstAccount, DefaultBlockParameterName.LATEST).send();

            BigDecimal balanceValue= Convert.fromWei(ethGetBalance.getBalance().toString(),Convert.Unit.ETHER);
            System.out.println("firstAccount:"+  firstAccount);
            System.out.println("balance:"+  ethGetBalance.getBalance());
            System.out.println("balanceValue:"+  balanceValue);

            EthGasPrice ethGasPrice=admin.ethGasPrice().send();

            System.out.println("gasPrice0:"+  ethGasPrice.getGasPrice());
            System.out.println("gasPrice1:"+  Convert.fromWei(ethGasPrice.getGasPrice().toString(),Convert.Unit.ETHER));
//            Credentials credentials = Credentials.create(firstAccount);
            System.out.println("address:"+  credentials.getAddress());
            List<byte[]> list=new ArrayList<>();
            list.add(stringToBytes("zhangsan"));
            list.add(stringToBytes("lisi"));

            //这样就可以创建一个凭证
            //0x0ec1a8054248dcd032a3267a9ff4a759f6add74e
            Voting voting= Voting.deploy(web3j,credentials,BigInteger.valueOf(200000), BigInteger.valueOf(2000000),list).send();
            System.out.println("contractAddress:"+voting.getContractAddress());

//            PersonalUnlockAccount personalUnlockAccount = admin.personalUnlockAccount ("0xbc7fad24b6ec57730a4341a7bca4210a6871b6a3", "123456").send();
//            if (personalUnlockAccount.accountUnlocked()) {
//                System.out.println("accountUnlocked success:");
//                // send a transaction
//                //这样就可以创建一个凭证
//                Voting voting= Voting.deploy(web3j,credentials,BigInteger.valueOf(200000), BigInteger.valueOf(20000000),list).sendAsync().get();
//                System.out.println(voting.getContractAddress());
//            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void voteForCandidate(){
        try{
            Web3j web3j= Web3JClient.getClient();
            Web3ClientVersion web3ClientVersion = web3j.web3ClientVersion().send();
            String clientVersion = web3ClientVersion.getWeb3ClientVersion();

            //为用户创建凭证credentials
            String filePath = "E:\\eth\\data\\keystore";
            File file=new File(filePath);
            System.out.println("file:"+ file.isDirectory());
//            String fileName = WalletUtils.generateLightNewWalletFile("a12345678",new File(filePath));
            String fileName="UTC--2020-01-20T03-00-33.351009800Z--bc7fad24b6ec57730a4341a7bca4210a6871b6a3";
            System.out.println("file:"+filePath + "/" + fileName);
            Credentials credentials = WalletUtils.loadCredentials("123456", filePath + "/" + fileName);
            Voting voting= Voting.load("0x0ec1a8054248dcd032a3267a9ff4a759f6add74e",web3j, credentials, BigInteger.valueOf(200000), BigInteger.valueOf(2000000));
          if(voting.isValid()){
              TransactionReceipt transactionReceipt= voting.totalVotesFor(stringToBytes("zhangsan")).sendAsync().get();

              System.out.println(transactionReceipt);
          }


        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void main(String [] args){

        TestVoting.voteForCandidate();
    }
}
