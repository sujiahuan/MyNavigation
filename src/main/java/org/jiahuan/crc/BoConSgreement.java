package org.jiahuan.crc;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.jiahuan.common.util.DataPackageUtils;

/**
 * 博控私有协议（油烟）
 *
 * @author Administrator
 *
 */
public class BoConSgreement {

	/**
	 * 设备状态上报命令
	 *
	 * @param malfunctionNumber 故障数：1~9
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	private static void sendDeviceStatus(int malfunctionNumber) throws UnknownHostException, IOException {
		for (String mnAndType : SendTcpToolsTest.pointMN) {
			String msg = JSON.parseObject(mnAndType).getString("mn") + "," + Common.getTime("second") + "&&STATE,2,1,4,12.1,A0,0" + malfunctionNumber
					+ ",1d00001727bdf101&&ABCD";
			Common.send(DataPackageUtils.composeDataPackage(Common.positiveExpression(msg), false));
		}
	}

	/**
	 * 实时报文
	 *
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public static void gas2011() throws UnknownHostException, IOException {
		for (String mnAndType : SendTcpToolsTest.pointMN) {
			SendTcpToolsTest sendTcpToolsTest = new SendTcpToolsTest();
			sendTcpToolsTest.setpoiDivisorValue();
			String msg = JSON.parseObject(mnAndType).getString("mn") + "," + Common.getTime("second") + getParameterLink(sendTcpToolsTest, "RTD") + ",11&&ABCD";
			Common.send(DataPackageUtils.composeDataPackage(Common.positiveExpression(msg), false));
		}
	}

	/**
	 * 分钟报文
	 *
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public static void gas2051() throws UnknownHostException, IOException {
		for (String mnAndType : SendTcpToolsTest.pointMN) {
			SendTcpToolsTest sendTcpToolsTest = new SendTcpToolsTest();
			sendTcpToolsTest.setpoiDivisorValue();
			String msg = JSON.parseObject(mnAndType).getString("mn") + "," + Common.getTime("minute") + getParameterLink(sendTcpToolsTest, "HIS2") + ",11&&ABCD";
			Common.send(DataPackageUtils.composeDataPackage(Common.positiveExpression(msg), false));
		}
	}

	/**
	 * 小时报文
	 *
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public static void gas2061() throws UnknownHostException, IOException {
		for (String mnAndType : SendTcpToolsTest.pointMN) {
			SendTcpToolsTest sendTcpToolsTest = new SendTcpToolsTest();
			sendTcpToolsTest.setpoiDivisorValue();
			String msg = JSON.parseObject(mnAndType).getString("mn") + "," + Common.getTime("hour") + getParameterLink(sendTcpToolsTest, "HIS3") + ",11&&ABCD";
			Common.send(DataPackageUtils.composeDataPackage(Common.positiveExpression(msg), false));
		}
	}

	/**
	 * 日报文
	 *
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public static void gas2031() throws UnknownHostException, IOException {
		for (String mnAndType : SendTcpToolsTest.pointMN) {
			SendTcpToolsTest sendTcpToolsTest = new SendTcpToolsTest();
			sendTcpToolsTest.setpoiDivisorValue();
			String msg = JSON.parseObject(mnAndType).getString("mn") + "," + Common.getTime("day") + getParameterLink(sendTcpToolsTest, "HIS4") + ",11&&ABCD";
			Common.send(DataPackageUtils.composeDataPackage(Common.positiveExpression(msg), false));
		}
	}

	/**
	 * 拼接监测因子
	 * @param sendTcpToolsTest 对象
	 * @param key           实时（RTD）/十分钟（HIS2）/小时（HIS3）/日（HIS4）
	 * @return 拼接的监测因子
	 */
	private static String getParameterLink(SendTcpToolsTest sendTcpToolsTest, String key) {
		Set<String> keySet = sendTcpToolsTest.pointDivisor.keySet();
		List<String> coding = new ArrayList<String>(keySet);
		StringBuffer buffer = new StringBuffer();
		JSONObject parseObject;
		Integer fan = null;
		Integer purifier = null;
		buffer.append("&&" + key + ",");
		for (String cod : coding) {
			if ("a34041".equals(cod)) {
				parseObject = JSON.parseObject(sendTcpToolsTest.pointDivisor.get(cod));
				buffer.append(parseObject.getString("Avg") + ",");
				buffer.append(parseObject.getString("Min") + ",");
				buffer.append(parseObject.getString("Max") + ",");
				buffer.append(parseObject.getString("Cou") + ",");
				String flag = parseObject.getString("Flag");
				if ("N".equals(flag)) {
					buffer.append("0,");
				} else if ("D".equals(flag)) {
					buffer.append(Common.getRandom(1,7)+",");
				} else if ("B".equals(flag)) {
					buffer.append("8,");
				}

			} else if ("a01018".equals(cod)) {
				parseObject = JSON.parseObject(sendTcpToolsTest.pointDivisor.get(cod));
				fan = Integer.valueOf(parseObject.getString("Avg"));
			} else if ("a01022".equals(cod)) {
				parseObject = JSON.parseObject(sendTcpToolsTest.pointDivisor.get(cod));
				purifier = Integer.valueOf(parseObject.getString("Avg"));
			}
		}

		if (fan == 1 && purifier == 1) {
			buffer.append("3");
		} else if (fan == 0 && purifier == 1) {
			buffer.append("2");
		} else if (fan == 1 && purifier == 0) {
			buffer.append("1");
		} else if (fan == 0 && purifier == 0) {
			buffer.append("0");
		}
		return buffer.toString();
	}

}
