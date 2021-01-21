package com.pi.server.gui_services_out;

import com.pi.server.gui_controller_out.MainController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class ServerDataStatusService {

    private final Logger log = LoggerFactory.getLogger(MainController.class);
    private String serverStatus;

    public ServerDataStatusService(){
        setNewServerStatusAfterDataChange();
    }

    public void setNewServerStatusAfterDataChange(){
        serverStatus = UUID.randomUUID().toString();
        //log.info(serverStatus);
    }

    public String getServerStatus(){
        return serverStatus;
    }

}
