package com.hsd.core;

/**
 * @Author: Jinmeng Tang
 * @Date: Created in 2018/4/16 上午10:08
 */
public final class ProjectConstant {

    /**
     * 项目基础包名称，根据项目修改
     */
    public static final String BASE_PACKAGE = "com.hsd";
    /**
     * Model所在包
     */
    public static final String MODEL_PACKAGE = BASE_PACKAGE + ".model";
    /**
     * Mapper所在包
     */
    public static final String MAPPER_PACKAGE = BASE_PACKAGE + ".dao";
    /**
     * Service所在包
     */
    public static final String SERVICE_PACKAGE = BASE_PACKAGE + ".service";
    /**
     * ServiceImpl所在包
     */
    public static final String SERVICE_IMPL_PACKAGE = SERVICE_PACKAGE + ".impl";
    /**
     * Controller所在包
     */
    public static final String CONTROLLER_PACKAGE = BASE_PACKAGE + ".web";

    //Mapper插件基础接口的完全限定名
    public static final String MAPPER_INTERFACE_REFERENCE = BASE_PACKAGE + ".core.Mapper";

}
