package com.lcsk42.biz.admin.common.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AdminRocketMQConstant {

    public static final String EVENT_NAME_GENERALLY_EVENT = "GenerallyEvent";
    public static final String TOPIC_GENERALLY_EVENT_ASYNC = "lcsk42_admin-service_generally-event-async_topic";
    public static final String TAG_GENERALLY_EVENT_ASYNC = "lcsk42_admin-service_generally-event-async_tag";
    public static final String CG_GENERALLY_EVENT_ASYNC = "lcsk42_admin-service_generally-event-async_cg";



    public static final Long SEND_TIMEOUT = 2000L;
}
