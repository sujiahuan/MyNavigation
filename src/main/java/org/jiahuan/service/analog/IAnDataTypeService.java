package org.jiahuan.service.analog;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jiahuan.entity.analog.AnDataType;
import org.jiahuan.entity.sys.SysDevice;

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
public interface IAnDataTypeService extends IService<AnDataType> {

    /**
     * 设置补发状态，将会影响到是否继续补发
     * @param deviceId
     * @param supplyStatus
     */
    void setSupplyAgainStatus(Integer deviceId,boolean supplyStatus);

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
     * @param deviceId 设备id
     * @param dataType 数据类型，1实时/2分钟/3小时/4日
     * @return
     */
    AnDataType getCounDataTypeByDeviceId(Integer deviceId, Integer dataType);

    /**
     *获取该设备的所有数据类型
     * @param deviceId
     * @return
     */
    List<AnDataType> getListCounDataTypeByDeviceId(Integer deviceId);

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
     * @param dataType 提取数据类型 实时（1）\分钟（2）\小时（3）\日（4）\状态(5)\参数(6)
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
    void sendParam3020(Integer deviceId,Integer dataType) throws Exception;

    /**
     * 发送组装好的数据包
     * @param sysDevice 设备对象
     * @param message 数据包
     * @throws IOException
     */
    void sendMessage(SysDevice sysDevice, String message, List<String> dataPack) throws Exception;

    /**
     * 发送自定义数据
     * @param deviceId 设备id
     * @param message 消息
     * @throws Exception
     */
    void sendCustomizeMessage(Integer deviceId, String message) throws Exception;

}
