package org.bitcoinj.net;

import org.bitcoinj.utils.JSONHelper;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Random;

public class NetHelper {
    public String getCashAccountAddress(String cashAccount)
    {
        String[] cashAcctServers = new String[]{
                "https://cashacct.imaginary.cash",
                "https://api.cashaccount.info"
        };

        int randExplorer = new Random().nextInt(cashAcctServers.length);
        String lookupServer = cashAcctServers[randExplorer];

        String address = "";

        String[] splitAccount = cashAccount.split("#");
        String name = splitAccount[0];
        String block = splitAccount[1];

        if(!block.contains(".")) {

            InputStream is = null;
            try {
                is = new URL(lookupServer + "/account/" + block + "/" + name).openStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
                String jsonText = JSONHelper.readJSONFile(rd);
                JSONObject json = new JSONObject(jsonText);
                address = json.getJSONObject("information").getJSONArray("payment").getJSONObject(0).getString("address");
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else
        {
            String[] splitBlock = block.split("\\.");
            String mainBlock = splitBlock[0];
            String blockCollision = splitBlock[1];


            InputStream is = null;
            try {
                is = new URL(lookupServer + "/account/" + mainBlock + "/" + name + "/" + blockCollision).openStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
                String jsonText = JSONHelper.readJSONFile(rd);
                JSONObject json = new JSONObject(jsonText);
                address = json.getJSONObject("information").getJSONArray("payment").getJSONObject(0).getString("address");
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return address;
    }
}
