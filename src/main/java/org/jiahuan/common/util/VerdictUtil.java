package org.jiahuan.common.util;

public class VerdictUtil {

	/**判断是否不为空
	 * @param Obj 可以传入任何对象
	 * @return
	 */
	public static boolean isNotNull(Object Obj) {
		return  null != Obj && !"".equals(Obj);
	}

	public static boolean isNull(Object Obj) {
		return  null == Obj && "".equals(Obj);
	}

}
