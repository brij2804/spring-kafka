package com.brijesh.kafka.utils;

import java.util.HashMap;
import java.util.Map;

public class EnvironmentVariable {

        /*public static void main (String[] args) {
            EnvironmentVariable envObj= new EnvironmentVariable();
            Map<String, String> env = envObj.getTwitterKey();
            for (String envName : env.keySet()) {
                System.out.format("%s=%s%n",
                        envName,
                        env.get(envName));
            }
        }*/

        public Map<String,String> getTwitterKey(){

            Map<String,String> keys= new HashMap<>();
            Map<String, String> env = System.getenv();
            for (String envName : env.keySet()) {

                if(envName.equalsIgnoreCase("consumerKey")){
                    keys.put(envName,env.get(envName));
                }else if (envName.equalsIgnoreCase("consumerSecret")){
                    keys.put(envName,env.get(envName));
                }else if(envName.equalsIgnoreCase("token")){
                    keys.put(envName,env.get(envName));
                }else if(envName.equalsIgnoreCase("secret")){
                    keys.put(envName,env.get(envName));
                }
            }
            return keys;

        }


}
