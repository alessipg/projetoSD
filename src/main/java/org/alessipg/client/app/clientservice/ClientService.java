package org.alessipg.client.app.clientservice;

import org.alessipg.client.infra.tcp.TcpClient;
import org.alessipg.client.infra.tcp.TcpClientHolder;
import org.alessipg.shared.util.IntegerAsStringAdapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public abstract class ClientService {
    protected TcpClient client;
    protected Gson gson;

    public ClientService() {
        this.client = TcpClientHolder.get();
        this.gson = new GsonBuilder()
                .registerTypeAdapter(Integer.class, new IntegerAsStringAdapter())
                .create();
    }
}
