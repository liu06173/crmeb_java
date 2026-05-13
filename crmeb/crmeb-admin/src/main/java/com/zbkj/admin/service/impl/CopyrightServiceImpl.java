package com.zbkj.admin.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.zbkj.admin.copyright.CopyrightInfoResponse;
import com.zbkj.admin.copyright.CopyrightUpdateInfoRequest;
import com.zbkj.admin.service.CopyrightService;
import com.zbkj.common.config.CrmebConfig;
import com.zbkj.common.constants.Constants;
import com.zbkj.common.constants.SysConfigConstants;
import com.zbkj.common.exception.CrmebException;
import com.zbkj.common.utils.RestTemplateUtil;
import com.zbkj.service.service.SystemAttachmentService;
import com.zbkj.service.service.SystemConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 版权服务实现类
 * +----------------------------------------------------------------------
 * +----------------------------------------------------------------------
 * +----------------------------------------------------------------------
 * +----------------------------------------------------------------------
 * +----------------------------------------------------------------------
 */
@Service
public class CopyrightServiceImpl implements CopyrightService {

    @Autowired
    private SystemConfigService systemConfigService;
    @Autowired
    private CrmebConfig crmebConfig;
    @Autowired
    private RestTemplateUtil restTemplateUtil;


    private static final String COPYRIGHT_URL = "";
    private static final String COPYRIGHT_URL_DATA = "data";
    private static final String COPYRIGHT_URL_STATUS = "status";
    private static final String COPYRIGHT_URL_COPYRIGHT = "copyright";
    private static final String COPYRIGHT_URL_AUTHCODE = "auth_code";


    /**
     * 获取版权信息
     */
    @Override
    public CopyrightInfoResponse getInfo() {
        CopyrightInfoResponse response = new CopyrightInfoResponse();
        String domainName = systemConfigService.getValueByKey(Constants.CONFIG_KEY_API_URL);
        if (StrUtil.isBlank(domainName)) {
            response.setStatus(-2);
            return response;
        }
        String label = systemConfigService.getValueByKey(SysConfigConstants.CONFIG_COPYRIGHT_LABEL);
        String version = crmebConfig.getVersion();
        if (StrUtil.isBlank(version)) {
            throw new CrmebException("请先在yml中配置版本号");
        }
        response.setDomainUrl(domainName);
        response.setLabel(Integer.parseInt(label));
        response.setVersion(version);

        JSONObject jsonObject = restTemplateUtil.post(StrUtil.format(COPYRIGHT_URL, domainName, label, version));
        if (ObjectUtil.isNull(jsonObject.getInteger("status")) || !jsonObject.getInteger("status").equals(200)) {
            throw new CrmebException("版权接口调用失败," + jsonObject);
        }
        JSONObject dataJson = jsonObject.getJSONObject(COPYRIGHT_URL_DATA);

        response.setStatus(dataJson.getInteger(COPYRIGHT_URL_STATUS));
        response.setCopyright(dataJson.getString(COPYRIGHT_URL_COPYRIGHT));
        if (!dataJson.getInteger(COPYRIGHT_URL_STATUS).equals(1)) {
            return response;
        }
        response.setAuthCode(dataJson.getString(COPYRIGHT_URL_AUTHCODE));
        response.setCompanyName(systemConfigService.getValueByKey(SysConfigConstants.CONFIG_COPYRIGHT_COMPANY_INFO));
        response.setCompanyImage(systemConfigService.getValueByKey(SysConfigConstants.CONFIG_COPYRIGHT_COMPANY_IMAGE));
        return response;
    }

}
