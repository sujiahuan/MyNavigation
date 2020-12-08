package org.jiahuan.service.coun;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jiahuan.entity.coun.CounCode;
import org.jiahuan.entity.coun.CounCountercharge;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jh
 * @since 2020-08-07
 */
public interface ICounCounterchargeService extends IService<CounCountercharge> {

    CounCountercharge getCounCounterchargeByDeviceId(Integer deviceId);

    void openConnection(Integer deviceId) throws Exception;

    void closeConnection(Integer deviceId);

    void addInitByDeviceId(Integer deviceId);

    void deleteByDeviceId(Integer deviceId);

    void updateCounCountercharge(CounCountercharge counCountercharge);

}
