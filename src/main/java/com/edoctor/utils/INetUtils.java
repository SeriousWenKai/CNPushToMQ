package com.edoctor.utils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class INetUtils {

    /**
     * 获取服务器IP地址
     *
     * @return
     */
    public static List<String> getLocalhostIP(){
        List<String> localIPList = new ArrayList<>(16);
        try {
            Enumeration<?> enumeration=NetworkInterface.getNetworkInterfaces();
            InetAddress ip=null;
            while(enumeration.hasMoreElements()){
                NetworkInterface netInterface = (NetworkInterface) enumeration.nextElement();
                Enumeration<?> addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    ip = (InetAddress) addresses.nextElement();
                    if (ip != null && ip instanceof Inet4Address){
                        localIPList.add(ip.getHostAddress());
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return localIPList;
    }

    public static List<String> getMatchPatternIP(List<String> ipAddresses, String patternString) {
        List<String> result = new ArrayList<>(16);
        Pattern pattern = Pattern.compile(patternString);
        for(String ipAddress : ipAddresses) {
            Matcher matcher = pattern.matcher(ipAddress);
            while(matcher.find()) {
                System.out.println(matcher.group());
                result.add(matcher.group());
            }
        }
        return result;
    }
}
