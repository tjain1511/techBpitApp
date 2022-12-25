package com.indianapp.techbpit.model;

import com.indianapp.techbpit.ApiController.BaseData;
import com.indianapp.techbpit.ApiController.RESTController;

public class PayLoad {
    public RESTController.RESTCommands command;
    public BaseData<?> data;
    public RESTController.OnResponseStatusListener listener;

    public PayLoad(RESTController.RESTCommands command, BaseData<?> payloadData, RESTController.OnResponseStatusListener listener) {
        this.command = command;
        this.data = payloadData;
        this.listener = listener;
    }
}
