package org.jiahuan.service.analog;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jiahuan.entity.analog.AnalogDataType;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wj
 * @since 2020-08-02
 */
public interface IAnalogDataTypeService extends IService<AnalogDataType> {

    AnalogDataType getCounDataTypeByDeviceId(Integer deviceId, Integer dataType);

    List<AnalogDataType> getListCounDataTypeByDeviceId(Integer deviceId);

    void addInitByDeviceId(Integer deviceId);

    void deleteByDeviceId(Integer deviceId);

    /**
     * 发送实时数据
     * @param deviceId 设备id
     * @param dataType 发的数据类型 实时（1）\分钟（2）\小时（3）\日（4）
     * @throws IOException
     */
    void sendRealTime(Integer deviceId,Integer dataType) throws Exception;


    /**
     * 获取数据包
     * @param deviceId 设备id
     * @param dataType 提取数据类型 实时（1）\分钟（2）\小时（3）\日（4）
     * @param is3020 是否是3020
     * @return
     */
    String getDataPackage(Integer deviceId,Integer dataType,boolean is3020) throws IOException;

    /**
     * 发送补发数据
     * @param deviceId 设备id
     * @param dataType 发的数据类型 实时（1）\分钟（2）\小时（3）\日（4）
     * @throws IOException
     */
    void sendSupplyAgain(Integer deviceId,Integer dataType) throws Exception;

    /**
     * 终止补发
     * @param deviceId
     * @throws IOException
     */
    void cancelSupplyAgain(Integer deviceId) throws IOException ;


    int getSupplyAgainCount(Integer deviceId, Integer dataType);

    /**
     * 发送3020数据包
     * @param deviceId 设备id
     * @param dataType 发的数据类型 实时（1）\分钟（2）\小时（3）\日（4）\参数（5）\状态（6）
     */
    void sendParam3020(Integer deviceId,Integer dataType);

    /**
     * 发送组装好的数据包
     * @param deviceId 设备对象
     * @param message 数据包
     * @throws IOException
     */
    void sendMessage(Integer deviceId, String message) throws Exception;

//    /**
//     * 获取已进行crc加密的实时数据组装报文
//     * @param counDevice  设备对象
//     * @param dataType 实时（1）\分钟（2）\小时（3）\日（4）\参数（5）\状态（6）
//     * @param is3020 是否是3020数据
//     * @return 返回组装好的crc加密数据包
//     */
//    String getRealTimeDataPackage(CounDevice counDevice, Date date, HashMap<String, Map<String, String>> divisorParameter, Integer pnum, Integer pno, Integer dataType, boolean is3020);
//
//    /**
//     * 获取因子参数map
//     * @param parameters 因子参数集合
//     * @param is3020 是否是3020
//     * @return map
//     */
//    public HashMap<String, Map<String, String>> getDivisorParameterMap(List<Object> parameters, boolean is3020);

}
