package com.zhaodanmu.persistence.elasticsearch;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public interface Log {

    Logger defLogger = LogManager.getLogger("defLogger");

    Logger sysLogger = LogManager.getLogger("sysLogger");


}