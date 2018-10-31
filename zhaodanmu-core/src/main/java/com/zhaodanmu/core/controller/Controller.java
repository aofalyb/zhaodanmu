package com.zhaodanmu.core.controller;

import com.zhaodanmu.common.URI;

public interface Controller {

    String getRequestMapping();

    Object handle(URI uri, String body);
}
