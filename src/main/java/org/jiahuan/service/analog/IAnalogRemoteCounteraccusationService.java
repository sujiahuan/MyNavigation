package org.jiahuan.service.analog;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jiahuan.entity.analog.AnalogRemoteCounteraccusation;

import java.io.IOException;

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

    void openSocketConnetion(Integer deviceId) throws IOException;

    void colseSocketConnetion(Integer deviceId);

    boolean getSocketConnetionStatus(Integer deviceId);

    void openControlConnection(Integer deviceId) throws Exception;

    void colseControlConnection(Integer deviceId);

    void addInitByDeviceId(Integer deviceId);

    void deleteByDeviceId(Integer deviceId);

    void updateCounCountercharge(AnalogRemoteCounteraccusation analogRemoteCounteraccusation);

}
