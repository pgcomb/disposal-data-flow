package com.github.pgcomb.ddf.sort;

import com.github.pgcomb.ddf.common.Nameable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StopSortManager implements Runnable,Nameable {

    private static final Logger log = LoggerFactory.getLogger(StopSortManager.class);

    private static final String NAME = "stop_sort_manager";

    private PassiveSorterManager passiveSorterManager;

    public StopSortManager(PassiveSorterManager passiveSorterManager) {
        this.passiveSorterManager = passiveSorterManager;
    }

    @Override
    public void run() {
        log.debug("stop passiveSorterManager thread start");
        passiveSorterManager.stop();
        log.debug("stop passiveSorterManager thread end");
    }

    @Override
    public String getName() {
        return NAME;
    }
}
