package com.middleware.provider.vtex.model.rest;

import java.util.List;

public class Clients {

  List<Client> clients; 
   
    
    public Clients() {
    }

    public Clients(List<Client> clients) {
        this.clients = clients;
    }

    public List<Client> getClients() {
        return clients;
    }

    public void setClients(List<Client> clients) {
        this.clients = clients;
    }

}
