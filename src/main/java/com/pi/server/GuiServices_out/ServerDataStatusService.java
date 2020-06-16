package com.pi.server.GuiServices_out;

import com.pi.server.SecurityHandling.Crypt;
import org.springframework.stereotype.Service;

@Service
public class ServerDataStatusService {

    private String serverStatus;

    public ServerDataStatusService(){
        setNewServerStatusAfterDataChange();
    }

    public void setNewServerStatusAfterDataChange(){
        Crypt crypt = new Crypt(Crypt.CRYPT_USE_DEFAULT_KEY);
        String s = crypt.encryptLong(System.currentTimeMillis());
        serverStatus = s.substring(0, Math.min(s.length(), 6));;
    }

    public String getServerStatus(){
        return serverStatus;
    }

}
