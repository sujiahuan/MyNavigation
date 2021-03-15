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

    /**
     * 获取补发数据状态
     * @param deviceId
     * @return
     */
    boolean getSupplyAgainStatus(Integer deviceId) ;

    /**
     * 等待补发完成
     * @param deviceId
     * @throws InterruptedException
     */
    void waitForTheReissueToComplete(Integer deviceId) throws InterruptedException;

    /**
     * 获取该设备的某一个数据类型
     * @param deviceId
     * @param dataType
     * @return
     */
    AnalogDataType getCounDataTypeByDeviceId(Integer deviceId, Integer dataType);

    /**
     *获取该设备的所有数据类型
     * @param deviceId
     * @return
     */
    List<AnalogDataType> getListCounDataTypeByDeviceId(Integer deviceId);

    /**
     * 添加初始化的
     * @param deviceId
     */
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
     * @param dataType 提取数据类型 实时（1）\分钟（2）\小时（3）\日（4）\
     * @return
     */
    String getDataPackage(Integer deviceId,Integer dataType) ;

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
    void cancelSupplyAgain(Integer deviceId)  ;


    /**
     * 获取补发统计的数据
     * @param deviceId
     * @param dataType
     * @return
     */
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
    void sendMessage(Integer deviceId, String message,List<String> dataPack) throws Exception;

}
