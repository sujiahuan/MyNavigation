package org.jiahuan.crc;

import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import java.util.*;

/**
 * 油烟系统自动发送脚本
 */

@Slf4j
public class SendTcpToolsTest {
	/** MN号集合 */
	static List<String> pointMN = new ArrayList<String>();
	/** 监测因子编码 */
	Map<String, String> pointDivisor = new LinkedHashMap<String, String>();
	/** 单个仪表型号 */
	public String meterModel;
	/** 有多少个仪表型号 */
	public List<String> meterModelList = new ArrayList<String>();
	/** 参数和状态编码对应值 */
	public Map<String, String> meterParamStatusValue = new LinkedHashMap<String, String>();
	/** 接收器ip、端口、废水（32）/废气（31） */
	public static final String[] AddressAndPort;


	static {
//		AddressAndPort= new String[]{"192.168.10.101","3111"};// 运维saas版
//		AddressAndPort= new String[]{"192.168.5.11","3111"};// 在线监测
//		AddressAndPort = new String[] { "183.24.83.186", "3101"};// 油烟
		AddressAndPort= new String[]{"192.168.10.101","3101"};//运维标准版
//		AddressAndPort= new String[]{"192.168.10.101","5101"};//东莞


		pointMN.add("{'mn':'12345678910','type':'32','agreement':'05'}");
//		pointMN.add("{'mn':'12345678911','type':'31','agreement':'17'}");
//		pointMN.add("{'mn':'51110201','type':'22','agreement':'17'}");
//		for (int mn = 1001; mn <= 1001; mn++) {
//			pointMN.add(String.valueOf(mn));
//		}
//		for (int mn = 1000; mn < 1000; mn++) {
//			pointMN.add(String.valueOf(mn));
//		}
	}


	/**
	 * 为参数/状态赋予值
	 */
	public void setparmStateValue() {
		meterModelList.add("w21001");
//		meterModelList.add("w01018");

		meterParamStatusValue.put("i12001", "6");
		meterParamStatusValue.put("i12002", "0");
		meterParamStatusValue.put("i12003", "0");
		meterParamStatusValue.put("i13001", "21");
		meterParamStatusValue.put("i13002", "21");
		meterParamStatusValue.put("i13003", "15");
		meterParamStatusValue.put("i13004", "15");
		meterParamStatusValue.put("i13005", "15");
		meterParamStatusValue.put("i13006", "16");
		meterParamStatusValue.put("i13007", "17");
		meterParamStatusValue.put("i13008", "18");
		meterParamStatusValue.put("i13009", "20");
	}

	/**
	 * 设置监测因子值
	 */
	public void setpoiDivisorValue() {
//		pointDivisor.put("w33001","{'Avg':'10','Cou':'2','Max':'3','Min':'4','ZsAvg':'11','ZsMin':'5','ZsMax':'6','Flag':'N'}");
//		pointDivisor.put("w01018","{'Avg':'10','Cou':'2','Max':'3','Min':'4','ZsAvg':'11','ZsMin':'5','ZsMax':'6','Flag':'N'}");
//		pointDivisor.put("a19001","{'Avg':'11','Cou':'2','Max':'3','Min':'4','ZsAvg':'11','ZsMin':'5','ZsMax':'6','Flag':'N'}");

		// 05废气
//		pointDivisor.put("S01","{'Avg':'6','Cou':'2','Max':'3','Min':'4','ZsAvg':'11','ZsMin':'5','ZsMax':'6','Flag':'N'}");
//		pointDivisor.put("02","{'Avg':'7','Cou':'2','Max':'3','Min':'4','ZsAvg':'11','ZsMin':'5','ZsMax':'6','Flag':'N'}");
//		pointDivisor.put("03","{'Avg':'6','Cou':'2','Max':'3','Min':'4','ZsAvg':'11','ZsMin':'5','ZsMax':'6','Flag':'N'}");
//		pointDivisor.put("01","{'Avg':'7','Cou':'2','Max':'3','Min':'4','ZsAvg':'11','ZsMin':'5','ZsMax':'6','Flag':'N'}");
//		pointDivisor.put("S02","{'Avg':'7','Cou':'2','Max':'3','Min':'4','ZsAvg':'11','ZsMin':'5','ZsMax':'6','Flag':'N'}");
		// 05废水
		pointDivisor.put("LG","{'Avg':'21.1','Cou':'2','Max':'3','Min':'4','ZsAvg':'11.2','ZsMin':'5','ZsMax':'6','Flag':'F'}");
		pointDivisor.put("tm","{'Avg':'20.1','Cou':'2','Max':'3','Min':'4','ZsAvg':'10.2','ZsMin':'5','ZsMax':'6','Flag':'C'}");
		pointDivisor.put("BX","{'Avg':'19.1','Cou':'2','Max':'3','Min':'4','ZsAvg':'9','ZsMin':'5','ZsMax':'6','Flag':'D'}");
		pointDivisor.put("FL","{'Avg':'18.1','Cou':'2','Max':'3','Min':'4','ZsAvg':'0','ZsMin':'5','ZsMax':'6','Flag':'B'}");
		pointDivisor.put("Bat","{'Avg':'17.1','Cou':'2','Max':'3','Min':'4','ZsAvg':'0','ZsMin':'5','ZsMax':'6','Flag':'M'}");

//		pointDivisor.put("a34004","{'Avg':'33.49','Cou':'2','Max':'3','Min':'4','ZsAvg':'11.2','ZsMin':'5','ZsMax':'6','Flag':'N'}");
//		pointDivisor.put("a34002","{'Avg':'66.35','Cou':'2','Max':'3','Min':'4','ZsAvg':'11.2','ZsMin':'5','ZsMax':'6','Flag':'N'}");
//		pointDivisor.put("a21026","{'Avg':'4.1','Cou':'2','Max':'3','Min':'4','ZsAvg':'10.2','ZsMin':'5','ZsMax':'6','Flag':'C'}");
//		pointDivisor.put("a21003","{'Avg':'7.38','Cou':'2','Max':'3','Min':'4','ZsAvg':'9','ZsMin':'5','ZsMax':'6','Flag':'D'}");
//		pointDivisor.put("a19001","{'Avg':'3','Cou':'2','Max':'3','Min':'4','ZsAvg':'0','ZsMin':'5','ZsMax':'6','Flag':'B'}");
//		pointDivisor.put("a21005","{'Avg':'1.63','Cou':'2','Max':'3','Min':'4','ZsAvg':'0','ZsMin':'5','ZsMax':'6','Flag':'N'}");
		// 17废水
//		pointDivisor.put("w01018","{'Avg':'10','Cou':'2','Max':'3','Min':'4','ZsAvg':'11','ZsMin':'5','ZsMax':'6','Flag':'N'}");
//		pointDivisor.put("w21003","{'Avg':'10','Cou':'2','Max':'3','Min':'4','ZsAvg':'11','ZsMin':'5','ZsMax':'6','Flag':'N'}");
//		pointDivisor.put("w00000","{'Avg':'10','Cou':'2','Max':'3','Min':'4','ZsAvg':'11','ZsMin':'5','ZsMax':'6','Flag':'N'}");
//		pointDivisor.put("w01001","{'Avg':'10','Cou':'2','Max':'3','Min':'4','ZsAvg':'11','ZsMin':'5','ZsMax':'6','Flag':'N'}");
		// 17废气
//		pointDivisor.put("a01012","{'Avg':'"+Common.getRandom(11,100)+"','Cou':'2','Max':'3','Min':'4','ZsAvg':'11','ZsMin':'5','ZsMax':'6','Flag':'N'}");
//		pointDivisor.put("a21004","{'Avg':'"+Common.getRandom(0,10)+"','Cou':'2','Max':'3','Min':'4','ZsAvg':'11','ZsMin':'5','ZsMax':'6','Flag':'D'}");
		//自定义
//		pointDivisor.put("05online","{'Avg':'"+Common.getRandom(10,10)+"','Cou':'2','Max':'3','Min':'4','ZsAvg':'11','ZsMin':'5','ZsMax':'6','Flag':'T'}");
//		pointDivisor.put("17online","{'Avg':'"+Common.getRandom(20,30)+"','Cou':'2','Max':'3','Min':'4','ZsAvg':'11','ZsMin':'5','ZsMax':'6','Flag':'N'}");
//		pointDivisor.put("17onlinez","{'Avg':'"+Common.getRandom(1,1)+"','Cou':'2','Max':'3','Min':'4','ZsAvg':'11','ZsMin':'5','ZsMax':'6','Flag':'N'}");
//		pointDivisor.put("05water","{'Avg':'"+Common.getRandom(11,11)+"','Cou':'2','Max':'3','Min':'4','ZsAvg':'11','ZsMin':'5','ZsMax':'6','Flag':'D'}");
//		pointDivisor.put("17water","{'Avg':'"+Common.getRandom(-1,-1)+"','Cou':'2','Max':'3','Min':'4','ZsAvg':'11','ZsMin':'5','ZsMax':'6','Flag':'N'}");

//		pointDivisor.put("ph","{'Avg':'"+Common.getRandom(8,8)+"','Cou':'100','Max':'3','Min':'4','ZsAvg':'11','ZsMin':'5','ZsMax':'6','Flag':'D'}");
//		pointDivisor.put("diandaolv","{'Avg':'"+Common.getRandom(0,5)+"','Cou':'100','Max':'3','Min':'4','ZsAvg':'11','ZsMin':'5','ZsMax':'6','Flag':'D'}");
//		pointDivisor.put("diangonglv","{'Avg':'"+Common.getRandom(2,4)+"','Cou':'100','Max':'3','Min':'4','ZsAvg':'11','ZsMin':'5','ZsMax':'6','Flag':'M'}");
//		pointDivisor.put("wushuishunshi","{'Avg':'"+Common.getRandom(0,35)+"','Cou':'100','Max':'3','Min':'4','ZsAvg':'11','ZsMin':'5','ZsMax':'6','Flag':'F'}");
//		pointDivisor.put("zilaishuishunshi","{'Avg':'"+Common.getRandom(139,244)+"','Cou':'100','Max':'3','Min':'4','ZsAvg':'11','ZsMin':'5','ZsMax':'6','Flag':'N'}");
//		pointDivisor.put("paishui","{'Avg':'15','Cou':'52','Max':'3','Min':'4','ZsAvg':'11','ZsMin':'5','ZsMax':'6','Flag':'N'}");
//		pointDivisor.put("ws4001","{'Avg':'15','Cou':'52','Max':'3','Min':'4','ZsAvg':'11','ZsMin':'5','ZsMax':'6','Flag':'N'}");
//		pointDivisor.put("yongshui","{'Avg':'"+Common.getRandom(300,400)+"','Cou':'100','Max':'3','Min':'4','ZsAvg':'11','ZsMin':'5','ZsMax':'6','Flag':'N'}");
//		pointDivisor.put("ws3001","{'Avg':'"+Common.getRandom(10,10)+"','Cou':'1','Max':'3','Min':'4','ZsAvg':'11','ZsMin':'5','ZsMax':'6','Flag':'N'}");
//		pointDivisor.put("yongdian","{'Avg':'"+Common.getRandom(10,10)+"','Cou':'0','Max':'3','Min':'4','ZsAvg':'11','ZsMin':'5','ZsMax':'6','Flag':'N'}");
//		pointDivisor.put("ws2001","{'Avg':'"+Common.getRandom(10,10)+"','Cou':'1','Max':'3','Min':'4','ZsAvg':'11','ZsMin':'5','ZsMax':'6','Flag':'N'}");


		// 非甲烷
//		pointDivisor.put("a01001",
//				"{'Avg':'2','Cou':'0','Max':'2','Min':'1','ZsAvg':'10','ZsMin':'5','ZsMax':'6','Flag':'N'}");
//		// 颗粒物
//		pointDivisor.put("a01002",
//				"{'Avg':'3','Cou':'0','Max':'4','Min':'3','ZsAvg':'10','ZsMin':'5','ZsMax':'6','Flag':'N'}");
//		// 浓度
//		pointDivisor.put("a34041",
//				"{'Avg':'"+Common.getRandom(3,4)+"','Cou':'0','Max':'6','Min':'5','ZsAvg':'10','ZsMin':'5','ZsMax':'6','Flag':'D'}");
//		// 风机状态
//		pointDivisor.put("a01018",
//				"{'Avg':'1','Cou':'0','Max':'0','Min':'0','ZsAvg':'10','ZsMin':'5','ZsMax':'6','Flag':'N'}");
//		// 净化器状态
//		pointDivisor.put("a01022",
//				"{'Avg':'"+Common.getRandom(0,1)+"','Cou':'0','Max':'0','Min':'0','ZsAvg':'10','ZsMin':'5','ZsMax':'6','Flag':'N'}");
	}

	@Test
	private void testName2()throws Exception {
		HashMap<String, Set<String>> objectObjectHashMap = new HashMap<>();
		Set<String> dev1 = new HashSet<>();
		dev1.add("1");
		dev1.add("2");
		dev1.add("3");
		dev1.add("2");
		objectObjectHashMap.put("设备1", dev1);
		Iterator<String> iterator = dev1.iterator();
		while (iterator.hasNext()){
			String next = iterator.next();
			if(next.equals("1")){
				iterator.remove();
			}
		}

		for (String s : dev1) {
			System.out.println(s);
		}
	}


	@Test
	public void testName() throws Exception {
		//补发
		String startDate="2020-07-21";
		String endDate="2020-07-21";
//		SupplyAgain.gas2011("17",startDate+"00:00:00",endDate+"00:00:00",30,"none");
//		SupplyAgain.gas2051("17",startDate+"00:00", endDate+"00:00", 10, "none");
//		SupplyAgain.gas2061("17",startDate+"06", endDate+"12",1,"none");
//		SupplyAgain.gas2031("17",startDate, endDate,1,"none");

		//实时
//		RealTime.gas2011("05",false,"none");
//		RealTime.gas2051("05",false,"none");
//		RealTime.gas2061("05",false,"none");
//		RealTime.gas2031("05",false,"none");
//		RealTime.param3020();
//		RealTime.status3020();
		//获取
//		RealTime.getTcp(17,"2011","none");

		//手动
//		RealTime.manuallySend();

//		RemoteCounteraccusation.connectionService("", 9011, 2);
	}


	@AfterClass
	public void end() throws InterruptedException {
		SupplyAgain.executorServiceStop();
		RealTime.executorServiceStop();
		RemoteCounteraccusation.executorServiceStop();
	}

}
