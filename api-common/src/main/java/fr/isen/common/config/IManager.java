package fr.isen.common.config;

import fr.isen.common.logger.IsenLogger;

public abstract class IManager<P> {

    protected P plugin;
    protected IsenLogger logger;
    private final String name;

    public IManager(P plugin, IsenLogger logger, String name) {
        this.plugin = plugin;
        this.logger = logger;
        this.name = name;

        logger.log(name + " loaded", "INFO");
    }

    public String getName() {
        return name;
    }
}
