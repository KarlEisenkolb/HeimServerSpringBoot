package com.pi.server.GuiServices_out;

import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class ServerDataStatusService {

    private String serverStatus;

    public ServerDataStatusService(){
        setNewServerStatusAfterDataChange();
    }

    public void setNewServerStatusAfterDataChange(){
        serverStatus = UUID.randomUUID().toString();
        //System.out.println(serverStatus);
    }

    public String getServerStatus(){
        return serverStatus;
    }

}
