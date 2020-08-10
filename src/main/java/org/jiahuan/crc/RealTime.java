package org.jiahuan.crc;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.jiahuan.common.util.DataPackageUtils;

@Slf4j
public class RealTime {

	/** 线程池 */
	static ScheduledExecutorService newScheduledThreadPool = Executors.newScheduledThreadPool(5);


	static int count=0;


	/**等待线程池执行完成
	 * @throws InterruptedException
	 */
	public static void executorServiceStop() throws InterruptedException {
		if(count>0) {
			while(true) {
				Thread.sleep(1000000);
			}
		}
	}


	/**
	 * 获取报文数据
	 * @param  agreement 05/17协议
	 * @param key 实时（2011）\分钟（2051）\小时（2061）\日（2031）\参数（para）\状态（sta）
	 * @param zs 合（join）/分（divide）/没有（none）
	 */
	public static void getTcp(String agreement,String key, String zs) {
		for (String mnAndType : SendTcpToolsTest.pointMN) {
			SendTcpToolsTest yy = new SendTcpToolsTest();
			yy.setparmStateValue();
			yy.setpoiDivisorValue();
			String message;
			if (key.equals("status") || key.equals("para")) {
				for (String meter : yy.meterModelList) {
					yy.meterModel = meter;
					message = DataPackageUtils.composeDataPackage(Common.positiveExpression(getLink(yy,agreement, key, mnAndType, zs)),
							false);
					log.info(message);
				}
			} else {
				message = DataPackageUtils.composeDataPackage(Common.positiveExpression(getLink(yy,agreement, key, mnAndType, zs)),
						false);
				log.info(message);
			}

		}
	}

	/**
	 * 获取报文内容
	 * @param mnObj 对象
	 * @param agreement 协议
	 * @param key 实时（2011）\分钟（2051）\小时（2061）\日（2031）\参数（para）\状态（status）
	 * @param mnAndType MN号跟类型
	 * @param zs 合（join）/分（divide）/没有（none）
	 * @return
	 */
	static String getLink(SendTcpToolsTest mnObj, String agreement, String key, String mnAndType, String zs) {
		JSONObject jsonObject = JSON.parseObject(mnAndType);
		String link=null;
		switch (key) {
		case "2011":
			link= "##0235QN=" + Common.getTime("millisecond") + ";ST=" + jsonObject.getString("type") + ";CN=2011;PW=123456;MN="
					+ jsonObject.getString("mn") + ";Flag=4;CP=&&DataTime=" + Common.getTime("second") + ";"
					+ Common.getParameterLink(mnObj, "realTime", zs) + "&&B381";
			break;
		case "2051":
			link= "##0178QN=" + Common.getTime("millisecond") + ";ST=" + jsonObject.getString("type") + ";CN=2051;PW=123456;MN="
					+  jsonObject.getString("mn") + ";Flag=4;CP=&&DataTime=" + Common.getTime("minute") + ";"
					+ Common.getParameterLink(mnObj, "history", zs) + "&&B381";
			break;
		case "2061":
			link= "##0160QN=" + Common.getTime("millisecond") + ";ST=" + jsonObject.getString("type") + ";CN=2061;PW=123456;MN="
					+  jsonObject.getString("mn") + ";Flag=4;CP=&&DataTime=" + Common.getTime("hour") + ";" + Common.getParameterLink(mnObj, "history", zs)
					+ "&&B381";
			break;
		case "2031":
			link= "##0171QN=" + Common.getTime("millisecond") + ";ST=" + jsonObject.getString("type") + ";CN=2031;PW=123456;MN="
					+  jsonObject.getString("mn") + ";Flag=4;CP=&&DataTime=" + Common.getTime("day") + ";" + Common.getParameterLink(mnObj, "history", zs)
					+ "&&B381";
			break;
		case "para":
			link= "##0171QN=" + Common.getTime("millisecond") + ";ST=" + jsonObject.getString("type") + ";CN=3020;PW=123456;MN="
					+  jsonObject.getString("mn") + ";Flag=4;CP=&&DataTime=" + Common.getTime("second") + ";PolId=" + mnObj.meterModel + ";"
					+ Common.getParameterLink(mnObj, "parameter", zs) + "&&B381";
			break;
		case "status":
			link= "##0171QN=" + Common.getTime("millisecond") + ";ST=" + jsonObject.getString("type") + ";CN=3020;PW=123456;MN="
					+  jsonObject.getString("mn") + ";Flag=4;CP=&&DataTime=" + Common.getTime("second") + ";PolId=" + mnObj.meterModel + ";"
					+ Common.getParameterLink(mnObj, "status", zs) + "&&B381";
			break;
		}
		if(link!=null&&"05".equals(agreement)&&"2011".equals(key)) {
			int indexOf = link.indexOf("ST=");
			link=link.substring(indexOf, link.length());
		}else if(link!=null&&"05".equals(agreement)&&!"2011".equals(key)){
			int indexOf = link.indexOf("ST=");
			link=link.substring(indexOf, link.length());
			String regex="[,;]\\w+-Flag=[a-zA-Z]";
			link=link.replaceAll(regex,"");
		}
		return link;
	}


	/**
	 * 实时报文
	 * @param agreement 协议包
	 * @param isTiming 是否定时发送
	 * @param zs 合（join）/分（divide）/没有（none）
	 * @throws InterruptedException
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public static void gas2011(String agreement, Boolean isTiming,String zs) throws InterruptedException, UnknownHostException, IOException {

		if(isTiming) {
			System.out.println("============启用定时发送实时数据============");
			Runnable second = new Runnable() {
				public void run() {
					try {
						for (String mnAndType : SendTcpToolsTest.pointMN) {
							SendTcpToolsTest yy = new SendTcpToolsTest();
							yy.setpoiDivisorValue();
							Common.send(DataPackageUtils
									.composeDataPackage(Common.positiveExpression(getLink(yy,agreement, "2011", mnAndType, zs)), false));
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			Thread thread = new Thread(second);
			newScheduledThreadPool.scheduleAtFixedRate(thread, 0, 60, TimeUnit.SECONDS);
			count++;
		}else {
			System.out.println("============启用非定时发送实时数据============");
			for (String mnAndType : SendTcpToolsTest.pointMN) {
				SendTcpToolsTest yy = new SendTcpToolsTest();
				yy.setpoiDivisorValue();
				Common.send(DataPackageUtils
						.composeDataPackage(Common.positiveExpression(getLink(yy,agreement, "2011", mnAndType, zs)), false));
			}
		}

	}

	/**
	 * 分钟报文
	 * @param agreement 协议包
	 * @param isTiming 是否定时发送
	 * @param zs 合（join）/分（divide）/没有（none）
	 * @throws InterruptedException
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public static void gas2051(String agreement, Boolean isTiming,String zs) throws InterruptedException, UnknownHostException, IOException {

		if(isTiming) {
			System.out.println("============启用定时发送分钟数据============");
			Runnable second = new Runnable() {
				public void run() {
					try {
						for (String mnAndType : SendTcpToolsTest.pointMN) {
							SendTcpToolsTest yy = new SendTcpToolsTest();
							yy.setpoiDivisorValue();
							Common.send(DataPackageUtils
									.composeDataPackage(Common.positiveExpression(getLink(yy,agreement, "2051", mnAndType, zs)), false));
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			Thread thread = new Thread(second);
			newScheduledThreadPool.scheduleAtFixedRate(thread, 0, 60 * 10, TimeUnit.SECONDS);
			count++;
		}else {
			System.out.println("=============启用非定时发送分钟数据=============");
			for (String mnAndType : SendTcpToolsTest.pointMN) {
				SendTcpToolsTest yy = new SendTcpToolsTest();
				yy.setpoiDivisorValue();
				Common.send(DataPackageUtils
						.composeDataPackage(Common.positiveExpression(getLink(yy,agreement, "2051", mnAndType, zs)), false));
			}
		}

	}

	/**小时报文
	 * @param agreement 协议包
	 * @param isTiming 是否定时发送
	 * @param zs 合（join）/分（divide）/没有（none）
	 * @throws InterruptedException
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public static void gas2061(String agreement, Boolean isTiming,String zs) throws InterruptedException, UnknownHostException, IOException {
		if(isTiming) {
			System.out.println("============启用定时发送小时数据============");
			Runnable second = new Runnable() {
				public void run() {
					try {
						for (String mnAndType : SendTcpToolsTest.pointMN) {
							SendTcpToolsTest mnObj = new SendTcpToolsTest();
							mnObj.setpoiDivisorValue();
							Common.send(DataPackageUtils
									.composeDataPackage(Common.positiveExpression(getLink(mnObj,agreement, "2061", mnAndType, zs)), false));
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			Thread thread = new Thread(second);
			newScheduledThreadPool.scheduleAtFixedRate(thread, 0, 3600, TimeUnit.SECONDS);
			count++;
		}else {
			System.out.println("=============启用非定时发送小时数据=============");
			for (String mnAndType : SendTcpToolsTest.pointMN) {
				SendTcpToolsTest mnObj = new SendTcpToolsTest();
				mnObj.setpoiDivisorValue();
				Common.send(DataPackageUtils
						.composeDataPackage(Common.positiveExpression(getLink(mnObj,agreement, "2061", mnAndType, zs)), false));
			}
		}
	}

	/**
	 * 日报文
	 * @param agreement 协议包
	 * @param isTiming 是否定时发送
	 * @param zs 合（join）/分（divide）/没有（none）
	 * @throws InterruptedException
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public static void gas2031(String agreement, Boolean isTiming,String zs) throws InterruptedException, UnknownHostException, IOException {
		if(isTiming) {
			System.out.println("=============启用定时发送日数据=============");
			Runnable second = new Runnable() {
				public void run() {
					try {
						for (String mnAndType : SendTcpToolsTest.pointMN) {
							SendTcpToolsTest yy = new SendTcpToolsTest();
							yy.setpoiDivisorValue();
							Common.send(DataPackageUtils
									.composeDataPackage(Common.positiveExpression(getLink(yy,agreement, "2031", mnAndType, zs)), false));
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			Thread thread = new Thread(second);
			newScheduledThreadPool.scheduleAtFixedRate(thread, 0, 60 * 60 * 24, TimeUnit.SECONDS);
			count++;
		}else {
			System.out.println("=============启用非定时发送日数据=============");
			for (String mnAndType : SendTcpToolsTest.pointMN) {
				SendTcpToolsTest yy = new SendTcpToolsTest();
				yy.setpoiDivisorValue();
				Common.send(DataPackageUtils
						.composeDataPackage(Common.positiveExpression(getLink(yy,agreement, "2031", mnAndType, zs)), false));
			}
		}
	}

	/**
	 * 发送状态报文
	 *
	 * @throws InterruptedException
	 * @throws IOException
	 * @throws UnknownHostException
	 */
	public static void status3020() throws InterruptedException, UnknownHostException, IOException {
		System.out.println("============已发送状态报文数据============");
		for (String mnAndType : SendTcpToolsTest.pointMN) {
			SendTcpToolsTest yy = new SendTcpToolsTest();
			yy.setparmStateValue();
			for (String meter : yy.meterModelList) {
				yy.meterModel = meter;
				Common.send(DataPackageUtils.composeDataPackage(
						Common.positiveExpression(getLink(yy,"17", "status", mnAndType, "none")), false));
			}
		}
	}

	/**
	 * 发送参数报文
	 *
	 * @throws InterruptedException
	 * @throws IOException
	 * @throws UnknownHostException
	 */
	public static void param3020() throws InterruptedException, UnknownHostException, IOException {
		System.out.println("============已发送参数报文数据============");
		for (String mnAndType : SendTcpToolsTest.pointMN) {
			SendTcpToolsTest yy = new SendTcpToolsTest();
			yy.setparmStateValue();
			for (String meter : yy.meterModelList) {
				yy.meterModel = meter;
				Common.send(DataPackageUtils.composeDataPackage(
						Common.positiveExpression(getLink(yy,"17", "para", mnAndType, "none")), false));
			}
		}
	}

	/**
	 * 手动发送
	 *
	 * @throws InterruptedException
	 */
	public static void manuallySend() throws InterruptedException {
		Thread.sleep(500);
		while (true) {
			System.out.print("请输入：");
			Scanner scan = new Scanner(System.in);
			String msg = scan.nextLine();
			msg = Common.positiveExpression(msg);
			String crc = DataPackageUtils.composeDataPackage(msg, false);
			try {
				Common.send(crc);
				System.out.println("已发送到服务器");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
