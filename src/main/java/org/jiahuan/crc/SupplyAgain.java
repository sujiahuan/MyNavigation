package org.jiahuan.crc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.jiahuan.common.util.DataPackageUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SupplyAgain {

	public static ExecutorService executorService = Executors.newFixedThreadPool(4);

	/**等待线程池执行完成
	 * @throws InterruptedException
	 */
	public static void executorServiceStop() throws InterruptedException {
		SupplyAgain.executorService.shutdown();
		while(!SupplyAgain.executorService.isTerminated()) {
			Thread.sleep(1000);
		}
	}
	/**
	 * 获取报文内容
	 *
	 * @param mnObj 对象
	 * @param date 补发时间
	 * @param key 实时（second）\分钟（minute）\小时（hour）\日（day）\参数（parameter）\状态（status）
	 * @param zs 合（join）/分（divide）/没有（none）
	 * @return
	 */
	private static String getLink(SendTcpToolsTest mnObj, Date date, String agreement, String key, String mnAndType, String zs) {
		JSONObject jsonObject = JSON.parseObject(mnAndType);
		String link=null;
		switch (key) {
		case "second":
			link= "##0235QN=" + Common.getTime("millisecond") + ";ST=" + jsonObject.getString("type") + ";CN=2011;PW=123456;MN="
					+ jsonObject.getString("mn") + ";Flag=4;CP=&&DataTime=" + getReissueTime(date,"second") + ";"
					+ Common.getParameterLink(mnObj, "realTime", zs) + "&&B381";
			break;
		case "minute":
			link= "##0178QN=" + Common.getTime("millisecond") + ";ST=" + jsonObject.getString("type") + ";CN=2051;PW=123456;MN="
					+ jsonObject.getString("mn") + ";Flag=4;CP=&&DataTime=" + getReissueTime(date,"minute") + ";"
					+ Common.getParameterLink(mnObj, "history", zs) + "&&B381";
			break;
		case "hour":
			link= "##0160QN=" + Common.getTime("millisecond") + ";ST=" + jsonObject.getString("type") + ";CN=2061;PW=123456;MN="
					+ jsonObject.getString("mn") + ";Flag=4;CP=&&DataTime=" + getReissueTime(date,"hour") + ";" + Common.getParameterLink(mnObj, "history", zs)
					+ "&&B381";
			break;
		case "day":
			link= "##0171QN=" + Common.getTime("millisecond") + ";ST=" + jsonObject.getString("type") + ";CN=2031;PW=123456;MN="
					+ jsonObject.getString("mn") + ";Flag=4;CP=&&DataTime=" + getReissueTime(date,"day") + ";" + Common.getParameterLink(mnObj, "history", zs)
					+ "&&B381";
			break;
		case "parameter":
			link= "##0171QN=" + Common.getTime("millisecond") + ";ST=" + jsonObject.getString("type") + ";CN=3020;PW=123456;MN="
					+ jsonObject.getString("mn") + ";Flag=4;CP=&&DataTime=" + getReissueTime(date,"second") + ";PolId=" + mnObj.meterModel + ";"
					+ Common.getParameterLink(mnObj, "parameter", zs) + "&&B381";
			break;
		case "status":
			link= "##0171QN=" + Common.getTime("millisecond") + ";ST=" + jsonObject.getString("type") + ";CN=3020;PW=123456;MN="
					+ jsonObject.getString("mn") + ";Flag=4;CP=&&DataTime=" + getReissueTime(date,"second") + ";PolId=" + mnObj.meterModel + ";"
					+ Common.getParameterLink(mnObj, "status", zs) + "&&B381";
			break;
		}
		if(link!=null&&"05".equals(agreement)&&"second".equals(key)) {
			int indexOf = link.indexOf("ST=");
			link=link.substring(indexOf, link.length());
		}else if(link!=null&&"05".equals(agreement)&&!"second".equals(key)){
			int indexOf = link.indexOf("ST=");
			link=link.substring(indexOf, link.length());
			String regex="[,;]\\w+-Flag=[a-zA-Z]";
			link=link.replaceAll(regex,"");
		}
		return link;
	}

	/**
	 * 获取时间
	 *
	 * @param key 精确到毫秒（millisecond）/秒（second）/分钟（minute）/小时（hour）/日（day）
	 * @return 返回格式化后的日期
	 */
	private static String getReissueTime(Date ReissueDate,String key) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(ReissueDate);
		Date date = calendar.getTime();
		switch (key) {
		case "millisecond":
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			String format = simpleDateFormat.format(date);
			return format;
		case "second":
			SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyyMMddHHmmss");
			String format1 = simpleDateFormat1.format(date);
			return format1;
		case "minute":
			SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyyMMddHHmm");
			String format2 = simpleDateFormat2.format(date);
			return format2 + "00";
		case "hour":
			SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("yyyyMMddHH");
			String format3 = simpleDateFormat3.format(date);
			return format3 + "0000";
		case "day":
			SimpleDateFormat simpleDateFormat4 = new SimpleDateFormat("yyyyMMdd");
			String format4 = simpleDateFormat4.format(date);
			return format4 + "000000";
		default:
			return null;
		}
	}


	/**
	 * 实时报文
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @param second 秒
	 * @param zs 合（join）/分（divide）/没有（none）
	 * @return
	 * @throws InterruptedException
	 */
	public static void gas2011(String agreement,String startTime, String endTime,int second,String zs) throws InterruptedException {
		System.out.println("============启用自动补发实时数据============");
		Runnable runnable = new Runnable() {
			public void run() {
				try {
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
					Date start = simpleDateFormat.parse(startTime);
					Date end = simpleDateFormat.parse(endTime);
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(start);
					int sum=0;
					while(calendar.getTime().before(end)) {

					for (String mnAndType : SendTcpToolsTest.pointMN) {

						SendTcpToolsTest yy = new SendTcpToolsTest();
						yy.setpoiDivisorValue();
						Common.send(DataPackageUtils
								.composeDataPackage(Common.positiveExpression(getLink(yy,calendar.getTime(), agreement,"second", mnAndType, zs)), false));
						sum++;
					}
					calendar.add(Calendar.SECOND,second);

					}
					System.out.println("共补发了"+sum+"条实时数据");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		executorService.submit(runnable);
	}

	/**
	 * 分钟报文
	 *
	 * @param zs 合（join）/分（divide）/没有（none）
	 * @throws InterruptedException
	 */
	public static void gas2051(String agreement,String startTime, String endTime,int minute,String zs) throws InterruptedException {
		System.out.println("============启用自动补发分钟数据============");
		Runnable runnable = new Runnable() {
			public void run() {
				try {
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-ddHH:mm");
					Date start = simpleDateFormat.parse(startTime);
					Date end = simpleDateFormat.parse(endTime);
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(start);
					int sum=0;
					while(calendar.getTime().before(end)) {
					for (String mnAndType : SendTcpToolsTest.pointMN) {
						SendTcpToolsTest yy = new SendTcpToolsTest();
						yy.setpoiDivisorValue();
						Common.send(DataPackageUtils
								.composeDataPackage(Common.positiveExpression(getLink(yy,calendar.getTime(),agreement, "minute", mnAndType, zs)), false));
						sum++;
					}
					calendar.add(Calendar.MINUTE,minute);
					}
					System.out.println("共补发了"+sum+"条分钟数据");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		executorService.submit(runnable);
	}

	/**
	 * 小时报文
	 *
	 * @param zs 合（join）/分（divide）/没有（none）
	 * @throws InterruptedException
	 */
	public static void gas2061(String agreement,String startTime, String endTime,int hour,String zs) throws InterruptedException {
		System.out.println("============启用自动补发小时数据============");
		Runnable runnable = new Runnable() {
			public void run() {
				try {
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-ddHH");
					Date start = simpleDateFormat.parse(startTime);
					Date end = simpleDateFormat.parse(endTime);

					Calendar calendar = Calendar.getInstance();

					calendar.setTime(start);
					int sum=0;
					while(calendar.getTime().before(end)) {
					for (String mnAndType : SendTcpToolsTest.pointMN) {
						SendTcpToolsTest yy = new SendTcpToolsTest();
						yy.setpoiDivisorValue();
						Common.send(DataPackageUtils
								.composeDataPackage(Common.positiveExpression(getLink(yy,calendar.getTime(),agreement, "hour", mnAndType, zs)), false));
						sum++;
					}
					calendar.add(Calendar.HOUR,hour);
					}
					System.out.println("共补发了"+sum+"条小时数据");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		executorService.submit(runnable);
	}

	/**
	 * 日报文
	 *
	 * @param zs 合（join）/分（divide）/没有（none）
	 * @throws InterruptedException
	 */
	public static void gas2031(String agreement,String startTime, String endTime,int day,String zs) throws InterruptedException {
		System.out.println("=============启用自动补发日数据=============");
		Runnable runnable = new Runnable() {
			public void run() {
				try {
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
					Date start = simpleDateFormat.parse(startTime);
					Date end = simpleDateFormat.parse(endTime);
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(start);
					int sum=0;
					while(calendar.getTime().before(end)) {
					for (String mnAndType : SendTcpToolsTest.pointMN) {
						SendTcpToolsTest yy = new SendTcpToolsTest();
						yy.setpoiDivisorValue();
						Common.send(DataPackageUtils
								.composeDataPackage(Common.positiveExpression(getLink(yy,calendar.getTime(),agreement, "day", mnAndType, zs)), false));
						sum++;
					}
					calendar.add(Calendar.DATE,day);
					}
					System.out.println("共补发了"+sum+"条日数据");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		executorService.submit(runnable);
	}
}
