package org.jiahuan.service.analog;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jiahuan.entity.analog.AnalogRemoteCounteraccusation;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jh
 * @since 2020-08-07
 */
public interface IAnalogRemoteCounteraccusationService extends IService<AnalogRemoteCounteraccusation> {

    AnalogRemoteCounteraccusation getCounCounterchargeByDeviceId(Integer deviceId);

    void addInitByDeviceId(Integer deviceId);

    void updateCounCountercharge(AnalogRemoteCounteraccusation analogRemoteCounteraccusation);

    void deleteByDeviceId(Integer deviceId);

    boolean openSocketConnetion(Integer deviceId) ;

    void colseSocketConnetion(Integer deviceId);

    boolean getSocketConnetionStatus(Integer deviceId);

    void openControlConnection(Integer deviceId) throws Exception;

    void colseControlConnection(Integer deviceId);

    boolean isControlConnection(Integer deviceId);

    Set<Integer> getAllControlDeviceId();

    void processMessage(Integer deviceId,String message);

}
