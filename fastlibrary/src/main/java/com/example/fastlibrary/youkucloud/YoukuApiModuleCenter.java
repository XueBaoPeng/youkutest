package com.example.fastlibrary.youkucloud;


import com.example.fastlibrary.youkucloud.Module.Base;

import java.lang.reflect.Method;
import java.util.TreeMap;

/**
 * @brief 模块调用类
 * @author luhanlin
 */
public class YoukuApiModuleCenter {

	private Base module;
	
	/**
	 * 构造模块调用类
	 * @param module 实际模块实例
	 */
	public YoukuApiModuleCenter(Base module){
		this.module = module;
	}

	public void setRequestMethod (String requestMethod) {
		module.setRequestMethod(requestMethod);
	}
	
	/**
	 * Api调用
	 * @param actionName 模块动作名称
	 * @param params 模块请求参数
	 * @return json字符串
	 * @throws Exception
	 */
	public String call(String actionName, TreeMap<String, Object> params) throws Exception
	{
		for(Method method : module.getClass().getMethods()){
			if(method.getName().equals(actionName)){
				try {
					return (String) method.invoke(module, params);
				} catch (Exception e) {
					throw e;
				} 
			}
		}
		return module.call(params, null);
	}
}
