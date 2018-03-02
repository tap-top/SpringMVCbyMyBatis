package com.boe.tww.webservice;

import javax.jws.WebService;
import javax.xml.ws.Endpoint;

@WebService

/**
 * @author tangwawnwan
 */
public class Function {
    public String transWords(String words) {
        String res = "";
        for (char ch : words.toCharArray()) {
            res += ch + ",";
        }
        return res;
    }

    public static void main(String[] args) {
        Endpoint.publish("http://localhost:8089/service/function", new Function());
        System.out.print("Pubslish Success");
    }
}
