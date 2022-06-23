package com.brohoof.brohoofeconomy.command;

import com.brohoof.brohoofeconomy.API;
import com.brohoof.brohoofeconomy.Settings;

public abstract class AbstractCommand {
    
    
    protected API api;
    protected Settings settings;
    
    
    public AbstractCommand(Settings settings, API api) {
        this.api = api;
        this.settings = settings;
    }
}
