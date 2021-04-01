package org.jiahuan.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

/**
 * crc校验工具
 *
 * @author Administrator
 *
 */
public class DataPackageUtils {
	public static Logger log = LoggerFactory.getLogger(DataPackageUtils.class);
	/**
	 * 包头
	 */
	public static final String PACKAGE_HEAD = "##";
	/**
	 * 包尾
	 */
	public static final String PACKAGE_TAIL = "\r\n";

	/**
	 *
	 * @param now
	 * @return true:校验通过 false:校验失败
	 */
	public static boolean checkDateTime(Date now) {
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			if (now == null) {
				return false;
			}
			String beforeStr = "2010-01-01 00:00:00";
			String afterStr = "";
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			int year = calendar.get(Calendar.YEAR);
			afterStr = year + "-12-31 23:59:59";

			calendar.setTime(sdf.parse(beforeStr));
			// 以前的日期
			Date before = calendar.getTime();

			calendar.setTime(sdf.parse(afterStr));
			// 以后的日期
			Date after = calendar.getTime();

			// 排除掉2010年度数据和大于当年的时间
			if (now.before(before) || now.after(after)) {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 *
	 * 校验crc true:校验通过 false:校验失败
	 */
	public static boolean checkCrc(String msg) {
		// 默认校验不通过
		boolean flag = false;
		if (StringUtils.isEmpty(msg)) {
			return flag;
		}
		String preCrc = msg.substring(msg.length() - 4);
		String crc = generateCrc(msg.substring(6, msg.length() - 4));
		if (crc.equalsIgnoreCase(preCrc)) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 生成crc
	 *
	 * @param msg
	 * @return
	 */
	public static String generateCrc(String msg) {
		int len = 4;
//		Crc16 crc16 = new Crc16();
		int crc = tocrc16(msg.getBytes());
		String gethexstr = Integer.toHexString(crc);
		if (gethexstr.length() < len) {
			gethexstr = "0" + gethexstr;
		}
		if (crc == 0) {
			gethexstr = "0000";
		}
		return gethexstr.toUpperCase();
	}

	/**
	 * 加上包头和数据段长度
	 *
	 * @param getostrh
	 * @return
	 */
	public static String getDataLength(String getostrh) {
		int len1000 = 1000;
		int len100 = 100;
		int len10 = 10;
		String dataLength = "";
		if ((getostrh.length()) >= len1000) {
			dataLength = String.valueOf(getostrh.length());
		} else if ((getostrh.length()) >= len100 && (getostrh.length()) < len1000) {
			dataLength = "0" + getostrh.length();
		} else if ((getostrh.length()) >= len10 && (getostrh.length()) < len100) {
			dataLength = "00" + (getostrh.length());
		} else if ((getostrh.length()) < len10) {
			dataLength = "000" + (getostrh.length());
		}
		return dataLength;
	}

	/**
	 *
	 * @Title: composeDataPackage
	 * @Description: 组装数据包
	 * @param msg
	 * @param flag 1:是反控包 0：不是反控包
	 * @return
	 */
	public static synchronized String composeDataPackage(String msg, boolean flag) {
		String dataLength = getDataLength(msg);
		String crc = generateCrc(msg);
		StringBuffer sb = new StringBuffer();
//		if (msg.indexOf("QN=") != -1) {
			sb.append(PACKAGE_HEAD);
			sb.append(dataLength);
			sb.append(msg);
			sb.append(crc);
			if (flag == true) {
				// 反控包
				sb.append("**");
			}
			sb.append(PACKAGE_TAIL);
			return sb.toString();
//		} else {
//			sb.append(msg);
//			sb.append(crc);
//			if (flag == true) {
//				// 反控包
//				sb.append("**");
//			}
//			sb.append(PACKAGE_TAIL);
//			return sb.toString();
//		}
	}

	/**
	 * 上次的qn号,防止生成相同的qn
	 */
	public static Long qnOld = null;

	/**
	 *
	 * @Title: convertToMap
	 * @Description: 转化为map
	 * @param cn  命令号
	 * @param qn  唯一标识
	 * @param msg 数据包
	 * @return
	 */
	public static Map<String, String> convertToMap(String cn, String qn, String msg) {
		Map<String, String> paramMap = Maps.newHashMap();
		if (StringUtils.isNotEmpty(cn)) {
			paramMap.put("cn", cn);
		} else {
			return null;
		}
		if (StringUtils.isNotEmpty(qn)) {
			paramMap.put("qn", qn);
		}
		if (StringUtils.isNotEmpty(msg)) {
			paramMap.put("msg", msg);
		}
		return paramMap;
	}

	/**
	 *
	 * @Title: replaceMsgQn
	 * @Description: (将qn替换，重新生成反控数据包)
	 * @param msg ##0124QN=20180519013039400;ST=32;CN=2061;PW=123456;MN=88888880000020;Flag=3;CP=&&BeginTime=20180518070000,EndTime=20180518070000&&D581**
	 * @return 返回新的msg
	 */
	public static String replaceMsgQn(String msg, String qn) {
		if (StringUtils.isEmpty(msg) || StringUtils.isEmpty(qn)) {
			return null;
		}
		// 重新生成CRC
		String[] arr1 = msg.split("QN=\\d+");
		String[] arr2 = arr1[1].split("&&");
		String crcString = "QN=" + qn;
		for (int i = 0; i < arr2.length - 1; i++) {
			crcString += (arr2[i] + "&&");
		}
		msg = DataPackageUtils.composeDataPackage(crcString, true);
		return msg;
	}

	/**
	 * 得到cpString，比如
	 * ST=32;CN=2011;PW=123456;MN=88888880000001;CP=&&DataTime=20040516020111;B01-Rtd=100&&
	 * 得到：DataTime=20040516020111;B01-Rtd=100
	 *
	 * @param msg
	 * @return
	 */
	public static String getCpString(String msg) {
		String[] arr = msg.split("&&");
		return arr[1];
	}

	/**
	 * 将数据包转成key value的map
	 *
	 * @param msg
	 * @return
	 */
	public static Map<String, Object> convertToMap(String msg) {
		Map<String, Object> resovleMap = Maps.newHashMap();
		msg = msg.substring(6, msg.length() - 6);
		String[] arr = msg.split(";");
		Map<String, Object> metrics = Maps.newHashMap();
		for (String temp : arr) {
			if (temp.startsWith("CP=&&")) {
				temp = temp.replace("CP=&&", "");
			}
			// 有逗号
			if (temp.indexOf(",") != -1) {
				String polKey = null;
				String[] arr2 = temp.split(",");
				// 指标map
				Map<String, String> metricsMap = Maps.newHashMap();
				boolean inculdeFlag = false;
				for (int i = 0; i < arr2.length; i++) {
					inculdeFlag = true;
					String temp2 = arr2[i];
					String[] split = temp2.split("=")[0].split("-");
					if (i == 0) {
						polKey = split[0];
					}
					String key = split[1];
					String value = temp2.split("=")[1];
					metricsMap.put(key, value);

				}
				if (inculdeFlag) {
					metrics.put(polKey, metricsMap);
				}

			} else if (StringUtils.isNotEmpty(temp)) {
				// 没有逗号
				resovleMap.put(temp.split("=")[0], temp.split("=")[1]);
			}
		}
		int length = (int) resovleMap.get("DataTime").toString().length();
		String sn = resovleMap.get("DataTime").toString().substring((length - 4), length);

		Map<String, Object> map = Maps.newHashMap();
		map.put(sn, metrics);

		resovleMap.put("metrics", map);

		return resovleMap;
	}

	/**
	 * 将数据包转成key value的map
	 *
	 * @author wangjian
	 * @param msg
	 * @return
	 */
	public static Map<String, Object> toMap(String msg) {
		Map<String, Object> resultMap = Maps.newHashMap();
		Map<String, Object> metricsMap = Maps.newHashMap();

		// 将字符串以“&&”特殊标志分为三个部分
		String[] arr = msg.split("&&");
		String head = arr[0];
		// 去掉头(包头和四位长度)和尾(;CP=)
		head = head.substring(6, head.length() - 4);

		// 处理头
		String[] headArr = head.split(";");
		for (String temp : headArr) {
			resultMap.put(temp.split("=")[0].toLowerCase(), temp.split("=")[1]);
		}

		// 处理数据段
		String middle = arr[1];
		if (StringUtils.isNotEmpty(middle)) {
			String[] includePol = middle.split(";");
			String[] dataTimeArr = includePol[0].split("=");
			// 加入时间
			resultMap.put(dataTimeArr[0].toLowerCase(), dataTimeArr[1]);
			for (int i = 1; i < includePol.length; i++) {
				String pol = includePol[i];
				// 有逗号
				if (pol.indexOf(",") != -1) {
					String polKey = null;
					String[] arr2 = pol.split(",");
					// 指标map
					Map<String, String> detailMetricsMap = Maps.newHashMap();
					for (int j = 0; j < arr2.length; j++) {
						String detail = arr2[j];
						String[] split = detail.split("=")[0].split("-");
						if (j == 0) {
							polKey = split[0];
						}
						String key = split[1];
						String value = detail.split("=")[1];
						detailMetricsMap.put(key.toLowerCase(), value);
					}
					metricsMap.put(polKey.toLowerCase(), detailMetricsMap);
				} else if (StringUtils.isNotEmpty(pol)) {
					// 没有逗号
					resultMap.put(pol.split("=")[0], pol.split("=")[1]);
				}
			}
		}
		// 加入指标字段
		resultMap.put("metrics", metricsMap);
		return resultMap;
	}

	/**
	 * 使用reflect进行转换,map集合转javabean
	 *
	 * @param map
	 * @param beanClass
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public static <T> T mapToObject(Map<String, Object> map, Class<T> beanClass)
			throws IllegalAccessException, InstantiationException {
		if (map == null) {
			return null;
		}

		T obj = beanClass.newInstance();

		Field[] fields = obj.getClass().getDeclaredFields();

		for (Field field : fields) {
			int mod = field.getModifiers();
			if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
				continue;
			}

			field.setAccessible(true);
			field.set(obj, map.get(field.getName()));
		}
		return obj;
	}

	private static int tocrc16(byte[] buf) {

		int r = 0xffff;

		for (int j = 0; j < buf.length; j++) {
			int hi = r >> 8;
			hi ^= buf[j];
			r = hi;

			for (int i = 0; i < 8; i++) {
				int flag = r & 0x0001;
				r = r >> 1;
				if (flag == 1) {
					r ^= 0xa001;
				}
			}
		}
		return r;
	}

	/**
	 * 正表达式
	 *
	 * @param msg 要匹配的内容
	 * @return 返回匹配内容
	 */
	public static String positiveExpression(String msg) {
		String regex;
		Pattern p;
		Matcher m;
		String group;
		try {
			//17协议
			regex = "QN=[\\w\\d\\p{Punct}]+&{2,4}[\\w\\d\\p{Punct} ]+&{2,4}";
			p = Pattern.compile(regex);
			m = p.matcher(msg);
			m.find();
			group = m.group();
			return group;
		} catch (Exception a) {
			try {
				//05协议
				regex = "ST=[\\w\\d\\p{Punct}]+&{2,4}[\\w\\d\\p{Punct} ]+&{2,4}";
				p = Pattern.compile(regex);
				m = p.matcher(msg);
				m.find();
				group = m.group();
				return group;
			} catch (Exception b) {
				try {
					//油烟博控私有协议
					regex = "[\\w]*,[\\d]*&&[\\w,\\.;-]*&&";
					p = Pattern.compile(regex);
					m = p.matcher(msg);
					m.find();
					group = m.group();
					return group;
				} catch (Exception c) {
					c.printStackTrace();
					return msg;
				}
			}

		}
	}

}
