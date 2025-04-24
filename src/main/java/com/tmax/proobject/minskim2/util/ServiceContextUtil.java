//package com.tmax.proobject.minskim2.util;
//
//import com.tmax.proobject.core2.service.ServiceName;
//import com.tmax.proobject.runtime.context.ServiceContext;
//import com.tmax.proobject.runtime.context.ServiceContextHolder;
//
//public class ServiceContextUtil {
//
//	/**
//	 * @ProObjectTest 에서 사용할 ServiceLogger를 생성
//	 */
//	public static void setServiceContext() {
//		setServiceContext("tmax", "proobject", "TestLogger");
//	}
//
//	/**
//	 * @ProObjectTest 에서 사용할 ServiceLogger를 생성
//	 *
//	 * @param appName 애플리케이션명
//	 * @param svgName 서비스 그룹명
//	 * @param svcName 서비스명
//	 */
//	public static void setServiceContext(String appName, String svgName, String svcName) {
//		ServiceContextHolder.removeServiceContext();
//		String fullName = appName + "." + svgName + "." + svcName;
//		ServiceContext serviceContext = ServiceContextHolder.getServiceContext();
//		ServiceName serviceName = new ServiceName(fullName);
//		serviceContext.setServiceName(serviceName);
//	}
//}
