package com.cs506group12.backend.models;

import com.cs506group12.backend.interfaces.*;

public abstract class PlayerDecorator implements Player{
    protected Player decoratedPlayer;

    public PlayerDecorator(Player player){
        this.decoratedPlayer = player;
    }

}
