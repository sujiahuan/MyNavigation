package org.jiahuan.service.coun;

import org.jiahuan.entity.coun.CounDataType;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jiahuan.entity.coun.CounDevice;

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
public interface ICounDataTypeService extends IService<CounDataType> {

    CounDataType getCounDataTypeByDeviceId(Integer deviceId,Integer dataType);

    List<CounDataType> getListCounDataTypeByDeviceId(Integer deviceId);

    void addInitByDeviceId(Integer deviceId);

    void deleteByDeviceId(Integer deviceId);

    /**
     * 发送实时数据
     * @param deviceId 设备id
     * @param agreement 协议17/05
     * @param dataType 发的数据类型 实时（1）\分钟（2）\小时（3）\日（4）
     * @throws IOException
     */
    void sendRealTime(Integer deviceId,String agreement,Integer dataType) throws IOException;

    /**
     * 发送补发数据
     * @param deviceId 设备id
     * @param agreement 协议17/05
     * @param dataType 发的数据类型 实时（1）\分钟（2）\小时（3）\日（4）
     * @throws IOException
     */
    void sendSupplyAgain(Integer deviceId,String agreement,Integer dataType) throws IOException;


    int getSupplyAgainCount(Integer deviceId, Integer dataType);

    /**
     * 发送3020数据包
     * @param deviceId 设备id
     * @param agreement 协议17/05
     * @param dataType 发的数据类型 实时（1）\分钟（2）\小时（3）\日（4）\参数（5）\状态（6）
     */
    void sendParam3020(Integer deviceId,String agreement,Integer dataType);

    /**
     * 发送组装好的数据包
     * @param counDevice 设备对象
     * @param message 数据包
     * @throws IOException
     */
    void sendMessage(CounDevice counDevice, String message) throws IOException ;

    /**
     * 获取已进行crc加密的实时数据组装报文
     * @param counDevice  设备对象
     * @param agreement 协议
     * @param dataType 实时（1）\分钟（2）\小时（3）\日（4）\参数（5）\状态（6）
     * @param is3020 是否是3020数据
     * @return 返回组装好的crc加密数据包
     */
    String getRealTimeDataPackage(CounDevice counDevice, String agreement, Integer dataType,boolean is3020);

}
