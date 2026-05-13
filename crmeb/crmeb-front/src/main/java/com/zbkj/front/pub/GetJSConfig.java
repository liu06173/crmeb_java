package com.zbkj.front.pub;

import com.zbkj.common.constants.Constants;
import com.zbkj.service.service.SystemConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("api/public/jsconfig")
@Api(tags = "公共JS配置")
public class GetJSConfig {

    @Autowired
    private SystemConfigService systemConfigService;

    @PreAuthorize("hasAuthority('public:jsconfig:getchatchatconfig')")
    @ApiOperation(value = "客服统计")
    @RequestMapping(value = "/getchatchatconfig", method = RequestMethod.GET)
    public String set(){
        return systemConfigService.getValueByKey(Constants.JS_CONFIG_CHAT_TONGJI);
    }
}
